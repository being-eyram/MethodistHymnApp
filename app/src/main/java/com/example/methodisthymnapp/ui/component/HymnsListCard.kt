package com.example.methodisthymnapp.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.methodisthymnapp.database.HymnEntity

@Composable
fun HymnListCard(
    hymn: HymnEntity,
    isFavorite: Boolean,
    onFavoriteButtonToggle: () -> Unit,
    onCardClick: () -> Unit
) {
    val (num, title, author, lyrics) = hymn

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
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.87f),
                style = MaterialTheme.typography.h2,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.width(16.dp))

            Divider(
                modifier = Modifier
                    .size(1.dp, 80.dp)
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
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.87f),
                    maxLines = 1,
                )

                Text(
                    modifier = Modifier.paddingFromBaseline(top = 16.dp, bottom = 4.dp),
                    text = lyrics,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.60f),
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
                    FavoriteToggleButton(
                        isFavorite = isFavorite,
                        onClick = onFavoriteButtonToggle
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

@SuppressLint("MissingColorAlphaChannel")
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
            modifier = Modifier.align(Alignment.CenterVertically),
            text = author,
            fontSize = 10.sp,
            color = Color(0xFF50D1AA),
            maxLines = 1
        )
    }
}