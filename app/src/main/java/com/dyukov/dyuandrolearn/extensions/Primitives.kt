package com.dyukov.dyuandrolearn.extensions

import android.content.res.Resources
import android.util.TypedValue
import kotlin.math.roundToInt

fun Int.toDp() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics)

fun Float.toPx() = (this * Resources.getSystem().displayMetrics.density).roundToInt()
fun Int.toPx() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

fun Float.mmToPx() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, this, Resources.getSystem().displayMetrics).toInt()
fun Int.mmToPx() = this.toFloat().mmToPx()

fun Boolean?.falseIfNull() = this ?: false