package com.example.atentotap.core.localization

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

object DebugLocaleManager {

    private const val LANGUAGE_EN = "en"
    private const val LANGUAGE_ES = "es"
    private const val PREFS_FILE = "debug_locale_prefs"
    private const val PREF_LANGUAGE_TAG = "selected_language_tag"

    fun applyLanguage(languageTag: String) {
        val safeTag = normalizeLanguageTag(languageTag)
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(safeTag))
    }

    fun applyAndPersist(context: Context, languageTag: String) {
        val safeTag = normalizeLanguageTag(languageTag)
        context
            .getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)
            .edit()
            .putString(PREF_LANGUAGE_TAG, safeTag)
            .apply()
        applyLanguage(safeTag)
    }

    fun restorePersistedLanguage(context: Context) {
        val persistedTag = context
            .getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)
            .getString(PREF_LANGUAGE_TAG, null)
            ?: return

        applyLanguage(persistedTag)
    }

    fun currentLanguageTag(context: Context): String {
        val appLocales = AppCompatDelegate.getApplicationLocales()
        val selectedTag = appLocales.toLanguageTags().takeIf { it.isNotBlank() }
        if (selectedTag != null) {
            return selectedTag.substringBefore(',')
        }

        return context.resources.configuration.locales[0]?.language ?: LANGUAGE_EN
    }

    private fun normalizeLanguageTag(languageTag: String): String {
        return when (languageTag) {
            LANGUAGE_ES -> LANGUAGE_ES
            else -> LANGUAGE_EN
        }
    }
}


