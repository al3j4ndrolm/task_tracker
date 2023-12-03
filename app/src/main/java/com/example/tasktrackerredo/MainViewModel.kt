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

    fun handleSaveAction(className: String, newTask: TaskInformation) {
        val classIndex =
            taskTrackBarInformationList.indexOfFirst { it.first.className == className }
        if (classIndex != -1) {
            // Class already exists, add the new task
            val updatedClassInfo = taskTrackBarInformationList[classIndex].first
            val updatedTasksList =
                taskTrackBarInformationList[classIndex].second.toMutableList().apply {
                    add(newTask) // Add the new task
                }

            // Recalculate progress
            val completedTasks = updatedTasksList.count { it.isCompleted }
            val progress =
                if (updatedTasksList.isNotEmpty()) completedTasks.toFloat() / updatedTasksList.size else 0f
            updatedClassInfo.taskProgressPercentage = progress

            // Update the list to trigger recomposition
            taskTrackBarInformationList[classIndex] = updatedClassInfo to updatedTasksList
        } else {
            // Class does not exist, create a new class and add the task
            val initialProgress =
                if (newTask.isCompleted) 1.0f / 1.0f else 0.0f // Assuming one task, progress is either 0% or 100%
            taskTrackBarInformationList.add(
                Pair(
                    TaskTrackerBarInformation(className, initialProgress),
                    mutableListOf(newTask)
                )
            )
        }
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
        val classIndex =
            taskTrackBarInformationList.indexOfFirst { it.first.className == className }
        if (classIndex != -1) {
            val updatedTasksList =
                taskTrackBarInformationList[classIndex].second.toMutableList().apply {
                    add(TaskInformation(taskDescription, false))
                }

            // Now the updatedTasksList can be used outside the apply block
            taskTrackBarInformationList[classIndex] =
                taskTrackBarInformationList[classIndex].first to updatedTasksList

            // Update the list and recalculate progress...
            // Your progress update logic here
        }
    }

    fun deleteTask(className: String, taskDescription: String) {
        val classIndex =
            taskTrackBarInformationList.indexOfFirst { it.first.className == className }
        if (classIndex != -1) {
            val updatedTasksList = taskTrackBarInformationList[classIndex].second.filter {
                it.description != taskDescription
            }.toMutableList()

            taskTrackBarInformationList[classIndex] =
                taskTrackBarInformationList[classIndex].first to updatedTasksList

            // Recalculate progress and update the list...
        }
    }

    fun deleteClass(className: String) {
        // Logic to remove the class from the list
        taskTrackBarInformationList.removeIf { it.first.className == className }
    }
}