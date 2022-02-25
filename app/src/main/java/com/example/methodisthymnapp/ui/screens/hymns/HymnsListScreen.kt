package com.example.methodisthymnapp.ui.screens.hymns

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.methodisthymnapp.R
import com.example.methodisthymnapp.ui.component.HymnListCard
import com.example.methodisthymnapp.ui.component.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HymnsListScreen(
    viewModel: HymnsListViewModel = viewModel(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    listState: LazyListState = rememberLazyListState(),
    navController: NavHostController,
) {

    val uiState = viewModel.uiState.collectAsState().value
    val hymns = uiState.hymns
    val showScrollToBottomButton by remember { derivedStateOf { listState.firstVisibleItemIndex > 20 } }
    val contentPadding  =  PaddingValues(8.dp,8.dp, 8.dp, 56.dp)

    Scaffold(
        topBar = {
            HymnsListAppBar(
                elevation = listState.elevation,
                showOverflowMenu =  uiState.isShowingOverflowMenu,
                onOverflowClick = viewModel::onOverflowClick,
                onDismissRequest = viewModel::onDismissRequest,
                onSearchActionClick = { navController.navigate(Screen.Search.createRoute()) },
            )
        },
        floatingActionButton = {
            if (showScrollToBottomButton) {
                ScrollToBottomButton(
                    modifier = Modifier.padding(bottom = 56.dp),
                    onClick = {
                        coroutineScope.launch { listState.scrollToItem(hymns.lastIndex) }
                    }
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            contentPadding = contentPadding
        ) {

            items(hymns, key = { hymnListUiState -> hymnListUiState.hymn.id }) { hymnListUiState ->
                HymnListCard(
                    hymn = hymnListUiState.hymn,
                    onFavoriteButtonToggle = { hymnListUiState.onFavoriteToggle() },
                    onCardClick = {
                        navController.navigate(
                            Screen.HymnDestails.createRoute("$HYMN_DETAILS_KEY/${hymnListUiState.hymn.id}")
                        )
                    }
                )
                Spacer(Modifier.padding(top = 16.dp))
            }
        }
    }
}

val LazyListState.elevation: Dp
    get() = if (derivedStateOf { firstVisibleItemIndex == 0 }.value) {
        // For the first element, use the minimum of scroll offset and default elevation
        // i.e. a value between 0 and 4.dp
        minOf(firstVisibleItemScrollOffset.toFloat().dp, AppBarDefaults.TopAppBarElevation)
    } else {
        // If not the first element, always set elevation and show the shadow
        AppBarDefaults.TopAppBarElevation
    }


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ScrollToBottomButton(modifier: Modifier, onClick: () -> Unit) {
    AnimatedVisibility(
        visible = true,
        enter = scaleIn(tween()),
        exit = scaleOut(spring())
    ) {
        Button(
            modifier = modifier,
            onClick = onClick,
            shape = CircleShape,
        ) {
            Text("Scroll To Bottom")
        }
    }
}


@Composable
fun HymnsListAppBar(
    elevation: Dp,
    showOverflowMenu: Boolean,
    onOverflowClick: () -> Unit,
    onDismissRequest: () -> Unit,
    onSearchActionClick: () -> Unit
) {

    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        elevation = elevation
    ) {

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = "Methodist Hymn App",
                style = MaterialTheme.typography.h1,
                color = MaterialTheme.colors.onBackground
            )

            Row(Modifier.wrapContentSize()) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    IconButton(onClick = onSearchActionClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search_alt),
                            contentDescription = "Search Hymns",
                            tint = MaterialTheme.colors.onBackground
                        )
                    }
                    IconButton(onClick = onOverflowClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_overflow_menu),
                            contentDescription = "Sort Hymns",
                            tint = MaterialTheme.colors.onBackground
                        )
                    }
                }
                OverflowMenu(
                    show = showOverflowMenu,
                    onDismissRequest = onDismissRequest
                )
            }
        }
    }
}

@Composable
fun OverflowMenu(show: Boolean, onDismissRequest: () -> Unit) {
    DropdownMenu(expanded = show, onDismissRequest = onDismissRequest) {
        DropdownMenuItem(onClick = { /*TODO*/ }) {
            Text("Sort Hymns")
        }
        DropdownMenuItem(onClick = { /*TODO*/ }) {
            Text("Dark Theme")
        }
    }
}


const val CLICKED_HYMN_ID = "clickedHymnId"
const val HYMN_DETAILS_KEY = "HymnsContent"
