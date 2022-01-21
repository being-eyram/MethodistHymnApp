package com.example.methodisthymnapp.ui.screens.favorites

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.methodisthymnapp.R
import com.example.methodisthymnapp.database.HymnEntity
import com.example.methodisthymnapp.ui.component.AuthorTag
import com.example.methodisthymnapp.ui.component.paddHymnNum
import com.example.methodisthymnapp.ui.theme.MHATheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoritesScreen(viewModel: FavoritesViewModel = viewModel()) {

    val favorites by viewModel.getFavorites().collectAsState(listOf())
    val selectedCardMap = mutableMapOf<Int, MutableState<Boolean>>()
    var selectedCount by remember { mutableStateOf(0) }

//    if (selectedCount  > 1){
//        Log.i("Count", "Greater ")
//    }

    Scaffold(

    ) {

        LazyVerticalGrid(
            cells = GridCells.Adaptive(minSize = 168.dp),
            contentPadding = PaddingValues(8.dp, 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            itemsIndexed(favorites) { index, hymn ->
                selectedCardMap[index] = remember { mutableStateOf(false) }

                SideEffect {
                    selectedCount = selectedCardMap.count { (it.value).value }
                    Log.i("Count", "$selectedCount")
                }
                FavoriteItemCard(
                    hymn = hymn,
                    isSelected = selectedCardMap[index]?.value!!,
                    onCardClick = {
                        if (selectedCount > 0) {
                            selectedCardMap[index]?.value = !selectedCardMap[index]?.value!!
                        }
                    },
                    onCheckMarkClick = {
                        selectedCardMap[index]?.value = !selectedCardMap[index]?.value!!
                    },
                    onLongPress = {
                        selectedCardMap[index]?.value = !selectedCardMap[index]?.value!!
                    },
                )

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
                    color = Color(0xFF232323),
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