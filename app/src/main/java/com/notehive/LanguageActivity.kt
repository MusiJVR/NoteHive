package com.notehive

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LanguageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        LanguageManager.applyLanguage(this)
        ThemeManager.applyTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)

        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        val languageRecyclerView: RecyclerView = findViewById(R.id.languageRecyclerView)
        languageRecyclerView.layoutManager = LinearLayoutManager(this)

        val languages = listOf(
            LanguageManager.LANGUAGE_ENGLISH to getString(R.string.language_english),
            LanguageManager.LANGUAGE_RUSSIAN to getString(R.string.language_russian),
            LanguageManager.LANGUAGE_GERMAN to getString(R.string.language_german),
            LanguageManager.LANGUAGE_FRENCH to getString(R.string.language_french),
            LanguageManager.LANGUAGE_SPANISH to getString(R.string.language_spanish)
        )

        val adapter = LanguageAdapter(languages) { selectedLanguage ->
            LanguageManager.saveLanguage(this, selectedLanguage)
            LanguageManager.applyLanguage(this)

            MainActivity.instance?.recreate()
            SettingsActivity.instance?.recreate()
            recreate()
        }

        languageRecyclerView.adapter = adapter
    }
}
