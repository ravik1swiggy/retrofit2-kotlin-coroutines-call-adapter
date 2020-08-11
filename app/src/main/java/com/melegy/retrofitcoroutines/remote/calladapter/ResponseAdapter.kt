package com.melegy.retrofitcoroutines.remote.calladapter

import com.melegy.retrofitcoroutines.BaseResponse
import com.melegy.retrofitcoroutines.remote.retrofitcall.ResponseCall
import com.melegy.retrofitcoroutines.remote.vo.Response
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class ResponseAdapter<T : BaseResponse<Any>>(
	private val successType: Type
) : CallAdapter<T, Call<Response<T>>> {

	override fun responseType(): Type = successType

	override fun adapt(call: Call<T>): Call<Response<T>> = ResponseCall(call)
}
