package com.example.newsapp.utils

import android.app.UiModeManager
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.example.newsapp.R
import java.security.InvalidParameterException

class ThemeProvider(private val context: Context) {
    fun getThemeFromPreferences(): Int {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        val selectedTheme = sharedPreferences.getString("dark_theme", "system_default")

        return selectedTheme?.let { getTheme(it) } ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }

    fun getThemFromPreferencesDescription(preferencesValue: String?): String = when(preferencesValue) {
        "system_default" -> context.getString(R.string.system_default)
        "light_theme" -> context.getString(R.string.light_theme)
        else -> context.getString(R.string.dark_theme)
    }

    fun getTheme(selectedTheme: String): Int = when (selectedTheme) {
        "system_default" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        "light_theme" -> UiModeManager.MODE_NIGHT_NO
        "dark_theme" -> UiModeManager.MODE_NIGHT_YES
        else -> throw InvalidParameterException("Theme not defined for $selectedTheme")
    }
}