package com.example.methodisthymnapp.di

import android.content.Context
import com.example.methodisthymnapp.ui.screens.search.SearchViewModel
import com.example.methodisthymnapp.ui.screens.favorites.FavoritesViewModel
import com.example.methodisthymnapp.ui.screens.hymns.HymnDetailsViewModel
import com.example.methodisthymnapp.ui.screens.hymns.HymnsListViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ViewModelEntryPoint {
    fun hymnsListViewModel(): HymnsListViewModel
    fun hymnDetailsViewModel(): HymnDetailsViewModel
    fun favoritesViewModel(): FavoritesViewModel
    fun searchViewModel() : SearchViewModel
}

val Context.viewModelEntryPoint: ViewModelEntryPoint
    get() = EntryPointAccessors.fromApplication(this, ViewModelEntryPoint::class.java)