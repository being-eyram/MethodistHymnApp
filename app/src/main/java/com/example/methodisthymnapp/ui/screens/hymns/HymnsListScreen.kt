package com.example.methodisthymnapp.ui.screens.hymns

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.methodisthymnapp.ui.component.HymnListCard
import com.example.methodisthymnapp.ui.component.HymnsListAppBar
import com.example.methodisthymnapp.ui.component.Screen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HymnsListScreen(
    viewModel: HymnsListViewModel = viewModel(),
    navController: NavHostController,
) {
    val hymns by viewModel.getAllHymns().collectAsState(listOf())
    val listState = rememberLazyListState()
    val showScrollToBottomButton by remember { derivedStateOf { listState.firstVisibleItemIndex > 20 } }

    Scaffold(
        topBar = {
            HymnsListAppBar(
                listState.elevation,
                onSearchActionClick = { navController.navigate(Screen.FullScreen.Search.route) },
            )
        },
        floatingActionButton = {
            if (showScrollToBottomButton) {
                AnimatedVisibility(
                    visible = true,
                    enter = scaleIn(tween()),
                    exit = scaleOut(spring())
                ) {
                    ScrollToBottomButton(Modifier.padding(bottom = 56.dp)) {

                    }
                }
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = it.calculateBottomPadding()),
            state = listState,
            contentPadding = PaddingValues(8.dp)
        ) {

            items(hymns, key = { hymn -> hymn.id }) { hymn ->
                HymnListCard(
                    hymn = hymn,
                    isFavorite = hymn.isFavorite != FALSE,
                    onFavoriteButtonToggle = {
                        viewModel.updateFavoriteState(
                            hymn.id,
                            if (hymn.isFavorite == FALSE) TRUE else FALSE
                        )
                    },
                    onCardClick = {
                        navController.navigate(Screen.FullScreen.HymnDestails.createRoute("$HYMNS_CONTENT_KEY/${hymn.id}"))
                    }
                )
                Spacer(Modifier.padding(top = 16.dp))
            }
        }
    }
}

val LazyListState.elevation: Dp
    get() = if (firstVisibleItemIndex == 0) {
        // For the first element, use the minimum of scroll offset and default elevation
        // i.e. a value between 0 and 4.dp
        minOf(firstVisibleItemScrollOffset.toFloat().dp, AppBarDefaults.TopAppBarElevation)
    } else {
        // If not the first element, always set elevation and show the shadow
        AppBarDefaults.TopAppBarElevation
    }


//fun NavGraphBuilder.hymnsGraph(navController: NavHostController) {
//    navigation(
//        startDestination = Screen.HymnsList.createRoute("HymnsList"),
//        route = Screen.HymnsList.route
//    ) {
//
//
//    }
//}

@Composable
fun ScrollToBottomButton(modifier: Modifier, onClick: () -> Unit) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = CircleShape,
    ) {
        Text("Scroll To Bottom")
    }
}

const val CLICKED_HYMN_ID = "clickedHymnId"
const val HYMNS_CONTENT_KEY = "HymnsContent"
const val FALSE = 0
const val TRUE = 1