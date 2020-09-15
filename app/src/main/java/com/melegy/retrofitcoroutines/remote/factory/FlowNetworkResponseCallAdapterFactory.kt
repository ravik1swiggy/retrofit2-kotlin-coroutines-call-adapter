package com.melegy.retrofitcoroutines.remote.factory

import com.melegy.retrofitcoroutines.BaseResponse
import com.melegy.retrofitcoroutines.remote.calladapter.FlowNetworkResponseCallAdapter
import com.melegy.retrofitcoroutines.remote.vo.ResponseV2
import kotlinx.coroutines.flow.Flow
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by ravi on 09/08/20.
 */
class FlowNetworkResponseCallAdapterFactory private constructor() : CallAdapter.Factory() {

	companion object {
		@JvmStatic
		fun create() =
			FlowNetworkResponseCallAdapterFactory()
	}

	override fun get(
		returnType: Type,
		annotations: Array<Annotation>,
		retrofit: Retrofit
	): CallAdapter<*, *>? {
		if (getRawType(returnType) != Flow::class.java) {
			return null
		}
		check(returnType is ParameterizedType) {
			"Flow return type must be parameterized as Flow<Foo> or Flow<out Foo>"
		}
		val responseType = getParameterUpperBound(0, returnType)
		if (getRawType(responseType) != ResponseV2::class.java) {
			return null
		}
		check(responseType is ParameterizedType) {
			"Response must be parameterized as Response<Foo> or Response<out Foo>"
		}
		val successBodyType = getParameterUpperBound(0, responseType)
		val errorBodyType = getParameterUpperBound(1, responseType)
		val errorBodyConverter =
			retrofit.nextResponseBodyConverter<BaseResponse<Any>>(
				null, errorBodyType, annotations
			)
		return FlowNetworkResponseCallAdapter<BaseResponse<Any>, BaseResponse<Any>>(
			successBodyType,
			errorBodyConverter
		)
	}
}
