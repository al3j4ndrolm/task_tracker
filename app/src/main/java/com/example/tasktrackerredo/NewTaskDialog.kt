package com.example.tasktrackerredo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

class NewTaskDialog {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun EditTaskDialog(
        onDismiss: () -> Unit,
        onSave: (String) -> Unit,
        existingTaskDescriptions: List<String> // Add this parameter
    ) {
        val taskDescription = remember { mutableStateOf("") }
        val isError = remember { mutableStateOf(false) }
        val errorMessage = remember { mutableStateOf("") }

        // Define validateAndSaveTask function here
        fun validateAndSaveTask() {
            val trimmedDescription = taskDescription.value.trim()
            when {
                trimmedDescription.isBlank() -> {
                    errorMessage.value = "Task description cannot be empty"
                    isError.value = true
                }
                existingTaskDescriptions.contains(trimmedDescription) -> {
                    errorMessage.value = "Task description already exists"
                    isError.value = true
                }
                else -> {
                    onSave(trimmedDescription)
                    onDismiss()
                }
            }
        }

        Dialog(onDismissRequest = onDismiss) {
            Card {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text("Add New Task")

                    TextField(
                        value = taskDescription.value,
                        onValueChange = {
                            taskDescription.value = it
                            isError.value = false // Reset error on input change
                        },
                        label = { Text("Task Description") },
                        isError = isError.value,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { validateAndSaveTask() })
                    )

                    if (isError.value) {
                        Text(
                            text = errorMessage.value,
                            color = Color.Red,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(onClick = { validateAndSaveTask() }) {
                        Text("Save")
                    }
                }
            }
        }
    }
}


