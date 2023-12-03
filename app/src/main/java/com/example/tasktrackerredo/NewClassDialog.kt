package com.example.tasktrackerredo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

class NewClassDialog {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CreateClassDialog(
        onDismiss: () -> Unit,
        onCreateClass: (String) -> Unit
    ) {
        val className = remember { mutableStateOf("") }

        Dialog(onDismissRequest = onDismiss) {
            Card {
                Column(modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()) {
                    Text("Create New Class")

                    TextField(
                        value = className.value,
                        onValueChange = { className.value = it },
                        label = { Text("Class Name") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(onClick = {
                        if (className.value.isNotBlank()) {
                            onCreateClass(className.value)
                            onDismiss()
                        }
                    }) {
                        Text("Create")
                    }
                }
            }
        }
    }
}