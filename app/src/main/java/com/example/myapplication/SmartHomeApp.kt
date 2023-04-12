package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class SmartHomeApp : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set
    }
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}