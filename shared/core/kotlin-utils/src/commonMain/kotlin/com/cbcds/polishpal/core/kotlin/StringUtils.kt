package com.cbcds.polishpal.core.kotlin

fun String.withoutDiacritics(): String {
    return buildString {
        for (ch in this@withoutDiacritics) {
            append(ch.withoutDiacritics())
        }
    }
}
