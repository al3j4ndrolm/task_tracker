package com.example.tasktrackerredo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun TaskTrackerBar(
    tasks: List<TaskInformation>,
    taskTrackerBarInformation: TaskTrackerBarInformation,
    viewModel: MainViewModel?
) {
    var expanded by remember { mutableStateOf(false) }
    var showTaskDialog by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(taskTrackerBarInformation.taskProgressPercentage) }
    val newTaskDialog = NewTaskDialog()


    // Observe changes in tasks and taskTrackerBarInformation
    LaunchedEffect(taskTrackerBarInformation, tasks) {
        progress = taskTrackerBarInformation.taskProgressPercentage
    }
    Box(modifier = Modifier.fillMaxWidth()) {
        Column {
            ClassTitle(taskTrackerBarInformation, tasks.size)
            ClassProgressBar(taskTrackerBarInformation.taskProgressPercentage)
            ClassProgressBarPercentage(taskTrackerBarInformation.taskProgressPercentage)

            if (expanded) {
                tasks.forEach { task ->
                    TasksListItem(
                        taskInfo = task.description,
                        isChecked = task.isCompleted,
                        onCheckedChange = { isChecked ->
                            viewModel?.updateTaskCompletion(taskTrackerBarInformation.className, task.description, isChecked)
                        }
                    )
                }
            }
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
                onSave = { className, taskInfo ->
                    viewModel?.handleSaveAction(className, taskInfo)
                    showTaskDialog = false
                }
            )
        }
    }

    ClassItemExpandButton { expanded = !expanded }
}


@Composable
private fun ClassTitle(taskTrackerBarInformation: TaskTrackerBarInformation, taskCount: Int) {
    // Append the task count to the class name
    val titleWithTaskCount = "${taskTrackerBarInformation.className} - $taskCount tasks"
    Text(
        text = titleWithTaskCount, modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 8.dp)
    )
}

@Composable
private fun ClassProgressBar(progress: Float) {

    LinearProgressIndicator(
        progress = progress,
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
    Box(
        modifier = Modifier
            .background(Color.hsl(214f, .21f, .89f))
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = R.drawable.expand_icon),
            contentDescription = "Expand or collapse item",
            alignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun TasksListItem(
    taskInfo: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange, // Here, the state should be updated
            modifier = Modifier.padding(start = 10.dp)
        )

        Text(
            text = taskInfo,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}

