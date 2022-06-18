package com.example.methodisthymnapp.ui.screens.search

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.methodisthymnapp.reposiitory.MHARepository
import com.example.methodisthymnapp.ui.screens.hymns.HymnListItemUiState
import com.example.methodisthymnapp.ui.screens.hymns.invert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: MHARepository) : ViewModel() {
    private val searchQuery = mutableStateOf("")
    private var _uiState = MutableStateFlow(
        SearchUiState(
            searchResults = listOf(),
            query = searchQuery
        )
    )
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
                }.collect { searchResults ->
                    _uiState.update { currentUiState ->
                        currentUiState.copy(searchResults = searchResults)
                    }
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
                }.collect { searchResults ->
                    _uiState.update { currentUiState ->
                        currentUiState.copy(searchResults = searchResults)
                    }
                }
        }
    }

    fun onSearchTermChange(query: String) {
        searchQuery.value = query

        val searchIsHymnNumber = query.matches("""\d+""".toRegex())
        if (searchIsHymnNumber) search(query.toInt()) else search(query)
        Log.i("MainActivity", searchQuery.value)
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
    val searchResults: List<HymnListItemUiState>,
    val query: MutableState<String>
)