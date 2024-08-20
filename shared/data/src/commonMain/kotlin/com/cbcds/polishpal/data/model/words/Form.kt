package com.cbcds.polishpal.data.model.words

import kotlinx.collections.immutable.ImmutableList

data class Form(
    val values: ImmutableList<String>,
    val person: Person,
    val gender: Gender,
)
