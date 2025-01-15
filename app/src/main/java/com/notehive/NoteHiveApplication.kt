package com.notehive

import android.app.Application
import com.notehive.util.LanguageManager
import com.notehive.util.ThemeManager

class NoteHiveApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        LanguageManager.applyLanguage(this)
        ThemeManager.applyTheme(this)
    }
}
