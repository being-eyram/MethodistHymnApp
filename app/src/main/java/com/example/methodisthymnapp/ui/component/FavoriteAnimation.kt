package com.example.methodisthymnapp.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.example.methodisthymnapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val NUMBER_OF_RINGS = 3
private enum class FavoriteState(@DrawableRes val drawable: Int) {
    NOPE(R.drawable.ic_heartoutlined),
    YES(R.drawable.ic_heartfilled)
}


//TODO: hoist the Favorite state & its event to make it usable with the isFavorite field
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun AnimatedFavoriteIcon(modifier: Modifier = Modifier) {
    val rippleRadius = MutableList(
        size = NUMBER_OF_RINGS,
        init = { remember { mutableStateOf(0f) } }
    )
    val rippleStrokeWidth = MutableList(
        size = NUMBER_OF_RINGS,
        init = { remember { mutableStateOf(0f) } }
    )
    val rippleAlpha = MutableList(
        size = NUMBER_OF_RINGS,
        init = { remember { mutableStateOf(1f) } }
    )

    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    var favoriteState by remember { mutableStateOf(FavoriteState.NOPE) }

    Box(
        modifier = modifier
            .height(40.dp)
            .width(48.dp)
            .padding(bottom = 4.dp)
            .clickable(
                indication = null,
                interactionSource = interactionSource
            ) {
                favoriteState = when (favoriteState) {
                    FavoriteState.NOPE -> FavoriteState.YES
                    FavoriteState.YES -> FavoriteState.NOPE
                }
            },

        ) {
        /** draw three rings */
        repeat(NUMBER_OF_RINGS) { index ->
            Ripple(
                modifier = Modifier.align(Alignment.Center),
                color = Color.Red,
                radius = rippleRadius[index].value,
                alpha = rippleAlpha[index].value,
                style = Stroke(width = rippleStrokeWidth[index].value)
            )
        }

        AnimatedContent(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 7.dp),
            targetState = favoriteState,
            transitionSpec = {
                ContentTransform(
                    //if the incoming favorite is nope, (scale + slide), else show ripple and scale
                    targetContentEnter = if (favoriteState == FavoriteState.NOPE) {
                        scaleIn(spring()) + slideInHorizontally(spring(Spring.DampingRatioHighBouncy))
                    } else {
                        animateRipple(
                            coroutineScope = coroutineScope,
                            density = density,
                            rippleRadius = rippleRadius,
                            rippleStrokeWidth = rippleStrokeWidth,
                            rippleAlpha = rippleAlpha
                        )
                        scaleIn(spring(Spring.DampingRatioMediumBouncy))
                    },
                    initialContentExit = scaleOut(spring()),
                    sizeTransform = SizeTransform(clip = false)
                )
            }
        ) {
            Icon(
                painter = painterResource(id = favoriteState.drawable),
                contentDescription = "Favorites Button",
                tint = Color.Unspecified,
            )
        }
    }
}

/**
 * Modify values of [rippleRadius],[rippleAlpha] and [rippleStrokeWidth] to
 * animate three rings from initial to target value delaying each by (40* index).
 *
 * @param coroutineScope coroutineScope in which the animation is launched.
 * @param density provides [Density].
 * @param rippleRadius mutableList of initial radius of ripple.
 * @param rippleStrokeWidth mutableList of initial stroke width of ripple.
 * @param rippleAlpha mutableList of initial alpha of ripple.
 */
private fun animateRipple(
    coroutineScope: CoroutineScope,
    density: Density,
    rippleRadius: MutableList<MutableState<Float>>,
    rippleStrokeWidth: MutableList<MutableState<Float>>,
    rippleAlpha: MutableList<MutableState<Float>>
) {
    val targetRadius = with(density) { 20.dp.toPx() }
    val targetStrokeWidth = with(density) { (3).dp.toPx() }

    coroutineScope.launch {

        repeat(NUMBER_OF_RINGS) { index ->
            delay(40L * index)

            launch {
                animate(
                    initialValue = 0f,
                    targetValue = targetRadius,
                    animationSpec = tween(durationMillis = 300)
                ) { value, _ -> rippleRadius[index].value = value }
            }

            launch {
                animate(
                    initialValue = 0f,
                    targetValue = targetStrokeWidth,
                    animationSpec = tween(durationMillis = 300)
                ) { value, _ -> rippleStrokeWidth[index].value = value }
            }

            launch {
                animate(
                    initialValue = 1f,
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 400)
                ) { value, _ -> rippleAlpha[index].value = value }
            }
        }
    }
}

@Composable
fun Ripple(
    modifier: Modifier = Modifier,
    color: Color,
    radius: Float,
    alpha: Float,
    style: DrawStyle
) {
    Canvas(modifier = modifier) {
        drawCircle(
            color = color,
            radius = radius,
            alpha = alpha,
            style = style
        )
    }
}


