package com.example.myapplicationview

import android.app.Application
import com.example.myapplicationview.core.util.timberInit

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        timberInit()
    }
}
