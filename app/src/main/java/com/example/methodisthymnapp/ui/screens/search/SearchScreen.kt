package com.example.methodisthymnapp.ui.screens.search

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
import com.example.methodisthymnapp.ui.component.HymnListCard
import com.example.methodisthymnapp.ui.component.SearchInputField
import com.example.methodisthymnapp.ui.component.navigateTo
import com.example.methodisthymnapp.ui.screens.Screen
import com.example.methodisthymnapp.ui.screens.hymns.elevation
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.pop

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    navController: NavController<Screen>,
    viewModel: SearchViewModel,
    listState: LazyListState = rememberLazyListState()
) {
    val uiState = viewModel.uiState.collectAsState().value
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val isScrollInProgress by remember { derivedStateOf { listState.isScrollInProgress } }

    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            SearchAppBar(
                listState = listState,
                searchFocusRequester = focusRequester,
                query = uiState.query.value,
                onClearClick = viewModel::onClearClick,
                onSearchTermChange = viewModel::onSearchTermChange ,
                onReturnClick = navController::pop,
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
                            navController.navigateTo(
                                Screen.SecondaryScreen.HymnDetails(searchUiState.hymn.id)
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
        SearchInputField(
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