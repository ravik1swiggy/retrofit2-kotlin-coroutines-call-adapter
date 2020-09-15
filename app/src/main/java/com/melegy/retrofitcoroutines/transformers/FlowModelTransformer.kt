package com.melegy.retrofitcoroutines.transformers

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by ravi on 10/10/20.
 */
abstract class FlowModelTransformer<in P, out R> : FlowUseCase<P, R>() {

	override var coroutineDispatcher: () -> CoroutineDispatcher = { default }

}
