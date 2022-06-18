package com.example.methodisthymnapp.ui.component

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.methodisthymnapp.di.viewModelEntryPoint
import com.example.methodisthymnapp.ui.screens.Screen
import com.example.methodisthymnapp.ui.screens.search.SearchScreen
import com.example.methodisthymnapp.ui.screens.canticles.CanticlesScreen
import com.example.methodisthymnapp.ui.screens.favorites.FavoritesScreen
import com.example.methodisthymnapp.ui.screens.hymns.HymnDetailsScreen
import com.example.methodisthymnapp.ui.screens.hymns.HymnsListScreen
import com.example.methodisthymnapp.ui.theme.MHATheme
import dev.olshevski.navigation.reimagined.*


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MethodistHymnApp() {
    val navController = rememberNavController<Screen>(
        startDestination = Screen.PrimaryScreen.HymnsList
    )
    val navBackStackEntry = navController.backstack.entries.last()
    val currentDestination = navBackStackEntry.destination
    var showBottomNavBar by remember { mutableStateOf(true) }

    val bottomNavBar: @Composable () -> Unit = {
        AnimatedVisibility(
            visible = showBottomNavBar,
            enter = slideInVertically { it / 2 } + fadeIn(tween(90)),
            exit = fadeOut(tween(90)) + slideOutVertically()
        ) {
            MHABottomNavBar(
                currentDestination = currentDestination,
                navController = navController,
            )
        }
    }

    MHATheme {
        Scaffold(bottomBar = bottomNavBar) {

            val context = LocalContext.current
            val viewModelEntryPoint = context.viewModelEntryPoint

            NavBackHandler(navController)

            NavHost(controller = navController) { screen ->

                when (screen) {

                    is Screen.PrimaryScreen.HymnsList -> {
                        showBottomNavBar = true
                        HymnsListScreen(
                            navController = navController,
                            viewModel = viewModelEntryPoint.hymnsListViewModel()
                        )
                    }

                    is Screen.PrimaryScreen.Canticles -> {
                        showBottomNavBar = true
                        CanticlesScreen()
                    }

                    is Screen.PrimaryScreen.Favorites -> {
                        FavoritesScreen(
                            navController = navController,
                            viewModel = viewModelEntryPoint.favoritesViewModel(),
                            onFavoriteCardOverflowClick = { showBottomNavBar = false }
                        ) { showBottomNavBar = true }
                    }

                    is Screen.SecondaryScreen.HymnDetails -> {
                        showBottomNavBar = false
                        val clickedHymnId = screen.hymnNumber
                        HymnDetailsScreen(
                            navController = navController,
                            viewModel = viewModelEntryPoint.hymnDetailsViewModel(),
                            clickedHymnId = clickedHymnId
                        )
                    }

                    is Screen.SecondaryScreen.Search -> {
                        showBottomNavBar = false
                        SearchScreen(
                            navController = navController,
                            viewModel = viewModelEntryPoint.searchViewModel()
                        )
                    }
                }
            }
        }
    }
}


fun NavController<Screen>.navigateTo(destination: Screen) {
    if (!this.moveToTop { it == destination }) {
        this.navigate(destination)
    }
}