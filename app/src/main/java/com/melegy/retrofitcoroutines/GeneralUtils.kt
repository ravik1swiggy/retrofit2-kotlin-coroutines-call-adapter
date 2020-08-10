package com.melegy.retrofitcoroutines

object GeneralUtils {

	fun <T> unsafeLazy(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)
}
