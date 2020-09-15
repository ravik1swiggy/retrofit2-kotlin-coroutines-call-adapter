package com.melegy.retrofitcoroutines

import io.reactivex.FlowableTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by ravi on 27/02/20.
 */

@Suppress("UNCHECKED_CAST")
object SwiggyRxSchedulers {

    //  Observable transformer
    private var transformer: ObservableTransformer<Any, Any> = ObservableTransformer { upstream ->
        upstream
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    internal fun <T> applySchedulers(): ObservableTransformer<T, T> {
        return transformer as ObservableTransformer<T, T>
    }

    //  Flowable transformer
    private var flowableTransformer: FlowableTransformer<Any, Any> = FlowableTransformer { upstream ->
        upstream
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    internal fun <T> applyFlowableSchedulers(): FlowableTransformer<T, T> {
        return flowableTransformer as FlowableTransformer<T, T>
    }

    //  Single transformer
    private var singleTransformer: SingleTransformer<Any, Any> = SingleTransformer { upstream ->
        upstream
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    internal fun <T> applySingleSchedulers(): SingleTransformer<T, T> {
        return singleTransformer as SingleTransformer<T, T>
    }
}
