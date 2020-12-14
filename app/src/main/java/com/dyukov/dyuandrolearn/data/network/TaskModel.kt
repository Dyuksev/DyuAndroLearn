package com.dyukov.dyuandrolearn.data.network

import android.os.Parcelable
import com.dyukov.dyuandrolearn.data.db.model.PracticeData
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TaskModel(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("data")
    var data: ArrayList<PracticeData>? = null,
    @SerializedName("theoryId")
    var theoryId: Int? = null,
    @SerializedName("points")
    var points: Int? = null,
    @SerializedName("primary")
    var primary: Boolean? = null,
    @SerializedName("done")
    var done: Boolean? = null
) : Parcelable