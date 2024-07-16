package com.cbcds.polishpal.data.model

import kotlinx.datetime.LocalTime

data class AppSettings(
    val theme: Theme,
    val locale: Locale,
    val notificationsEnabled: Boolean,
    val notificationsTime: LocalTime,
)
