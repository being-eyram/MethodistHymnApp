package com.example.methodisthymnapp.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.methodisthymnapp.R
import com.example.methodisthymnapp.ui.screens.SearchScreen
import com.example.methodisthymnapp.ui.screens.canticles.CanticlesScreen
import com.example.methodisthymnapp.ui.screens.favorites.FavoritesScreen
import com.example.methodisthymnapp.ui.screens.hymns.CLICKED_HYMN_ID
import com.example.methodisthymnapp.ui.screens.hymns.HYMNS_CONTENT_KEY
import com.example.methodisthymnapp.ui.screens.hymns.HymnContentScreen
import com.example.methodisthymnapp.ui.screens.hymns.HymnsListScreen
import com.example.methodisthymnapp.ui.theme.MHATheme


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MethodistHymnApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    var showBottomNavBar by remember { mutableStateOf(true) }

    val bottomNavBar: @Composable () -> Unit = {
        AnimatedVisibility(visible = showBottomNavBar,enter = slideInVertically{it/2} + fadeIn(tween(90))) {
            MHABottomNavBar(
                currentDestination = currentDestination,
                navController = navController,
            )
        }
    }

    MHATheme {
        Scaffold(
            bottomBar = bottomNavBar
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.BottomNavScreen.HymnsList.route
            ) {

                composable(route = Screen.BottomNavScreen.HymnsList.route) {
                    showBottomNavBar = true
                    HymnsListScreen(hiltViewModel(), navController)
                }

                composable(
                    route = Screen.FullScreen.HymnDestails.createRoute("$HYMNS_CONTENT_KEY/{$CLICKED_HYMN_ID}"),
                    arguments = listOf(navArgument(CLICKED_HYMN_ID) { type = NavType.IntType })
                ) {
                    showBottomNavBar = false
                    val clickedHymnId = it.arguments?.getInt(CLICKED_HYMN_ID)!!
                    HymnContentScreen(navController, clickedHymnId, hiltViewModel())
                }

                composable(route = Screen.FullScreen.Search.route) {
                    showBottomNavBar = false
                    SearchScreen(navController, hiltViewModel())
                }

                composable(route = Screen.BottomNavScreen.Canticles.route) {
                    showBottomNavBar = true
                    CanticlesScreen()
                }

                composable(route = Screen.BottomNavScreen.Favorites.route) {
                    FavoritesScreen(
                        viewModel = hiltViewModel(),
                        navController = navController,
                        onFavoriteCardOverflowClick = { showBottomNavBar = false },
                        onBottomSheetDismiss = { showBottomNavBar = true }
                    )
                }
            }
        }
    }
}


fun NavHostController.navigateTo(destination: String) {
    navigate(destination) {
        restoreState = true
        launchSingleTop = true
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
    }
}

sealed class Screen() {

    sealed class FullScreen(val route: String) : Screen() {
        object Search : FullScreen("search")
        object HymnDestails : FullScreen("Details") {
            fun createRoute(route: String) = "Hymns/$route"
        }
    }

    sealed class BottomNavScreen(val route: String, @DrawableRes val icon: Int) : Screen() {
        object Canticles : BottomNavScreen("canticles", R.drawable.ic_canticles)
        object Favorites : BottomNavScreen("favorites", R.drawable.ic_favorite_button_active)
        object HymnsList : BottomNavScreen("hymns", R.drawable.ic_hymns)
    }
}