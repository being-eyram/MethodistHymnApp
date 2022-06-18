package com.example.methodisthymnapp.ui.screens.hymns

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.methodisthymnapp.R
import com.example.methodisthymnapp.data.Hymn
import com.example.methodisthymnapp.ui.screens.Screen
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.pop

private val MAX_LYRICS_TEXT_SIZE = 24.sp
const val INTENT_TYPE_TEXT = "text/plain"

@Composable
fun HymnDetailsScreen(
    navController: NavController<Screen>,
    clickedHymnId: Int,
    viewModel: HymnDetailsViewModel
) {
    viewModel.getHymn(clickedHymnId)

    val clickedHymn by viewModel.result.observeAsState()
    var sliderPosition by remember { mutableStateOf(0f) }
    val defaultLyricsTextSize = typography.body1.fontSize
    var lyricsTextSize by remember { mutableStateOf(defaultLyricsTextSize) }
    var showTextSizeSlider by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            ContentAppBar(
                title = getAppBarTitle(clickedHymnId),
                onNavigationActionClick = { navController.pop() },
                onTextSizeActionClick = { showTextSizeSlider = !showTextSizeSlider },
                onShareActionClick = {
                    onShareActionClick(
                        context = context,
                        lyrics = clickedHymn?.lyrics ?: "Lyrics Not Available"
                    )
                },
                elevation = if (scrollState.value > 1) 4.dp else 0.dp
            )

            clickedHymn?.let {
                HymnContent(
                    hymn = it,
                    lyricsTextSize = lyricsTextSize,
                    scrollState = scrollState
                )
            }
        }

        if (showTextSizeSlider) {
            Dialog(onDismissRequest = { showTextSizeSlider = !showTextSizeSlider }) {
                TextSizeSlider(
                    sliderPosition = sliderPosition,
                    onSliderPositionChange = { sliderPosition = it },
                    onValueChangeFinished = {
                        lyricsTextSize = (MAX_LYRICS_TEXT_SIZE * sliderPosition) / 100f
                    }
                )
            }
        }
    }
}

@Composable
fun ContentAppBar(
    title: String,
    elevation: Dp,
    onNavigationActionClick: () -> Unit,
    onTextSizeActionClick: () -> Unit,
    onShareActionClick: () -> Unit,
) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        backgroundColor = MaterialTheme.colors.background,
        elevation = elevation,
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            IconButton(
                modifier = Modifier.align(Alignment.CenterStart),
                onClick = onNavigationActionClick
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Navigate Up",
                    tint = MaterialTheme.colors.onBackground,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }

            Text(
                text = title,
                color = MaterialTheme.colors.onBackground,
                style = typography.subtitle1.copy(fontSize = 16.sp),
            )

            Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                IconButton(onClick = onTextSizeActionClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_text_size),
                        contentDescription = "Change text size",
                        tint = MaterialTheme.colors.onBackground,
                    )
                }

                IconButton(onClick = onShareActionClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_share),
                        contentDescription = "Share Hymn",
                        tint = MaterialTheme.colors.onBackground,
                    )
                }
            }
        }
    }
}

@Composable
fun HymnContent(
    hymn: Hymn,
    lyricsTextSize: TextUnit,
    scrollState: ScrollState
) {
    val (_, title, author, lyrics) = hymn
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .paddingFromBaseline(48.dp)
                .padding(horizontal = 16.dp),
            text = title,
            textAlign = TextAlign.Left,
            style = typography.h3
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .paddingFromBaseline(64.dp)
                .padding(horizontal = 16.dp),
            text = lyrics,
            textAlign = TextAlign.Start,
            style = typography.body1.copy(fontSize = lyricsTextSize)
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .paddingFromBaseline(32.dp)
                .padding(horizontal = 16.dp),
            text = author,
            textAlign = TextAlign.Start,
            style = typography.caption
        )

        Spacer(modifier = Modifier.padding(bottom = 168.dp))
    }
}

@Composable
fun TextSizeSlider(
    modifier: Modifier = Modifier,
    sliderPosition: Float,
    onSliderPositionChange: (sliderPosition: Float) -> Unit,
    onValueChangeFinished: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(96.dp)
            .clip(MaterialTheme.shapes.small),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "ADJUST FONT SIZE",
                modifier = Modifier
                    .paddingFromBaseline(top = 20.dp)
                    .align(Alignment.TopStart),
                style = typography.overline,
                color = MaterialTheme.colors.onBackground
            )

            Row(Modifier.align(Alignment.Center)) {
                Icon(
                    modifier = Modifier
                        .padding(end = 8.dp, top = 8.dp)
                        .size(20.dp)
                        .align(Alignment.CenterVertically),
                    painter = painterResource(id = R.drawable.ic_text),
                    contentDescription = "Reduce Font Size",
                )

                Slider(
                    modifier = Modifier.weight(1f),
                    value = sliderPosition,
                    valueRange = 50f..100f,
                    onValueChange = onSliderPositionChange,
                    onValueChangeFinished = onValueChangeFinished,
                )

                Icon(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(28.dp)
                        .align(Alignment.CenterVertically),
                    painter = painterResource(id = R.drawable.ic_text),
                    contentDescription = "Increase Font Size"
                )
            }
        }
    }
}

private fun getAppBarTitle(id: Int): String {
    return when {
        (id < 10) -> "MH00$id"
        (id < 99) -> "MH0$id"
        else -> "MH$id"
    }
}

fun onShareActionClick(context: Context, lyrics: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, lyrics)
        type = INTENT_TYPE_TEXT
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}