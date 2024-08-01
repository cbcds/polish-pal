package com.cbcds.polishpal.data.datasource

import com.cbcds.polishpal.data.model.settings.Locale
import kotlinx.coroutines.flow.Flow

interface LocaleProvider {

    val locale: Flow<Locale>

    fun setLocale(locale: Locale)
}
