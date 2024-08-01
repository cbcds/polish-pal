package com.cbcds.polishpal.data.model.words

enum class Gender(val number: Number) {
    FEMININE(Number.SINGULAR),
    MASCULINE(Number.SINGULAR),
    NEUTER(Number.SINGULAR),
    ALL_SINGULAR(Number.SINGULAR),
    MASCULINE_PERSONAL(Number.PLURAL),
    NON_MASCULINE_PERSONAL(Number.PLURAL),
    ALL_PLURAL(Number.PLURAL),
}
