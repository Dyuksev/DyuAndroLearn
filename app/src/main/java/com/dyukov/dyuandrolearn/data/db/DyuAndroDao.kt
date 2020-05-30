package com.dyukov.dyuandrolearn.data.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DyuAndroDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLesson(item: LessonItem)

    @Delete
    suspend fun deleteLesson(item: LessonItem)

    @Query("select * from lesson_item")
    fun getAllShoppingItem(): LiveData<List<LessonItem>>
}