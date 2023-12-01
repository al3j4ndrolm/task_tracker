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
import androidx.compose.foundation.shape.RoundedCornerShape
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
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                UserHeaderMainScreen()
                viewModel?.taskTrackBarInformationList?.forEach { (taskTrackerBarInfo, tasks) ->
                    TaskTrackerBar(
                        tasks = tasks,
                        taskTrackerBarInformation = taskTrackerBarInfo,
                        viewModel = viewModel // Pass the viewModel here
                    )
                }
            }
            NewClassButton()
        }
    }


    @Composable
    private fun UserHeaderMainScreen() {
        Box(modifier = Modifier.background(Color.hsl(148f, .0f, 0.87f))) {
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
                    fontStyle = FontStyle(R.font.inter_regular),
                    fontSize = 18.sp
                )
                Text(text = "Task Tracker", fontStyle = FontStyle((R.font.inter_regular)))
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
    fun NewClassButton() {
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
                    .align(Alignment.BottomEnd).padding(10.dp), // Align the button to the bottom end of the Box
                contentPadding = PaddingValues(0.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.edit_icon),
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
}

