package com.melegy.retrofitcoroutines.remote.vo

import android.os.SystemClock
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

abstract class CacheModel<T>(
	@PrimaryKey @ColumnInfo(name = "id") var primaryId: T,
	@ColumnInfo(name = "created_at")
	val createdAt: Long = SystemClock.elapsedRealtime()
)
