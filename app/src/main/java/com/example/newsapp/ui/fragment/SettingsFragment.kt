package com.example.newsapp.ui.fragment

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.newsapp.R
import com.example.newsapp.utils.ThemeProvider

class SettingsFragment : PreferenceFragmentCompat() {
    private val themeProvider by lazy { ThemeProvider(requireContext()) }
    private val themePreference by lazy { findPreference<ListPreference>("dark_theme") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        setThemePreference()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
    private fun setThemePreference() {
        themePreference?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newsValue ->
            if (newsValue is String) {
                val theme = themeProvider.getTheme(newsValue)
                AppCompatDelegate.setDefaultNightMode(theme)
            }
            true
        }
        themePreference?.summaryProvider = Preference.SummaryProvider<ListPreference> { preference ->
            themeProvider.getThemFromPreferencesDescription(preference.value)
        }
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val menuItem = menu.findItem(R.id.searchNews)
        if (menuItem != null) {
            menuItem.isVisible = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}