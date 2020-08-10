package com.melegy.retrofitcoroutines

import android.app.Application
import android.os.Build
import android.os.StrictMode
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

class MyApplication : Application() {

	override fun onCreate() {
		super.onCreate()
		Logger.addLogAdapter(AndroidLogAdapter())
	}

	private fun turnOnStrictMode() {
		if (BuildConfig.DEBUG) {
			/**
			 * Doesn't enable anything on the main thread that related
			 * to resource access.
			 */
			StrictMode.setThreadPolicy(
				StrictMode.ThreadPolicy.Builder()
					.detectAll()
					.detectNetwork()
					.apply {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
							detectUnbufferedIo()
						}
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
							detectResourceMismatches()
						}
					}
					//.detectDiskReads()
					.detectDiskWrites()
					.detectCustomSlowCalls()
					.penaltyLog()
					.penaltyFlashScreen()
					.penaltyDropBox()
					//.penaltyDialog()
					//.penaltyDeath()
					.build()
			)
			/**
			 * Doesn't enable any leakage of the application's components.
			 */
			StrictMode.setVmPolicy(
				StrictMode.VmPolicy.Builder()
					.detectAll()
					.detectLeakedSqlLiteObjects()
					.detectActivityLeaks()
					.detectLeakedClosableObjects()
					.detectLeakedRegistrationObjects()
					.detectFileUriExposure()
					.apply {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
							detectCleartextNetwork()
						}
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
							detectContentUriWithoutPermission()
							detectUntaggedSockets()
						}
					}
					.penaltyLog()
					.penaltyDropBox()
					//.penaltyDeath()
					.build()
			)
		}
	}

}
