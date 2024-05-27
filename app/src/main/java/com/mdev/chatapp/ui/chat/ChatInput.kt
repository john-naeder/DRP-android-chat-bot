package com.mdev.chatapp.ui.chat

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.twotone.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.mdev.chatapp.ui.theme.spacing


@SuppressLint("UnrememberedMutableState")
@Composable
fun ChatInput(
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel
) {
    val context = LocalContext.current
    val state = viewModel.state
    val scrollState = remember { ScrollState(0) }
    Box (
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Row(
            modifier = modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(vertical = MaterialTheme.spacing.extraSmall)
                .padding(horizontal = MaterialTheme.spacing.small),
            verticalAlignment = Alignment.Bottom
        ) {
            TextField(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.extraLarge)
                    .weight(1f)
                    .focusable(true)
                    .verticalScroll(scrollState)
                    .fillMaxHeight()
                    .heightIn(min = TextFieldDefaults.MinHeight, max = 4 * TextFieldDefaults.MinHeight),
                value = state.inputMessage,
                onValueChange = { viewModel.onEvent(ChatUIEvent.OnInputMessageChanged(it)) },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    Text(text = "Aa")
                },
                leadingIcon = {
                    IconButton(onClick = {
                        Toast.makeText(
                            context,
                            "Not Available",
                            Toast.LENGTH_SHORT
                        ).show()
                    }) {
                        Icon(imageVector = Icons.Filled.AttachFile, contentDescription = "File")
                    }
                },
                trailingIcon = {
                    Row {
                        IconButton(onClick = {
                            Toast.makeText(
                                context,
                                "Camera Clicked.\n(Not Available)",
                                Toast.LENGTH_SHORT
                            ).show()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.CameraAlt,
                                contentDescription = "Camera"
                            )
                        }
                    }
                },
                singleLine = false,
                maxLines = 4
            )
            TextButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .align(Alignment.CenterVertically),
                shape = CircleShape,
                onClick = {
                    if (state.inputMessage.isNotEmpty()) {
                        viewModel.onEvent(ChatUIEvent.SendMessage(state.inputMessage))
                    } else {
                        // TODO(Send message by voice)
                    }
                }
            ) {
                Icon(
                    imageVector = if (
                        state.inputMessage.isEmpty()
                    ) Icons.Filled.Mic else Icons.TwoTone.Send,
                    contentDescription = null
                )
            }
        }
    }
}



