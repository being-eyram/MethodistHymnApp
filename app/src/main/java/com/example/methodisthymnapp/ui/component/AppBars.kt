package com.example.methodisthymnapp.ui.component

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import com.example.methodisthymnapp.R





// Bottom App Bar

val bottomNavScreens = listOf(
    Screen.BottomNavScreen.HymnsList,
    Screen.BottomNavScreen.Canticles,
    Screen.BottomNavScreen.Favorites
)

@Composable
fun MHABottomNavBar(
    currentDestination: NavDestination?,
    navController: NavHostController,
) {
    BottomNavigation(
        Modifier.height(56.dp),
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.onBackground,
        elevation = 8.dp
    ) {
        bottomNavScreens.forEach { screen ->

            BottomNavigationItem(
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                selectedContentColor = Color(0xFF50D1AA),
                unselectedContentColor = Color(0xFF707070),
                label = {
                    val label = screen.route.replaceFirstChar { it.uppercaseChar() }
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Normal
                    )
                },
                onClick = { navController.navigateTo(screen.route) },
                icon = {
                    Icon(
                        painter = painterResource(id = screen.icon),
                        contentDescription = "Navigate to ${screen.route} screen"
                    )
                }
            )
        }
    }
}

//Favorites Screen AppBars

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FavoritesAppBar(
    elevation: Dp,
    selectedCount: Int,
    onReturnClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var showContextAppBar by remember { mutableStateOf(false) }
    showContextAppBar = selectedCount > 0

    AnimatedContent(
        targetState = showContextAppBar,
        transitionSpec = {
            scaleIn(initialScale = 0.50f, animationSpec = tween(220, delayMillis = 90)) +
                    fadeIn(animationSpec = tween(220, delayMillis = 90)) with
                    fadeOut(animationSpec = tween(90))
        },
        contentAlignment = Alignment.Center
    ) { state ->
        when {
            state -> FavoritesContextAppBar(
                selectedCount = selectedCount,
                onReturnClick = onReturnClick,
                onDeleteClick = onDeleteClick
            )

            else -> FavoritesDefaultAppBar(elevation = elevation)
        }
    }
}

@Composable
fun FavoritesDefaultAppBar(elevation: Dp) {
    TopAppBar(
        elevation = elevation,
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.onBackground,
        title = { Text("Favorites") }
    )
}

@Composable
fun FavoritesContextAppBar(
    selectedCount: Int,
    onReturnClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    TopAppBar(
        title = { Text("$selectedCount") },
        backgroundColor = MaterialTheme.colors.background,
        navigationIcon = {
            IconButton(onClick = onReturnClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_return),
                    contentDescription = null,
                    tint = MaterialTheme.colors.onBackground
                )
            }
        },
        actions = {
            IconButton(onClick = onDeleteClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = null,
                    tint = MaterialTheme.colors.onBackground
                )
            }
        }
    )
}
