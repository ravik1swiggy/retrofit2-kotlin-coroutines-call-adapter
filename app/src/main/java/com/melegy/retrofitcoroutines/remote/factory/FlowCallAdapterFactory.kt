package com.melegy.retrofitcoroutines.remote.factory

import com.melegy.retrofitcoroutines.remote.calladapter.BodyCallAdapter
import com.melegy.retrofitcoroutines.remote.calladapter.FlowCallAdapter
import kotlinx.coroutines.flow.Flow
import retrofit2.CallAdapter
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by ravi on 11/10/20.
 */
internal class FlowCallAdapterFactory private constructor() : CallAdapter.Factory() {

	companion object {
		@JvmStatic
		val flowCallAdapterFactory by lazy { FlowCallAdapterFactory() }
	}

	override fun get(
		returnType: Type,
		annotations: Array<Annotation>,
		retrofit: Retrofit
	): CallAdapter<*, *>? {
		if (getRawType(returnType) != Flow::class.java) return null
		check(returnType is ParameterizedType) { "Flow return type must be parameterized as Flow<Foo> or Flow<out Foo>" }
		val responseType = getParameterUpperBound(0, returnType)
		val rawType = getRawType(responseType)
		return if (rawType == Response::class.java) {
			check(responseType is ParameterizedType) { "Response must be parameterized as Response<Foo> or Response<out Foo>" }
			FlowCallAdapter<Any>(getParameterUpperBound(0, responseType))
		} else {
			BodyCallAdapter<Any>(responseType)
		}
	}

}
