package com.melegy.retrofitcoroutines.room

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuoteResponse(
	@Json(name = "_id")
	val _internalId: String,
	val id: String,
	@Json(name = "en")
	var title: String,
	val author: String
)
