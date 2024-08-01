package com.cbcds.polishpal.core.kotlin

fun String.withoutDiacritics(): String {
    return buildString {
        for (ch in this@withoutDiacritics) {
            var newCh = when (ch.lowercaseChar()) {
                'ą' -> 'a'
                'ć' -> 'c'
                'ę' -> 'e'
                'ł' -> 'l'
                'ń' -> 'n'
                'ó' -> 'o'
                'ś' -> 's'
                'ź', 'ż' -> 'z'
                else -> ch
            }
            if (ch.isUpperCase()) {
                newCh = newCh.uppercaseChar()
            }
            append(newCh)
        }
    }
}
