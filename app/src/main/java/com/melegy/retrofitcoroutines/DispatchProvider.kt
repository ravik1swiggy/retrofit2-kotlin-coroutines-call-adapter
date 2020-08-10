package com.melegy.retrofitcoroutines

import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers

/**
 * Created by ravi on 15/05/20.
 */
object DispatchProvider : IDispatchProvider {
	override val ui: CoroutineDispatcher = Dispatchers.Main
	override val io = Dispatchers.IO
	override val default = Dispatchers.Default
	override val unconfined = Dispatchers.Unconfined
	override val mainImmediate: CoroutineDispatcher = Dispatchers.Main.immediate
	override val exceptionHandler by lazy {
		CoroutineExceptionHandler { cx, exception ->
			Logger.e("${cx.javaClass.simpleName} $exception ${this.hashCode()} ")
		}
	}
}

interface IDispatchProvider {
	companion object :
		CustomDependency<IDispatchProvider> by object : CustomProvider<IDispatchProvider>({
			DispatchProvider
		}) {}

	val ui: CoroutineDispatcher
	val io: CoroutineDispatcher
	val default: CoroutineDispatcher
	val unconfined: CoroutineDispatcher
	val mainImmediate: CoroutineDispatcher
	val exceptionHandler: CoroutineExceptionHandler
}
