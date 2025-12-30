package com.yjotdev.playermusic.application.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.yjotdev.playermusic.application.theme.PlayerMusicTheme
import com.yjotdev.playermusic.application.utils.ComponentPreview

@Composable
fun MyAlertDialog(
    confirm: String,
    dismiss: String,
    title: String,
    message: String,
    confirmClicked: ()-> Unit,
    dismissClicked: ()-> Unit,
    case: Int = 1,
    value: String = "",
    label: String = "",
    onValue: (String)-> Unit = {}
){
    AlertDialog(
        onDismissRequest = dismissClicked,
        confirmButton = { Button(
                                onClick = confirmClicked,
                                modifier = Modifier.testTag("Confirm")
                            ){ Text(confirm) }
                        },
        dismissButton = { Button(onClick = dismissClicked){ Text(dismiss) }},
        title = { Text(title) },
        text = {
            when(case){
                1 -> { Text(message) }
                2 -> {
                    Column{
                        Text(message)
                        TextField(
                            value = value,
                            label = { Text(label) },
                            onValueChange = { onValue(it) },
                            modifier = Modifier.testTag("ChangeNameFromPlaylist")
                        )
                    }
                }
            }
        }
    )
}

@ComponentPreview
@Composable
private fun PreviewMyAlertDialog(){
    PlayerMusicTheme {
        MyAlertDialog(
            confirm = "Confirm",
            dismiss = "Dismiss",
            title = "Title",
            message = "Message",
            confirmClicked = {},
            dismissClicked = {}
        )
    }
}