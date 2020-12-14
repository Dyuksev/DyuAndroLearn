package com.dyukov.dyuandrolearn.data.db.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.util.ArrayList

@Parcelize
@Entity
data class Lesson(
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @SerializedName("name")
    var name: String?,
    @SerializedName("type")
    var type: Int?,
    @SerializedName("theoryId")
    var theoryId: Int?,
    @SerializedName("tasksId")
    var tasksId: ArrayList<Int>?,
    @SerializedName("points")
    var points: Int?,
    var isShowForUser: Boolean? = false
    ) : Parcelable
