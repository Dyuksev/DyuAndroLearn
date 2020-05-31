package com.dyukov.dyuandrolearn.data.network

data class LessonModel(
    var id: Int? = null,
    var lessonName: String? = null,
    var lessonDescription: String? = null,
    var experienceCount: Int? = null,
    var tasks: List<TaskModel>? = null,
    var type: Int? = null
)