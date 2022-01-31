package com.example.methodisthymnapp.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
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

// Top App Bar
@Composable
fun MHAAppBar(
    elevation: Dp,
    onSearchActionClick: () -> Unit,
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        elevation = elevation
    ) {

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = "Methodist Hymn App",
                style = MaterialTheme.typography.h1,
                color = MaterialTheme.colors.onBackground
            )

            Row(Modifier.wrapContentSize()) {
                IconButton(onClick = onSearchActionClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Search Hymns",
                        tint = MaterialTheme.colors.onBackground
                    )
                }
                IconButton(onClick = {/*TODO*/ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_sort_list),
                        contentDescription = "Sort Hymns",
                        tint = MaterialTheme.colors.onBackground
                    )
                }
            }
        }
    }
}


// Bottom App Bar

val bottomNavScreens = listOf(
    Screen.HymnsList,
    Screen.Canticles,
    Screen.Favorites
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


