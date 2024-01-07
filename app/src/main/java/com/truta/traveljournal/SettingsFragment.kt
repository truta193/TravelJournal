package com.truta.traveljournal

import android.app.LocaleManager
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.LocaleList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat.getSystemService
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import java.util.Locale
import com.truta.traveljournal.service.ThemeService

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        preferenceManager.findPreference<Preference>(getString(R.string.settings_theme_key))?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference: Preference?, newValue: Any? ->
                ThemeService.applyTheme(newValue as String, requireContext())
                true
            }

        preferenceManager.findPreference<Preference>(getString(R.string.settings_language_key))?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference: Preference?, newValue: Any? ->
                var tag = ""
                if (newValue as String == "language_english") {
                    tag = "en"
                } else if (newValue == "language_romanian") {
                    tag = "ro"
                }
                val localeManager = getSystemService(requireContext(), LocaleManager::class.java) as LocaleManager
                localeManager.applicationLocales = LocaleList.forLanguageTags(tag)
                true
            }
    }
}