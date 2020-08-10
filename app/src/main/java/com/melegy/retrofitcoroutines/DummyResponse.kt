package com.melegy.retrofitcoroutines

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SuccessResponse(
    @Json(name = "id")
    val id: String,

    @Json(name = "name")
    val name: String,

    @Json(name = "avatar")
    val avatar: String
)

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    @Json(name = "status")
    val status: String,

    @Json(name = "message")
    val message: String
)
