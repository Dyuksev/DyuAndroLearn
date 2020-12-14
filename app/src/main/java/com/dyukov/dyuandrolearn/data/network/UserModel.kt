package com.dyukov.dyuandrolearn.data.network

data class UserModel(
    var email: String = "",
    var name: String = "",
    var level: Int = 0,
    var progress: Int = 0,
    var doneLessonsId: ArrayList<Int> = arrayListOf(),
    var doneTheoryId: ArrayList<Int> = arrayListOf()
)