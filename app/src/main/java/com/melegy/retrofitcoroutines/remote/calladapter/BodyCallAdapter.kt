package com.melegy.retrofitcoroutines.remote.calladapter

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

/**
 * Created by ravi on 11/10/20.
 */
class BodyCallAdapter<T>(private val responseType: Type) : CallAdapter<T, Flow<T>> {

	@ExperimentalCoroutinesApi
	override fun adapt(call: Call<T>): Flow<T> {
		return callbackFlow {
			call.enqueue(object : Callback<T> {
				override fun onResponse(call: Call<T>, response: Response<T>) {
					try {
						offer(response.body()!!)
					} catch (e: Exception) {
						close(e)
					}
				}

				override fun onFailure(call: Call<T>, t: Throwable) {
					close(t)
				}
			})
			awaitClose { call.cancel() }
		}
	}

	override fun responseType() = responseType
}
