package com.buchanancreative.gymlogger

import com.buchanancreative.loggerlibrary.Util.Constants
import com.buchanancreative.loggerlibrary.Util.Utility
import okhttp3.internal.Util

import org.junit.Test

import org.junit.Assert.*
import kotlin.math.abs
import kotlin.math.roundToLong

/**
 * Created by buchanancreative on 12/28/17.
 */

class UtilityUnitTests {

    @Test
    @Throws(Exception::class)
    fun oneRepMaxIsCorrect() {
        val reps = 10
        val weight = 300
        val actual = Utility.calculateOneRepMax(weight.toDouble(), reps)

        assertEquals(400, actual.toLong())

    }


    @Test
    fun imperialToMetricConversionIsCorrect() {
        // The app should allow values up to a tenth to be displayed 10.1 and 10.9,
        // but 10.0 should be displayed at 10.
        // 9.99 should also be displayed as 10
        // 9.59 should be displayed at 9.6

        val weights =  arrayListOf(9.6999999, 9.99, 10.0, 20000.455555, 7.05, 1.37, 5.05, 20.0)

        weights.forEach { weight ->

            val storedValue = weight / Constants.kgToLbsConversion

            val toDisplay = Utility.getConvertedKgToLbsValue(storedValue)

            println("Original: $weight, Displayed: $toDisplay")

        }

    }

}
