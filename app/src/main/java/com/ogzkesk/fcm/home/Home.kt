package com.ogzkesk.fcm.home

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.ogzkesk.fcm.notification.util.Logger
import com.ogzkesk.fcm.notification.model.Notification


@Composable
fun Home(onSendMessage: (remoteToken: String, notification: Notification) -> Unit) {

    val clipboardManager = LocalClipboardManager.current
    val scrollState = rememberScrollState()
    var dialogState by remember { mutableStateOf(false) }
    var remoteToken by remember { mutableStateOf("") }
    var messageValue by remember { mutableStateOf("") }
    var titleValue by remember { mutableStateOf("") }

    TokenDialog(
        enabled = dialogState,
        onDismiss = { dialogState = false },
        onSubmit = { token ->
            remoteToken = token
            dialogState = false
        }
    )

    HomeScaffold(
        scrollState = scrollState,
        clipboardManager = clipboardManager,
        remoteToken = remoteToken,
        messageValue = messageValue,
        titleValue = titleValue,
        onSendNotification = onSendMessage,
        onDialogStateChanged = { dialogState = true },
        onMessageValueChanged = { messageValue = it },
        onTitleValueChanged = { titleValue = it }
    )
}

@Composable
private fun HomeScaffold(
    scrollState: ScrollState,
    clipboardManager: ClipboardManager,
    remoteToken: String,
    messageValue: String,
    titleValue: String,
    onDialogStateChanged: () -> Unit,
    onSendNotification: (remoteToken: String, notification: Notification) -> Unit,
    onMessageValueChanged: (String) -> Unit,
    onTitleValueChanged: (String) -> Unit,
) {

    Scaffold { padd ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(padd),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(onClick = {
                Firebase.messaging.token.addOnSuccessListener { token ->
                    clipboardManager.setText(AnnotatedString(token))
                    Logger.log("Token copied")
                }
            }, content = {
                Text(text = "Copy Device Token")
            })

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onDialogStateChanged) {
                Text(text = "Enter Remote Token")
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "Selected remote device:\n${remoteToken.take(10)}")

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = titleValue,
                onValueChange = onTitleValueChanged,
                placeholder = {
                    Text(text = "Enter notification title")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = messageValue,
                onValueChange = onMessageValueChanged,
                placeholder = {
                    Text(text = "Enter notification message")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val notification = Notification(title = titleValue, body = messageValue)
                onSendNotification(remoteToken, notification)
            }) {
                Text(text = "Send message as notification")
            }
        }
    }
}