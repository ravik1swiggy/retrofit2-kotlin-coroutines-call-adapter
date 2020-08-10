package com.melegy.retrofitcoroutines

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseResponse<T>(
	@Json(name = "statusCode")
	val statusCode: Int = -1,

	@Json(name = "statusMessage")
	val statusMessage: String = "",

	@Json(name = "data")
	val data: T? = null
) {

	companion object {
		const val OK_STATUS_CODE = 0
		const val NOT_OK_STATUS_CODE = 1
		const val SESSION_INVALID_STATUS_CODE = 999
	}

	fun isResponseOk(): Boolean {
		return statusCode == OK_STATUS_CODE
	}
}
