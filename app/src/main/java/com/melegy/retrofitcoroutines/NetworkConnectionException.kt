package com.melegy.retrofitcoroutines

import java.io.IOException

data class NetworkConnectionException(override val message: String? = null) :
	IOException(message)
