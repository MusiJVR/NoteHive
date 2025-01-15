package com.notehive

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import java.util.Locale

object LanguageManager {
    private const val PREFS_NAME = "user_preferences"
    private const val KEY_LANGUAGE = "current_language"

    const val LANGUAGE_ENGLISH = "en"
    const val LANGUAGE_RUSSIAN = "ru"
    const val LANGUAGE_GERMAN = "de"
    const val LANGUAGE_FRENCH = "fr"
    const val LANGUAGE_SPANISH = "es"

    fun applyLanguage(context: Context) {
        val prefs = getPreferences(context)
        val language = prefs.getString(KEY_LANGUAGE, LANGUAGE_ENGLISH) ?: LANGUAGE_ENGLISH
        setLocale(context, language)
    }

    fun saveLanguage(context: Context, language: String) {
        val prefs = getPreferences(context)
        prefs.edit().putString(KEY_LANGUAGE, language).apply()
    }

    fun getCurrentLanguage(context: Context): String {
        val prefs = getPreferences(context)
        return prefs.getString(KEY_LANGUAGE, LANGUAGE_ENGLISH) ?: LANGUAGE_ENGLISH
    }

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private fun setLocale(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)

        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}
