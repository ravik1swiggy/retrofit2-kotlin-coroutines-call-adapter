package com.melegy.retrofitcoroutines.remote

import com.melegy.retrofitcoroutines.remote.vo.Response
import com.melegy.retrofitcoroutines.IDispatchProvider
import com.melegy.retrofitcoroutines.remote.vo.Error
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

/*@OptIn(ExperimentalCoroutinesApi::class)
inline fun <DB : Any, REMOTE : Any> networkBoundResource(
	crossinline fetchFromLocal: () -> Flow<DB>,
	crossinline shouldFetchFromRemote: (DB?) -> Boolean = { true },
	crossinline fetchFromRemote: () -> Flow<CustomResponse<REMOTE>>,
	crossinline processRemoteResponse: (response: CustomResponse<REMOTE>) -> Unit = { Unit },
	crossinline saveRemoteData: (REMOTE) -> Unit = { Unit },
	crossinline onFetchFailed: Error.() -> Unit = { },
	crossinline coroutineDispatcher: () -> CoroutineDispatcher = { IDispatchProvider.get().io }
): Flow<CustomResponse<DB>> = flow {
	emit(CustomResponse.loading())
	val localData = fetchFromLocal().first()
	if (shouldFetchFromRemote(localData)) {
		emit(CustomResponse.loading())
		fetchFromRemote().collect { apiResponse ->
			when (apiResponse) {
				CustomResponse.Loading -> TODO()
				is CustomResponse.Success -> {
					processRemoteResponse(apiResponse)
					apiResponse.response?.let { saveRemoteData(it) }
					emitAll(fetchFromLocal().map { dbData ->
						CustomResponse.success(dbData)
					})
				}
				is CustomResponse.Failure -> {
					onFetchFailed(apiResponse.error)
					emitAll(fetchFromLocal().map {
						CustomResponse.failure<DB>(
							apiResponse.error
						)
					})
				}
			}
		}
	} else {
		emitAll(fetchFromLocal().map { CustomResponse.success(it) })
	}
}.flowOn(coroutineDispatcher.invoke())*/

@OptIn(ExperimentalCoroutinesApi::class)
inline fun <DB : Any, REMOTE : Any> networkBoundResource(
	crossinline fetchFromLocal: () -> Flow<DB> = { emptyFlow() },
	crossinline shouldCache: () -> Boolean = { true },
	crossinline shouldFetchFromRemote: () -> Boolean = { true },
	crossinline fetchFromRemote: () -> Flow<Response<REMOTE>>,
	crossinline processRemoteResponse: (response: Response<REMOTE>) -> Unit = { },
	crossinline saveRemoteData: (REMOTE) -> Unit = { },
	crossinline onFetchFailed: Error.() -> Unit = { },
	crossinline coroutineDispatcher: () -> CoroutineDispatcher = { IDispatchProvider.get().io }
//	crossinline errorResolver: () -> Pair<IErrorChecker<REMOTE>, ITransformer<REMOTE, Error>>? = { null },
//	crossinline modelTransformer: () -> ITransformer<REMOTE, TRANS>? = { null }
): Flow<Response<DB>> = flow {
	emit(Response.loading())
	val storeLocal = shouldCache.invoke()
	val localData = try {
		if (storeLocal) fetchFromLocal().first() else null
	} catch (e: Exception) {
		Logger.e("NBR $e")
	}
	if (shouldFetchFromRemote()) {
		emit(Response.loading())
		fetchFromRemote().collect { apiResponse ->
			when (apiResponse) {
				Response.Loading -> TODO()
				is Response.Success -> {
					processRemoteResponse(apiResponse)
					apiResponse.data?.let { saveRemoteData(it) }
					emitAll(fetchFromLocal().map { dbData ->
						Response.success(dbData)
					})
				}
				is Response.Failure -> {
					onFetchFailed(apiResponse.error)
					emitAll(fetchFromLocal().map {
						Response.failure<DB>(
							apiResponse.error
						)
					})
				}
			}
		}
	} else {
		if (storeLocal) {
			emitAll(fetchFromLocal().map { Response.success(it) })
		} else {
			Response.success(localData)
		}
	}
}.flowOn(coroutineDispatcher.invoke())
