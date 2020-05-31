package com.dyukov.dyuandrolearn.data.network


data class TaskModel(
    var taskType: Int? = null,
    var id: Int? = null,
    var tastName: String? = null,
    var taskDescription: String? = null,
    var content: String? = null,
    var isCompleted: Boolean = false,
    var points: Int? = null
)