package com.dyukov.dyuandrolearn.extensions

import android.content.res.Resources
import android.util.TypedValue
import kotlin.math.roundToInt

fun Float.toPx() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

