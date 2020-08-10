package com.melegy.retrofitcoroutines.remote

import com.melegy.retrofitcoroutines.BaseResponse
import kotlinx.coroutines.flow.Flow

/**
 * Created by ravi on 09/08/20.
 */
//T success type, U error type
sealed class NetworkResponse<out T : Any, out U : Any> {

	object Loading : NetworkResponse<Nothing, Nothing>()

	data class Success<T : Any>(
		val body: T? = null, val httpCode: Int? = null, val isCached: Boolean? = null
	) : NetworkResponse<T, Nothing>()

	data class Failure<U : Any>(
		val error: Error, val body: U? = null, val httpCode: Int? = null,
		val isCached: Boolean? = null
	) : NetworkResponse<Nothing, U>()

	companion object {

		fun <T : Any> success(body: T? = null, httpCode: Int? = null, isCached: Boolean? = null) =
			Success(body, httpCode, isCached)

		fun <T : Any> failure(
			error: Error, body: T? = null, httpCode: Int? = null, isCached: Boolean? = null
		) = Failure(error, body, httpCode, isCached)

		fun loading() = Loading
	}

}

typealias GenericResponse<S> = NetworkResponse<BaseResponse<S>, BaseResponse<Any>>

typealias FlowResponse<S> = Flow<NetworkResponse<BaseResponse<S>, BaseResponse<Any>>>
