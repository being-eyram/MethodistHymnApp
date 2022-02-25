package com.example.methodisthymnapp.ui.screens.hymns

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.methodisthymnapp.data.Hymn
import com.example.methodisthymnapp.reposiitory.MHARepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HymnsListViewModel @Inject constructor(private val repository: MHARepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HymnListUiState())
    val uiState: StateFlow<HymnListUiState> = _uiState

    init {
        fetchAllHymns()
    }

    fun onOverflowClick() = _uiState.update { currentUiState ->
        currentUiState.copy(isShowingOverflowMenu = true)
    }

    fun onDismissRequest() = _uiState.update { currentUiState ->
        currentUiState.copy(isShowingOverflowMenu = false)
    }

    private fun updateFavoriteState(id: Int, isFavorite: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateFavoriteState(id, isFavorite)
        }
    }

    private fun fetchAllHymns() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.allHymns().map { hymns ->
                hymns.map {
                    HymnListItemUiState(
                        hymn = it,
                        onFavoriteToggle = { updateFavoriteState(it.id, invert(it.isFavorite)) }
                    )
                }
            }.collect {
                _uiState.value = HymnListUiState(it)
            }
        }
    }
}

data class HymnListUiState(
    val hymns: List<HymnListItemUiState> = listOf(),
    val isShowingOverflowMenu: Boolean = false
)

data class HymnListItemUiState(
    val hymn: Hymn,
    val onFavoriteToggle: () -> Unit,
)

fun invert(isFavorite: Int) = if (isFavorite == FALSE) TRUE else FALSE
const val FALSE = 0
const val TRUE = 1
