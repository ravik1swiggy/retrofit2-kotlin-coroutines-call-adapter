package com.melegy.retrofitcoroutines.transformers

import com.melegy.retrofitcoroutines.IDispatchProvider
import com.melegy.retrofitcoroutines.dispatchProvider
import com.melegy.retrofitcoroutines.remote.vo.Error
import com.melegy.retrofitcoroutines.remote.vo.Response
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

/**
 * Created by ravi on 10/10/20.
 */
abstract class FlowUseCase<in P, out R> : IDispatchProvider by dispatchProvider {

	open val coroutineDispatcher: () -> CoroutineDispatcher = { io }

	operator fun invoke(parameters: P): Flow<Response<R>> = execute(parameters)
		.catch { e -> emit(Response.failure(Error.UnhandledExceptionError(e))) }
		.flowOn(coroutineDispatcher.invoke())

	protected abstract fun execute(parameters: P): Flow<Response<R>>
}
