package com.dyukov.dyuandrolearn.data.db.model

import android.os.Parcelable
import androidx.room.Embedded
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PracticeData(
    @SerializedName("type")
    var type: Int? = null,
    @SerializedName("question")
    var question: String? = null,
    @SerializedName("answers")
    var answers: ArrayList<Answer>? = null,
    @SerializedName("rightAnswer")
    var rightAnswer: Int? = null,
    @SerializedName("rightAnswerStr")
    var rightAnswerStr: String? = null,
    @SerializedName("primary")
    var primary: Boolean? = null,
) : Parcelable

@Parcelize
data class Answer(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("text")
    var text: String? = null
) : Parcelable
