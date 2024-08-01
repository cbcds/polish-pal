package com.cbcds.polishpal.data.datasource

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.cbcds.polishpal.data.datasource.prefs.Preferences
import com.cbcds.polishpal.data.model.settings.Locale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale as JavaLocale

internal class AndroidLocaleProvider(
    private val preferences: Preferences,
) : LocaleProvider {

    override val locale: Flow<Locale> = readLocale()

    override fun setLocale(locale: Locale) {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(locale.language)
        )
        preferences.writeString(KEY_LANGUAGE, locale.language)
    }

    private fun readLocale(): Flow<Locale> {
        return preferences.readString(KEY_LANGUAGE).map {
            it?.toLocale()
                ?: JavaLocale.getDefault().toLocale()
                ?: Locale.default()
        }
    }

    private fun JavaLocale.toLocale(): Locale? {
        return Locale.entries.find { it.language == language }
    }

    private fun String.toLocale(): Locale? {
        return Locale.entries.find { it.language == this }
    }

    private companion object {

        const val KEY_LANGUAGE = "app_language"
    }
}
