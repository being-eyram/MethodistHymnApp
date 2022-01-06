package com.example.methodisthymnapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.methodisthymnapp.ui.component.*
import com.example.methodisthymnapp.ui.screens.hymns.HymnsListViewModel

@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: HymnsListViewModel = viewModel()
) {
    val searchResult by viewModel.searchResult.observeAsState()
    var search by remember { mutableStateOf("") }

    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            TopAppBar {
                SearchBox(
                    Modifier
                        .height(40.dp)
                        .padding(horizontal = 8.dp),
                    search = search,
                    onSearchTermChange = {
                        search = it
                        viewModel.search(it)
                    },
                    onClearClick = {
                        search = emptyString
                    },
                    onReturnClick = { navController.navigateUp() }
                )
            }

        }) {
        Box(Modifier.fillMaxSize()) {
            LazyColumn {
                if (searchResult != null) {
                    items(searchResult!!) { hymn ->
                        val initState =
                            if (hymn.isFavorite == 0) FavoriteState.NOPE else FavoriteState.YES
                        var favoriteState by remember { mutableStateOf(initState) }

                        HymnCard(
                            num = hymn.id,
                            title = hymn.title,
                            body = hymn.lyrics,
                            author = hymn.author,
                            favoriteState = favoriteState,
                            onFavoriteIcClick = {
                                favoriteState = when (favoriteState) {
                                    FavoriteState.NOPE -> FavoriteState.YES
                                    FavoriteState.YES -> FavoriteState.NOPE
                                }
                                viewModel.updateFavoriteState(hymn.id, favoriteState.ordinal)
                            },
                            onCardClick = {
                                navController.navigate(Screen.HymnsList.createRoute("details"))
                            }
                        )
                        Spacer(Modifier.padding(top = 16.dp))
                    }
                }
            }
        }
    }
}