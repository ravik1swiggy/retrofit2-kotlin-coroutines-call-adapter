package com.melegy.retrofitcoroutines.remote.vo

import android.os.SystemClock
import androidx.collection.ArrayMap
import com.orhanobut.logger.Logger
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.TimeUnit
import kotlin.math.abs

/**
 * Created by ravi on 14/10/20.
 * Utility class that decides whether we should fetch some data or not.
 */
class RateLimiter<in KEY>(
	minTtl: Long = 0,
	maxTtl: Long = 0,
	timeUnit: TimeUnit = TimeUnit.SECONDS
) {

	private val mutex = Mutex()
	private val timestamps = ArrayMap<KEY, Long>()
	private val minTtl = timeUnit.toMillis(minTtl)
	private val maxTtl = timeUnit.toMillis(maxTtl)

	suspend fun shouldFetchRemote(key: KEY, oldTimeStamp: Long? = null): Boolean = mutex.withLock {
		oldTimeStamp?.let { timestamps[key] = it }
		val lastFetched = timestamps[key]
		val now = SystemClock.elapsedRealtime()
		if (lastFetched == null) {
			timestamps[key] = now
			return true
		}
		val diff: Long = abs(now - lastFetched)
		val flag = diff !in minTtl..maxTtl
		Logger.e("should fetch diff $diff flag $flag now $now $lastFetched minTtl $minTtl maxTtl $maxTtl")
		if (flag) {
			timestamps[key] = now
			return true
		}
		return false
	}

	suspend fun reset(key: KEY) {
		mutex.withLock { timestamps.remove(key) }
	}
}
