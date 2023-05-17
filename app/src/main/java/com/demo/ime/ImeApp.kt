package com.demo.ime

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ImeApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}