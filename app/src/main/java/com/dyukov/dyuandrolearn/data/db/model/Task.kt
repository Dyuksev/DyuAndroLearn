package com.dyukov.dyuandrolearn.data.db.model


import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Task(
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @SerializedName("data")
    var data: ArrayList<PracticeData>? = null,
    @SerializedName("theoryId")
    var theoryId: Int? = null,
    @SerializedName("points")
    var points: Int? = null,
    @SerializedName("isPrimary")
    var isPrimary: Boolean? = null,
    @SerializedName("done")
    var done: Boolean? = null
) : Parcelable