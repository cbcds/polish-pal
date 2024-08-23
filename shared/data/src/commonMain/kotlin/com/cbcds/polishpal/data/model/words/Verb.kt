package com.cbcds.polishpal.data.model.words

import androidx.compose.runtime.Immutable
import com.cbcds.polishpal.data.model.JavaSerializable

@Immutable
data class Verb(
    val id: Int,
    val favorite: Boolean,
    val aspect: Aspect,
    val infinitive: String,
    val definition: String? = null,
    val indicativeMood: MoodForms.Indicative? = null,
    val imperativeMood: MoodForms.Imperative? = null,
    val conditionalMood: MoodForms.Conditional? = null,
) : JavaSerializable
