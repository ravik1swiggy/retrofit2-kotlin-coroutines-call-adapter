package com.melegy.retrofitcoroutines.remote.calladapter

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Response
import retrofit2.awaitResponse
import java.lang.reflect.Type

/**
 * Created by ravi on 11/10/20.
 */
internal class FlowCallAdapter<T>(private val responseType: Type) : CallAdapter<T, Flow<Response<T>>> {

    override fun adapt(call: Call<T>): Flow<Response<T>> = flow { emit(call.awaitResponse()) }

    override fun responseType() = responseType
}
