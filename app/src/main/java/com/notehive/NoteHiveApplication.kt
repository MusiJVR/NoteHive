package com.notehive

import android.app.Application

class NoteHiveApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        LanguageManager.applyLanguage(this)
        ThemeManager.applyTheme(this)
    }
}
