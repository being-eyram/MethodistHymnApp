package com.example.methodisthymnapp.ui.screens.hymns

import android.content.Intent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.methodisthymnapp.R
import com.example.methodisthymnapp.database.HymnEntity

private val maxTitleFontSize = 50.sp
private val maxLyricsFontSize = 40.sp
private const val INTENT_TYPE = "text/plain"

@Composable
fun HymnContentScreen(
    navController: NavController,
    clickedHymnId: Int,
    hymnContentViewModel: HymnContentViewModel
) {
    hymnContentViewModel.getHymn(clickedHymnId)

    val clickedHymn = hymnContentViewModel.result.observeAsState()
    var sliderPosition by remember { mutableStateOf(0f) }
    var bodyTitleFontSize by remember { mutableStateOf(20.sp) }
    var lyricsFontSize by remember { mutableStateOf(16.sp) }
    var isTextSizeActionClicked: Boolean by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Box(modifier = Modifier
        .fillMaxSize()
        .clickable(indication = null, interactionSource = interactionSource) {
            if (isTextSizeActionClicked) {
                isTextSizeActionClicked = false
            }
        }) {
//scrollState.value
        Column(modifier = Modifier.fillMaxSize()) {
            ContentAppBar(
                title = getAppBarTitle(clickedHymnId),
                onNavigationActionClick = { navController.navigateUp() },
                onTextSizeActionClick = { isTextSizeActionClicked = !isTextSizeActionClicked },
                onShareActionClick = {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
                        type = INTENT_TYPE
                    }

                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                },
            elevation = if (scrollState.value > 1 ) 4.dp else 0.dp
            )
            clickedHymn.value?.let {
                HymnContent(
                    hymn = it,
                    titleFontSize = bodyTitleFontSize,
                    bodyFontSize = lyricsFontSize,
                    scrollState = scrollState
                )
            }


        }

        if (scrollState.isScrollInProgress && isTextSizeActionClicked) {
            isTextSizeActionClicked = false
        }

        if (isTextSizeActionClicked) {
            SliderCard(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp),
                sliderPosition = sliderPosition,
                onSliderPositionChange = { sliderPosition = it },
                onValueChangeFinished = {
                    bodyTitleFontSize = (maxTitleFontSize * sliderPosition) / 100f
                    lyricsFontSize = (maxLyricsFontSize * sliderPosition) / 100f
                }
            )
        }
    }
}


@Composable
fun ContentAppBar(
    title: String,
    elevation : Dp,
    onNavigationActionClick: () -> Unit,
    onTextSizeActionClick: () -> Unit,
    onShareActionClick: () -> Unit,
) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        backgroundColor = Color.White,
        elevation = elevation,
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            Icon(
                Icons.Default.ArrowBack,
                "Navigate Up",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(onClick = onNavigationActionClick)
                    .padding(16.dp)
            )

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = title
            )

            Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                Icon(
                    //Try changing the resource to ic_textsize
                    painterResource(id = R.drawable.ic_text_size),
                    "Navigate Up",
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(onClick = onTextSizeActionClick)
                        .padding(16.dp, 16.dp, 12.dp, 16.dp)
                )
                Icon(
                    painterResource(id = R.drawable.ic_share),
                    contentDescription = "Share Hymn",
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(onClick = onShareActionClick)
                        .padding(12.dp, 16.dp, 16.dp, 16.dp)
                )
            }

        }
    }
}

@Composable
fun HymnContent(
    hymn: HymnEntity,
    titleFontSize: TextUnit,
    bodyFontSize: TextUnit,
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
            textAlign = TextAlign.Center,
            style = typography.h3
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .paddingFromBaseline(48.dp)
                .padding(start = 16.dp, end = 16.dp),
            text = lyrics,
            textAlign = TextAlign.Start,
            style = typography.body1
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .paddingFromBaseline(32.dp)
                .padding(horizontal = 16.dp),
            text = author,
            textAlign = TextAlign.Center,
            style = typography.caption
        )

        Spacer(modifier = Modifier.padding(bottom = 168.dp))
    }
}

@Composable
fun SliderCard(
    modifier: Modifier = Modifier,
    sliderPosition: Float,
    onSliderPositionChange: (sliderPosition: Float) -> Unit,
    onValueChangeFinished: () -> Unit
) {
    Card(
        modifier = modifier
            .height(88.dp)
            .width(312.dp)
            .shadow(elevation = 8.dp)
            .clip(MaterialTheme.shapes.small),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                "FONT SIZE",
                modifier = Modifier
                    .paddingFromBaseline(top = 16.dp)
                    .align(Alignment.TopStart),
                style = typography.overline,
                color = Color.DarkGray
            )

            Row(Modifier.align(Alignment.Center)) {
                Icon(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .align(Alignment.CenterVertically),
                    //Change the resource to ic_minus
                    painter = painterResource(id = R.drawable.ic_search),
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
                        .align(Alignment.CenterVertically),
                    // Change the resource to a ic_plus
                    painter = painterResource(id = R.drawable.ic_heartfilled),
                    contentDescription = "Reduce Font Size"
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

//@Preview
//@Composable
//fun HymnsContentScreenPreview() {
//    HymnsContentScreen()
//}