package com.notehive

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ArchiveActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.applyTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive)
    }
}
