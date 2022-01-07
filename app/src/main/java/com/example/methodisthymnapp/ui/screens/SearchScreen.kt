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
import com.example.methodisthymnapp.ui.component.*
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
    var readOnly by remember { mutableStateOf(false) }
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
                        viewModel.search(it)
                    },
                    readOnly = readOnly,
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            keyboardController?.hide()
                            readOnly = !readOnly
                        }
                    ),
                    onClearClick = { search = emptyString },
                    onReturnClick = { navController.navigateUp() },
                    onTextFieldClick = {
                        if (readOnly) readOnly = false
                        keyboardController?.show()
                    }
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

                        SideEffect {
                            if (listState.isScrollInProgress) {
                                keyboardController?.hide()
                                readOnly = !readOnly
                            }
                        }
                    }
                }
            }
        }
    }
}