package com.example.methodisthymnapp.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.methodisthymnapp.reposiitory.MHARepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val repository: MHARepository) : ViewModel() {
    fun getFavorites() = repository.getFavorites().buffer()
    fun updateFavoriteState(id: Int, isFavorite: Int) = viewModelScope.launch {
        repository.updateFavoriteState(id, isFavorite)
    }
}