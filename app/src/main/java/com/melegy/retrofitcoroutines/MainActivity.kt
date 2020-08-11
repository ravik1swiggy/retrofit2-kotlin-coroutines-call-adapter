package com.melegy.retrofitcoroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.melegy.retrofitcoroutines.remote.factory.CoroutineNetworkResponseAdapterFactory
import com.melegy.retrofitcoroutines.remote.factory.CoroutineResponseAdapterFactory
import com.melegy.retrofitcoroutines.remote.factory.FlowNetworkResponseCallAdapterFactory
import com.melegy.retrofitcoroutines.remote.factory.FlowResponseCallAdapterFactory
import com.melegy.retrofitcoroutines.remote.vo.*
import com.melegy.retrofitcoroutines.room.QuoteDatabase
import com.melegy.retrofitcoroutines.room.QuoteResponse
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET

@OptIn(ExperimentalCoroutinesApi::class)
class MainActivity : AppCompatActivity() {

	private val database by lazy { QuoteDatabase(this) }
	private val quoteDao by lazy { database.quoteDao() }
	private lateinit var service: ApiService
	private lateinit var service2: ApiService2

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		val okHttpClient = initOkHttpClient()
		val retrofit = createRetrofit(okHttpClient)
		service = retrofit.create()
		service2 = retrofit.create()
		testButton.setOnClickListener {
			clearTexts()
			initServiceNetworkCall()
			initServiceNetworkCall2()
		}
		initServiceNetworkCall()
		initServiceNetworkCall2()
	}

	private fun initServiceNetworkCall() {
		GlobalScope.launch {
			/*when (val response1 = service.getSuccess()) {
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
			}*/
			/*when (val response2 = service.getError()) {
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
			}*/
			/*when (val response3 = service.getFailure()) {
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
			}*/
			when (val response2 = service.getRandom()) {
				is NetworkResponse.Success -> {
					dummyText2.post {
						dummyText2.text = "${response2.data}"
					}
				}
				is NetworkResponse.Failure -> {
					dummyText2.post {
						dummyText2.text = "${response2.data}"
					}
				}
			}
			when (val response3 = service.getRandom2()) {
				is Response.Success -> {
					dummyText3.post {
						dummyText3.text = "${response3.data}"
					}
				}
				is Response.Failure -> {
					dummyText3.post {
						dummyText3.text = "${response3.data}"
					}
				}
			}
		}
	}

	private fun initServiceNetworkCall2() {
		GlobalScope.launch {
			/*service2.getSuccess().collect {
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
			}*/
			/*service2.getError().collect {
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
			}*/
			/*service2.getRandom().collect {
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
			}*/
			service2.getRandom().collect {
				when (val response5 = it) {
					is NetworkResponse.Success -> {
						dummyText5.post {
							dummyText5.text = "${response5.data}"
						}
					}
					is NetworkResponse.Failure -> {
						dummyText5.post {
							dummyText5.text = "${response5.data}"
						}
					}
				}
			}
			service2.getRandom2().collect {
				when (val response6 = it) {
					is Response.Success -> {
						dummyText6.post {
							dummyText6.text = "${response6.data}"
						}
					}
					is Response.Failure -> {
						dummyText6.post {
							dummyText6.text = "${response6.data}"
						}
					}
				}
			}
		}
	}

	interface ApiService {

		@GET("success")
		suspend fun getSuccess(): GenericNetworkResponse<SuccessResponse>

		@GET("error")
		suspend fun getError(): GenericNetworkResponse<ErrorResponse>

		@GET("failure")
		suspend fun getFailure(): GenericNetworkResponse<ErrorResponse>

		@GET("random")
		suspend fun getRandom(): GenericNetworkResponse<QuoteResponse>

		@GET("random")
		suspend fun getRandom2(): GenericResponse<QuoteResponse>

	}

	interface ApiService2 {

		@GET("success")
		fun getSuccess(): FlowNetworkResponse<SuccessResponse>

		@GET("error")
		fun getError(): FlowNetworkResponse<ErrorResponse>

		@GET("failure")
		fun getFailure(): FlowNetworkResponse<ErrorResponse>

		@GET("random")
		fun getRandom(): FlowNetworkResponse<QuoteResponse>

		@GET("random")
		fun getRandom2(): FlowResponse<QuoteResponse>

	}

	private fun createRetrofit(client: OkHttpClient): Retrofit {
		return Retrofit.Builder()
			.baseUrl("https://7dac5580-146f-4be7-a44f-e770ee8a9565.mock.pstmn.io/")
			.addCallAdapterFactory(CoroutineNetworkResponseAdapterFactory.create())
			.addCallAdapterFactory(CoroutineResponseAdapterFactory.create())
			.addCallAdapterFactory(FlowNetworkResponseCallAdapterFactory.create())
			.addCallAdapterFactory(FlowResponseCallAdapterFactory.create())
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
