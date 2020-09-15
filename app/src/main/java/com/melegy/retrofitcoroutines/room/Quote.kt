package com.melegy.retrofitcoroutines.room

import android.os.SystemClock
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

const val ID = 0

@Entity(tableName = "quote")
@JsonClass(generateAdapter = true)
data class Quote(
	@ColumnInfo(name = "_internalId")
	@Json(name = "_id")
	val _internalId: String,
	@ColumnInfo(name = "quote_id")
	val id: String,
	@Json(name = "en")
	var title: String,
	val author: String,
	@ColumnInfo(name = "created_at")
	val createdAt: Long = SystemClock.elapsedRealtime()
) {
	@PrimaryKey(autoGenerate = false)
	@ColumnInfo(name = "id")
	var primaryId: Int = ID
}
