package com.cbcds.polishpal.data.repository.exercises

import com.cbcds.polishpal.data.datasource.prefs.Preferences
import com.cbcds.polishpal.data.model.exercises.ExerciseSettings
import com.cbcds.polishpal.data.model.exercises.ExerciseType
import com.cbcds.polishpal.data.model.words.Aspect
import com.cbcds.polishpal.data.model.words.Tense
import kotlinx.coroutines.flow.firstOrNull

internal class ExercisesSettingsRepositoryImpl(
    private val preferences: Preferences,
) : ExercisesSettingsRepository {

    override suspend fun getExerciseSettings(exerciseType: ExerciseType): ExerciseSettings {
        return when (exerciseType) {
            ExerciseType.INDICATIVE_MOOD -> getIndicativeMoodExerciseSettings()
            ExerciseType.IMPERATIVE_MOOD -> getImperativeMoodExerciseSettings()
            ExerciseType.CONDITIONAL_MOOD -> getConditionalMoodExerciseSettings()
        }
    }

    override fun saveExerciseSettings(settings: ExerciseSettings) {
        return when (settings) {
            is ExerciseSettings.IndicativeMood -> setIndicativeMoodExerciseSettings(settings)
            is ExerciseSettings.ImperativeMood -> setImperativeMoodExerciseSettings(settings)
            is ExerciseSettings.ConditionalMood -> setConditionalMoodExerciseSettings(settings)
        }
    }

    private suspend fun getIndicativeMoodExerciseSettings(): ExerciseSettings.IndicativeMood {
        val aspects = preferences.readStringSet(KEY_INDICATIVE_MOOD_ASPECTS).firstOrNull()
            ?.mapNotNull { aspect -> runCatching { Aspect.valueOf(aspect) }.getOrNull() }
            ?.toSet()
            ?: setOf(Aspect.IMPERFECTIVE)

        val tenses = preferences.readStringSet(KEY_INDICATIVE_MOOD_TENSES).firstOrNull()
            ?.mapNotNull { tense -> runCatching { Tense.valueOf(tense) }.getOrNull() }
            ?.toSet()
            ?: setOf(Tense.PRESENT)

        val wordsNumber = preferences.readInt(KEY_INDICATIVE_MOOD_WORDS_NUMBER).firstOrNull()
            ?: DEFAULT_WORDS_NUMBER

        return ExerciseSettings.IndicativeMood(wordsNumber, aspects, tenses)
    }

    private fun setIndicativeMoodExerciseSettings(settings: ExerciseSettings.IndicativeMood) {
        preferences.apply {
            writeInt(KEY_INDICATIVE_MOOD_WORDS_NUMBER, settings.wordsNumber)
            writeStringSet(KEY_INDICATIVE_MOOD_ASPECTS, settings.aspects.map { it.name }.toSet())
            writeStringSet(KEY_INDICATIVE_MOOD_TENSES, settings.tenses.map { it.name }.toSet())
        }
    }

    private suspend fun getImperativeMoodExerciseSettings(): ExerciseSettings.ImperativeMood {
        val wordsNumber = preferences.readInt(KEY_IMPERATIVE_MOOD_WORDS_NUMBER).firstOrNull()
            ?: DEFAULT_WORDS_NUMBER

        return ExerciseSettings.ImperativeMood(wordsNumber)
    }

    private fun setImperativeMoodExerciseSettings(settings: ExerciseSettings.ImperativeMood) {
        preferences.writeInt(KEY_IMPERATIVE_MOOD_WORDS_NUMBER, settings.wordsNumber)
    }

    private suspend fun getConditionalMoodExerciseSettings(): ExerciseSettings.ConditionalMood {
        val wordsNumber = preferences.readInt(KEY_CONDITIONAL_MOOD_WORDS_NUMBER).firstOrNull()
            ?: DEFAULT_WORDS_NUMBER

        return ExerciseSettings.ConditionalMood(wordsNumber)
    }

    private fun setConditionalMoodExerciseSettings(settings: ExerciseSettings.ConditionalMood) {
        preferences.writeInt(KEY_CONDITIONAL_MOOD_WORDS_NUMBER, settings.wordsNumber)
    }

    private companion object {

        const val KEY_INDICATIVE_MOOD_WORDS_NUMBER = "ind_words_number"
        const val KEY_INDICATIVE_MOOD_ASPECTS = "ind_aspects"
        const val KEY_INDICATIVE_MOOD_TENSES = "ind_tenses"
        const val KEY_IMPERATIVE_MOOD_WORDS_NUMBER = "imp_words_number"
        const val KEY_CONDITIONAL_MOOD_WORDS_NUMBER = "cond_words_number"

        const val DEFAULT_WORDS_NUMBER = 3
    }
}
