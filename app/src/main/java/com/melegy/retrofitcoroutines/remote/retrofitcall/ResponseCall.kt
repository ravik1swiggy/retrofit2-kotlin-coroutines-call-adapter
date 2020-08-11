package com.melegy.retrofitcoroutines.remote.retrofitcall

import com.melegy.retrofitcoroutines.BaseResponse
import com.melegy.retrofitcoroutines.remote.vo.Response
import com.melegy.retrofitcoroutines.NetworkConnectionException
import com.melegy.retrofitcoroutines.remote.vo.Error
import com.melegy.retrofitcoroutines.remote.factory.CoroutineNetworkResponseAdapterFactory.Companion.ERROR_CODE_INTERNAL
import com.melegy.retrofitcoroutines.remote.factory.CoroutineNetworkResponseAdapterFactory.Companion.ERROR_CODE_INTERNAL_SECONDARY
import com.melegy.retrofitcoroutines.remote.factory.CoroutineNetworkResponseAdapterFactory.Companion.ERROR_CODE_THROTTLE
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response as RetroResponse

internal class ResponseCall<T : BaseResponse<Any>>(
	private val delegate: Call<T>
) : Call<Response<T>> {

	override fun enqueue(callback: Callback<Response<T>>) {
		return delegate.enqueue(object : Callback<T> {
			override fun onResponse(call: Call<T>, response: RetroResponse<T>) {
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
						callback, body, code,
						Error.InternalError(), error
					)
					code == ERROR_CODE_THROTTLE -> initErrorResponse(
						callback, body, code,
						Error.ThrottleError(), error
					)
					response.isSuccessful -> {
						when {
							body?.isResponseOk() == true -> {
								callback.onResponse(
									this@ResponseCall,
									RetroResponse.success(Response.success(body, code, false))
								)
							}
							body?.isSessionInValid() == true -> {
								initErrorResponse(
									callback, body, code,
									Error.ExpiredTokenError(
										"Session expiry from api : ${request().url}"
									), error
								)
							}
							else -> {
								initErrorResponse(
									callback, body, code, internalError, error
								)
							}
						}
					}
					else -> {
						initErrorResponse(
							callback, body, code, internalError, error
						)
					}
				}
			}

			override fun onFailure(call: Call<T>, throwable: Throwable) {
				val customError: Error = when (throwable) {
					is NetworkConnectionException -> Error.NoNetworkError()
					else -> Error.UnhandledExceptionError(
						throwable
					)
				}
				callback.onResponse(
					this@ResponseCall, RetroResponse.success(
						Response.failure(error = customError, isCached = false)
					)
				)
			}
		})
	}

	override fun isExecuted() = delegate.isExecuted

	override fun clone() = ResponseCall(delegate.clone())

	override fun isCanceled() = delegate.isCanceled

	override fun cancel() = delegate.cancel()

	override fun execute(): RetroResponse<Response<T>> =
		throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")

	override fun request(): Request = delegate.request()

	override fun timeout(): Timeout = delegate.timeout()

	private fun initErrorResponse(
		callback: Callback<Response<T>>,
		body: T?,
		code: Int,
		customError: Error,
		error: ResponseBody? = null
	) {
		val errorBody = when {
			error == null -> null
			error.contentLength() == 0L -> null
			else -> null
		}
		callback.onResponse(
			this@ResponseCall,
			RetroResponse.success(
				Response.failure(
					error = customError,
					data = errorBody ?: body,
					httpStatusCode = code,
					isCached = false
				)
			)
		)
	}

}
