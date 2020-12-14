package com.dyukov.dyuandrolearn.data.db

import androidx.room.TypeConverter
import com.dyukov.dyuandrolearn.data.db.model.Answer
import com.dyukov.dyuandrolearn.data.db.model.PracticeData
import com.dyukov.dyuandrolearn.data.db.model.Theory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
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

    @TypeConverter
    fun fromPracticeDataList(countryLang: ArrayList<PracticeData?>?): String? {
        if (countryLang == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object :
            TypeToken<ArrayList<PracticeData?>?>() {}.type
        return gson.toJson(countryLang, type)
    }

    @TypeConverter
    fun toPracticeDataList(countryLangString: String?): ArrayList<PracticeData>? {
        if (countryLangString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object :
            TypeToken<List<PracticeData?>?>() {}.type
        return gson.fromJson<ArrayList<PracticeData>>(countryLangString, type)
    }

    @TypeConverter
    fun fromAnswerList(countryLang: ArrayList<Answer?>?): String? {
        if (countryLang == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object :
            TypeToken<ArrayList<Answer?>?>() {}.type
        return gson.toJson(countryLang, type)
    }

    @TypeConverter
    fun toAnswerList(countryLangString: String?): ArrayList<Answer>? {
        if (countryLangString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object :
            TypeToken<ArrayList<Answer?>?>() {}.type
        return gson.fromJson<ArrayList<Answer>>(countryLangString, type)
    }

    @TypeConverter
    fun fromTheoryList(theories: ArrayList<Theory?>?): String? {
        if (theories == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object :
            TypeToken<ArrayList<Theory?>?>() {}.type
        return gson.toJson(theories, type)
    }

    @TypeConverter
    fun toTheoriesDataList(theoriesString: String?): ArrayList<Theory>? {
        if (theoriesString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object :
            TypeToken<List<Theory?>?>() {}.type
        return gson.fromJson<ArrayList<Theory>>(theoriesString, type)
    }
}