package com.example.methodisthymnapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.methodisthymnapp.ui.component.HymnCard
import com.example.methodisthymnapp.ui.component.Screen
import com.example.methodisthymnapp.ui.component.SearchBox
import com.example.methodisthymnapp.ui.component.emptyString
import com.example.methodisthymnapp.ui.screens.hymns.HYMNS_CONTENT_KEY
import com.example.methodisthymnapp.ui.screens.hymns.HymnsListViewModel
import com.example.methodisthymnapp.ui.screens.hymns.elevation

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: HymnsListViewModel = viewModel()
) {
    val searchResult by viewModel.searchResult.observeAsState()
    var search by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val searchFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current


    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                backgroundColor = Color.White,
                contentColor = Color(0xFF232323),
                elevation = listState.elevation
            ) {
                SearchBox(
                    Modifier
                        .height(40.dp)
                        .padding(horizontal = 8.dp)
                        .focusRequester(searchFocusRequester),
                    search = search,
                    onSearchTermChange = {
                        search = it
                        val searchIsHymnNumber = it.matches("""\d+""".toRegex())
                        if (searchIsHymnNumber)
                            viewModel.search(it.toInt())
                        else
                            viewModel.search(it)
                    },
                    keyboardActions = KeyboardActions(
                        onSearch = { keyboardController?.hide() }
                    ),
                    onClearClick = { search = emptyString },
                    onReturnClick = { navController.navigateUp() },
                )

                SideEffect {
                    searchFocusRequester.requestFocus()
                }
            }
        }
    ) {
        Box(Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .focusRequester(searchFocusRequester),
                state = listState
            ) {
                if (searchResult != null) {
                    items(searchResult!!) { hymn ->
                        var isFavorite by remember { mutableStateOf(hymn.isFavorite != 0) }

                        HymnCard(
                            hymn,
                            isFavorite = isFavorite,
                            onFavoriteButtonToggle = {
                                isFavorite = !isFavorite
                                viewModel.updateFavoriteState(hymn.id, isFavorite.compareTo(false))
                            },
                            onCardClick = {
                                navController.navigate(Screen.HymnsList.createRoute("$HYMNS_CONTENT_KEY/${hymn.id}"))
                            }
                        )
                        Spacer(Modifier.padding(top = 16.dp))
                    }
                }
            }
        }
    }
}