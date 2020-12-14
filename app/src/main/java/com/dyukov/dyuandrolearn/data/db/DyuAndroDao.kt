package com.dyukov.dyuandrolearn.data.db

import androidx.room.*
import com.dyukov.dyuandrolearn.data.db.model.Lesson
import com.dyukov.dyuandrolearn.data.db.model.Task
import com.dyukov.dyuandrolearn.data.db.model.Theory
import com.dyukov.dyuandrolearn.data.db.model.User

@Dao
interface DyuAndroDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Lesson)

    @Query("SELECT * FROM lesson WHERE id LIKE :uid0")
    suspend fun getLesson(uid0: Int): Lesson

    @get:Query("SELECT * FROM lesson")
    val allLessons: List<Lesson>

    @Delete
    suspend fun delete(item: Lesson)

    @Update
    suspend fun update(task: Lesson)

    @get:Query("SELECT * FROM task")
    val allTasks: List<Task>

    @Update
    suspend fun update(task: Task)

    @Query("SELECT * FROM task WHERE id LIKE :uid1")
    suspend fun getTasksFromLesson(uid1: Int): Task

    @Delete
    suspend fun delete(item: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Task)

    @Insert
    suspend fun insert(item: User)

    @Update
    suspend fun update(item: User)

    @get:Query("SELECT * FROM user WHERE id = 1")
    val user: User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Theory)

    @Query("SELECT * FROM Theory WHERE id LIKE :uid2")
    suspend fun getTheory(uid2: Int): Theory

    @get:Query("SELECT * FROM Theory")
    val allTheories: List<Theory>

    @Delete
    suspend fun delete(item: Theory)

    @Update
    suspend fun update(task: Theory)
}