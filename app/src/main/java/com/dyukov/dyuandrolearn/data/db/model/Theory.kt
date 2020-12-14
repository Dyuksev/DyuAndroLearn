package com.dyukov.dyuandrolearn.data.db.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class  Theory(
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("composition")
    var composition: String? = null,
    @SerializedName("sample")
    var sample: String? = null,
    @SerializedName("earned")
    var earned: Boolean? = null
) : Parcelable