package com.melegy.retrofitcoroutines

sealed class CustomResponse<T> {
	class Success<T>(val response: T, val isCached: Boolean? = null) : CustomResponse<T>()
	class Failure<T>(val error: Error, val isCached: Boolean? = null) : CustomResponse<T>()
	companion object {

		fun <T> success(data: T, isCached: Boolean? = null): CustomResponse<T> {
			return Success(data, isCached)
		}

		fun <T> failure(error: Error, isCached: Boolean? = null): CustomResponse<T> {
			return Failure(error, isCached)
		}
	}
}
