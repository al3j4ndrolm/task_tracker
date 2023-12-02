package com.example.tasktrackerredo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

val colorList = listOf(
    Color.hsl(217f, 0.89f, 0.61f),
    Color.hsl(5f, 0.81f, 0.56f),
    Color.hsl(45f, 0.97f, 0.50f),
    Color.hsl(136f, 0.53f, 0.43f)
)
@Composable
fun TaskTrackerBar(
    taskTrackerBarInformation: TaskTrackerBarInformation, // This parameter should be defined
    tasks: List<TaskInformation>,
    viewModel: MainViewModel?,
    currentClassName: String,
    currentColor: Color
) {
    var expanded by remember { mutableStateOf(false) }
    var showTaskDialog by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(taskTrackerBarInformation.taskProgressPercentage) }
    val newTaskDialog = NewTaskDialog()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedTaskDescription by remember { mutableStateOf("") }


    // Observe changes in tasks and taskTrackerBarInformation
    LaunchedEffect(taskTrackerBarInformation, tasks) {
        progress = taskTrackerBarInformation.taskProgressPercentage
    }
    Box(modifier = Modifier.fillMaxWidth()) {
        Column {
            ClassTitle(taskTrackerBarInformation, tasks.size)
            ClassProgressBar(
                progress = taskTrackerBarInformation.taskProgressPercentage,
                color = currentColor)
            ClassProgressBarPercentage(taskTrackerBarInformation.taskProgressPercentage)

            if (expanded) {
                if (tasks.isEmpty()) {
                    Text(
                        "Oops! Looks like there are no tasks here yet",
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    tasks.forEach { task ->
                        TasksListItem(
                            taskInfo = task.description,
                            isChecked = task.isCompleted,
                            onCheckedChange = { isChecked ->
                                viewModel?.updateTaskCompletion(currentClassName, task.description, isChecked)
                            },
                            onLongPress = {
                                selectedTaskDescription = task.description
                                showDeleteDialog = true
                            },
                            color = currentColor // Pass the currentColor here
                        )
                    }
                }
            }
        }

        if (showDeleteDialog) {
            // Show confirmation dialog
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Task") },
                text = { Text("Are you sure you want to delete this task?") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel?.deleteTask(currentClassName, selectedTaskDescription)
                            showDeleteDialog = false
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDeleteDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }


        // Add Task Button, visible and aligned only when expanded
        if (expanded) {
            Button(
                onClick = { showTaskDialog = true },
                colors = ButtonDefaults.buttonColors(Color.hsl(217f, 0.89f, 0.81f)),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(10.dp)
                    .size(40.dp)
                    .clip(RoundedCornerShape(30.dp)),
                contentPadding = PaddingValues(0.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.add_fill0_wght400_grad0_opsz24),
                    contentDescription = "",
                    modifier = Modifier.size(30.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
        }

        if (showTaskDialog) {
            newTaskDialog.EditTaskDialog(
                onDismiss = { showTaskDialog = false },
                onSave = { taskDescription ->
                    viewModel?.addTaskToClass(taskTrackerBarInformation.className, taskDescription)
                    showTaskDialog = false
                }
            )
        }
    }

    ClassItemExpandButton { expanded = !expanded }
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
            text = taskTrackerBarInformation.className,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "$taskCount tasks",
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ClassProgressBar(progress: Float, color: Color) {
    LinearProgressIndicator(
        progress = progress,
        color = color,
        modifier = Modifier
            .fillMaxWidth()
            .size(height = 15.dp, width = 0.dp)
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(10.dp))
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
private fun ClassItemExpandButton(onClick: () -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .background(Color.hsl(214f, .21f, .89f))
            .clickable {
                isExpanded = !isExpanded
                onClick() // This triggers the external action, likely expanding or collapsing content
            }
    ) {
        val iconId = if (isExpanded) R.drawable.expand_less else R.drawable.expand_icon
        Image(
            painter = painterResource(id = iconId),
            contentDescription = "Expand or collapse item",
            alignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TasksListItem(
    taskInfo: String,
    isChecked: Boolean,
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
                uncheckedColor = Color.Gray, // Or any other color for the unchecked state
                checkmarkColor = Color.White), // Color of the checkmark
            modifier = Modifier.padding(start = 10.dp)
        )

        Text(
            text = taskInfo,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}

