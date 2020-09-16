package com.melegy.retrofitcoroutines.remote.vo

import android.os.SystemClock
import java.util.concurrent.TimeUnit
import kotlin.math.abs

/**
 * Created by ravi on 14/10/20.
 * Utility class that decides whether we should fetch some data or not.
 */
class RateLimiter(
	minTtl: Long = 0, maxTtl: Long = 0, timeUnit: TimeUnit = TimeUnit.SECONDS
) {

	private val minTtl = timeUnit.toMillis(minTtl)
	private val maxTtl = timeUnit.toMillis(maxTtl)

	fun shouldFetchRemote(oldTimeStamp: Long? = null): Boolean =
		oldTimeStamp?.let { old ->
			abs(SystemClock.elapsedRealtime() - old) !in minTtl..maxTtl
		} ?: true

}
