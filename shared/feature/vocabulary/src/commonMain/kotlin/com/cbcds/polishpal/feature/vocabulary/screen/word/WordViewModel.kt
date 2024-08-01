package com.cbcds.polishpal.feature.vocabulary.screen.word

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbcds.polishpal.data.model.words.Verb
import com.cbcds.polishpal.data.repository.words.VocabularyRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class WordViewModel(
    private val vocabularyRepository: VocabularyRepository,
) : ViewModel() {

    private var _uiState = MutableStateFlow<WordUiState>(WordUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var updateUiStateJob: Job? = null

    private var verbId: Int? = null

    fun setCurrentVerbId(id: Int) {
        if (verbId != id) {
            this.verbId = id
            loadVerb(id)
        }
    }

    fun onFavoriteClick() {
        (uiState.value as? WordUiState.Loaded)?.word?.let { verb ->
            viewModelScope.launch {
                vocabularyRepository.setIsVerbFavorite(verb.id, !verb.favorite)
            }
        }
    }

    private fun loadVerb(id: Int) {
        updateUiStateJob?.cancel()
        updateUiStateJob = viewModelScope.launch {
            vocabularyRepository.getVerb(id).toUiStateFlow().collect {
                _uiState.value = it
            }
        }
    }

    private fun Flow<Verb?>.toUiStateFlow(): Flow<WordUiState> {
        return map { verb ->
            verb
                ?.let(WordUiState::Loaded)
                ?: WordUiState.Loading
        }
    }
}
