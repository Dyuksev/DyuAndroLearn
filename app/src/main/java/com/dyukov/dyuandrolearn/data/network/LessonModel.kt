package com.dyukov.dyuandrolearn.data.network

import android.os.Parcelable
import com.dyukov.dyuandrolearn.data.db.model.Theory
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LessonModel(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("type")
    var type: Int? = null,
    @SerializedName("theoryId")
    var theoryId: Int? = null,
    @SerializedName("tasksId")
    var tasksId: ArrayList<Int>? = null,
    @SerializedName("points")
    var points: Int? = null
) : Parcelable


@Parcelize
data class DyuData(
    var lessons: ArrayList<LessonModel>? = null,
    var tasks: ArrayList<TaskModel>? = null,
    var theories: ArrayList<Theory>? = null
) : Parcelable
