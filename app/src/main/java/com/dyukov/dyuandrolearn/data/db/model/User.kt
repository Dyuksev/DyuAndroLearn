package com.dyukov.dyuandrolearn.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "email")
    var email: String,
    @ColumnInfo(name = "level")
    var level: Int,
    @ColumnInfo(name = "progress")
    var progress: Int,
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
)