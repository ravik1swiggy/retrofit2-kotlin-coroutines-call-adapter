package com.melegy.retrofitcoroutines.remote.vo

/**
 * Created by ravi on 09/08/20.
 */
sealed class Response<out T> {

	data class Success<out T>(
		val response: T? = null, val httpStatusCode: Int? = null, val isCached: Boolean? = null
	) : Response<T>()

	data class Failure(
		val error: Error, val response: Any? = null, val httpStatusCode: Int? = null,
		val isCached: Boolean? = null
	) : Response<Nothing>()

	companion object {

		fun <T> success(
			data: T? = null, httpStatusCode: Int? = null, isCached: Boolean? = null
		): Response<T> = Success(data, httpStatusCode, isCached)

		fun failure(
			error: Error, data: Any? = null, httpStatusCode: Int? = null,
			isCached: Boolean? = null
		): Response<Nothing> = Failure(error, data, httpStatusCode, isCached)

	}

	override fun toString(): String {
		return when (this) {
			is Success -> "Success[response=$response,httpStatusCode=$httpStatusCode,isCached=$isCached]"
			is Failure -> "Failure[error$error,response=$response,,httpStatusCode=$httpStatusCode,isCached=$isCached]"
		}
	}
}
