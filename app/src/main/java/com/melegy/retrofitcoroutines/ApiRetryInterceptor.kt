package com.melegy.retrofitcoroutines

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by ravi on 08/07/20.
 */
class ApiRetryInterceptor(val context: Context) : Interceptor {

	@Throws(IOException::class)
	override fun intercept(chain: Interceptor.Chain): Response {
		val originalRequest = chain.request()
		if (!isNetworkAvailable(context)) {
			throw NetworkConnectionException("Network NOT available")
		}
		return chain.proceed(originalRequest)
	}

	fun isNetworkAvailable(context: Context): Boolean {
		val connectivityManager =
			context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		val mActiveNetworkInfo = connectivityManager.activeNetworkInfo
		return mActiveNetworkInfo != null && mActiveNetworkInfo.isConnected
	}

}
