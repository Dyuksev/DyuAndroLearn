package com.dyukov.dyuandrolearn.extensions

import android.content.res.Resources
import android.util.TypedValue
import kotlin.math.roundToInt

inline fun <T> T.applyIf(predicate: T.() -> Boolean, block: T.() -> Unit): T = apply {
    if (predicate(this))
        block(this)
}

inline fun <T> T.applyIf(predicate: Boolean, block: T.() -> Unit): T = apply {
    if (predicate)
        block(this)
}

fun Int.toDp() =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics)

fun Int.pXtoSp() =
    this / Resources.getSystem().displayMetrics.density


fun Float.toPx() = (this * Resources.getSystem().displayMetrics.density).roundToInt()
fun Int.toPx() = (this * Resources.getSystem().displayMetrics.density).roundToInt()
fun Boolean?.falseIfNull() = this ?: false

fun String?.containsLetter(): Boolean {
    if (this == null) {
        return false
    }
    val array = this.toCharArray()
    array.forEach {
        if (it.isLetter()) {
            return true
        }
    }
    return false
}

fun String?.containsDigit(): Boolean {
    if (this == null) {
        return false
    }
    val array = this.toCharArray()
    array.forEach {
        if (it.isDigit()) {
            return true
        }
    }
    return false
}
