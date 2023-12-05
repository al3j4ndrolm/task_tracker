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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

class NewClassDialog {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CreateClassDialog(
        onDismiss: () -> Unit,
        onCreateClass: (String) -> Unit,
        existingClassNames: List<String> // Add this parameter
    ) {
        val className = remember { mutableStateOf("") }
        val isError = remember { mutableStateOf(false) }
        val errorMessage = remember { mutableStateOf("") }

        Dialog(onDismissRequest = onDismiss) {
            Card {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text("Create New Class")

                    TextField(
                        value = className.value,
                        onValueChange = {
                            className.value = it
                            isError.value = false // Reset error on input change
                        },
                        label = { Text("Class Name") },
                        isError = isError.value
                    )

                    if (isError.value) {
                        Text(
                            text = errorMessage.value,
                            color = Color.Red,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(onClick = {
                        val trimmedClassName = className.value.trim()
                        when {
                            trimmedClassName.isBlank() -> {
                                errorMessage.value = "Class name cannot be empty"
                                isError.value = true
                            }
                            trimmedClassName.length > 20 -> { // Check for length
                                errorMessage.value = "Class name is too long (max 20 characters)"
                                isError.value = true
                            }
                            existingClassNames.contains(trimmedClassName) -> {
                                errorMessage.value = "Class name already exists"
                                isError.value = true
                            }
                            else -> {
                                onCreateClass(trimmedClassName)
                                onDismiss()
                            }
                        }
                    }) {
                        Text("Create")
                    }
                }
            }
        }
    }
}