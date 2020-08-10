package com.melegy.retrofitcoroutines

import com.melegy.retrofitcoroutines.GeneralUtils.unsafeLazy

/**
 * Created by ravi on 13/11/19.
 */
//UT usage --> Something.mocked = object: Something { /*...*/ }.
interface CustomDependency<T> {
    var mocked: T?
    fun get(): T
    fun lazyGet(): Lazy<T> = lazy { get() }
    fun unSafeLazyGet(): Lazy<T> = unsafeLazy { get() }
}

abstract class CustomProvider<T>(val init: () -> T) :
    CustomDependency<T> {
    private var original: T? = null
    override var mocked: T? = null
    override fun get(): T = mocked ?: original ?: init()
        .apply { original = this }
}
