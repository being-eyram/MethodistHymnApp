package com.example.methodisthymnapp.ui.screens.favorites

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.methodisthymnapp.R
import com.example.methodisthymnapp.database.HymnEntity
import com.example.methodisthymnapp.ui.component.AuthorTag
import com.example.methodisthymnapp.ui.component.paddHymnNum

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoritesScreen(viewModel: FavoritesViewModel = viewModel()) {

    val favorites by viewModel.getFavorites().collectAsState(listOf())
//Add a button to unfavorite all favorite items
    // Use the delete icon for the delete favorite rather.
    Box(Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            cells = GridCells.Adaptive(minSize = 168.dp),
            contentPadding = PaddingValues(
                horizontal = 8.dp,
                vertical = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
        /**
         * Changed the Undo FavoriteIcon to a delete button until I
         * change my mind.
         *
         * I'm thinking maybe I should hide the Undo Favorite Icon 🙂
         * but that'll be after I learn how to do dialogs in Compose.
         */
            items(favorites) { hymn ->
                FavoriteItemCard(
                    hymn = hymn,
                    onCardClick = { },
                    onRemoveFavoriteButtonClick = {
                        viewModel.updateFavoriteState(hymn.id, 0)
                    }
                )
            }
        }
    }
}


@Composable
fun FavoriteItemCard(
    hymn: HymnEntity,
    onCardClick: () -> Unit,
    onRemoveFavoriteButtonClick: () -> Unit,
) {
    val (num, title, author) = hymn

    Card(
        modifier = Modifier
            .height(112.dp)
            .width(168.dp)
            .clickable(onClick = onCardClick),
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
                    color = Color(0xFF232323),
                    style = MaterialTheme.typography.h2
                )

                IconButton(
                    modifier = Modifier.size(48.dp, 40.dp),
                    onClick = onRemoveFavoriteButtonClick
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }

//                FavoriteToggleButton(
//                    isFavorite = isFavorite,
//                    onClick = onFavoriteButtonToggle
//                )
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

//@Preview
//@Composable
//fun FavoriteItemCardPreview() {
//    MHATheme {
//        FavoriteItemCard(
//            HymnEntity(
//                0,
//                "On my way to become big",
//                "Eyram Michael",
//                "There is no lyrics bruh",
//                1
//            ),
//            onCardClick = {},
//            onFavoriteButtonToggle = {}
//        )
//    }
//}