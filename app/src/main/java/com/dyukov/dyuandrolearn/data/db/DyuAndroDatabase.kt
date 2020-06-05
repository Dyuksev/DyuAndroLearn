package com.dyukov.dyuandrolearn.data.db

import android.content.Context
import androidx.room.*
import com.dyukov.dyuandrolearn.data.db.model.Lesson
import com.dyukov.dyuandrolearn.data.db.model.Task
import com.dyukov.dyuandrolearn.data.db.model.User

@Database(entities = [Lesson::class, Task::class, User::class], version = 1)
@TypeConverters(Converter::class)
abstract class DyuAndroDatabase : RoomDatabase() {
    abstract fun getDyuAndroDao(): DyuAndroDao

    companion object {
        @Volatile
        private var instance: DyuAndroDatabase? = null

        private var Lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(Lock) {
            instance ?: createDatabase(
                context
            ).also { instance = it }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            DyuAndroDatabase::class.java, "DyuAndroDb"
        ).build()
    }
}
