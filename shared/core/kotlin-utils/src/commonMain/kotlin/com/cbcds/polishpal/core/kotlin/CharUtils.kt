package com.cbcds.polishpal.core.kotlin

fun Char.withoutDiacritics(): Char {
    var newCh = when (lowercaseChar()) {
        'ą' -> 'a'
        'ć' -> 'c'
        'ę' -> 'e'
        'ł' -> 'l'
        'ń' -> 'n'
        'ó' -> 'o'
        'ś' -> 's'
        'ź', 'ż' -> 'z'
        else -> this
    }
    if (isUpperCase()) {
        newCh = newCh.uppercaseChar()
    }

    return newCh
}

fun Char.polishCompareTo(other: Char): Int {
    if (this == other) return 0

    val thisWithoutDiacritics = withoutDiacritics()
    val otherWithoutDiacritics = other.withoutDiacritics()
    val diffWithoutDiacritics = thisWithoutDiacritics - otherWithoutDiacritics

    return if (diffWithoutDiacritics != 0) {
        diffWithoutDiacritics
    } else {
        when {
            this == thisWithoutDiacritics && other != otherWithoutDiacritics -> -1
            this != thisWithoutDiacritics && other == otherWithoutDiacritics -> 1
            this == 'ź' && other == 'ż' -> -1
            else -> 1
        }
    }
}
