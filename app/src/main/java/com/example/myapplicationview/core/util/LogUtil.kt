package com.example.myapplicationview.core.util

import android.util.Log
import com.example.myapplicationview.BuildConfig
import timber.log.Timber

inline fun <T : Any> T.log(format: (T) -> String = Any::toString): T {
    Timber.d(format(this))
    return this
}

inline fun <T : Any> T.logI(format: (T) -> String = Any::toString): T {
    Timber.i(format(this))
    return this
}

fun timberInit() {
    if (BuildConfig.DEBUG) {
        Timber.plant(Timber.DebugTree())
    } else {
        Timber.plant(object : Timber.Tree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                if (BuildConfig.DEBUG || priority == Log.VERBOSE || priority == Log.DEBUG) {
                    return
                }
                super.log(priority, tag, message, t)
            }
        })
    }
}
