package com.notehive

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.applyTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val themeSpinner: Spinner = findViewById(R.id.themeSelector)
        val themes = listOf("Light", "Dark", "Green")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, themes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        themeSpinner.adapter = adapter

        val currentTheme = ThemeManager.getCurrentTheme(this)
        themeSpinner.setSelection(
            when (currentTheme) {
                ThemeManager.THEME_LIGHT -> 0
                ThemeManager.THEME_DARK -> 1
                ThemeManager.THEME_GREEN -> 2
                else -> 0
            }
        )

        themeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                val selectedTheme = when (position) {
                    0 -> ThemeManager.THEME_LIGHT
                    1 -> ThemeManager.THEME_DARK
                    2 -> ThemeManager.THEME_GREEN
                    else -> ThemeManager.THEME_LIGHT
                }
                if (ThemeManager.getCurrentTheme(this@SettingsActivity) != selectedTheme) {
                    ThemeManager.saveTheme(this@SettingsActivity, selectedTheme)

                    MainActivity.instance?.recreate()
                    recreate()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}
