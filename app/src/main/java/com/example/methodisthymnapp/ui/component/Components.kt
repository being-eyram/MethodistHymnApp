package com.example.methodisthymnapp.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import com.example.methodisthymnapp.R

@Composable
fun HymnCard(
    num: Int,
    title: String,
    body: String,
    author: String,
    favoriteState: FavoriteState,
    onFavoriteIcClick: () -> Unit,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .height(112.dp)
            .fillMaxWidth()
            .clickable(onClick = onCardClick),
        elevation = 0.dp,
        border = BorderStroke(width = Dp.Hairline, color = Color.Gray),
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                modifier = Modifier
                    .width(45.dp)
                    .align(Alignment.CenterVertically),
                text = paddHymnNum(num),
                color = Color(0xFF232323),
                style = MaterialTheme.typography.h2
            )

            Spacer(modifier = Modifier.width(16.dp))

            Divider(
                modifier = Modifier
                    .height(80.dp)
                    .width(1.dp)
                    .align(Alignment.CenterVertically)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {

                Text(
                    modifier = Modifier.paddingFromBaseline(
                        top = 28.dp,
                        bottom = 4.dp
                    ),
                    text = title,
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 1,
                )

                Text(
                    modifier = Modifier.paddingFromBaseline(top = 16.dp, bottom = 4.dp),
                    text = body,
                    style = MaterialTheme.typography.body2,
                    color = Color(0xFF7D7D7D),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AuthorTag(
                        modifier = Modifier.padding(top = 7.dp),
                        author = author
                    )
                    AnimatedFavoriteIcon(
                        state = favoriteState,
                        onFavoriteIcClick = onFavoriteIcClick
                    )
                }
            }
            Spacer(Modifier.width(16.dp))
        }
    }
}

fun paddHymnNum(num: Int): String {
    return when {
        (num < 10) -> "00$num"
        (num < 100) -> "0$num"
        else -> "$num"
    }
}

@Composable
fun AuthorTag(modifier: Modifier = Modifier, author: String) {
    val tagGreenLight = Color(0x6BE2BE).copy(alpha = 0.24f)
    Row(
        modifier = modifier
            .wrapContentWidth()
            .height(20.dp)
            .clip(shape = RoundedCornerShape(4.dp))
            .background(color = tagGreenLight)
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            text = author,
            fontSize = 10.sp,
            color = Color(0xFF50D1AA),
            maxLines = 1
        )
    }
}

// Top App Bar

@Composable
fun MHAAppBar(
    elevation: Dp,
    onSearchActionClick: () -> Unit,
) {
    TopAppBar(
        backgroundColor = Color.White,
        contentColor = Color(0xFF232323),
        elevation = elevation
    ) {

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = "Methodist Hymn App",
                style = MaterialTheme.typography.h1
            )

            IconButton(onClick = onSearchActionClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Search Hymns Action"
                )
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
        backgroundColor = Color.White,
        contentColor = contentColorFor(backgroundColor = Color.White),
        elevation = 8.dp
    ) {
        bottomNavScreens.forEach { screen ->

            BottomNavigationItem(
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                selectedContentColor = Color.Blue,
                unselectedContentColor = Color(0xFF707070),
                label = {
                    val label = screen.route.replaceFirstChar { it.uppercaseChar() }
                    Text(label)
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


