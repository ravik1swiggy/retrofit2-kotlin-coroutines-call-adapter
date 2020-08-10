package com.melegy.retrofitcoroutines.remote

sealed class Error(val tid: String? = null, val sid: String? = null) {
	class BlackZoneError(val title: String?, val message: String?, tid: String?, sid: String?) : Error(tid, sid)
	class DescriptiveError(val code: Int, val message: String, tid: String?, sid: String?) : Error(tid, sid)
	class InternalError(val statusTitle: String? = null, val statusMessage: String? = null, val code: Int? = null) : Error()
	class LocationNotServiceableError(tid: String?, sid: String?) : Error(tid, sid)
	class ThrottleError : Error()
	class UnhandledError(val message: String, val code: Int? = null, tid: String? = null, sid: String? = null) : Error(tid, sid)
	class ExpiredTokenError(val message: String? = null) : Error()
	class UnhandledExceptionError(val error: Throwable? = null) : Error()
	class NoNetworkError : Error()
}
