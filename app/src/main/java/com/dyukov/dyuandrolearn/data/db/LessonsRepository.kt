package com.dyukov.dyuandrolearn.data.db

class LessonsRepository(
    private val db: DyuAndroDatabase
) {
    suspend fun upsert(item: LessonItem) = db.getLessonsDb().upsertLesson(item)

    suspend fun delete(item: LessonItem) = db.getLessonsDb().deleteLesson(item)

    fun getAllshoppingItem() = db.getLessonsDb().getAllShoppingItem()
}