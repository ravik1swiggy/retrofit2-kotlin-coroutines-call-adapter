package com.melegy.retrofitcoroutines.remote.factory

import com.melegy.retrofitcoroutines.BaseResponse
import com.melegy.retrofitcoroutines.remote.vo.NetworkResponse
import com.melegy.retrofitcoroutines.remote.calladapter.NetworkResponseAdapter
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by ravi on 09/08/20.
 */
class CoroutineNetworkResponseAdapterFactory private constructor() : CallAdapter.Factory() {

	companion object {
		@JvmStatic
		fun create() =
			CoroutineNetworkResponseAdapterFactory()

		const val ERROR_CODE_INTERNAL = 503
		const val ERROR_CODE_INTERNAL_SECONDARY = 403
		const val ERROR_CODE_THROTTLE = 429
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
		if (getRawType(responseType) != NetworkResponse::class.java) {
			return null
		}
		check(responseType is ParameterizedType) {
			"Response must be parameterized as NetworkResponse<Foo> or NetworkResponse<out Foo>"
		}
		val successBodyType = getParameterUpperBound(0, responseType)
		val errorBodyType = getParameterUpperBound(1, responseType)
		val errorBodyConverter =
			retrofit.nextResponseBodyConverter<BaseResponse<Any>>(null, errorBodyType, annotations)
		return NetworkResponseAdapter<BaseResponse<Any>, BaseResponse<Any>>(
			successBodyType,
			errorBodyConverter
		)
	}
}
