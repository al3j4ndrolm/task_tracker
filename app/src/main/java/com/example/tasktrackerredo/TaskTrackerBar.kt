package com.example.tasktrackerredo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

val colorList = listOf(
    Color.hsl(217f, 0.89f, 0.70f),
    Color.hsl(5f, 0.81f, 0.70f),
    Color.hsl(45f, 0.97f, 0.70f),
    Color.hsl(136f, 0.53f, 0.70f)
)

@Composable
fun TaskTrackerBar(
    taskTrackerBarInformation: TaskTrackerBarInformation,
    viewModel: MainViewModel?,
    currentClassName: String,
    currentColor: Color,
    onAddTaskClicked: () -> Unit,
    showTaskDialog: MutableState<Boolean>,
    showDeleteDialog: MutableState<Boolean>, // Correctly marked as MutableState
    showDeleteClassDialog: MutableState<Boolean>,
    onAddNewTaskClicked: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(taskTrackerBarInformation.taskProgressPercentage) }
    val newTaskDialog = NewTaskDialog()
    var selectedTaskDescription by remember { mutableStateOf("") }
    val rotationAngle by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300) // Define the duration and type of animation
    )
    val animatedProgress = animateFloatAsState(
        targetValue = taskTrackerBarInformation.taskProgressPercentage,
        animationSpec = tween(durationMillis = 1000) // Adjust the duration as needed
    )


    // Observe changes in tasks and taskTrackerBarInformation
    LaunchedEffect(taskTrackerBarInformation, taskTrackerBarInformation.tasks) {
        progress = taskTrackerBarInformation.taskProgressPercentage
    }
    Box(modifier = Modifier.fillMaxWidth()) {
        Column {
            ClassTitle(taskTrackerBarInformation, taskTrackerBarInformation.tasks.size)
            ClassProgressBar(
                progress = animatedProgress.value,
                color = currentColor,
                onClick = { onAddTaskClicked() },
                onLongPress = { showDeleteClassDialog.value = true }
            )

            if (showDeleteClassDialog.value){
                Dialog(onDismissRequest = { showDeleteClassDialog.value = false }) {
                    // Use Surface for card-like appearance
                    Surface(
                        shape = RoundedCornerShape(16.dp), // Adjust the corner shape as needed
                        color = Color.hsl(360f, 1f, .91f) // Set the background color here
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                "Delete Class",
                                modifier = Modifier.padding(8.dp),
                                color = Color.hsl(360f, 1f, .70f), // Adjust the color as needed
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                "Are you sure you want to delete the entire class and its tasks?",
                                color = Color.hsl(360f, 1f, .70f),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            Spacer(modifier = Modifier.height(0.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                // Dismiss Button
                                Button(
                                    onClick = { showDeleteClassDialog.value = false },
                                    modifier = Modifier.weight(1f).size(40.dp),
                                    shape = RectangleShape, // Set button shape to rectangle (no rounded corners)
                                    colors = ButtonDefaults.buttonColors(Color.hsl(360f, 1f, .68f))
                                ) {
                                    Text("Cancel")
                                }

                                Spacer(modifier = Modifier.width(2.dp)) // Separation between buttons

                                // Confirm Button
                                Button(
                                    onClick = {
                                        viewModel?.deleteClass(taskTrackerBarInformation.getClassName())
                                        showDeleteClassDialog.value = false
                                    },
                                    modifier = Modifier.weight(1f).size(40.dp),
                                    shape = RectangleShape, // Set button shape to rectangle (no rounded corners)
                                    colors = ButtonDefaults.buttonColors(Color.hsl(360f, 1f, .68f))
                                ) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
            }


            ClassProgressBarPercentage(taskTrackerBarInformation.taskProgressPercentage)
            Column(
                modifier = Modifier.animateContentSize()
            ){
                if (expanded) {
                    if (taskTrackerBarInformation.tasks.isEmpty()) {
                        Text(
                            "Press \"+\" to create a new task",
                            modifier = Modifier.padding(16.dp),
                            fontStyle = FontStyle.Italic
                        )
                    } else {
                        taskTrackerBarInformation.tasks.forEach { task ->
                            TasksListItem(
                                taskInfo = task.description,
                                isChecked = task.isCompleted,
                                onCheckedChange = { isChecked ->
                                    viewModel?.updateTaskCompletion(
                                        currentClassName,
                                        task.description,
                                        isChecked
                                    )
                                },
                                onLongPress = {
                                    selectedTaskDescription = task.description
                                    showDeleteDialog.value = true // Correct way to set the state
                                },
                                color = currentColor
                            )
                        }
                    }

                    if (showDeleteDialog.value) {
                        // Show confirmation dialog
                        Dialog(onDismissRequest = { showDeleteDialog.value = false }) {
                            // Use Surface for card-like appearance
                            Surface(
                                shape = RoundedCornerShape(16.dp), // Adjust the corner shape as needed
                                color = Color.hsl(360f, 1f, .91f) // Set the background color here
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(0.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        "Delete Task",
                                        modifier = Modifier
                                            .padding(8.dp),
                                        color = Color.hsl(360f, 1f, .70f), // Adjust the color as needed
                                        fontWeight = FontWeight.Bold
                                    )


                                    Text("Are you sure you want to delete this task?",
                                        color = Color.hsl(360f, 1f, .70f))

                                    Spacer(modifier = Modifier.height(0.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        // Dismiss Button
                                        Button(
                                            onClick = { showDeleteDialog.value = false },
                                            modifier = Modifier.weight(1f).size(40.dp),
                                            shape = RectangleShape, // Set button shape to rectangle (no rounded corners)
                                            colors = ButtonDefaults.buttonColors(Color.hsl(360f, 1f, .68f))
                                        ) {
                                            Text("Cancel")
                                        }
//

                                        Spacer(modifier = Modifier.width(2.dp)) // Separation between buttons

//                                        Confirm Button
                                        Button(
                                            onClick = {
                                                viewModel?.deleteTask(currentClassName, selectedTaskDescription)
                                                showDeleteDialog.value = false
                                            },
                                            modifier = Modifier.weight(1f).size(40.dp),
                                            shape = RectangleShape, // Set button shape to rectangle (no rounded corners)
                                            colors = ButtonDefaults.buttonColors(Color.hsl(360f, 1f, .68f))
                                        ) {
                                            Text("Confirm")
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (showTaskDialog.value) {
                        // Extract existing task descriptions for the specific class
                        val existingTaskDescriptions = viewModel?.taskTrackBarInformationList
                            ?.firstOrNull { it.getClassName() == taskTrackerBarInformation.getClassName() }
                            ?.tasks
                            ?.map { it.description }
                            ?: emptyList()

                        newTaskDialog.EditTaskDialog(
                            onDismiss = { showTaskDialog.value = false },
                            onSave = { taskDescription ->
                                viewModel?.addTaskToClass(
                                    taskTrackerBarInformation.getClassName(),
                                    taskDescription
                                )
                                showTaskDialog.value = false
                            },
                            existingTaskDescriptions = existingTaskDescriptions // Pass the existing task descriptions
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        IconButton(onClick = { onAddNewTaskClicked() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.add_fill0_wght400_grad0_opsz24),
                                contentDescription = "Add Task",
                                tint = Color.LightGray
                            )
                        }
                    }
                }
            }


            ClassItemExpandButton(
                onClick = { expanded = !expanded },
                rotationAngle = rotationAngle
            )
        }
    }
}

@Composable
private fun ClassTitle(taskTrackerBarInformation: TaskTrackerBarInformation, taskCount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = taskTrackerBarInformation.getClassName(),
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "$taskCount tasks",
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ClassProgressBar(
    progress: Float,
    color: Color,
    onClick: () -> Unit,
    onLongPress: () -> Unit
) {
    LinearProgressIndicator(
        progress = progress,
        color = color,
        modifier = Modifier
            .fillMaxWidth()
            .size(height = 15.dp, width = 0.dp)
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { onLongPress() },
                    onPress = { /* Called when the user presses down on the screen */ }
                )
            }
    )
}

private fun convertToPercentageString(percentage: Float): String {
    val percentageNumber: Int = (percentage * 100).toInt()
    return "$percentageNumber %"
}

@Composable
private fun ClassProgressBarPercentage(progress: Float) {
    Text(
        text = convertToPercentageString(percentage = progress),
        textAlign = TextAlign.End,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp)
    )
}

@Composable
private fun ClassItemExpandButton(onClick: () -> Unit, rotationAngle: Float) {
    Box(
        modifier = Modifier
            .background(Color.hsl(214f, .21f, .89f))
            .fillMaxWidth() // Set the Box to fill the maximum width
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center // Center the content inside the Box
    ) {
        Icon(
            painter = painterResource(id = R.drawable.expand_icon),
            contentDescription = "Expand or collapse item",
            modifier = Modifier
                .rotate(rotationAngle) // Apply rotation to the Icon
        )
    }
}
@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
private fun TasksListItem(
    taskInfo: String,
    isChecked: Boolean,
//    isNew: Boolean, // New parameter to indicate if the task is new
    onCheckedChange: (Boolean) -> Unit,
    onLongPress: () -> Unit,
    color: Color
) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .combinedClickable(
                    onClick = { /* Handle click if necessary */ },
                    onLongClick = { onLongPress() }
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = color,
                    uncheckedColor = Color.Gray,
                    checkmarkColor = Color.White
                ),
                modifier = Modifier.padding(start = 10.dp)
            )

            Text(
                text = taskInfo,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
    }

