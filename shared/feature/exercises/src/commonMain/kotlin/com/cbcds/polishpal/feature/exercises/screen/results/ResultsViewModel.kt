package com.cbcds.polishpal.feature.exercises.screen.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbcds.polishpal.data.model.words.Verb
import com.cbcds.polishpal.data.repository.exercises.ExerciseRepository
import com.cbcds.polishpal.feature.exercises.model.ExerciseGroupStatistics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class ResultsViewModel(
    statistics: ExerciseGroupStatistics,
    private val exerciseRepository: ExerciseRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ResultsUiState>(ResultsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val verbIds = statistics.verbIdToResult.keys
            exerciseRepository.getVerbInfinitives(verbIds).collect { verbs ->
                _uiState.value = ResultsUiState.Loaded(
                    overallResult = statistics.overallResult,
                    verbToResult = verbs.mapToResults(statistics.verbIdToResult),
                )
            }
        }
    }

    fun onFavoriteClick(verbId: Int) {
        val verb = (_uiState.value as? ResultsUiState.Loaded)?.verbToResult?.keys
            ?.firstOrNull { it.id == verbId }

        verb?.let {
            viewModelScope.launch {
                exerciseRepository.setIsVerbFavorite(verb.id, !verb.favorite)
            }
        }
    }

    private fun List<Verb>.mapToResults(
        verbIdToResult: LinkedHashMap<Int, Float>
    ): LinkedHashMap<Verb, Float> {
        val verbToResult = linkedMapOf<Verb, Float>()
        for ((id, result) in verbIdToResult) {
            firstOrNull { it.id == id }?.let { verb ->
                verbToResult[verb] = result
            }
        }

        return verbToResult
    }
}
