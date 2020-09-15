package com.melegy.retrofitcoroutines.remote.calladapter

import com.melegy.retrofitcoroutines.BaseResponse
import com.melegy.retrofitcoroutines.remote.vo.ResponseV2
import com.melegy.retrofitcoroutines.remote.retrofitcall.NetworkResponseCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.*
import java.lang.reflect.Type

/**
 * Created by ravi on 09/08/20.
 */
class FlowNetworkResponseCallAdapter<S : BaseResponse<Any>, E : BaseResponse<Any>>(
	private val responseType: Type,
	private val errorBodyConverter: Converter<ResponseBody, E>
) : CallAdapter<S, Flow<ResponseV2<S, E>>> {

	override fun responseType() = responseType

	override fun adapt(call: Call<S>): Flow<ResponseV2<S, E>> = flow {
		emit(NetworkResponseCall(call, errorBodyConverter).await())
	}

}
