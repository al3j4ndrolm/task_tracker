package com.example.tasktrackerredo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.tasktrackerredo.ui.theme.TaskTrackerRedoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel by viewModels() // Proper initialization
            MainScreen(viewModel = viewModel).Launch()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    TaskTrackerRedoTheme {
        // Pass null for the viewModel in the preview environment
        val mainScreen = MainScreen(viewModel = null)
        mainScreen.Launch()
    }
}