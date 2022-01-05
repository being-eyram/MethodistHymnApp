package com.example.methodisthymnapp.ui.screens.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun FavoritesScreen() {
    Box(Modifier.fillMaxSize()) {

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "Favorites Screen"
        )
    }
}