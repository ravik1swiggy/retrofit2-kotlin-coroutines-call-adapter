package com.melegy.retrofitcoroutines

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableInt
import androidx.lifecycle.lifecycleScope
import com.melegy.retrofitcoroutines.remote.buildResponse
import com.melegy.retrofitcoroutines.remote.factory.FlowCallAdapterFactory.Companion.flowCallAdapterFactory
import com.melegy.retrofitcoroutines.remote.networkBoundResource
import com.melegy.retrofitcoroutines.remote.vo.FlowRetroResponse
import com.melegy.retrofitcoroutines.remote.vo.RateLimiter
import com.melegy.retrofitcoroutines.remote.vo.Response
import com.melegy.retrofitcoroutines.room.Quote
import com.melegy.retrofitcoroutines.room.QuoteDatabase
import com.melegy.retrofitcoroutines.room.QuoteResponse
import com.melegy.retrofitcoroutines.transformers.QuoteTransformer
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET

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

	@SuppressLint("SetTextI18n")
	private fun initServiceNetworkCall() {
		lifecycleScope.launch {
			/*when (val response = service.getSuccess()) {
				is ResponseV2.Success -> {
					dummyText.post {
						dummyText.text = "Success ${response.response}"
					}
				}
				is ResponseV2.Failure -> {
					dummyText.post {
						dummyText.text = "Failure ${response.response}"
					}
				}
			}
			service.getSuccessRx().map { it.body() }
				.compose(SwiggyRxSchedulers.applySingleSchedulers())
				.subscribe { t1, _ ->
					dummyText2.post {
						dummyText2.text = "$t1"
					}
				}
			service.getSuccessRx2().map { it }
				.compose(SwiggyRxSchedulers.applySingleSchedulers())
				.subscribe { t1, _ ->
					dummyText3.post {
						dummyText3.text = "$t1"
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
			/*when (val response = service.getRandom()) {
				is NetworkResponse.Success -> {
					dummyText2.post {
						dummyText2.text = "${response.data}"
					}
				}
				is NetworkResponse.Failure -> {
					dummyText2.post {
						dummyText2.text = "${response.data}"
					}
				}
			}
			when (val response = service.getRandom2()) {
				is Response.Success -> {
					dummyText3.post {
						dummyText3.text = "${response.data}"
					}
				}
				is Response.Failure -> {
					dummyText3.post {
						dummyText3.text = "${response.data}"
					}
				}
			}*/
		}
	}

	@SuppressLint("SetTextI18n")
	private fun initServiceNetworkCall2() {
		lifecycleScope.launch {
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
			/*service2.getRandom().collect {
				when (val response = it) {
					is NetworkResponse.Success -> {
						dummyText5.post {
							dummyText5.text = "${response.data}"
						}
					}
					is NetworkResponse.Failure -> {
						dummyText5.post {
							dummyText5.text = "${response.data}"
						}
					}
				}
			}
			service2.getRandom2().collect {
				when (val response = it) {
					is Response.Success -> {
						dummyText6.post {
							dummyText6.text = "${response.data}"
						}
					}
					is Response.Failure -> {
						dummyText6.post {
							dummyText6.text = "${response.data}"
						}
					}
				}
			}*/
			/*getRandomQuoteNoCache().collect {
				Logger.e("getRandomQuoteNoCache $it")
				when (val response = it) {
					is Response.Success -> {
						dummyText4.post {
							dummyText4.text = "Success $response"
						}
					}
					is Response.Failure -> {
						dummyText4.post {
							dummyText4.text = "Failure $response"
						}
					}
				}
			}*/
			getRandomQuote().collect {
				Logger.e("getRandomQuote $it")
				when (val response = it) {
					is Response.Success -> {
						dummyText5.post {
							dummyText5.text = "Success $response"
						}
					}
					is Response.Failure -> {
						dummyText5.post {
							dummyText5.text = "Failure $response"
						}
					}
				}
			}
		}
	}

	val quoteTransformer = QuoteTransformer()

	val randomRateLimiter = RateLimiter(0, 10)

	val flag = ObservableInt(1)

	fun getRandomQuote(): Flow<Response<Quote>> {
		return networkBoundResource(
			fetchFromLocal = { quoteDao.getQuote(responseHash) },
			shouldFetchFromRemote = {
				val rateFlag = randomRateLimiter.shouldFetchRemote(it?.createdAt)
				val flag = it == null || rateFlag
				Logger.e("should fetch shouldFetchFromRemote $responseHash rateFlag $rateFlag flag $flag it?.createdAt ${it?.createdAt}")
				flag
			},
			fetchFromRemote = { buildResponse(service2.getRandom(), quoteTransformer) },
			saveRemoteData = {
				it.primaryId = responseHash
				it.title = "${flag.get()} ${it.title}"
				quoteDao.insertOrUpdate(it)
				Logger.e("should fetch saveRemoteData $it")
				flag.set(flag.get() + 1)
			}
		)
	}

	/*private fun getRandomQuoteNoCache(): Flow<Response<QuoteResponse>> {
		return networkBoundResource(
			fetchFromRemote = { buildResponse(service2.getRandom()) },
			shouldCache = { false },
			shouldFetchFromRemote = { true },
			fetchFromLocal = { emptyFlow() },
			onFetchFailed = { }
		)
	}*/

	/*private fun getRandomQuote(): Flow<Response<Quote>> {
		return networkBoundResource(
			fetchFromLocal = { quoteDao.getQuote() },
			fetchFromRemote = { service2.getRandom3() },
			saveRemoteData = { quoteDao.insertOrUpdateQuote(it) }
		)
	}

	private fun getRandomQuoteNoCache(): Flow<Response<QuoteResponse>> {
		return networkBoundResource(
			fetchFromRemote = { service2.getRandom2() },
			shouldCache = { false }
		)
	}*/

	interface ApiService {

		/*@GET("success")
		fun getSuccessRx(): RxRetroResponse<SuccessResponse>

		@GET("success")
		fun getSuccessRx2(): RxResponse<SuccessResponse>

		@GET("success")
		suspend fun getSuccess(): GenericResponseV2<SuccessResponse>

		@GET("error")
		suspend fun getError(): GenericResponseV2<ErrorResponse>

		@GET("failure")
		suspend fun getFailure(): GenericResponseV2<ErrorResponse>

		@GET("random")
		suspend fun getRandom(): GenericResponseV2<QuoteResponse>

		@GET("random")
		suspend fun getRandom2(): GenericResponse<QuoteResponse>*/

	}

	interface ApiService2 {

		@GET("random")
		fun getRandom(): FlowRetroResponse<QuoteResponse>

		/*@GET("success")
		fun getSuccess(): FlowResponseV2<SuccessResponse>*/

		/*@GET("success")
		fun getSuccess(): FlowResponseV2<SuccessResponse>*/

		/*@GET("error")
		fun getError(): FlowResponseV2<ErrorResponse>

		@GET("failure")
		fun getFailure(): FlowResponseV2<ErrorResponse>

		@GET("random")
		fun getRandom(): FlowResponseV2<QuoteResponse>

		@GET("random")
		fun getRandom2(): FlowResponse<QuoteResponse>

		@GET("random")
		fun getRandom3(): FlowResponse<Quote>*/

	}

	private fun createRetrofit(client: OkHttpClient): Retrofit {
		return Retrofit.Builder()
			.baseUrl("https://7dac5580-146f-4be7-a44f-e770ee8a9565.mock.pstmn.io/")
			.addCallAdapterFactory(flowCallAdapterFactory)
			/*.addCallAdapterFactory(LegacyResponseAdapterFactory.create())
			.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
			.addCallAdapterFactory(CoroutineNetworkResponseAdapterFactory.create())
			.addCallAdapterFactory(CoroutineResponseAdapterFactory.create())
			.addCallAdapterFactory(FlowNetworkResponseCallAdapterFactory.create())
			.addCallAdapterFactory(FlowResponseCallAdapterFactory.create())*/
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
