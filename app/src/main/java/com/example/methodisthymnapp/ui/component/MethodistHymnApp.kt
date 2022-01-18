package com.example.methodisthymnapp.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.methodisthymnapp.R
import com.example.methodisthymnapp.ui.screens.canticles.CanticlesScreen
import com.example.methodisthymnapp.ui.screens.favorites.FavoritesScreen
import com.example.methodisthymnapp.ui.screens.hymns.hymnsGraph
import com.example.methodisthymnapp.ui.theme.MHATheme


@Composable
fun MethodistHymnApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavBar: @Composable () -> Unit = {
        MHABottomNavBar(
            currentDestination = currentDestination,
            navController = navController,
        )
    }

    MHATheme {
        Scaffold(
            bottomBar = bottomNavBar
        ) {
            MHANavGraph(navController = navController)
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


@Composable
fun MHANavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.HymnsList.route
    ) {
        hymnsGraph(navController)

        composable(route = Screen.Canticles.route) {
            CanticlesScreen()
        }

        composable(route = Screen.Favorites.route) {
            FavoritesScreen(hiltViewModel())
        }
    }
}

sealed class Screen(val route: String, @DrawableRes val icon: Int) {
    object Canticles : Screen("canticles", R.drawable.ic_canticles)
    object Favorites : Screen("favorites", R.drawable.ic_heartfilled)
    object HymnsList : Screen("hymns", R.drawable.ic_hymns) {
        fun createRoute(route: String) = "Hymns/$route"
    }
}





