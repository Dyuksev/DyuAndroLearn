package com.dyukov.dyuandrolearn.data.db

import com.dyukov.dyuandrolearn.data.db.model.Lesson
import com.dyukov.dyuandrolearn.data.db.model.Task
import com.dyukov.dyuandrolearn.data.db.model.User

class Repository(
    private val db: DyuAndroDatabase
) {
    suspend fun insert(item: Lesson) = db.getDyuAndroDao().insert(item)

    suspend fun insert(item: Task) = db.getDyuAndroDao().insert(item)

    suspend fun insert(item: User) = db.getDyuAndroDao().insert(item)

    suspend fun update(item: Lesson) = db.getDyuAndroDao().update(item)

    suspend fun update(item: User) = db.getDyuAndroDao().update(item)

    suspend fun update(item: Task) = db.getDyuAndroDao().update(item)

    suspend fun delete(item: Lesson) = db.getDyuAndroDao().delete(item)

    suspend fun delete(item: Task) = db.getDyuAndroDao().delete(item)

    suspend fun getAllLessons() = db.getDyuAndroDao().allLessons

    suspend fun getLesson(id: Int) = db.getDyuAndroDao().getLesson(id)

    suspend fun getTasksFromLesson(id: Int) = db.getDyuAndroDao().getTasksFromLesson(id)

    suspend fun getAllTasks() = db.getDyuAndroDao().allTasks

    suspend fun getUserData() = db.getDyuAndroDao().user
}