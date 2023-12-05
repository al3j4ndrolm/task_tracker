package com.example.tasktrackerredo

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var taskTrackBarInformationList =
        mutableStateListOf<Pair<TaskTrackerBarInformation, MutableList<TaskInformation>>>()

    init {
        // Initialize with your default data
        taskTrackBarInformationList.addAll(
            listOf(
                Pair(
                    TaskTrackerBarInformation(className = "ART", taskProgressPercentage = .0f),
                    mutableListOf(TaskInformation("Art Task 1", false))
                ),
                Pair(
                    TaskTrackerBarInformation(className = "CODING", taskProgressPercentage = .0f),
                    mutableListOf(
                        TaskInformation("Coding Task 1", false),
                        TaskInformation("Coding Task 2", false)
                    )
                )
            )
        )
    }

    fun updateTaskCompletion(className: String, taskDescription: String, isCompleted: Boolean) {
        val classIndex =
            taskTrackBarInformationList.indexOfFirst { it.first.className == className }
        if (classIndex != -1) {
            val classTasks = taskTrackBarInformationList[classIndex]
            classTasks.second.find { it.description == taskDescription }?.isCompleted = isCompleted

            // Recalculate and update progress
            val completedTasks = classTasks.second.count { it.isCompleted }
            val progress = completedTasks.toFloat() / classTasks.second.size

            // Update the item in the list to trigger recomposition
            taskTrackBarInformationList[classIndex] =
                classTasks.first.copy(taskProgressPercentage = progress) to classTasks.second
        }

    }

    fun createClass(className: String) {
        // Check if the class already exists
        val classExists = taskTrackBarInformationList.any { it.first.className == className }
        if (!classExists) {
            // Create a new TaskTrackerBarInformation with 0% progress
            val newClassInfo = TaskTrackerBarInformation(className, 0.0f)
            // Create an empty list of tasks for this new class
            val newTasksList = mutableListOf<TaskInformation>()
            // Add the new class and its task list to the taskTrackBarInformationList
            taskTrackBarInformationList.add(Pair(newClassInfo, newTasksList))
        }
    }

    fun addTaskToClass(className: String, taskDescription: String) {
        val classIndex = taskTrackBarInformationList.indexOfFirst { it.first.className == className }
        if (classIndex != -1) {
            taskTrackBarInformationList[classIndex].second.add(TaskInformation(taskDescription, false))
            recalculateProgress(classIndex)
        }
    }

    fun deleteTask(className: String, taskDescription: String) {
        val classIndex = taskTrackBarInformationList.indexOfFirst { it.first.className == className }
        if (classIndex != -1) {
            taskTrackBarInformationList[classIndex].second.removeIf { it.description == taskDescription }
            recalculateProgress(classIndex)
        }
    }

    fun deleteClass(className: String) {
        // Logic to remove the class from the list
        taskTrackBarInformationList.removeIf { it.first.className == className }
    }
    fun deleteAllClassesAndTasks() {
        taskTrackBarInformationList.clear()
    }

    private fun recalculateProgress(classIndex: Int) {
        val tasks = taskTrackBarInformationList[classIndex].second
        val completedTasks = tasks.count { it.isCompleted }
        val progress = if (tasks.isNotEmpty()) completedTasks.toFloat() / tasks.size else 0.0f

        // Update the progress in the task tracker bar information
        val classInfo = taskTrackBarInformationList[classIndex].first
        taskTrackBarInformationList[classIndex] = classInfo.copy(taskProgressPercentage = progress) to tasks
    }
}