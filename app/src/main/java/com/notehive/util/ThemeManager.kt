package com.notehive.util

import android.content.Context
import android.content.SharedPreferences
import com.notehive.R

object ThemeManager {
    private const val PREFS_NAME = "user_preferences"
    private const val KEY_THEME = "current_theme"

    const val THEME_LIGHT = "Theme.NoteHive.Light"
    const val THEME_DARK = "Theme.NoteHive.Dark"
    const val THEME_GREEN = "Theme.NoteHive.Green"

    fun applyTheme(context: Context) {
        val prefs = getPreferences(context)
        val theme = prefs.getString(KEY_THEME, THEME_LIGHT) ?: THEME_LIGHT
        context.setTheme(getThemeResId(theme))
    }

    fun saveTheme(context: Context, theme: String) {
        val prefs = getPreferences(context)
        prefs.edit().putString(KEY_THEME, theme).apply()
    }

    fun getCurrentTheme(context: Context): String {
        val prefs = getPreferences(context)
        return prefs.getString(KEY_THEME, THEME_LIGHT) ?: THEME_LIGHT
    }

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private fun getThemeResId(theme: String): Int {
        return when (theme) {
            THEME_LIGHT -> R.style.Theme_NoteHive_Light
            THEME_DARK -> R.style.Theme_NoteHive_Dark
            THEME_GREEN -> R.style.Theme_NoteHive_Green
            else -> R.style.Theme_NoteHive_Light
        }
    }
}
