package com.notehive

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ThemeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
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

