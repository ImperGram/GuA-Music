package org.gua.music

import android.app.Application
import android.content.Context

class ApplicationLoader : Application() {

    companion object {
        lateinit var applicationContext: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        ApplicationLoader.applicationContext = this
    }
}