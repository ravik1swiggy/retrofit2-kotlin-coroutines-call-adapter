package com.melegy.retrofitcoroutines.remote

import com.melegy.retrofitcoroutines.BaseResponse
import com.melegy.retrofitcoroutines.IDispatchProvider
import com.melegy.retrofitcoroutines.remote.vo.Error
import com.melegy.retrofitcoroutines.remote.vo.Response
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@Suppress("UNCHECKED_CAST")
@OptIn(ExperimentalCoroutinesApi::class)
fun <DB : Any, REMOTE : Any> networkBoundResource(
	fetchFromLocal: () -> Flow<DB> = { emptyFlow() },
	shouldCache: () -> Boolean = { true },
	shouldFetchFromRemote: (DB?) -> Boolean = { it == null },
	fetchFromRemote: () -> Flow<Response<BaseResponse<REMOTE>>>,
	processRemoteResponse: (response: REMOTE?) -> Unit = {},
	saveRemoteData: suspend (REMOTE) -> Unit = {},
	onFetchFailed: Error.() -> Unit = {},
	coroutineDispatcher: () -> CoroutineDispatcher = { IDispatchProvider.get().io }
): Flow<Response<DB>> = flow {
	emit(Response.loading())
	val storeCache = shouldCache()
	val localData = if (storeCache) fetchFromLocal().first() else null
	if (shouldFetchFromRemote(localData) || !storeCache) {
		emit(Response.loading())
		fetchFromRemote().collect { apiResponse ->
			when (apiResponse) {
				Response.Loading -> emit(Response.loading())
				is Response.Success -> {
					processRemoteResponse(apiResponse.response?.data)
					if (storeCache) {
						try {
							apiResponse.response?.data?.let {
								saveRemoteData(it)
							}
							emitAll(fetchFromLocal().map { dbData ->
								Response.success(dbData, isCached = false)
							})
						} catch (e: Exception) {
							onFetchFailed(Error.UnhandledExceptionError(e))
							emit(
								Response.success(
									apiResponse.response?.data as? DB,
									isCached = false
								)
							)
						}
					} else {
						emit(Response.success(apiResponse.response?.data as? DB, isCached = false))
					}
				}
				is Response.Failure -> {
					onFetchFailed(apiResponse.error)
					if (storeCache) {
						emitAll(fetchFromLocal().map {
							Response.failure(apiResponse.error, data = it)
						})
					} else {
						emit(
							Response.failure(
								apiResponse.error, data = apiResponse.response as? DB
							)
						)
					}
				}
			}
		}
	} else {
		if (storeCache) {
			emitAll(fetchFromLocal().map { Response.success(it, isCached = true) })
		} else {
			Response.success(localData)
		}
	}
}.flowOn(coroutineDispatcher.invoke())
