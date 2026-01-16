package com.example.myapplicationview

import android.app.Application
import com.example.myapplicationview.core.util.timberInit
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        timberInit()
    }
}
