package com.melegy.retrofitcoroutines.remote

import com.melegy.retrofitcoroutines.BaseResponse
import com.melegy.retrofitcoroutines.NetworkConnectionException
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException

internal class NetworkResponseCall<S : BaseResponse<Any>, E : BaseResponse<Any>>(
	private val delegate: Call<S>,
	private val errorConverter: Converter<ResponseBody, E>
) : Call<NetworkResponse<S, E>> {

	override fun enqueue(callback: Callback<NetworkResponse<S, E>>) {
		return delegate.enqueue(object : Callback<S> {
			override fun onResponse(call: Call<S>, response: Response<S>) {
				val body = response.body()
				val code = response.code()
				val error = response.errorBody()
				if (response.isSuccessful) {
					if (body?.isResponseOk() == true) {
						callback.onResponse(
							this@NetworkResponseCall,
							Response.success(NetworkResponse.Success(body))
						)
					} else {
						initErrorResponse(error, callback, body, code)
					}
				} else {
					initErrorResponse(error, callback, body, code)
				}
			}

			override fun onFailure(call: Call<S>, throwable: Throwable) {
				val networkResponse = when (throwable) {
					is NetworkConnectionException -> NetworkResponse.NetworkError(throwable)
					is IOException -> NetworkResponse.NetworkError(throwable)
					else -> NetworkResponse.UnknownError(throwable)
				}
				callback.onResponse(this@NetworkResponseCall, Response.success(networkResponse))
			}
		})
	}

	override fun isExecuted() = delegate.isExecuted

	override fun clone() = NetworkResponseCall(delegate.clone(), errorConverter)

	override fun isCanceled() = delegate.isCanceled

	override fun cancel() = delegate.cancel()

	override fun execute(): Response<NetworkResponse<S, E>> {
		throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")
	}

	override fun request(): Request = delegate.request()

	override fun timeout(): Timeout = delegate.timeout()

	@Suppress("UNCHECKED_CAST")
	private fun initErrorResponse(
		error: ResponseBody?,
		callback: Callback<NetworkResponse<S, E>>,
		body: S?,
		code: Int
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
				NetworkResponse.ApiError(
					errorBody ?: body as? E,
					code
				)
			)
		)
	}
}
