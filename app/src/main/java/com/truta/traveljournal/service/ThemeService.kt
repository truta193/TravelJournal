package com.truta.traveljournal.service

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.truta.traveljournal.R


class ThemeService {
    companion object {
        fun applyTheme(mode: String, context: Context) {
            if (context.getString(R.string.settings_theme_value_dark) == mode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else if (context.getString(R.string.settings_theme_value_light) == mode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }

        fun applyTheme(context: Context) {
            val defaultSharedPreferences: SharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context)
            val value = defaultSharedPreferences.getString(
                context.getString(R.string.settings_theme_key),
                context.getString(R.string.settings_theme_value_default)
            )
            if (value != null) {
                applyTheme(value, context)
            }
        }
    }
}