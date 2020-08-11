package com.melegy.retrofitcoroutines.remote.vo

/**
 * Created by ravi on 09/08/20.
 */
sealed class Response<out T : Any> {

	object Loading : Response<Nothing>()

	data class Success<out T : Any>(
		val data: T? = null, val httpStatusCode: Int? = null, val isCached: Boolean? = null
	) : Response<T>()

	data class Failure<out T : Any>(
		val error: Error, val data: T? = null, val httpStatusCode: Int? = null,
		val isCached: Boolean? = null
	) : Response<T>()

	companion object {

		fun <T : Any> success(
			data: T? = null, httpStatusCode: Int? = null, isCached: Boolean? = null
		): Response<T> = Success(data, httpStatusCode, isCached)

		fun <T : Any> failure(
			error: Error, data: T? = null, httpStatusCode: Int? = null,
			isCached: Boolean? = null
		): Response<T> = Failure(error, data, httpStatusCode, isCached)

		fun loading(): Response<Nothing> = Loading
	}

	override fun toString(): String {
		return when (this) {
			is Success -> "Success[data=$data,httpStatusCode=$httpStatusCode,isCached=$isCached]"
			is Failure -> "Failure[error$error,data=$data,,httpStatusCode=$httpStatusCode,isCached=$isCached]"
			Loading -> "Loading"
		}
	}
}
