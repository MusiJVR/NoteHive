package com.notehive.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.notehive.util.LanguageManager
import com.notehive.R
import com.notehive.util.ThemeManager

class ArchiveActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        LanguageManager.applyLanguage(this)
        ThemeManager.applyTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive)
    }
}
