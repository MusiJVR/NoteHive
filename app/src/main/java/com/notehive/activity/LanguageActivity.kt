package com.notehive.activity

import android.os.Bundle
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.notehive.util.LanguageManager
import com.notehive.R
import com.notehive.util.ThemeManager

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

        val languageRadioGroup: RadioGroup = findViewById(R.id.languageRadioGroup)

        val languages = listOf(
            LanguageManager.LANGUAGE_ENGLISH to getString(R.string.language_english),
            LanguageManager.LANGUAGE_RUSSIAN to getString(R.string.language_russian),
            LanguageManager.LANGUAGE_GERMAN to getString(R.string.language_german),
            LanguageManager.LANGUAGE_FRENCH to getString(R.string.language_french),
            LanguageManager.LANGUAGE_SPANISH to getString(R.string.language_spanish)
        )

        val currentLanguage = LanguageManager.getCurrentLanguage(this)

        for ((code, name) in languages) {
            val radioButton = RadioButton(this).apply {
                layoutParams = RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT
                )
                text = name
                textSize = 18f
                setTextColor(getColorFromAttr(android.R.attr.textColor))
                isChecked = code == currentLanguage
                gravity = android.view.Gravity.CENTER
                buttonTintList = getColorStateListFromAttr(R.attr.radioButtonColor)

                setOnClickListener {
                    LanguageManager.saveLanguage(this@LanguageActivity, code)
                    LanguageManager.applyLanguage(this@LanguageActivity)

                    MainActivity.instance?.recreate()
                    SettingsActivity.instance?.recreate()
                    recreate()
                }
            }
            languageRadioGroup.addView(radioButton)
        }
    }

    private fun getColorFromAttr(attr: Int): Int {
        val typedArray = theme.obtainStyledAttributes(intArrayOf(attr))
        return typedArray.use {
            it.getColor(0, 0)
        }
    }

    private fun getColorStateListFromAttr(attr: Int): android.content.res.ColorStateList? {
        val typedValue = android.util.TypedValue()
        val theme = theme
        theme.resolveAttribute(attr, typedValue, true)
        return getColorStateList(typedValue.resourceId)
    }
}
