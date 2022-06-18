package com.example.methodisthymnapp.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.methodisthymnapp.reposiitory.MHARepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
class FavoritesViewModel @Inject constructor(private val repository: MHARepository) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState

    //Keep track of favorites that are selected for deletion.
    private val selected = mutableSetOf<Int>()

    init {
        viewModelScope.launch {
            repository.getFavorites().map { favoriteHymns ->
                favoriteHymns.map { favoriteHymn ->
                    FavoriteListItemUiState(
                        id = favoriteHymn.id,
                        title = favoriteHymn.title,
                        author = favoriteHymn.author,
                        onLongPress = ::onLongPress,
                        onCheckMarkClick = ::onCheckMarkClick,
                        toggleSelectOnClick = ::toggleSelectOnClick,
                        onOverflowMenuClick = ::onOverflowMenuClick
                    )
                }
            }.collect {
                _uiState.value = FavoritesUiState(favorites = it)
            }
        }
    }

   /** Toggle selection off for [FavoriteListItemUiState] when select icon is click */
    private fun onCheckMarkClick(id: Int) {
        _uiState.update { currentUiState ->
            selected.remove(id)
            val favoritesList = toggleSelectFor(id, false, currentUiState.favorites)

            currentUiState.copy(
                selected = selected,
                favorites = favoritesList,
                selectedCount = selected.count()
            )
        }
    }

    /** Select the [FavoriteListItemUiState] on long press */
    private fun onLongPress(id: Int) {
        _uiState.update { currentUiState ->
            selected.add(id)
            val favoritesList = toggleSelectFor(id, true, currentUiState.favorites)

            currentUiState.copy(
                selected = selected,
                favorites = favoritesList,
                selectedCount = selected.count()
            )
        }
    }

    /** Toggle select or unselect for [FavoriteListItemUiState] id passed on click */
    private fun toggleSelectOnClick(id: Int) {
        _uiState.update { currentUiState ->

            val clickedFavorite = currentUiState.favorites.find { it.id == id }!!
            val index = currentUiState.favorites.indexOf(clickedFavorite)
            val favoriteHymns = currentUiState.favorites.toMutableList()
            favoriteHymns[index] = clickedFavorite.copy(isSelected = !clickedFavorite.isSelected)

            if (!clickedFavorite.isSelected) selected.add(id) else selected.remove(id)

            currentUiState.copy(
                selected = selected,
                favorites = favoriteHymns,
                selectedCount = selected.count()
            )
        }
    }

    /** Find and toggle selected or unselected state for [FavoriteListItemUiState] id passed */
    private fun toggleSelectFor(
        id: Int,
        isSelected: Boolean,
        favorites: List<FavoriteListItemUiState>
    ): MutableList<FavoriteListItemUiState> {

        val pressedFavorite = favorites.find { it.id == id }!!
        val index = favorites.indexOf(pressedFavorite)
        val favoriteHymns = favorites.toMutableList()
        favoriteHymns[index] = pressedFavorite.copy(isSelected = isSelected)

        return favoriteHymns
    }

    /** Update uiState with [id] of the favorite whose overflow was clicked */
    private fun onOverflowMenuClick(id: Int) = _uiState.update { currentUiState ->
        currentUiState.copy(clickedOverflowId = id)
    }

    /** Get Lyrics for provided [id] */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getLyricsFor(id: Int): String {
        return viewModelScope.async {
            repository.getHymn(id)
        }.getCompleted().lyrics
    }

    /** Unselect all selected favorites on Return action click */
    fun onReturnClick() {
        _uiState.update { uiState ->
            selected.clear()
            val favorites = uiState.favorites.map { it.copy(isSelected = false) }
            uiState.copy(favorites = favorites, selected = selected, selectedCount = 0)
        }
    }

    /** Delete all selected favorites on AppBar Delete action Click */
    fun onDeleteClick() {
        _uiState.update { currentUiState ->
            selected.forEach { removeFromFavorites(it) }
            selected.clear()
            currentUiState.copy(selected = selected, selectedCount = selected.count())
        }
    }

    /** Unfavorite the favorite Item for the [id] passed */
    fun onRemoveFromFavoritesClick(id: Int) = removeFromFavorites(id)

    private fun removeFromFavorites(id: Int) = viewModelScope.launch {
        repository.updateFavoriteState(id, 0)
    }
}

data class FavoriteListItemUiState(
    val id: Int,
    val title: String,
    val author: String,
    val isSelected: Boolean = false,
    val onLongPress: (id: Int) -> Unit,
    val onCheckMarkClick: (id: Int) -> Unit,
    val toggleSelectOnClick: (id: Int) -> Unit,
    val onOverflowMenuClick: (id: Int) -> Unit
)

data class FavoritesUiState(
    val selected: Set<Int> = setOf(),
    val favorites: List<FavoriteListItemUiState> = listOf(),
    val selectedCount: Int = 0,
    val clickedOverflowId: Int = 0
)