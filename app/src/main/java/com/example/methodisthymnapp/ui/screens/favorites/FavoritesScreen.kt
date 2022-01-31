package com.example.methodisthymnapp.ui.screens.favorites

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.methodisthymnapp.R
import com.example.methodisthymnapp.database.HymnEntity
import com.example.methodisthymnapp.ui.component.*
import com.example.methodisthymnapp.ui.screens.hymns.HYMNS_CONTENT_KEY
import com.example.methodisthymnapp.ui.screens.hymns.elevation
import com.example.methodisthymnapp.ui.theme.MHATheme


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = viewModel(),
    navController: NavHostController
) {

    val favorites by viewModel.getFavorites().collectAsState(listOf())
    val selectedCardMap = mutableMapOf<Int, Boolean>()
    var selectedCount by remember { mutableStateOf(0) }
    var isReturnClick by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            FavoritesAppBar(
                elevation = listState.elevation,
                selectedCount = selectedCount,
                onReturnClick = {
                    selectedCardMap.clear()
                    selectedCount = 0
                    isReturnClick = true
                },
                onDeleteClick = {
                    // Unfavourite Items using their Ids if selected
                    val idsToUnfavorite = selectedCardMap.filter { id -> id.value }.keys
                    idsToUnfavorite.forEach { viewModel.updateFavoriteState(it, 0) }
                    selectedCardMap.clear()
                    selectedCount = 0
                }
            )
        }
    ) {
        LazyVerticalGrid(
            state = listState,
            cells = GridCells.Adaptive(minSize = 168.dp),
            contentPadding = PaddingValues(8.dp, 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(favorites) { hymn ->
                /**
                 * It seems to me that because of the key is [isSelected] is generated
                 * for each hymn in the favorites list.
                 */
                key(hymn.id) {
                    var isSelected by remember { mutableStateOf(false) }
                    /**
                     * Reset the value of remembered value of isSelected.
                     * This is done to prevent the AppBar from reappearing since the value of
                     * isSelected is remembered for each.
                     */
                    if (isReturnClick) isSelected = false
                    //store in a Map the Id of the hymn to it's selection truthValue
                    //selectedCardMap[hymn.id] = isSelected
                    FavoriteItemCard(
                        hymn = hymn,
                        isSelected = isSelected,
                        onCardClick = {
                            if (selectedCount > 0) {
                                isSelected = !isSelected
                            } else {
                                navController.navigate(Screen.HymnsList.createRoute("$HYMNS_CONTENT_KEY/${hymn.id}"))
                            }
                        },
                        onCheckMarkClick = { isSelected = !isSelected },
                        onLongPress = { isSelected = !isSelected },
                    )

                    SideEffect {
                        selectedCardMap[hymn.id] = isSelected
                        selectedCount = selectedCardMap.count { it.value }
                        //Reset the value of isReturnClick after recomposition.
                        isReturnClick = false
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteItemCard(
    hymn: HymnEntity,
    isSelected: Boolean,
    onCardClick: () -> Unit,
    onCheckMarkClick: () -> Unit,
    onLongPress: () -> Unit
) {
    val (num, title, author) = hymn

    Card(
        modifier = Modifier
            .height(112.dp)
            .width(168.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onCardClick() },
                    onLongPress = { onLongPress() },
                )
                //try do a detect drag gesture feature like selection on an iphone.
            },
        elevation = 0.dp,
        border = BorderStroke(width = Dp.Hairline, color = Color.Gray),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 11.dp),
        ) {

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
                    text = paddHymnNum(num),
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.h2
                )
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
                }
            }

            Text(
                modifier = Modifier.paddingFromBaseline(bottom = 4.dp),
                text = title,
                style = MaterialTheme.typography.subtitle1,
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            AuthorTag(
                modifier = Modifier.padding(top = 8.dp, bottom = 11.dp),
                author = author
            )
        }
    }
}


@Preview
@Composable
fun FavoritesAppBarPreview() {
    MHATheme {
        FavoritesDefaultAppBar(elevation = 0.dp)
    }
}


@Preview
@Composable
fun FavoriteItemCardPreview() {
    MHATheme {
        FavoriteItemCard(
            HymnEntity(
                0,
                "On my way to become big",
                "Eyram Michael",
                "There is no lyrics bruh",
                1
            ),
            isSelected = false,
            onCardClick = {},
            onLongPress = {},
            onCheckMarkClick = {},
        )
    }
}