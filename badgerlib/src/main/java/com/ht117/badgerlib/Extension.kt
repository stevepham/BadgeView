package com.ht117.badgerlib

import android.content.Context
import kotlin.math.roundToInt

fun Context.dipToPx(dip: Int): Int {
    return (dip * resources.displayMetrics.density + 0.5f).roundToInt()
}

fun Context.spToPx(sp: Float): Int {
    val fontScale = resources.displayMetrics.scaledDensity
    return (fontScale * sp + 0.5f).roundToInt()
}