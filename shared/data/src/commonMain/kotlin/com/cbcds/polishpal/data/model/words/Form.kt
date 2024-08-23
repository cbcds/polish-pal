package com.cbcds.polishpal.data.model.words

import androidx.compose.runtime.Immutable
import com.cbcds.polishpal.data.model.JavaSerializable

@Immutable
data class Form(
    val values: List<String>,
    val person: Person,
    val gender: Gender,
) : JavaSerializable
