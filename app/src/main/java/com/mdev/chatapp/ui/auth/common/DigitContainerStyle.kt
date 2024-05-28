package com.mdev.chatapp.ui.auth.common

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

sealed class DigitContainerStyle(
    open val containerColor: Color = Color.Unspecified,
    open val focusedBorderColor: Color = Color.Unspecified,
    open val unfocusedBorderColor: Color = Color.Unspecified,
    open val focusedBorderWidth: Dp = 2.dp,
    open val unfocusedBorderWidth: Dp = 2.dp,
    open val errorColor: Color = Color.Unspecified,
) {

    fun borderWidth(focused: Boolean): Dp {
        return if (focused) focusedBorderWidth else unfocusedBorderWidth
    }

    fun borderColor(focused: Boolean, error: Boolean): Color {
        return when {
            error -> errorColor
            focused -> focusedBorderColor
            else -> unfocusedBorderColor
        }
    }

    data class Outlined(
        val size: Dp = 54.dp,
        val shape: Shape = RoundedCornerShape(12.dp),
        override val containerColor: Color = Color.Unspecified,
        override val focusedBorderColor: Color = Color.Unspecified,
        override val unfocusedBorderColor: Color = Color.Unspecified,
        override val focusedBorderWidth: Dp = 2.dp,
        override val unfocusedBorderWidth: Dp = 2.dp,
        override val errorColor: Color = Color.Unspecified,
    ) : DigitContainerStyle(
        containerColor = containerColor,
        focusedBorderColor = focusedBorderColor,
        unfocusedBorderColor = unfocusedBorderColor,
        unfocusedBorderWidth = unfocusedBorderWidth,
        focusedBorderWidth = focusedBorderWidth,
        errorColor = errorColor,
    )


    data class Underlined(
        val size: Dp = 54.dp,
        override val containerColor: Color = Color.Unspecified,
        override val focusedBorderColor: Color = Color.Unspecified,
        override val unfocusedBorderColor: Color = Color.Unspecified,
        override val focusedBorderWidth: Dp = 2.dp,
        override val unfocusedBorderWidth: Dp = 2.dp,
        override val errorColor: Color = Color.Unspecified,
    ) : DigitContainerStyle(
        containerColor = containerColor,
        focusedBorderColor = focusedBorderColor,
        unfocusedBorderColor = unfocusedBorderColor,
        unfocusedBorderWidth = unfocusedBorderWidth,
        focusedBorderWidth = focusedBorderWidth,
        errorColor = errorColor
    )

}