package com.example.methodisthymnapp.ui.component

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
    val existingBackStackEntry = backQueue.find { it.destination.route == destination }
    if (existingBackStackEntry != null) {
        backQueue.remove(existingBackStackEntry)
        backQueue.addLast(existingBackStackEntry)
    }
    navigate(destination) {
        launchSingleTop = true
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
            OnBackPressed(navController)
        }

        composable(route = Screen.Favorites.route) {
            FavoritesScreen()
            OnBackPressed(navController)
        }
    }
}

//Custom back press logic that handles backStack Navigation
@Composable
fun OnBackPressed(navController: NavHostController) {
    val backQueue = navController.backQueue
    val backStackCount = backQueue.filter { backStackEntry ->
        backStackEntry.destination.route != null
    }.count()
    if (backStackCount > 1) {
        BackHandler {
            backQueue.removeLast()
            val topOfStack = backQueue.last().destination.route
            navController.navigate(topOfStack!!) {
                launchSingleTop = true
            }
        }
    }
}


sealed class Screen(val route: String, @DrawableRes val icon: Int) {
    object Canticles : Screen("canticles", R.drawable.ic_canticles)
    object Favorites : Screen("favorites", R.drawable.ic_heartfilled)
    object HymnsList : Screen("home", R.drawable.ic_hymns) {
        fun createRoute(route: String) = "hymns/$route"
    }
}





