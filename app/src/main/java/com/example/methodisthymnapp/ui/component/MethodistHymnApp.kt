package com.example.methodisthymnapp.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.methodisthymnapp.di.viewModelEntryPoint
import com.example.methodisthymnapp.ui.screens.Screen
import com.example.methodisthymnapp.ui.screens.canticles.CanticlesScreen
import com.example.methodisthymnapp.ui.screens.favorites.FavoritesScreen
import com.example.methodisthymnapp.ui.screens.hymns.HymnDetailsScreen
import com.example.methodisthymnapp.ui.screens.hymns.HymnsListScreen
import com.example.methodisthymnapp.ui.screens.search.SearchScreen
import com.example.methodisthymnapp.ui.theme.MHATheme
import dev.olshevski.navigation.reimagined.*
import kotlinx.coroutines.runBlocking


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
            enter = slideInVertically(
                animationSpec = tween(
                    durationMillis = 350,
                    delayMillis = 50,
                    easing = CubicBezierEasing(0.61F, 1F, 0.88F, 1F)
                ),
                initialOffsetY = { it }
            ),
            exit = runBlocking {
                slideOutVertically(
                    animationSpec = tween(
                        durationMillis = 350,
                        delayMillis = 50,
                        easing = CubicBezierEasing(0.12F, 0F, 0.39F, 0F)
                    ),
                    targetOffsetY = { it }
                )
            }
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