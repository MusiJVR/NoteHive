package com.notehive.activity

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.notehive.util.LanguageManager
import com.notehive.R
import com.notehive.util.ThemeAdapter
import com.notehive.util.ThemeItem
import com.notehive.util.ThemeManager

class ThemeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        LanguageManager.applyLanguage(this)
        ThemeManager.applyTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme)

        val themes = listOf(
            ThemeItem("Light", ThemeManager.THEME_LIGHT),
            ThemeItem("Dark", ThemeManager.THEME_DARK),
            ThemeItem("Green", ThemeManager.THEME_GREEN)
        )

        val recyclerView = findViewById<RecyclerView>(R.id.themeRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = ThemeAdapter(themes) { theme ->
            ThemeManager.saveTheme(this, theme.themeKey)
            Toast.makeText(this, "Theme applied: ${theme.name}", Toast.LENGTH_SHORT).show()
            MainActivity.instance?.recreate()
            SettingsActivity.instance?.recreate()
            recreate()
        }

        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
    }
}

