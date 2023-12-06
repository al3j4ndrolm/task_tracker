package com.example.tasktrackerredo

data class TaskTrackerBarInformation(
     private val className: String,
     var taskProgressPercentage: Float,
     val tasks: MutableList<TaskInformation> = mutableListOf(),
) {

    fun getClassName() = className

    fun getCompletedTasksCount() = tasks.count { it.isCompleted }


     fun recalculateProgress() {
        val completedTasks = tasks.count { it.isCompleted }
         taskProgressPercentage = if (tasks.isNotEmpty()) completedTasks.toFloat() / tasks.size else 0.0f
    }
}