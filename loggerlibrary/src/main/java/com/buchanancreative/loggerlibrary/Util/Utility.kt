package com.buchanancreative.loggerlibrary.Util

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToLong


/**
 * Created by buchanancreative on 10/11/17.
 */

object Utility {
    fun calculateOneRepMax(weight: Double, reps: Int): Int {
        val tempMax = weight * (1.0 + reps.toDouble() / 30.0)
        return tempMax.toInt()
    }

    fun getConvertedKgToLbsValue(kgWeight: Double): String {
       val lbsWeight = kgWeight * Constants.kgToLbsConversion

        val roundedValue = lbsWeight.roundToLong()

        val toDisplay = when {
            abs(lbsWeight - roundedValue) > .09 -> "%.1f".format(lbsWeight).toDouble()
            else -> roundedValue
        }

        return toDisplay.toString()
    }
}
