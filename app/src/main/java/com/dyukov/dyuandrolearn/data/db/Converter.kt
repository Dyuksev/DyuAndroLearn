package com.dyukov.dyuandrolearn.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class Converter {
    @TypeConverter
    fun fromString(value: String?): ArrayList<Int> {
        val listType =
            object : TypeToken<ArrayList<Int?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<Int?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}