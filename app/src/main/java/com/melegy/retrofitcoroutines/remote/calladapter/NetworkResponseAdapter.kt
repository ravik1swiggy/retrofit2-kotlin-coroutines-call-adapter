package com.melegy.retrofitcoroutines.remote.calladapter

import com.melegy.retrofitcoroutines.BaseResponse
import com.melegy.retrofitcoroutines.remote.vo.ResponseV2
import com.melegy.retrofitcoroutines.remote.retrofitcall.NetworkResponseCall
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter
import java.lang.reflect.Type

class NetworkResponseAdapter<S : BaseResponse<Any>, E : BaseResponse<Any>>(
	private val successType: Type,
	private val errorBodyConverter: Converter<ResponseBody, E>
) : CallAdapter<S, Call<ResponseV2<S, E>>> {

	override fun responseType(): Type = successType

	override fun adapt(call: Call<S>): Call<ResponseV2<S, E>> =
		NetworkResponseCall(call, errorBodyConverter)
}
