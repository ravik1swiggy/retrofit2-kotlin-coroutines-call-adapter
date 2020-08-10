package com.melegy.retrofitcoroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.melegy.retrofitcoroutines.remote.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
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
		val okHttpClient = initOkHttpClient()
		val retrofit = createRetrofit(okHttpClient)
		val service = retrofit.create<ApiService>()
		val service2 = retrofit.create<ApiService2>()
		testButton.setOnClickListener {
			clearTexts()
			initServiceNetworkCall(service)
			initServiceNetworkCall2(service2)
		}
		initServiceNetworkCall(service)
		initServiceNetworkCall2(service2)
	}

	private fun initServiceNetworkCall(service: ApiService) {
		GlobalScope.launch {
			when (val response1 = service.getSuccess()) {
				is NetworkResponse.Success -> {
					dummyText.post {
						dummyText.text = "${response1.body?.data}"
					}
				}
				is NetworkResponse.Failure -> {
					dummyText.post {
						dummyText.text = "${response1.body?.data}"
					}
				}
			}
			when (val response2 = service.getError()) {
				is NetworkResponse.Success -> {
					dummyText2.post {
						dummyText2.text = "${response2.body}"
					}
				}
				is NetworkResponse.Failure -> {
					dummyText2.post {
						dummyText2.text = "${response2.body}"
					}
				}
			}
			when (val response3 = service.getFailure()) {
				is NetworkResponse.Success -> {
					dummyText3.post {
						dummyText3.text = "${response3.body}"
					}
				}
				is NetworkResponse.Failure -> {
					dummyText3.post {
						dummyText3.text = "${response3.body}"
					}
				}
			}
		}
	}

	private fun initServiceNetworkCall2(service: ApiService2) {
		GlobalScope.launch {
			service.getSuccess().collect {
				when (val response4 = it) {
					is NetworkResponse.Success -> {
						dummyText4.post {
							dummyText4.text = "${response4.body?.data}"
						}
					}
					is NetworkResponse.Failure -> {
						dummyText4.post {
							dummyText4.text = "${response4.body?.data}"
						}
					}
				}
			}
			service.getError().collect {
				when (val response5 = it) {
					is NetworkResponse.Success -> {
						dummyText5.post {
							dummyText5.text = "${response5.body}"
						}
					}
					is NetworkResponse.Failure -> {
						dummyText5.post {
							dummyText5.text = "${response5.body}"
						}
					}
				}
			}
			service.getFailure().collect {
				when (val response6 = it) {
					is NetworkResponse.Success -> {
						dummyText6.post {
							dummyText6.text = "${response6.body}"
						}
					}
					is NetworkResponse.Failure -> {
						dummyText6.post {
							dummyText6.text = "${response6.body}"
						}
					}
				}
			}
		}
	}

	interface ApiService {

		@GET("success")
		suspend fun getSuccess(): GenericResponse<SuccessResponse>

		@GET("error")
		suspend fun getError(): GenericResponse<ErrorResponse>

		@GET("failure")
		suspend fun getFailure(): GenericResponse<ErrorResponse>

	}

	interface ApiService2 {

		@GET("success")
		fun getSuccess(): FlowResponse<SuccessResponse>

		@GET("error")
		fun getError(): FlowResponse<ErrorResponse>

		@GET("failure")
		fun getFailure(): FlowResponse<ErrorResponse>

	}

	private fun createRetrofit(client: OkHttpClient): Retrofit {
		return Retrofit.Builder()
			.baseUrl("https://7dac5580-146f-4be7-a44f-e770ee8a9565.mock.pstmn.io/")
			.addCallAdapterFactory(NetworkAdapterFactory.create())
			.addCallAdapterFactory(FlowCallAdapterFactory.create())
			.addConverterFactory(MoshiConverterFactory.create())
			.client(client)
			.build()
	}

	private fun initOkHttpClient() = OkHttpClient.Builder()
		.addInterceptor(ApiRetryInterceptor(this))
		.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
		.build()

	private fun clearTexts() {
		dummyText.post { dummyText.text = "" }
		dummyText2.post { dummyText2.text = "" }
		dummyText3.post { dummyText3.text = "" }
		dummyText4.post { dummyText4.text = "" }
		dummyText5.post { dummyText5.text = "" }
		dummyText6.post { dummyText6.text = "" }
	}
}
