package com.example.methodisthymnapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.methodisthymnapp.reposiitory.MHARepository
import com.example.methodisthymnapp.ui.screens.hymns.HymnListItemUiState
import com.example.methodisthymnapp.ui.screens.hymns.invert
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: MHARepository) : ViewModel() {

    private var _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    private fun search(query: String) {
        viewModelScope.launch {
            repository.search(sanitizeSearchQuery(query))
                .distinctUntilChanged()
                .map { results ->
                    results.map {
                        HymnListItemUiState(
                            hymn = it,
                            onFavoriteToggle = { updateFavoriteState(it.id, invert(it.isFavorite)) }
                        )
                    }
                }.collect {
                    _uiState.value = SearchUiState(searchResults = it)
                }
        }
    }

    private fun search(query: Int) {
        viewModelScope.launch {
            repository.search(query)
                .distinctUntilChanged()
                .map { results ->
                    results.map {
                        HymnListItemUiState(
                            hymn = it,
                            onFavoriteToggle = { updateFavoriteState(it.id, invert(it.isFavorite)) }
                        )
                    }
                }.collect {
                    _uiState.value = SearchUiState(searchResults = it)
                }
        }
    }

    fun onSearchTermChange(query: String) {
        val searchIsHymnNumber = query.matches("""\d+""".toRegex())
        if (searchIsHymnNumber) search(query.toInt()) else search(query)
    }

//
//    fun onClearClick() {
//        _uiState.update { currentUiState -> currentUiState.copy(query = emptyString) }
//    }

    private fun updateFavoriteState(id: Int, isFavorite: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateFavoriteState(id, isFavorite)
        }
    }

    private fun sanitizeSearchQuery(query: String?): String {
        if (query == null) return ""
        val queryWithEscapedQuotes = query.replace(Regex.fromLiteral("\""), "\"\"")
        return "*\"$queryWithEscapedQuotes\"*"
    }
}

data class SearchUiState(
    val searchResults: List<HymnListItemUiState> = listOf(),
)