package com.melegy.retrofitcoroutines.remote

import com.melegy.retrofitcoroutines.BaseResponse
import java.io.IOException

sealed class NetworkResponse<out T : Any, out U : Any> {
	/**
	 * Success response with body
	 */
	data class Success<T : Any>(val body: T? = null, val httpCode: Int? = null) :
		NetworkResponse<T, Nothing>()

	/**
	 * Failure response with body
	 */
	data class ApiError<U : Any>(val body: U? = null, val httpCode: Int? = null) :
		NetworkResponse<Nothing, U>()

	/**
	 * Network error
	 */
	data class NetworkError(val error: IOException) : NetworkResponse<Nothing, Nothing>()

	/**
	 * For example, json parsing error
	 */
	data class UnknownError(val error: Throwable?) : NetworkResponse<Nothing, Nothing>()
}

typealias GenericResponse<S> = NetworkResponse<BaseResponse<S>, BaseResponse<Any>>
