package com.dyukov.dyuandrolearn.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lesson_item")
data class LessonItem(

    @ColumnInfo(name = "item_name")
    var name: String,
    @ColumnInfo(name = "item_amount")
    var amount: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}