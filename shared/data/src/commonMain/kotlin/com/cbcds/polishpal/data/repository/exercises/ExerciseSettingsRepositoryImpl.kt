package com.cbcds.polishpal.data.repository.exercises

import com.cbcds.polishpal.data.datasource.prefs.Preferences
import com.cbcds.polishpal.data.model.exercises.ExerciseGroupType
import com.cbcds.polishpal.data.model.exercises.ExerciseSettings
import com.cbcds.polishpal.data.model.words.Aspect
import com.cbcds.polishpal.data.model.words.Tense
import kotlinx.coroutines.flow.firstOrNull

internal class ExerciseSettingsRepositoryImpl(
    private val preferences: Preferences,
) : ExerciseSettingsRepository {

    override suspend fun getSettings(exerciseGroupType: ExerciseGroupType): ExerciseSettings {
        return when (exerciseGroupType) {
            ExerciseGroupType.INDICATIVE_MOOD -> getIndicativeMoodExerciseSettings()
            ExerciseGroupType.IMPERATIVE_MOOD -> getImperativeMoodExerciseSettings()
            ExerciseGroupType.CONDITIONAL_MOOD -> getConditionalMoodExerciseSettings()
        }
    }

    override fun saveSettings(settings: ExerciseSettings) {
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
