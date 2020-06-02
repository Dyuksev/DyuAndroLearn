package com.dyukov.dyuandrolearn.data.network

data class TaskModel(
    var id: Int? = null,
    var type: Int? = null,
    var name: String? = null,
    var composition: String? = null,
    var done: Boolean = false,
    var suggested: Boolean = false,
    var points: Int? = null
)