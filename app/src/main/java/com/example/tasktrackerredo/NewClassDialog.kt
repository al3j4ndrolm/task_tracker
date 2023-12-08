package com.example.tasktrackerredo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
//        val scrollState = rememberScrollState()

        Dialog(onDismissRequest = onDismiss) {

            Surface(
                shape = RoundedCornerShape(18.dp),
                color = Color.hsl(205f,1f,.95f) // Set the background color here
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
//                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Create New Class",
                        modifier = Modifier.padding(16.dp, 6.dp).align(CenterHorizontally),
                        color = Color.hsl(217f, 0.89f, 0.70f),
                        fontWeight = FontWeight.Bold

                    )

                    TextField(
                        value = className.value,
                        onValueChange = {
                            className.value = it
                            isError.value = false
                        },
                        label = { Text("Class Name", color = Color.hsl(217f, 0.89f, 0.70f)) },
                        isError = isError.value,
                        singleLine = true,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.hsl(205f,1f,.95f),
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
                        Spacer(modifier = Modifier.height(18.dp)) // Spacer to maintain layout consistency
                    }

                    Button(
                        onClick = {
                                val trimmedClassName = className.value.trim()
                                when {
                                    trimmedClassName.isBlank() -> {
                                        errorMessage.value = "Class name cannot be empty"
                                        isError.value = true
                                    }

                                    trimmedClassName.length > 20 -> { // Check for length
                                        errorMessage.value =
                                            "Class name is too long (max 20 characters)"
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
                            },
                        modifier = Modifier.fillMaxWidth().size(40.dp),
                        shape = RoundedCornerShape(0.dp),
                        colors = ButtonDefaults.buttonColors(Color.hsl(217f, 0.89f, 0.70f), // Dark blue button
                            contentColor = Color.White // White text color
                        )


                    ) {
                        Text("Create")
                    }
                    }
                }
            }
        }
    }
