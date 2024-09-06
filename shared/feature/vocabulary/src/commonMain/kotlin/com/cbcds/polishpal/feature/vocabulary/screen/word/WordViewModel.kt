package com.cbcds.polishpal.feature.vocabulary.screen.word

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbcds.polishpal.data.model.words.Verb
import com.cbcds.polishpal.data.repository.vocabulary.VocabularyItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class WordViewModel(
    verb: Verb,
    private val vocabularyRepository: VocabularyItemRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WordUiState(verb))
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            vocabularyRepository.getVerb(verb.id).collect { verb ->
                verb?.let { _uiState.value = WordUiState(it) }
            }
        }
    }

    fun onFavoriteClick() {
        val verb = uiState.value.word
        viewModelScope.launch {
            vocabularyRepository.setIsVerbFavorite(verb.id, !verb.favorite)
        }
    }
}
