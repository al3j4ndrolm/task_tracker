package com.example.tasktrackerredo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

class NewTaskDialog {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun EditTaskDialog(
        onDismiss: () -> Unit,
        onSave: (String) -> Unit,
        existingTaskDescriptions: List<String>
    ) {
        val taskDescription = remember { mutableStateOf("") }
        val isError = remember { mutableStateOf(false) }
        val errorMessage = remember { mutableStateOf("") }

        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = Color.hsl(205f, 1f, .95f) // Background color
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
//                        .padding(16.dp), // Consistent padding
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Add New Task",
                        modifier = Modifier.padding(16.dp, 6.dp).align(CenterHorizontally),
                        color = Color.hsl(217f, 0.89f, 0.70f), // Text color
                        fontWeight = FontWeight.Bold
                    )

                    TextField(
                        value = taskDescription.value,
                        onValueChange = {
                            taskDescription.value = it
                            isError.value = false
                        },
                        label = { Text("Task Description", color = Color.hsl(217f, 0.89f, 0.70f)) },
                        isError = isError.value,
                        singleLine = false, // Allowing multiline input
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 2.dp)
                            .heightIn(max = 200.dp) // Max height
                            .verticalScroll(rememberScrollState()),
                        maxLines = 10,
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.hsl(205f, 1f, .95f),
                            textColor = Color.hsl(205f, 1f, .20f),
                            cursorColor = Color.hsl(205f, 1f, .20f),
                            focusedIndicatorColor = Color.hsl(217f, 0.89f, 0.70f),
                            unfocusedIndicatorColor = Color.hsl(217f, 0.89f, 0.70f)
                        )
                    )

                    if (isError.value) {
                        Text(
                            text = errorMessage.value,
                            color = Color.Red,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(18.dp)) // Spacer for layout consistency
                    }

                    Button(
                        onClick = {
                            val trimmedDescription = taskDescription.value.trim()
                            when {
                                trimmedDescription.isBlank() -> {
                                    errorMessage.value = "Task description cannot be empty"
                                    isError.value = true
                                }
                                trimmedDescription.length > 90 -> {
                                    errorMessage.value = "Task description is too long (max 60 characters)"
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
                        },
                        modifier = Modifier.fillMaxWidth().size(40.dp),
                        shape = RoundedCornerShape(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            Color.hsl(217f, 0.89f, 0.70f), // Dark blue button
                            contentColor = Color.White // White text color
                        )
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}


