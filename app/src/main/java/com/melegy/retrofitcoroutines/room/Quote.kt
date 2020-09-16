package com.melegy.retrofitcoroutines.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.melegy.retrofitcoroutines.remote.vo.CacheModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "quote")
@JsonClass(generateAdapter = true)
data class Quote(
	@ColumnInfo(name = "_internalId")
	@Json(name = "_id")
	val _internalId: String,
	@ColumnInfo(name = "quote_id") val id: String,
	@Json(name = "en") var title: String,
	val author: String
): CacheModel<Int>(0)
