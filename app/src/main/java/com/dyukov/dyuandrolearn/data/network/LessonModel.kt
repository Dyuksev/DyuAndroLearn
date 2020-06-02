package com.dyukov.dyuandrolearn.data.network

import com.google.firebase.database.PropertyName

data class LessonModel(

    var id: Int? = null,
    var type: Int? = null,
    var name: String? = null,
    var description: String? = null,
    var tasks: List<TaskModel>? = null,
    var points: Int? = null,
    var done: Boolean? = false
)

