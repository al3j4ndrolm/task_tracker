package com.example.tasktrackerredo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class AppNavigationDrawer {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainDrawer(
        drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
        viewModel: MainViewModel?, // If needed
        mainContent: @Composable () -> Unit // Main content of the screen
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = { DrawerContent(viewModel) },
            content = mainContent // Provide the main content here
        )
    }


    @Composable
    private fun DrawerContent(
        viewModel: MainViewModel? // If needed){}){}
    ) {
        remember { mutableStateOf(false) } // Define showDialog here

        Box(
            modifier = Modifier
                .background(Color.hsl(0f, 0f, .92f))
                .width(220.dp)
                .fillMaxHeight()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally, // Center items horizontally
                verticalArrangement = Arrangement.Center, // Center items vertically
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center, // Center items in the Row
                    verticalAlignment = Alignment.CenterVertically // Center items vertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.menu_book),
                        contentDescription = "Menu bar icon",
                        modifier = Modifier
                            .padding(8.dp)
                            .size(50.dp),
                        colorFilter = ColorFilter.tint(Color.hsl(0f, 0f, 0.40f))
                    )
                    Text(
                        text = "Task Tracker Menu",
                        modifier = Modifier.padding(6.dp),
                        fontWeight = FontWeight.Bold,
                        color = Color.hsl(0f, 0f, 0.40f)
                    )
                }
                Column(
                    modifier = Modifier
                        .background(Color.White) // Set background color to white for this column
                        .fillMaxSize() // Fill the remaining space
                ) {
                    DeleteAllInformationElement(viewModel)
                    FAQElement()

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(10.dp), // Padding around the version text
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Version 1.0.0",
                            fontSize = 12.sp, // Smaller font size for the version text
                            color = Color.LightGray // Light gray color for the text
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun DeleteAllInformationElement(viewModel: MainViewModel?) {
        val showDialog = remember { mutableStateOf(false) }
        Row(
            modifier = Modifier
                .fillMaxWidth() // Fill the width of the parent
                .padding(12.dp), // Add padding around the Row

            verticalAlignment = Alignment.CenterVertically // Align contents vertically to the center
        ) {
            Image(
                painter = painterResource(id = R.drawable.delete_forever),
                contentDescription = "Delete forever",
                modifier = Modifier.size(24.dp), // Increase the size of the image
                colorFilter = ColorFilter.tint(Color.hsl(5f, 0.81f, 0.70f))
            )
            Text(
                text = "Delete all",
                color = Color.hsl(5f, 0.81f, 0.70f),
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        showDialog.value = true
                    }, // Set showDialog to true to open the dialog
                // Add other styling as needed
            )

            // Confirmation Dialog
            if (showDialog.value) {
                AlertDialog(
                    onDismissRequest = { showDialog.value = false },
                    title = { Text("Confirmation") },
                    text = { Text("Are you sure you want to delete all classes and their tasks?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel?.deleteAllClassesAndTasks()
                                showDialog.value = false
                            }
                        ) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showDialog.value = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun FAQElement() {
        Row(
            modifier = Modifier
                .fillMaxWidth() // Fill the width of the parent
                .padding(12.dp), // Add padding around the Row
            verticalAlignment = Alignment.CenterVertically // Align contents vertically to the center
        ) {
            Image(
                painter = painterResource(id = R.drawable.help),
                contentDescription = "Frequent Questions",
                modifier = Modifier.size(24.dp) // Increase the size of the image
            )
            Text(
                "FAQ",
                modifier = Modifier.padding(8.dp) // Add padding only to the start of the text
            )
        }
    }
}