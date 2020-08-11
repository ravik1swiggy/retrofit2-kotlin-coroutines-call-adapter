package com.melegy.retrofitcoroutines.remote.calladapter

import com.melegy.retrofitcoroutines.BaseResponse
import com.melegy.retrofitcoroutines.remote.retrofitcall.ResponseCall
import com.melegy.retrofitcoroutines.remote.vo.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.await
import java.lang.reflect.Type

/**
 * Created by ravi on 09/08/20.
 */
class FlowResponseCallAdapter<T : BaseResponse<Any>>(
	private val responseType: Type
) : CallAdapter<T, Flow<Response<T>>> {

	override fun responseType() = responseType

	override fun adapt(call: Call<T>): Flow<Response<T>> = flow {
		emit(ResponseCall(call).await())
	}

}
