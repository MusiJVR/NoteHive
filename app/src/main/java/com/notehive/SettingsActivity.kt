package com.notehive

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class SettingsActivity : AppCompatActivity() {
    companion object {
        var instance: SettingsActivity? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        instance = this
        LanguageManager.applyLanguage(this)
        ThemeManager.applyTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        findViewById<CardView>(R.id.cardTheme).setOnClickListener {
            startActivity(Intent(this, ThemeActivity::class.java))
        }

        findViewById<CardView>(R.id.cardLanguage).setOnClickListener {
            startActivity(Intent(this, LanguageActivity::class.java))
        }
    }
}
