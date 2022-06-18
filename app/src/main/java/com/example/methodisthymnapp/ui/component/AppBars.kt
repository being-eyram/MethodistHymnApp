package com.example.methodisthymnapp.ui.component

import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.methodisthymnapp.ui.screens.Screen
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate


// Bottom Nav Bar
val bottomNavScreens = listOf(
    Screen.PrimaryScreen.HymnsList,
    Screen.PrimaryScreen.Canticles,
    Screen.PrimaryScreen.Favorites
)

@Composable
fun MHABottomNavBar(
    currentDestination: Screen,
    navController: NavController<Screen>,
) {
    BottomNavigation(
        Modifier.height(56.dp),
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.onBackground,
        elevation = 8.dp
    ) {
        bottomNavScreens.forEach { screen ->

            BottomNavigationItem(
                selected = currentDestination == screen,
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
                onClick = { navController.navigate(screen) },
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

