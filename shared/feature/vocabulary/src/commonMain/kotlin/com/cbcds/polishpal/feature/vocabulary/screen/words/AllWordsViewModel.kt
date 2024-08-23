package com.cbcds.polishpal.feature.vocabulary.screen.words

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbcds.polishpal.data.model.words.Aspect
import com.cbcds.polishpal.data.repository.words.VocabularyRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

internal class AllWordsViewModel(
    private val vocabularyRepository: VocabularyRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<AllWordsUiState>(AllWordsUiState.Loading)
    val uiState: StateFlow<AllWordsUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableStateFlow<AllWordsUiEffect?>(null)
    val uiEffect: StateFlow<AllWordsUiEffect?> = _uiEffect.asStateFlow()

    var query: String by mutableStateOf("")
        private set
    var onlyFavorite: Boolean by mutableStateOf(false)
        private set
    var aspect: Aspect? by mutableStateOf(null)
        private set

    private var updateUiStateJob: Job? = null

    init {
        loadVerbs()
    }

    fun onQueryChange(query: String) {
        this.query = query
        loadVerbs()
    }

    fun onOnlyFavoriteChange(onlyFavorite: Boolean) {
        this.onlyFavorite = onlyFavorite
        loadVerbs()
    }

    fun onAspectChange(aspect: Aspect?) {
        this.aspect = aspect
        loadVerbs()
    }

    fun onWordClick(verbId: Int) {
        viewModelScope.launch {
            vocabularyRepository.getVerb(verbId).firstOrNull()?.let {
                _uiEffect.value = AllWordsUiEffect.NavigateToWordScreen(it)
            }
        }
    }

    fun onNavigatedToWordScreen() {
        _uiEffect.value = null
    }

    private fun loadVerbs() {
        updateUiStateJob?.cancel()
        updateUiStateJob = viewModelScope.launch {
            vocabularyRepository.getAllVerbInfinitives(query, onlyFavorite, aspect).collect {
                _uiState.value = AllWordsUiState.Loaded(it)
            }
        }
    }
}
