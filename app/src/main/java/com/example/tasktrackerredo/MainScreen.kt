package com.example.tasktrackerredo

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainScreen(private val viewModel: MainViewModel?) {
    private val newTaskDialog = NewTaskDialog()
    private val appNavigationDrawer = AppNavigationDrawer() // Instance of AppNavigationDrawer

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Launch() {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val coroutineScope = rememberCoroutineScope()

        appNavigationDrawer.MainDrawer(
            drawerState = drawerState,
            viewModel = viewModel
        ) {
            MainContent(
                drawerState,
                coroutineScope
            ) // This is a hypothetical function for the main content
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun MainContent(drawerState: DrawerState, coroutineScope: CoroutineScope) {
        val hasClasses = viewModel?.taskTrackBarInformationList?.isNotEmpty() == true
        Box(modifier = Modifier.fillMaxSize()) {
            if (hasClasses) {
                LazyColumn {
                    item { UserHeaderMainScreen(drawerState, coroutineScope) }
                    itemsIndexed(
                        viewModel?.taskTrackBarInformationList ?: listOf()
                    ) { index, taskTrackerBarInfo ->
                        val colorIndex = index / 2 % colorList.size
                        val currentColor = colorList[colorIndex]
                        val showDeleteClassDialog = remember { mutableStateOf(false) }
                        val showDeleteDialog =
                            remember { mutableStateOf(false) } // Define the state here
                        val showTaskDialog =
                            remember { mutableStateOf(false) } // Define the state here

                        TaskTrackerBar(
                            taskTrackerBarInformation = taskTrackerBarInfo,
                            viewModel = viewModel,
                            currentClassName = taskTrackerBarInfo.getClassName(),
                            currentColor = currentColor,
                            showDeleteClassDialog = showDeleteClassDialog,
                            onAddTaskClicked = { showTaskDialog.value = true },
                            showDeleteDialog = showDeleteDialog, // Pass the state
                            showTaskDialog = showTaskDialog,
                            onAddNewTaskClicked = { showTaskDialog.value = true })


                        if (showTaskDialog.value) {
                            // Retrieve the list of existing task descriptions for the current class
                            val existingTaskDescriptions =
                                taskTrackerBarInfo.tasks.map { it.description }

                            // Show the dialog for creating a new task
                            newTaskDialog.EditTaskDialog(
                                onDismiss = {
                                    showTaskDialog.value = false
                                },
                                onSave = { taskDescription ->
                                    viewModel?.addTaskToClass(
                                        taskTrackerBarInfo.getClassName(), taskDescription
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
    val isDrawerOpen by derivedStateOf { drawerState.isOpen }

    // Animate the rotation
    val rotationAngle by animateFloatAsState(
        targetValue = if (isDrawerOpen) 180f else 0f,
        animationSpec = tween(durationMillis = 200) // Adjust duration as needed
    )

    // Determine which icon to display based on the rotation angle
    val iconId = when {
        rotationAngle > 90f -> R.drawable.menu_open
        else -> R.drawable.menu_button
    }

    Image(
        painter = painterResource(id = iconId),
        contentDescription = if (isDrawerOpen) "Close menu" else "Open menu",
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .size(30.dp)
            .clickable {
                coroutineScope.launch {
                    if (drawerState.isOpen) drawerState.close() else drawerState.open()
                }
            }
            .rotate(rotationAngle),
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
            viewModel?.taskTrackBarInformationList?.map { it.getClassName() } ?: emptyList()

        newClassDialog.CreateClassDialog(
            onDismiss = { showDialog.value = false },
            onCreateClass = { className ->
                viewModel?.createClass(className)
                showDialog.value = false
            },
            existingClassNames = existingClassNames // Pass the list of existing class names
        )
    }
}


