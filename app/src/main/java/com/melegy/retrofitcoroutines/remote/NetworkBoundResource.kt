package com.melegy.retrofitcoroutines.remote

import com.melegy.retrofitcoroutines.dispatchProvider
import com.melegy.retrofitcoroutines.remote.vo.Error
import com.melegy.retrofitcoroutines.remote.vo.Response
import com.melegy.retrofitcoroutines.transformers.FlowModelTransformer
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.*
import okhttp3.ResponseBody

@Suppress("UNCHECKED_CAST")
inline fun <DB : Any, REMOTE : Any> networkBoundResource(
	crossinline fetchFromLocal: () -> Flow<DB> = { emptyFlow() },
	crossinline shouldCache: () -> Boolean = { true },
	noinline shouldFetchFromRemote: suspend (DB?) -> Boolean = { it == null },
	crossinline fetchFromRemote: () -> Flow<Response<REMOTE>>,
	crossinline processRemoteResponse: (response: REMOTE?) -> Unit = { },
	crossinline saveRemoteData: (REMOTE) -> Unit = { },
	noinline onFetchFailed: suspend () -> Unit = { }
): Flow<Response<DB>> = flow {
	val fetchFromCache = shouldCache()
	val localData = if (fetchFromCache) fetchFromLocal().firstOrNull() else null
	if (shouldFetchFromRemote(localData) || !fetchFromCache) {
		fetchFromRemote().collect { apiResponse ->
			when (apiResponse) {
				is Response.Success -> {
					processRemoteResponse(apiResponse.response)
					if (fetchFromCache && apiResponse.response != null) {
						saveRemoteData(apiResponse.response)
						emitAll(fetchFromLocal().map { Response.success(it, isCached = true) })
					} else {
						emit(Response.success(apiResponse.response as DB, isCached = false))
					}
				}
				is Response.Failure -> {
					onFetchFailed()
					emit(apiResponse as Response.Failure)
				}
			}
		}
	} else {
		emitAll(fetchFromLocal().map { Response.success(it, isCached = true) })
	}
}.flowOn(dispatchProvider.io)
	.catch {
		Logger.e("catch $it ${it.message}")
		onFetchFailed()
		emit(Response.failure(Error.UnhandledExceptionError(it, it.message)))
	}

@Suppress("UNCHECKED_CAST")
fun <R, M> buildResponse(
	call: Flow<retrofit2.Response<R>>,
	modelTransformer: FlowModelTransformer<R, M>? = null
): Flow<Response<M>> {
	return call.transform {
		val response = it
		val body = response.body()
		val code = response.code()
		if (body != null && response.isSuccessful) {
			modelTransformer?.let { transformer -> emitAll(transformer(body)) }
				?: emit(Response.success(body as? M, httpStatusCode = code))
		} else {
			emit(buildFailureResponse(response, body, response.errorBody()))
		}
	}.catch {
		emit(
			Response.Failure(
				Error.UnhandledExceptionError(
					it, it.message
						?: "exception-transformer-$it"
				)
			)
		)
	}
}

const val MESSAGE_NULL_RESPONSE_BODY = "null_response_body"
const val MESSAGE_UNSUCCESSFUL_RESPONSE = "unsuccessful_response"

fun <R> buildFailureResponse(
	response: retrofit2.Response<R>,
	body: R? = null,
	errorBody: ResponseBody? = null
): Response.Failure {
	val error =
		Error.UnhandledError(if (body == null) MESSAGE_NULL_RESPONSE_BODY else MESSAGE_UNSUCCESSFUL_RESPONSE)
	return Response.Failure(error, body ?: errorBody, response.code())
}

inline fun <T> foo(crossinline coroutine: suspend () -> T): Flow<T> {
	return flow { emit(coroutine()) }
}
