package com.example.tasktrackerredo

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainScreen(private val viewModel: MainViewModel?) {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Launch() {
        val hasClasses = viewModel?.taskTrackBarInformationList?.isNotEmpty() == true
        val newTaskDialog = NewTaskDialog()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val coroutineScope = rememberCoroutineScope()
        val showDialog = remember { mutableStateOf(false)}

        ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
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
                                text = "Delete all the information",
                                color = Color.hsl(5f, 0.81f, 0.70f) ,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clickable { showDialog.value = true }, // Set showDialog to true to open the dialog
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
        }) {
            Box(modifier = Modifier.fillMaxSize()) {

                if (hasClasses) {
                    LazyColumn {
                        item { UserHeaderMainScreen(drawerState, coroutineScope) }
                        itemsIndexed(
                            viewModel?.taskTrackBarInformationList ?: listOf()
                        ) { index, (taskTrackerBarInfo, tasks) ->
                            val colorIndex = index / 2 % colorList.size
                            val currentColor = colorList[colorIndex]
                            val showDeleteClassDialog = remember { mutableStateOf(false) }
                            val showDeleteDialog =
                                remember { mutableStateOf(false) } // Define the state here
                            val showTaskDialog =
                                remember { mutableStateOf(false) } // Define the state here

                            TaskTrackerBar(tasks = tasks,
                                taskTrackerBarInformation = taskTrackerBarInfo,
                                viewModel = viewModel,
                                currentClassName = taskTrackerBarInfo.className,
                                currentColor = currentColor,
                                showDeleteClassDialog = showDeleteClassDialog,
                                onAddTaskClicked = { showTaskDialog.value = true },
                                showDeleteDialog = showDeleteDialog, // Pass the state
                                showTaskDialog = showTaskDialog,
                                onAddNewTaskClicked = { showTaskDialog.value = true })

                            if (showDeleteClassDialog.value) {
                                // AlertDialog logic for deleting the class
                                AlertDialog(onDismissRequest = {
                                    showDeleteClassDialog.value = false
                                },
                                    title = { Text("Delete Class") },
                                    text = { Text("Are you sure you want to delete the entire class and its tasks?") },
                                    confirmButton = {
                                        Button(onClick = {
                                            viewModel?.deleteClass(taskTrackerBarInfo.className)
                                            showDeleteClassDialog.value = false
                                        }) {
                                            Text("Delete")
                                        }
                                    },
                                    dismissButton = {
                                        Button(onClick = { showDeleteClassDialog.value = false }) {
                                            Text("Cancel")
                                        }
                                    })
                            }

                            if (showTaskDialog.value) {
                                // Retrieve the list of existing task descriptions for the current class
                                val existingTaskDescriptions = tasks.map { it.description }

                                // Show the dialog for creating a new task
                                newTaskDialog.EditTaskDialog(onDismiss = {
                                    showTaskDialog.value = false
                                },
                                    onSave = { taskDescription ->
                                        viewModel?.addTaskToClass(
                                            taskTrackerBarInfo.className, taskDescription
                                        )
                                        showTaskDialog.value = false
                                    },
                                    existingTaskDescriptions = existingTaskDescriptions // Pass the list of existing descriptions
                                )
                            }
                        }
                    }
                } else {
                    UserHeaderMainScreen(drawerState, coroutineScope)
                    Box(
                        contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(id = R.drawable.tt_logo),
                                contentDescription = "TT Logo",
                                modifier = Modifier.size(120.dp)
                            )
                            Text("You don't have tasks yet")
                        }
                    }
                }

                // Floating Action Button for New Class
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    NewClassButton(viewModel)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserHeaderMainScreen(drawerState: DrawerState, coroutineScope: CoroutineScope) {
    Box(
        modifier = Modifier
            .background(Color.hsl(148f, .0f, 0.87f))
            .clip(
                RoundedCornerShape(
                    topStart = 0.dp, topEnd = 0.dp, bottomEnd = 10.dp, bottomStart = 10.dp
                )
            ) // Add rounded corners here
    ) {
        Row {
            Box(modifier = Modifier, contentAlignment = Alignment.CenterStart) {
                HeaderAndroidLogo()
            }

            Box(modifier = Modifier) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HeaderText()
                    HeaderMenuButton(drawerState, coroutineScope)
                }
            }
        }
    }
}

@Composable
private fun HeaderText() {
    Box(
        modifier = Modifier
            .padding(5.dp)
            .size(height = 80.dp, width = 100.dp)
    ) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text(
                text = "Android",
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle(R.font.public_sans),
                fontSize = 18.sp
            )
            Text(text = "Task Tracker", fontStyle = FontStyle(R.font.public_sans))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HeaderMenuButton(drawerState: DrawerState, coroutineScope: CoroutineScope) {
    Image(
        painter = painterResource(id = R.drawable.menu_button),
        contentDescription = "",
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .size(30.dp)
            .clickable {
                // Open the drawer when the menu button is clicked
                coroutineScope.launch {
                    drawerState.open()
                }
            },
        alignment = Alignment.CenterEnd,
        colorFilter = ColorFilter.tint(Color.hsl(214f, .21f, .68f))
    )
}

@Composable
private fun HeaderAndroidLogo() {
    Image(
        painter = painterResource(id = R.drawable.tt_logo),
        contentDescription = "",
        modifier = Modifier
            .size(85.dp)
            .padding(5.dp),
        alignment = Alignment.CenterStart,
    )
}

@Composable
fun NewClassButton(viewModel: MainViewModel?) {
    val showDialog = remember { mutableStateOf(false) }
    val newClassDialog = NewClassDialog() // A new dialog for creating classes

    Box(
        modifier = Modifier.fillMaxSize() // Fill the parent
    ) {
        Button(
            onClick = { showDialog.value = true },
            colors = ButtonDefaults.buttonColors(Color.hsl(217f, 0.89f, 0.71f)),
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(30.dp))
                .align(Alignment.BottomEnd)
                .padding(10.dp), // Align the button to the bottom end of the Box
            contentPadding = PaddingValues(0.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.new_window),
                contentDescription = "",
                modifier = Modifier.size(30.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
    }

    if (showDialog.value) {
        // Retrieve the list of existing class names
        val existingClassNames =
            viewModel?.taskTrackBarInformationList?.map { it.first.className } ?: emptyList()

        newClassDialog.CreateClassDialog(onDismiss = { showDialog.value = false },
            onCreateClass = { className ->
                viewModel?.createClass(className)
                showDialog.value = false
            },
            existingClassNames = existingClassNames // Pass the list of existing class names
        )
    }
}


