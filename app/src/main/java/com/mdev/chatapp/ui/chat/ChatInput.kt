package com.mdev.chatapp.ui.chat

import android.annotation.SuppressLint
import android.content.Intent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.ScrollState
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
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.automirrored.twotone.Send
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.filled.Stop
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.times
import androidx.core.content.ContextCompat
import com.mdev.chatapp.ui.theme.spacing


@SuppressLint("UnrememberedMutableState")
@Composable
fun ChatInput(
    speechRecognizer: SpeechRecognizer,
    recognizerIntent: Intent,
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
    modifier: Modifier = Modifier,
    state: ChatState,
    onUIEvent: (ChatUIEvent) -> Unit
) {
    val context = LocalContext.current
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
            TextButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .align(Alignment.CenterVertically),
                shape = CircleShape,
                onClick = {
                    onUIEvent(ChatUIEvent.OnViewFollowUpQuestion)
                },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.PlaylistAdd,
                    contentDescription = null
                )
            }
            TextField(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.extraLarge)
                    .weight(1f)
                    .focusable(true)
                    .verticalScroll(scrollState)
                    .fillMaxHeight()
                    .heightIn(min = TextFieldDefaults.MinHeight, max = 4 * TextFieldDefaults.MinHeight),
                value = state.inputMessage,
                onValueChange = { onUIEvent(ChatUIEvent.OnInputMessageChanged(it)) },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    Text(text = "Aa")
                },
                trailingIcon = {
                    Row {
                        if (!state.isListening) {
                            IconButton(onClick = {
                                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO) ==
                                    android.content.pm.PackageManager.PERMISSION_GRANTED) {
                                    speechRecognizer.startListening(recognizerIntent)
                                } else {
                                    permissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
                                }                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Mic,
                                    contentDescription = "Microphone"
                                )
                            }
                        } else {
                            IconButton(onClick = {
                                speechRecognizer.stopListening()
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Stop,
                                    contentDescription = "Microphone"
                                )
                            }
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
                    onUIEvent(ChatUIEvent.SendMessage(state.inputMessage))
                },
                enabled = state.inputMessage.isNotEmpty()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.TwoTone.Send,
                    contentDescription = null
                )
            }
        }
    }
}



