package com.dyukov.dyuandrolearn.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task(
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "composition")
    var composition: String,
    @ColumnInfo(name = "points")
    var points: Int,
    @ColumnInfo(name = "type")
    var type: Int,
    @ColumnInfo(name = "done")
    var done: Boolean,
    @ColumnInfo(name = "suggested")
    var suggested: Boolean,
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
)