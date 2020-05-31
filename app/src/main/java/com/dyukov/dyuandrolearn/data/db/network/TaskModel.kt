package com.dyukov.dyuandrolearn.data.db.network


data class TaskModel(
    var taskType:Int? = null,
    var id:Int? = null,
    var tastName:String? = null,
    var taskDescription:String? = null
)