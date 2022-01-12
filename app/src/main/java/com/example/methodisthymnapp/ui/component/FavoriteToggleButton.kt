package com.example.methodisthymnapp.ui.component

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
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
const val IS_FAV_IC = R.drawable.ic_heartfilled
const val IS_NOT_FAV_IC = R.drawable.ic_heartoutlined

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun FavoriteToggleButton(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    onClick: () -> Unit
) {
    val rippleRadius = List(
        size = NUMBER_OF_RINGS,
        init = { remember { mutableStateOf(0f) } }
    )
    val rippleStrokeWidth = List(
        size = NUMBER_OF_RINGS,
        init = { remember { mutableStateOf(0f) } }
    )
    val rippleAlpha = List(
        size = NUMBER_OF_RINGS,
        init = { remember { mutableStateOf(1f) } }
    )
    val screenDensity = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .height(40.dp)
            .width(48.dp)
            .padding(bottom = 4.dp)
            .toggleable(
                value = isFavorite,
                onValueChange = { onClick() }
            ),
        contentAlignment = Alignment.Center
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
            targetState = isFavorite,
            transitionSpec = {
                ContentTransform(
                    //TODO : Change the state to boolean

                    /** If target State is yes then the enter animation should come with a ripple else it
                     * should (scale + slide)
                     * I faced a bug where I didn't compare the target state with the initial state and
                     * this made the ripple trigger everytime on recomposition
                     **/
                    targetContentEnter = if (targetState != initialState && targetState) {
                        animateRipple(
                            coroutineScope = coroutineScope,
                            density = screenDensity,
                            rippleRadius = rippleRadius,
                            rippleStrokeWidth = rippleStrokeWidth,
                            rippleAlpha = rippleAlpha
                        )
                        scaleIn(spring(Spring.DampingRatioMediumBouncy))
                    } else {
                        scaleIn(spring()) + slideInHorizontally(spring(Spring.DampingRatioHighBouncy))
                    },
                    initialContentExit = scaleOut(spring()),
                    sizeTransform = SizeTransform(clip = false)
                )
            }
        ) {
            Icon(
                painter = painterResource(id = if (isFavorite) IS_FAV_IC else IS_NOT_FAV_IC),
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
    rippleRadius: List<MutableState<Float>>,
    rippleStrokeWidth: List<MutableState<Float>>,
    rippleAlpha: List<MutableState<Float>>
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


