package com.ogzkesk.fcm.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun TokenDialog(
    enabled: Boolean,
    onSubmit: (token: String) -> Unit,
    onDismiss: () -> Unit,
) {

    var remoteToken by remember { mutableStateOf("") }

    if (enabled) {
        Dialog(onDismissRequest = onDismiss) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                OutlinedTextField(value = remoteToken,
                    onValueChange = { remoteToken = it },
                    placeholder = { Text(text = "Enter remote device token") })
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { onSubmit(remoteToken) },
                        content = {
                            Text(text = "Submit")
                        }
                    )
                }
            }
        }
    }
}