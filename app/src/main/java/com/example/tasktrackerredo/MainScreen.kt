package com.example.tasktrackerredo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainScreen(private val viewModel: MainViewModel?) {

    @Composable
    fun Launch() {

        val hasClasses = viewModel?.taskTrackBarInformationList?.isNotEmpty() == true

        Box(modifier = Modifier.fillMaxSize()) {

            if (hasClasses) {
                LazyColumn {
                    item { UserHeaderMainScreen() }
                    itemsIndexed(
                        viewModel?.taskTrackBarInformationList ?: listOf()
                    ) { index, (taskTrackerBarInfo, tasks) ->
                        val colorIndex = index / 2 % colorList.size
                        val currentColor = colorList[colorIndex]
                        val showDeleteClassDialog = remember { mutableStateOf(false) }

                        TaskTrackerBar(
                            tasks = tasks,
                            taskTrackerBarInformation = taskTrackerBarInfo,
                            viewModel = viewModel,
                            currentClassName = taskTrackerBarInfo.className,
                            currentColor = currentColor,
                            showDeleteClassDialog = showDeleteClassDialog
                        )

                        if (showDeleteClassDialog.value) {
                            // AlertDialog logic for deleting the class
                            AlertDialog(
                                onDismissRequest = { showDeleteClassDialog.value = false },
                                title = { Text("Delete Class") },
                                text = { Text("Are you sure you want to delete the entire class and its tasks?") },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            viewModel?.deleteClass(taskTrackerBarInfo.className)
                                            showDeleteClassDialog.value = false
                                        }
                                    ) {
                                        Text("Delete")
                                    }
                                },
                                dismissButton = {
                                    Button(onClick = { showDeleteClassDialog.value = false }) {
                                        Text("Cancel")
                                    }
                                }
                            )
                        }
                    }
                }
            } else {
                // Display the message and logo when there are no classes
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
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


@Composable
private fun UserHeaderMainScreen() {
    Box(
        modifier = Modifier
            .background(Color.hsl(148f, .0f, 0.87f))
            .clip(
                RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomEnd = 10.dp,
                    bottomStart = 10.dp
                )
            ) // Add rounded corners here
    ) {
        Row {
            Box(modifier = Modifier, contentAlignment = Alignment.CenterStart) {
                HeaderAndroidLogo()
            }

            Box(modifier = Modifier) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HeaderText()
                    HeaderMenuButton()
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

@Composable
private fun HeaderMenuButton() {
    Image(
        painter = painterResource(id = R.drawable.menu_button),
        contentDescription = "",
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .size(30.dp),
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
//            colorFilter = ColorFilter.tint(Color.hsl(99f, 0.43f, 0.38f))
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
        newClassDialog.CreateClassDialog(
            onDismiss = { showDialog.value = false },
            onCreateClass = { className ->
                viewModel?.createClass(className)
                showDialog.value = false
            }
        )
    }
}


