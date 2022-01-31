package com.example.methodisthymnapp.ui.screens.hymns

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.methodisthymnapp.ui.component.HymnCard
import com.example.methodisthymnapp.ui.component.HymnsListAppBar
import com.example.methodisthymnapp.ui.component.Screen
import com.example.methodisthymnapp.ui.screens.SearchScreen

@Composable
fun HymnsScreen(
    viewModel: HymnsListViewModel = viewModel(),
    navController: NavHostController,
) {
    val hymns by viewModel.getAllHymns().collectAsState(listOf())
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            HymnsListAppBar(
                listState.elevation,
                onSearchActionClick = { navController.navigate(Screen.HymnsList.createRoute("search")) }
            )
        }
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp),
            state = listState,
            contentPadding = PaddingValues(8.dp)
        ) {

            items(hymns) { hymn ->
                var favoriteState by remember { mutableStateOf(hymn.isFavorite != 0) }

                HymnCard(
                    hymn,
                    isFavorite = favoriteState,
                    onFavoriteButtonToggle = {
                        favoriteState = !favoriteState
                        viewModel.updateFavoriteState(hymn.id, favoriteState.compareTo(false))
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


val LazyListState.elevation: Dp
    get() = if (firstVisibleItemIndex == 0) {
        // For the first element, use the minimum of scroll offset and default elevation
        // i.e. a value between 0 and 4.dp
        minOf(firstVisibleItemScrollOffset.toFloat().dp, AppBarDefaults.TopAppBarElevation)
    } else {
        // If not the first element, always set elevation and show the shadow
        AppBarDefaults.TopAppBarElevation
    }


fun NavGraphBuilder.hymnsGraph(navController: NavHostController) {
    navigation(
        startDestination = Screen.HymnsList.createRoute("HymnsList"),
        route = Screen.HymnsList.route
    ) {

        composable(route = Screen.HymnsList.createRoute("HymnsList")) {
            HymnsScreen(hiltViewModel(), navController)
        }

        composable(
            route = Screen.HymnsList.createRoute("$HYMNS_CONTENT_KEY/{$CLICKED_HYMN_ID}"),
            arguments = listOf(navArgument(CLICKED_HYMN_ID) { type = NavType.IntType })
        ) {
            val clickedHymnId = it.arguments?.getInt(CLICKED_HYMN_ID)!!
            HymnContentScreen(navController, clickedHymnId, hiltViewModel())
        }

        composable(route = Screen.HymnsList.createRoute("Search")) {
            SearchScreen(navController, hiltViewModel())
        }
    }
}

const val CLICKED_HYMN_ID = "clickedHymnId"
const val HYMNS_CONTENT_KEY = "HymnsContent"