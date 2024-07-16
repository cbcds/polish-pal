package com.cbcds.polishpal.data.model

enum class Locale(
    val displayName: String,
    val language: String,
) {
    POLISH(displayName = "Polski", language = "pl"),
    ENGLISH(displayName = "English", language = "en"),
    BELARUSIAN(displayName = "Беларуская", language = "be");

    companion object {

        fun default(): Locale = POLISH
    }
}
