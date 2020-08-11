package com.melegy.retrofitcoroutines.remote.factory

import com.melegy.retrofitcoroutines.BaseResponse
import com.melegy.retrofitcoroutines.remote.calladapter.ResponseAdapter
import com.melegy.retrofitcoroutines.remote.vo.Response
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by ravi on 09/08/20.
 */
class CoroutineResponseAdapterFactory private constructor() : CallAdapter.Factory() {

	companion object {
		@JvmStatic
		fun create() =
			CoroutineResponseAdapterFactory()
	}

	override fun get(
		returnType: Type,
		annotations: Array<Annotation>,
		retrofit: Retrofit
	): CallAdapter<*, *>? {
		if (Call::class.java != getRawType(returnType)) {
			return null
		}
		check(returnType is ParameterizedType) {
			"return type must be parameterized as Call<NetworkResponse<<Foo>> or Call<NetworkResponse<out Foo>>"
		}
		val responseType = getParameterUpperBound(0, returnType)
		if (getRawType(responseType) != Response::class.java) {
			return null
		}
		check(responseType is ParameterizedType) {
			"Response must be parameterized as NetworkResponse<Foo> or NetworkResponse<out Foo>"
		}
		val successBodyType = getParameterUpperBound(0, responseType)
		return ResponseAdapter<BaseResponse<Any>>(
			successBodyType
		)
	}
}
