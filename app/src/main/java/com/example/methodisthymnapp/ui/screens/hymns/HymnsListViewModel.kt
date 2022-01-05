package com.example.methodisthymnapp.ui.screens.hymns

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.methodisthymnapp.database.HymnEntity
import com.example.methodisthymnapp.reposiitory.MHARepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HymnsListViewModel @Inject constructor(private val repository: MHARepository) : ViewModel() {

    private var _result = MutableLiveData<List<HymnEntity>>()
    val result: LiveData<List<HymnEntity>>
        get() = _result
    private var _searchResult = MutableLiveData<List<HymnEntity>>()
    val searchResult: LiveData<List<HymnEntity>>
        get() = _searchResult

    init {
        viewModelScope.launch {
            val result = repository.allHymns()
            _result.value = result
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            val result = repository.search(sanitizeSearchQuery(query))
            withContext(Dispatchers.Main) {
                _searchResult.value = result
            }
        }
    }

    fun search(query : Int ) {
        viewModelScope.launch {
            val result = repository.search(query)
            withContext(Dispatchers.Main) {
                _searchResult.value = result
            }
        }
    }

    private fun sanitizeSearchQuery(query: String?): String {
        if (query == null) return ""
        val queryWithEscapedQuotes = query.replace(Regex.fromLiteral("\""), "\"\"")
        return "*\"$queryWithEscapedQuotes\"*"
    }
}



