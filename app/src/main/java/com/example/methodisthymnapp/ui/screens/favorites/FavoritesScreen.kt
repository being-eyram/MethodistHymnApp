package com.example.methodisthymnapp.ui.screens.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun FavoritesScreen(viewModel: FavoritesViewModel = viewModel()) {

    val favorites by viewModel.getFavorites().collectAsState(listOf())

    Box(Modifier.fillMaxSize()) {
        LazyColumn {
            items(favorites) { favorite ->
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = favorite.author
                )
            }
        }
    }
}