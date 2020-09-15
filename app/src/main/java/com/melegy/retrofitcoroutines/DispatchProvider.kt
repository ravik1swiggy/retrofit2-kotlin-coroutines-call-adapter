package com.melegy.retrofitcoroutines

import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

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
	fun test() {
		val t = object :IDispatchProvider{
			override val ui: CoroutineDispatcher
				get() = TODO("Not yet implemented")
			override val io: CoroutineDispatcher
				get() = TODO("Not yet implemented")
			override val default: CoroutineDispatcher
				get() = TODO("Not yet implemented")
			override val unconfined: CoroutineDispatcher
				get() = TODO("Not yet implemented")
			override val mainImmediate: CoroutineDispatcher
				get() = TODO("Not yet implemented")
			override val exceptionHandler: CoroutineExceptionHandler
				get() = TODO("Not yet implemented")

		}
	}
}

val dispatchProvider by lazy { IDispatchProvider.get() }
val supervisorJob by lazy { SupervisorJob() }
val exceptionHandler: CoroutineExceptionHandler by lazy { dispatchProvider.exceptionHandler }

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
