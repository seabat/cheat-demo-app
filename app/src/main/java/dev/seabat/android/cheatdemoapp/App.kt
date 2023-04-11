package dev.seabat.android.cheatdemoapp

import android.app.Application
import android.content.Context

val sharedApplicationContext: Context
    get() = _sharedApplicationContext
    ?: throw IllegalStateException(
        "Application context not initialized yet."
    )
private var _sharedApplicationContext: Context? = null

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        _sharedApplicationContext = applicationContext
    }
}