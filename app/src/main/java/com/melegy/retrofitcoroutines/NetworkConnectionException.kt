package com.melegy.retrofitcoroutines

import java.io.IOException

class NetworkConnectionException(message: String?) :
	IOException(message)
