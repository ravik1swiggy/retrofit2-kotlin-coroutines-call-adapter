package com.melegy.retrofitcoroutines.remote.factory

import com.melegy.retrofitcoroutines.remote.factory.RetrofitException.Companion.asRetrofitException
import com.orhanobut.logger.Logger
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function
import retrofit2.*
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.IOException
import java.lang.reflect.Type

/**
 * Created by ravi on 09/08/20.
 */
internal class LegacyResponseAdapterFactory private constructor() : CallAdapter.Factory() {

	private val original: RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

	companion object {
		fun create() = LegacyResponseAdapterFactory()
	}

	override fun get(
		returnType: Type,
		annotations: Array<Annotation>,
		retrofit: Retrofit
	): CallAdapter<*, *>? {
		return RxCallAdapterWrapper(original.get(returnType, annotations, retrofit) ?: return null)
	}

	private class RxCallAdapterWrapper<R>(private val wrapped: CallAdapter<R, *>) :
		CallAdapter<R, Any> {

		override fun responseType(): Type {
			return wrapped.responseType()
		}

		override fun adapt(call: Call<R>): Any {
			//return wrapped.adapt(call)
			Logger.e("LRAF ${call.request()}")
			call.enqueue(object : Callback<R> {
				override fun onResponse(call: Call<R>, response: Response<R>) {
				}

				override fun onFailure(call: Call<R>, t: Throwable) {
				}
			})
			return when (val result = wrapped.adapt(call)) {
				is Single<*> -> {
					result.onErrorResumeNext(Function { throwable ->
						Single.error(
							asRetrofitException(throwable)
						)
					})
				}
				is Observable<*> -> {
					result.onErrorResumeNext(Function { throwable ->
						Observable.error(
							asRetrofitException(throwable)
						)
					})
				}
				is Completable -> {
					result.onErrorResumeNext(Function { throwable ->
						Completable.error(
							asRetrofitException(throwable)
						)
					})
				}
				else -> result
			}
		}
	}
}

data class SwiggyExpiredTokenException(override val message: String? = null) : IOException(message)
