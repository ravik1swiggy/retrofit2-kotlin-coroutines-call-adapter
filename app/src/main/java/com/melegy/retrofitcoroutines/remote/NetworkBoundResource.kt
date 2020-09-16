package com.melegy.retrofitcoroutines.remote

import com.melegy.retrofitcoroutines.dispatchProvider
import com.melegy.retrofitcoroutines.remote.vo.Error
import com.melegy.retrofitcoroutines.remote.vo.Response
import com.melegy.retrofitcoroutines.transformers.FlowModelTransformer
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.*
import okhttp3.ResponseBody
import retrofit2.Response as RetroResponse

const val MESSAGE_NULL_RESPONSE_BODY = "null_response_body"
const val MESSAGE_UNSUCCESSFUL_RESPONSE = "unsuccessful_response"

@Suppress("UNCHECKED_CAST")
inline fun <DB : Any, REMOTE : Any> networkBoundResource(
	crossinline fetchFromLocal: () -> Flow<DB> = { emptyFlow() },
	crossinline shouldCache: () -> Boolean = { true },
	crossinline shouldFetchFromRemote: suspend (DB?) -> Boolean = { it == null },
	crossinline fetchFromRemote: () -> Flow<Response<REMOTE>>,
	crossinline saveRemoteData: suspend (REMOTE) -> Unit = { },
	crossinline onFetchFailed: (Response.Failure) -> Unit = { }
): Flow<Response<DB>> = flow {
	val fetchFromCache = shouldCache()
	val localData: DB? = if (fetchFromCache) fetchFromLocal().firstOrNull() else null
	if (shouldFetchFromRemote(localData) || !fetchFromCache) {
		fetchFromRemote().collect { apiResponse ->
			when (apiResponse) {
				is Response.Success -> {
					if (fetchFromCache) {
						saveRemoteData(apiResponse.response)
						emitAll(fetchFromLocal().map { Response.success(it, isCached = true) })
					} else {
						emit(Response.success(apiResponse.response as DB, isCached = false))
					}
				}
				is Response.Failure -> {
					onFetchFailed(apiResponse)
					emit(apiResponse)
				}
			}
		}
	} else {
		localData?.let {
			emitAll(fetchFromLocal().map { Response.success(it, isCached = true) })
		} ?: emit(buildFailureResponse<DB>(errorMessage = MESSAGE_UNSUCCESSFUL_RESPONSE))
	}
}.flowOn(dispatchProvider.io)
	.catch {
		Logger.e("catch $it ${it.message}")
		val response: Response.Failure =
			Response.Failure(Error.UnhandledExceptionError(it, it.message))
		onFetchFailed(response)
		emit(response)
	}

@Suppress("UNCHECKED_CAST")
fun <R, M> buildResponse(
	call: Flow<RetroResponse<R>>,
	modelTransformer: FlowModelTransformer<R, M>? = null
): Flow<Response<M>> {
	return call.transform {
		val response = it
		val body = response.body()
		val code = response.code()
		if (body != null && response.isSuccessful) {
			modelTransformer?.let { transformer -> emitAll(transformer(body)) }
				?: emit(Response.success(body as M, httpStatusCode = code))
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

fun <R> buildFailureResponse(
	response: RetroResponse<R>? = null,
	body: R? = null,
	errorBody: ResponseBody? = null,
	errorMessage: String? = null
): Response.Failure {
	val error =
		Error.UnhandledError(
			errorMessage
				?: if (body == null && errorBody == null) MESSAGE_NULL_RESPONSE_BODY else MESSAGE_UNSUCCESSFUL_RESPONSE
		)
	return Response.Failure(error, body ?: errorBody, response?.code())
}

inline fun <T> emitFlow(crossinline block: suspend () -> T): Flow<T> {
	return flow { emit(block()) }
}
