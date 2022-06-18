package com.example.methodisthymnapp.ui.screens.favorites

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.methodisthymnapp.R
import com.example.methodisthymnapp.ui.component.AuthorTag
import com.example.methodisthymnapp.ui.component.Screen
import com.example.methodisthymnapp.ui.component.paddHymnNum
import com.example.methodisthymnapp.ui.screens.hymns.HYMN_DETAILS_KEY
import com.example.methodisthymnapp.ui.screens.hymns.elevation
import com.example.methodisthymnapp.ui.screens.hymns.onShareActionClick
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = hiltViewModel(),
    navController: NavHostController,
    listState: LazyListState = rememberLazyListState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    sheetState: ModalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden),
    onFavoriteCardOverflowClick: () -> Unit,
    onBottomSheetDismiss: () -> Unit,
) {

    val uiState = viewModel.uiState.collectAsState().value
    var selectedCount by remember { mutableStateOf(uiState.selectedCount) }
    val context = LocalContext.current

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            FavoritesModalSheetContent(
                onShareClick = {
                    onShareActionClick(context, viewModel.getLyricsFor(uiState.clickedOverflowId))
                    coroutineScope.launch { sheetState.hide() }
                },
                onRemoveFromFavoritesClick = {
                    viewModel.onRemoveFromFavoritesClick(uiState.clickedOverflowId)
                    coroutineScope.launch { sheetState.hide() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                FavoritesAppBar(
                    elevation = listState.elevation,
                    selectedCount = selectedCount,
                    onReturnClick = viewModel::onReturnClick,
                    onDeleteClick = viewModel::onDeleteClick
                )
            }
        ) {
            LazyVerticalGrid(
                state = listState,
                cells = GridCells.Adaptive(minSize = 168.dp),
                contentPadding = PaddingValues(
                    top = 16.dp,
                    bottom = 56.dp,
                    start = 8.dp,
                    end = 8.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(uiState.favorites) { favoriteHymn ->
                    key(favoriteHymn.id) {
                        FavoriteItemCard(
                            id = favoriteHymn.id,
                            title = favoriteHymn.title,
                            author = favoriteHymn.author,
                            isSelected = favoriteHymn.isSelected,
                            onCardClick = {
                                // the selectedCount value I get over here is consistently different from
                                // one at the top. Try looking into it.
                                if (selectedCount > 0) {
                                    favoriteHymn.toggleSelectOnClick(favoriteHymn.id)
                                } else {
                                    navController.navigate(
                                        Screen.HymnDestails.createRoute("$HYMN_DETAILS_KEY/${favoriteHymn.id}")
                                    )
                                }
                            },
                            onCheckMarkClick = { favoriteHymn.onCheckMarkClick(favoriteHymn.id) },
                            onLongPress = { favoriteHymn.onLongPress(favoriteHymn.id) },
                            onOverflowMenuClick = {
                                coroutineScope.launch {
                                    onFavoriteCardOverflowClick.invoke()
                                    sheetState.show()
                                    //putting this method in the coroutine makes it
                                    // run as desired. I don't know why yet. But I intend to find out.
                                    favoriteHymn.onOverflowMenuClick(favoriteHymn.id)
                                }

                            }
                        )
                    }
                    LaunchedEffect(uiState, sheetState.currentValue) {
                        selectedCount = uiState.selectedCount
                        if (!sheetState.isVisible) {
                            onBottomSheetDismiss.invoke()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteItemCard(
    id: Int,
    title: String,
    author: String,
    isSelected: Boolean,
    onCardClick: () -> Unit,
    onCheckMarkClick: () -> Unit,
    onOverflowMenuClick: () -> Unit,
    onLongPress: () -> Unit
) {

    Card(
        modifier = Modifier
            .size(168.dp, 112.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onCardClick.invoke() },
                    onLongPress = { onLongPress.invoke() },
                )
            },
        elevation = 0.dp,
        border = BorderStroke(width = Dp.Hairline, color = Color.Gray),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 11.dp)
        ) {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        modifier = Modifier
                            .width(45.dp)
                            .paddingFromBaseline(27.dp),
                        text = paddHymnNum(id),
                        color = MaterialTheme.colors.onBackground,
                        style = MaterialTheme.typography.h2
                    )
                    //Remember to use animated content over here.
                    if (isSelected) {
                        IconButton(
                            modifier = Modifier.size(48.dp, 40.dp),
                            onClick = onCheckMarkClick
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_checkmark),
                                contentDescription = null,
                                tint = Color(0xFF50D1AA)
                            )
                        }
                    } else {
                        IconButton(
                            modifier = Modifier.size(48.dp, 40.dp),
                            onClick = onOverflowMenuClick
                        ) {
                            Icon(
                                modifier = Modifier.size(18.dp),
                                painter = painterResource(id = R.drawable.ic_overflow_menu),
                                contentDescription = null,
                                tint = MaterialTheme.colors.onBackground.copy(alpha = 0.87f)
                            )
                        }
                    }
                }

                Text(
                    modifier = Modifier
                        .paddingFromBaseline(bottom = 4.dp)
                        .padding(end = 11.dp),
                    text = title,
                    style = MaterialTheme.typography.subtitle1,
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            AuthorTag(
                modifier = Modifier.padding(top = 8.dp, bottom = 11.dp, end = 11.dp),
                author = author
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FavoritesModalSheetContent(onShareClick: () -> Unit, onRemoveFromFavoritesClick: () -> Unit) {
    ListItem(
        modifier = Modifier.clickable(onClick = onShareClick),
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_share),
                contentDescription = null,
                tint = MaterialTheme.colors.onBackground.copy(alpha = 0.87f)
            )
        },
        text = { Text("Share") }
    )
    ListItem(
        modifier = Modifier.clickable(onClick = onRemoveFromFavoritesClick),
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_delete),
                contentDescription = null,
                tint = MaterialTheme.colors.onBackground.copy(alpha = 0.87f)
            )
        },
        text = { Text("Remove from Favorites") }
    )
}

// App Bars

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


//@Preview
//@Composable
//fun FavoritesAppBarPreview() {
//    MHATheme {
//        FavoritesDefaultAppBar(elevation = 0.dp)
//    }
//}

//@Preview
//@Composable
//fun FavoriteItemCardPreview() {
//    MHATheme {
//        FavoriteItemCard(
//            Hymn(
//                0,
//                "On my way to the stars",
//                "Eyram Michael",
//                "There is no lyrics bruh",
//                1
//            ),
//            isSelected = false,
//            onCardClick = {},
//            onLongPress = {},
//            onCheckMarkClick = {},
//            onOverflowMenuClick = {}
//        )
//    }
//}