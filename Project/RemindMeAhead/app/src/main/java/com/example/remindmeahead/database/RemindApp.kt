package com.example.remindmeahead.database

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RemindApp:Application() {
    override fun onCreate() {
        super.onCreate()
    }
}