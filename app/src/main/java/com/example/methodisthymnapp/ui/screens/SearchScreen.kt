package com.example.methodisthymnapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.methodisthymnapp.ui.component.HymnListCard
import com.example.methodisthymnapp.ui.component.Screen
import com.example.methodisthymnapp.ui.component.SearchBox
import com.example.methodisthymnapp.ui.screens.hymns.HYMN_DETAILS_KEY
import com.example.methodisthymnapp.ui.screens.hymns.elevation

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: SearchViewModel = viewModel(),
    listState: LazyListState = rememberLazyListState()
) {
    val uiState = viewModel.uiState.collectAsState().value
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val isScrollInProgress by remember { derivedStateOf { listState.isScrollInProgress } }
    // TODO : Migrate this state to viewmodel
    var query by remember { mutableStateOf("") }

    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            SearchAppBar(
                listState = listState,
                searchFocusRequester = focusRequester,
                query = query,
                onClearClick = {},
                onSearchTermChange = {
                    query = it
                    viewModel.onSearchTermChange(query)
                },
                onReturnClick = { navController.navigateUp() },
                keyboardController = keyboardController
            )
        }
    ) {
        Box(Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 16.dp)
                    .focusRequester(focusRequester),
                state = listState
            ) {

                items(uiState.searchResults, key = { it.hymn.id }) { searchUiState ->

                    HymnListCard(
                        hymn = searchUiState.hymn,
                        onFavoriteButtonToggle = { searchUiState.onFavoriteToggle() },
                        onCardClick = {
                            navController.navigate(
                                Screen.HymnDestails.createRoute("$HYMN_DETAILS_KEY/${searchUiState.hymn.id}")
                            )
                        }
                    )
                    Spacer(Modifier.padding(top = 16.dp))
                }
            }
        }
        if (isScrollInProgress) {
            focusRequester.freeFocus()
            keyboardController?.hide()
        }
    }
}

@ExperimentalComposeUiApi
@Composable
private fun SearchAppBar(
    query: String,
    listState: LazyListState,
    searchFocusRequester: FocusRequester,
    onReturnClick: () -> Unit,
    keyboardController: SoftwareKeyboardController?,
    onClearClick: () -> Unit,
    onSearchTermChange: (String) -> Unit
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.background.copy(0.87f),
        elevation = listState.elevation
    ) {
        SearchBox(
            Modifier
                .height(40.dp)
                .padding(horizontal = 8.dp)
                .focusRequester(searchFocusRequester),
            search = query,
            onClearClick = onClearClick,
            onSearchTermChange = onSearchTermChange,
            onReturnClick = onReturnClick,
            keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
        )

        SideEffect {
            searchFocusRequester.requestFocus()
        }
    }
}