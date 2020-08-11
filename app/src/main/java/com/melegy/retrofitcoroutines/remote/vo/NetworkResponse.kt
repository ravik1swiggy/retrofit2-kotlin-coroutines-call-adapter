package com.melegy.retrofitcoroutines.remote.vo

import com.melegy.retrofitcoroutines.BaseResponse
import kotlinx.coroutines.flow.Flow

/**
 * Created by ravi on 09/08/20.
 */
//T success data type, U error data type
sealed class NetworkResponse<out T : Any, out U : Any> {

	object Loading : NetworkResponse<Nothing, Nothing>()

	data class Success<T : Any>(
		val data: T? = null, val httpStatusCode: Int? = null, val isCached: Boolean? = null
	) : NetworkResponse<T, Nothing>()

	data class Failure<U : Any>(
		val error: Error, val data: U? = null, val httpStatusCode: Int? = null,
		val isCached: Boolean? = null
	) : NetworkResponse<Nothing, U>()

	companion object {

		fun <T : Any> success(
			body: T? = null, httpStatusCode: Int? = null, isCached: Boolean? = null
		): NetworkResponse<T, Nothing> = Success(body, httpStatusCode, isCached)

		fun <T : Any> failure(
			error: Error, body: T? = null, httpCode: Int? = null, isCached: Boolean? = null
		): NetworkResponse<Nothing, T> = Failure(error, body, httpCode, isCached)

		fun loading(): NetworkResponse<Nothing, Nothing> = Loading
	}

	override fun toString(): String {
		return when (this) {
			is Success -> "Success[data=$data,httpStatusCode=$httpStatusCode,isCached=$isCached]"
			is Failure -> "Failure[error$error,data=$data,,httpStatusCode=$httpStatusCode,isCached=$isCached]"
			Loading -> "Loading"
		}
	}

}

typealias GenericNetworkResponse<S> = NetworkResponse<BaseResponse<S>, BaseResponse<Any>>

typealias FlowNetworkResponse<S> = Flow<NetworkResponse<BaseResponse<S>, BaseResponse<Any>>>

typealias GenericResponse<S> = Response<BaseResponse<S>>

typealias FlowResponse<S> = Flow<Response<BaseResponse<S>>>
