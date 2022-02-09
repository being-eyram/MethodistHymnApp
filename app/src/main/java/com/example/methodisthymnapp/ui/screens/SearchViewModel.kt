package com.example.methodisthymnapp.ui.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.methodisthymnapp.database.Hymn
import com.example.methodisthymnapp.reposiitory.MHARepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: MHARepository) : ViewModel() {

    private var _searchResult = MutableLiveData<List<Hymn>>()
    val searchResult: LiveData<List<Hymn>>
        get() = _searchResult

    fun search(query: String) {
        viewModelScope.launch {
            val result = repository.search(sanitizeSearchQuery(query))
            _searchResult.value = result
        }
    }

    fun search(query: Int) {
        viewModelScope.launch {
            val result = repository.search(query)
            _searchResult.value = result
        }
    }

    fun updateFavoriteState(id: Int, isFavorite: Int) {
        viewModelScope.launch {
            repository.updateFavoriteState(id, isFavorite)
        }
    }

    private fun sanitizeSearchQuery(query: String?): String {
        if (query == null) return ""
        val queryWithEscapedQuotes = query.replace(Regex.fromLiteral("\""), "\"\"")
        return "*\"$queryWithEscapedQuotes\"*"
    }
}