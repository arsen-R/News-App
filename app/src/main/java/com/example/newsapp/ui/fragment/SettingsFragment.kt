package com.example.newsapp.ui.fragment

import androidx.preference.PreferenceFragmentCompat
import android.os.Bundle
import com.example.newsapp.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

}