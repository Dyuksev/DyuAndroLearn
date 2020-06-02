package com.dyukov.dyuandrolearn.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.ArrayList

@Entity(tableName = "lesson")
data class Lesson(
    @ColumnInfo(name = "item_name")
    var name: String,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "tasks")
    var tasks: ArrayList<Int>,
    @ColumnInfo(name = "points")
    var points: Int,
    @ColumnInfo(name = "type")
    var type: Int,
    @ColumnInfo(name = "done")
    var done: Boolean,
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
)
