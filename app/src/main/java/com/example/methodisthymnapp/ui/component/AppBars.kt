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

