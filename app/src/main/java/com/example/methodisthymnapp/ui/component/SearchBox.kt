package com.example.methodisthymnapp.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.compose.ui.unit.sp
import com.example.methodisthymnapp.R


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBox(
    modifier: Modifier = Modifier,
    placeholder: String = SEARCH,
    search: String = emptyString,
    onSearchTermChange: (String) -> Unit,
    keyboardActions: KeyboardActions,
    onReturnClick: () -> Unit,
    onClearClick: () -> Unit,
) {


    SearchFieldLayout(
        modifier = modifier,
        search = search,
        placeholder = { Text(placeholder, fontSize = 16.sp) },
        onSearchTermChange = onSearchTermChange,
        onClearClick = onClearClick,
        onReturnClick = onReturnClick,
        keyboardActions = keyboardActions,
    )

}

@Composable
fun SearchFieldLayout(
    modifier: Modifier,
    search: String,
    placeholder: @Composable (() -> Unit)? = null,
    onSearchTermChange: (String) -> Unit,
    keyboardActions: KeyboardActions,
    onClearClick: () -> Unit,
    onReturnClick: () -> Unit
) {
    BasicTextField(
        value = search,
        modifier = modifier
            .clip(cornerSize)
            .background(bgColor)
            .defaultMinSize(defaultSize),
        onValueChange = onSearchTermChange,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = keyboardActions,
        singleLine = true,
        cursorBrush = cursorColor,
        decorationBox = @Composable { innerTextField ->
            Layout(
                content = {
                    IconBox(
                        modifier = Modifier.clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = LocalIndication.current,
                            onClick = onReturnClick
                        ),
                        id = R.drawable.ic_return, contentDescription = "Search Icon"
                    )
                    Box(propagateMinConstraints = true) { innerTextField() }

                    if (placeholder != null) {
                        placeholder()
                    }

                    IconBox(
                        modifier = Modifier
                            .clip(cornerSize)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = LocalIndication.current,
                                onClick = onClearClick
                            ),
                        id = R.drawable.ic_clear_circled,
                        contentDescription = "Search Icon"
                    )
                }
            ) { (searchIc, textField, placeholder, clearIc), incomingConstraints ->

                /**
                 *
                 * @param incomingConstraints bounds given by the system for
                 *  the layout to be drawn.
                 *  It is converted into pixel values using the screen density.
                 *  Dividing it by the density gives the specified constraints given by the programmer.
                 *  Finding 30% of the given height and using it as padding puts the the textField
                 *  roughly in the middle.
                 *
                 * Offsetting the by negative constraints gives the value the width or height
                 * available to it.
                 */
                val txtFldPadding = ((incomingConstraints.maxHeight / density) * 0.3).dp.roundToPx()
                val constraints = incomingConstraints.copy(minWidth = 0, minHeight = 0)

                var spaceOccupiedHorizontally = 0

                val searchIcPlaceable = searchIc.measure(constraints)
                spaceOccupiedHorizontally += widthOrZero(searchIcPlaceable)

                val clearIcPlaceable =
                    clearIc.measure(constraints.offset(horizontal = -spaceOccupiedHorizontally))
                spaceOccupiedHorizontally += widthOrZero(clearIcPlaceable)

                val textFieldConstraints = constraints.offset(
                    horizontal = -spaceOccupiedHorizontally,
                    vertical = -txtFldPadding
                )
                val textFieldPlaceable = textField.measure(textFieldConstraints)
                val placeholderPlaceable = placeholder.measure(textFieldConstraints)

                val width = incomingConstraints.maxWidth
                val height = incomingConstraints.maxHeight

                layout(width, height) {
                    // var posY = 0
                    searchIcPlaceable.placeRelative(
                        0,
                        Alignment.CenterVertically.align(
                            searchIcPlaceable.height,
                            incomingConstraints.maxHeight
                        )
                    )
                    textFieldPlaceable.placeRelative(
                        searchIcPlaceable.width,
                        Alignment.CenterVertically.align(
                            textFieldPlaceable.height,
                            incomingConstraints.maxHeight
                        )
                    )

                    if (search.isEmpty()) {
                        placeholderPlaceable.placeRelative(
                            searchIcPlaceable.width,
                            Alignment.CenterVertically.align(
                                textFieldPlaceable.height,
                                incomingConstraints.maxHeight
                            )
                        )
                    }

                    if (search.isNotEmpty()) {
                        clearIcPlaceable.placeRelative(
                            incomingConstraints.maxWidth - clearIcPlaceable.width,
                            Alignment.CenterVertically.align(
                                clearIcPlaceable.height,
                                incomingConstraints.maxHeight
                            )
                        )
                    }
                }
            }
        })
}

@Composable
fun IconBox(modifier: Modifier = Modifier, @DrawableRes id: Int, contentDescription: String) {
    Box(
        modifier.defaultMinSize(48.dp, 48.dp),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painterResource(id = id),
            contentDescription = contentDescription,
            tint = Color.Unspecified
        )
    }
}

internal fun widthOrZero(placeable: Placeable?) = placeable?.width ?: 0
const val emptyString = ""
const val SEARCH = "Search"
val defaultSize = 40.dp
val bgColor = Color(0xFFEFEEEE).copy(alpha = 0.6F)
val cornerSize = RoundedCornerShape(12.dp)
val cursorColor = SolidColor(Color.Blue)