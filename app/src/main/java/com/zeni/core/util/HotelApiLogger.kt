package com.zeni.core.util

import android.util.Log

object HotelApiLogger {
    private const val TAG_DB = "HotelApi"

    fun apiOperation(message: String) {
        Log.d(TAG_DB, message)
    }

    fun apiError(message: String, throwable: Throwable? = null) {
        Log.e(TAG_DB, message, throwable)
    }
}