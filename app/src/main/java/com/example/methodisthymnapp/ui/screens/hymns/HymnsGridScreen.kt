package com.example.methodisthymnapp.ui.screens.hymns

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.methodisthymnapp.R
import com.example.methodisthymnapp.ui.theme.MHATheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HymnsGridScreen() {
    var isExpanded by remember { mutableStateOf(false) }

    HymnsGridContainerExpanded(
        text = "1 - 100 ",
        isExpanded = isExpanded,
        onClick = { isExpanded = !isExpanded }
    )
}


@Preview
@Composable
fun HymnsGridScreenPreview() {
    MHATheme {
        HymnsGridScreen()
    }
}

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun HymnsGridContainerExpanded(text: String, isExpanded: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
        onClick = onClick,
        indication = null,
        border = BorderStroke(Dp.Hairline, Color.Black),
        elevation = 0.dp,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = text,
                    style = MaterialTheme.typography.h2,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                IconButton(onClick = onClick) {
                    Icon(
                        modifier = Modifier.rotate(if (isExpanded) 180f else 0f),
                        painter = painterResource(id = R.drawable.ic_drop_down),
                        contentDescription = null,
                        tint = Color.Unspecified,
                    )
                }
            }

            AnimatedContent(
                targetState = isExpanded,
                contentAlignment = Alignment.Center
            ) { expanded ->
                if (expanded) {
                    LazyVerticalGrid(
                        cells = GridCells.Fixed(5),
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        val list = (1..100).toList()
                        items(list) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clickable { },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = it.toString(),
                                    style = MaterialTheme.typography.h2,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
