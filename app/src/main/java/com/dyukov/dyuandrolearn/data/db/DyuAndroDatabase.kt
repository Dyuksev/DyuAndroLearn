package com.dyukov.dyuandrolearn.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LessonItem::class], version = 1)
abstract class DyuAndroDatabase : RoomDatabase() {
    abstract fun getLessonsDb(): DyuAndroDao

    companion object {
        @Volatile
        private var instance: DyuAndroDatabase? = null

        private var Lock = Any()

        operator fun invoke(context: Context) = instance
            ?: synchronized(Lock) {
                instance
                    ?: createDatabase(
                        context
                    ).also { instance = it }

            }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            DyuAndroDatabase::class.java, "ShoppingList"
        ).build()
    }
}
