package com.example.methodisthymnapp.ui.screens.favorites

import androidx.lifecycle.ViewModel
import com.example.methodisthymnapp.reposiitory.MHARepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val repository: MHARepository) : ViewModel() {
    fun getFavorites() = repository.getFavorites()
}