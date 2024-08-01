package com.cbcds.polishpal.data.model.words

data class Verb(
    val id: Int,
    val favorite: Boolean,
    val aspect: Aspect,
    val infinitive: String,
    val definition: String? = null,
    val indicativeMood: Mood.Indicative? = null,
    val imperativeMood: Mood.Imperative? = null,
    val conditionalMood: Mood.Conditional? = null,
)
