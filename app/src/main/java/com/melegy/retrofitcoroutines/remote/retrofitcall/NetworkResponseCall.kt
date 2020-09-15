package com.melegy.retrofitcoroutines.remote.retrofitcall

import com.melegy.retrofitcoroutines.BaseResponse
import com.melegy.retrofitcoroutines.NetworkConnectionException
import com.melegy.retrofitcoroutines.remote.vo.Error
import com.melegy.retrofitcoroutines.remote.vo.ResponseV2
import com.melegy.retrofitcoroutines.remote.factory.CoroutineNetworkResponseAdapterFactory.Companion.ERROR_CODE_INTERNAL
import com.melegy.retrofitcoroutines.remote.factory.CoroutineNetworkResponseAdapterFactory.Companion.ERROR_CODE_INTERNAL_SECONDARY
import com.melegy.retrofitcoroutines.remote.factory.CoroutineNetworkResponseAdapterFactory.Companion.ERROR_CODE_THROTTLE
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response

internal class NetworkResponseCall<S : BaseResponse<Any>, E : BaseResponse<Any>>(
	private val delegate: Call<S>,
	private val errorConverter: Converter<ResponseBody, E>
) : Call<ResponseV2<S, E>> {

	override fun enqueue(callback: Callback<ResponseV2<S, E>>) {
		return delegate.enqueue(object : Callback<S> {
			override fun onResponse(call: Call<S>, response: Response<S>) {
				val body = response.body()
				val code = response.code()
				val error = response.errorBody()
				val internalError =
					Error.InternalError(
						body?.statusTitle, body?.statusMessage, body?.statusCode
					)
				when {
					code in setOf(
						ERROR_CODE_INTERNAL,
						ERROR_CODE_INTERNAL_SECONDARY
					) -> initErrorResponse(
						error, callback, body, code,
						Error.InternalError()
					)
					code == ERROR_CODE_THROTTLE -> initErrorResponse(
						error, callback, body, code,
						Error.ThrottleError()
					)
					response.isSuccessful -> {
						when {
							body?.isResponseOk() == true -> {
								callback.onResponse(
									this@NetworkResponseCall,
									Response.success(ResponseV2.success(body, code, false))
								)
							}
							body?.isSessionInValid() == true -> {
								initErrorResponse(
									error, callback, body, code,
									Error.ExpiredTokenError(
										"Session expiry from api : ${request().url}"
									)
								)
							}
							else -> {
								initErrorResponse(
									error, callback, body, code, internalError
								)
							}
						}
					}
					else -> {
						initErrorResponse(
							error, callback, body, code, internalError
						)
					}
				}
			}

			override fun onFailure(call: Call<S>, throwable: Throwable) {
				val customError: Error = when (throwable) {
					is NetworkConnectionException -> Error.NoNetworkError()
					else -> Error.UnhandledExceptionError(
						throwable
					)
				}
				callback.onResponse(
					this@NetworkResponseCall, Response.success(
						ResponseV2.failure(error = customError, isCached = false)
					)
				)
			}
		})
	}

	override fun isExecuted() = delegate.isExecuted

	override fun clone() = NetworkResponseCall(delegate.clone(), errorConverter)

	override fun isCanceled() = delegate.isCanceled

	override fun cancel() = delegate.cancel()

	override fun execute(): Response<ResponseV2<S, E>> =
		throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")

	override fun request(): Request = delegate.request()

	override fun timeout(): Timeout = delegate.timeout()

	@Suppress("UNCHECKED_CAST")
	private fun initErrorResponse(
        error: ResponseBody? = null,
        callback: Callback<ResponseV2<S, E>>,
        body: S? = null,
        code: Int,
        customError: Error
	) {
		val errorBody = when {
			error == null -> null
			error.contentLength() == 0L -> null
			else -> try {
				errorConverter.convert(error)
			} catch (ex: Exception) {
				null
			}
		}
		callback.onResponse(
			this@NetworkResponseCall,
			Response.success(
				ResponseV2.failure(
					error = customError,
					body = errorBody ?: body as? E,
					httpCode = code,
					isCached = false
				)
			)
		)
	}
}
