package com.example.tasktrackerredo

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var taskTrackBarInformationList = mutableStateListOf<TaskTrackerBarInformation>()

    init {
        // Initialize with your default data
        taskTrackBarInformationList.addAll(
            listOf(
                TaskTrackerBarInformation(
                    className = "ART",
                    tasks = mutableListOf(TaskInformation("Art Task 1", false)),
                    taskProgressPercentage = .0f
                ),
                TaskTrackerBarInformation(
                    className = "CODING", tasks = mutableListOf(
                        TaskInformation("Coding Task 1", false),
                        TaskInformation("Coding Task 2", false)
                    ), taskProgressPercentage = .0f
                )
            )
        )
    }

    fun updateTaskCompletion(className: String, taskDescription: String, isCompleted: Boolean) {
        val classIndex =
            taskTrackBarInformationList.indexOfFirst { it.getClassName() == className }
        if (classIndex != -1) {
            val trackerBarInformation = taskTrackBarInformationList[classIndex]
            trackerBarInformation.tasks.find { it.description == taskDescription }?.isCompleted =
                isCompleted

            // Recalculate and update progress
            val completedTasks = trackerBarInformation.tasks.count { it.isCompleted }
            val progress = completedTasks.toFloat() / trackerBarInformation.tasks.size

            // Update the item in the list to trigger recomposition
            taskTrackBarInformationList[classIndex] = trackerBarInformation.copy(taskProgressPercentage = progress)
        }

    }

    fun createClass(className: String) {
        // Check if the class already exists
        val classExists = taskTrackBarInformationList.any { it.getClassName() == className }
        if (!classExists) {
            // Create a new TaskTrackerBarInformation with 0% progress
            val newTrackerBarInformation = TaskTrackerBarInformation(className, 0.0f)
            taskTrackBarInformationList.add(newTrackerBarInformation)
        }
    }

    fun addTaskToClass(className: String, taskDescription: String) {
        val classIndex =
            taskTrackBarInformationList.indexOfFirst { it.getClassName() == className }
        if (classIndex != -1) {
            taskTrackBarInformationList[classIndex].tasks.apply {
                add(TaskInformation(taskDescription, false))
            }
            taskTrackBarInformationList[classIndex].recalculateProgress()
        }
    }

    fun deleteTask(className: String, taskDescription: String) {
        val classIndex =
            taskTrackBarInformationList.indexOfFirst { it.getClassName() == className }
        if (classIndex != -1) {
            taskTrackBarInformationList[classIndex].tasks.removeIf { it.description == taskDescription }
            taskTrackBarInformationList[classIndex].recalculateProgress()
        }
    }

    fun deleteClass(className: String) {
        // Logic to remove the class from the list
        taskTrackBarInformationList.removeIf { it.getClassName() == className }
    }

    fun deleteAllClassesAndTasks() {
        taskTrackBarInformationList.clear()
    }

    private fun recalculateProgress(classIndex: Int) {
        val tasks = taskTrackBarInformationList[classIndex].tasks
        val completedTasks = tasks.count { it.isCompleted }
        val progress = if (tasks.isNotEmpty()) completedTasks.toFloat() / tasks.size else 0.0f

        // Update the progress in the task tracker bar information
        val classInfo = taskTrackBarInformationList[classIndex]
        taskTrackBarInformationList[classIndex] =
            classInfo.copy(taskProgressPercentage = progress)
    }
}