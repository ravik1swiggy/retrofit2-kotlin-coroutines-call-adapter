package com.melegy.retrofitcoroutines.remote.vo

import com.melegy.retrofitcoroutines.BaseResponse
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow

/**
 * Created by ravi on 09/08/20.
 */
//T success data type, U error data type
sealed class ResponseV2<out T : Any, out U : Any> {

	object Loading : ResponseV2<Nothing, Nothing>()

	data class Success<T : Any>(
		val response: T? = null, val httpStatusCode: Int? = null, val isCached: Boolean? = null
	) : ResponseV2<T, Nothing>()

	data class Failure<U : Any>(
		val error: Error, val response: U? = null, val httpStatusCode: Int? = null,
		val isCached: Boolean? = null
	) : ResponseV2<Nothing, U>()

	companion object {

		fun <T : Any> success(
			body: T? = null, httpStatusCode: Int? = null, isCached: Boolean? = null
		): ResponseV2<T, Nothing> = Success(body, httpStatusCode, isCached)

		fun <T : Any> failure(
			error: Error, body: T? = null, httpCode: Int? = null, isCached: Boolean? = null
		): ResponseV2<Nothing, T> = Failure(error, body, httpCode, isCached)

		fun loading(): ResponseV2<Nothing, Nothing> = Loading
	}

	override fun toString(): String {
		return when (this) {
			is Success -> "Success[response=$response,httpStatusCode=$httpStatusCode,isCached=$isCached]"
			is Failure -> "Failure[error$error,response=$response,,httpStatusCode=$httpStatusCode,isCached=$isCached]"
			Loading -> "Loading"
		}
	}

}

typealias GenericResponseV2<S> = ResponseV2<BaseResponse<S>, BaseResponse<Any>>

typealias FlowResponseV2<S> = Flow<ResponseV2<BaseResponse<S>, BaseResponse<Any>>>

typealias RxRetroResponse<S> = Single<retrofit2.Response<BaseResponse<S>>>

typealias RxResponse<S> = Single<BaseResponse<S>>

typealias GenericResponse<S> = Response<BaseResponse<S>>

typealias FlowResponse<S> = Flow<Response<BaseResponse<S>>>

typealias FlowRetroResponse<S> = Flow<retrofit2.Response<BaseResponse<S>>>
