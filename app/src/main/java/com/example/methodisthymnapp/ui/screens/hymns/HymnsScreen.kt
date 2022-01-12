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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.methodisthymnapp.HymnApplication
import com.example.methodisthymnapp.ui.component.HymnCard
import com.example.methodisthymnapp.ui.component.MHAAppBar
import com.example.methodisthymnapp.ui.component.OnBackPressed
import com.example.methodisthymnapp.ui.component.Screen
import com.example.methodisthymnapp.ui.screens.SearchScreen

@Composable
fun HymnsScreen(
    viewModel: HymnsListViewModel = viewModel(),
    navController: NavHostController,
) {
    val hymns by viewModel.getAllHymns().collectAsState(initial = listOf())
    val listState = rememberLazyListState()
    val cloud = (LocalContext.current.applicationContext as HymnApplication).cloud



    Scaffold(
        topBar = {
            MHAAppBar(
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
                        cloud.id = hymn.id
                        navController.navigate(Screen.HymnsList.createRoute("details"))
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
        startDestination = Screen.HymnsList.createRoute("hymns"),
        route = Screen.HymnsList.route
    ) {

        composable(route = Screen.HymnsList.createRoute("hymns")) {
            HymnsScreen(hiltViewModel(), navController)
            OnBackPressed(navController)
        }

        composable(route = Screen.HymnsList.createRoute("details")) {
            val cloud = (LocalContext.current.applicationContext as HymnApplication).cloud
//            val clickedHymn = backStackEntry.arguments?.getInt("clickedHymn")!!
            val clickedHymn = cloud.id
            HymnContentScreen(navController, clickedHymn, hiltViewModel())
            OnBackPressed(navController)
        }

        composable(route = Screen.HymnsList.createRoute("search")) {
            SearchScreen(navController, hiltViewModel())
        }
    }
}