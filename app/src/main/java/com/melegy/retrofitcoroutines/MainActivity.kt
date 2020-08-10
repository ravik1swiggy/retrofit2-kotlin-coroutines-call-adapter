package com.melegy.retrofitcoroutines

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.melegy.retrofitcoroutines.remote.GenericResponse
import com.melegy.retrofitcoroutines.remote.NetworkResponse
import com.melegy.retrofitcoroutines.remote.NetworkResponseAdapterFactory
import com.squareup.moshi.Moshi
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		val moshi = Moshi.Builder().build()
		val okHttpClient = OkHttpClient.Builder()
			.addInterceptor(ApiRetryInterceptor(this))
			.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
			.build()
		val retrofit = createRetrofit(moshi, okHttpClient)
		val service = retrofit.create<ApiService>()

		testButton.setOnClickListener {
			initNetworkCall(service)
		}
		initNetworkCall(service)
	}

	private fun initNetworkCall(service: ApiService) {
		dummyText.post {
			dummyText.text = ""
		}
		dummyText2.post {
			dummyText2.text = ""
		}
		dummyText3.post {
			dummyText2.text = ""
		}
		GlobalScope.launch {
			val response1 = service.getSuccess()
			dummyText.post {
				dummyText.text = "$response1"
			}
			when (response1) {
				is NetworkResponse.Success -> Log.d(
					TAG,
					"Success response1.body ${response1.body}"
				)
				is NetworkResponse.ApiError -> Log.d(
					TAG,
					"ApiError response1.body ${response1.body}"
				)
				is NetworkResponse.NetworkError -> Log.d(TAG, "NetworkError")
				is NetworkResponse.UnknownError -> Log.d(TAG, "UnknownError")
			}
			val response2 = service.getError()
			dummyText2.post {
				dummyText2.text = "$response2"
			}
			when (response2) {
				is NetworkResponse.Success -> Log.d(TAG, "Success response2.body ${response2.body}")
				is NetworkResponse.ApiError -> Log.d(
					TAG,
					"ApiError response2.body ${response2.body}"
				)
				is NetworkResponse.NetworkError -> Log.d(TAG, "NetworkError")
				is NetworkResponse.UnknownError -> Log.d(TAG, "UnknownError")
			}
			val response3 = service.getFailure()
			dummyText3.post {
				dummyText3.text = "$response3"
			}
			when (response3) {
				is NetworkResponse.Success -> Log.d(TAG, "Success response3.body ${response3.body}")
				is NetworkResponse.ApiError -> Log.d(
					TAG,
					"ApiError response3.body ${response3.body}"
				)
				is NetworkResponse.NetworkError -> Log.d(TAG, "NetworkError")
				is NetworkResponse.UnknownError -> Log.d(TAG, "UnknownError")
			}
		}
	}

	interface ApiService {

		@GET("success")
		suspend fun getSuccess(): GenericResponse<BaseResponse<Success>>

		@GET("error")
		suspend fun getError(): GenericResponse<BaseResponse<Error>>

		@GET("failure")
		suspend fun getFailure(): GenericResponse<BaseResponse<Error>>
	}

	private fun createRetrofit(moshi: Moshi, client: OkHttpClient): Retrofit {
		return Retrofit.Builder()
			.baseUrl("https://7dac5580-146f-4be7-a44f-e770ee8a9565.mock.pstmn.io/")
			.addCallAdapterFactory(NetworkResponseAdapterFactory())
			.addConverterFactory(MoshiConverterFactory.create(moshi))
			.client(client)
			.build()
	}


	companion object {
		private val TAG = MainActivity::class.java.simpleName
	}
}
