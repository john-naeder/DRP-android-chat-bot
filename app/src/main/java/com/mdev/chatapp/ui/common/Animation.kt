package com.mdev.chatapp.ui.common

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import dev.jeziellago.compose.markdowntext.AutoSizeConfig
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.delay

@Composable
fun AnimatedDots() {
    val dots = listOf(
        remember { Animatable(0f) },
        remember { Animatable(0f) },
        remember { Animatable(0f) }
    )

    dots.forEachIndexed { index, item ->
        LaunchedEffect(item) {
            delay(index * 100L)
            item.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    repeatMode = RepeatMode.Restart,
                    animation = keyframes {
                        durationMillis = 1000
                        0.0f at 0 using LinearOutSlowInEasing
                        1.0f at 200 using LinearOutSlowInEasing
                        0.0f at 400 using LinearOutSlowInEasing
                        0.0f at 1000
                    },
                )
            )
        }
    }

    val dys = dots.map { it.value }
    val travelDistance = with(LocalDensity.current) { 2.dp.toPx() }

    Box(
        modifier = Modifier.height(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            dys.forEach { dy ->
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .offset(y = (-dy * travelDistance).dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onTertiaryContainer)
                )
            }
        }
    }
}

@Composable
fun TypewriterText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle = LocalTextStyle.current,
    typewriterDelay: Long = 50L,
    typeWriterPass: () -> Unit,
    imageLoader: ImageLoader? = null
) {
    val builder = StringBuilder()
    val chars = remember { text.toCharArray() }
    var currentText by remember { mutableStateOf("") }

    LaunchedEffect(true) {
        chars.forEachIndexed { index, char ->
            builder.append(char)
            currentText = builder.toString()
            delay(typewriterDelay)
            if (index % 15 == 0)
                typeWriterPass()
        }
    }
    MarkdownText(
        markdown = text,
        modifier = modifier,
        linkColor = MaterialTheme.colorScheme.primary,
        maxLines = maxLines,
        truncateOnTextOverflow = true,
        enableSoftBreakAddsNewLine = true,
        style = style,
        autoSizeConfig = AutoSizeConfig(
            autoSizeMaxTextSize = 16,
            autoSizeMinTextSize = 12,
            autoSizeStepGranularity = 2
        ),
        imageLoader = imageLoader
    )
}