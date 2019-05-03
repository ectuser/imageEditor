package com.example.myapplication

import android.widget.Spinner
import java.lang.Math.pow
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sqrt

class Cell(var x: Int = 0, var y: Int = 0, var g: Int = 0, var h: Double = 0.0, var f: Double = 0.0,
           var isFree: Boolean = true, var isOpened: Boolean = false, var isClosed: Boolean = false,
           var parentX: Int = -1, var parentY: Int = -1) {

    fun hCalculate(xEnd: Double, yEnd: Double, heuristicSpinner: Spinner): Double {
        if (heuristicSpinner.selectedItemPosition == 0) {
            return abs(xEnd - x) + abs(yEnd - y)
        }
        if (heuristicSpinner.selectedItemPosition == 1) {
            return max(abs(xEnd - x), abs(yEnd - y))
        }
        if (heuristicSpinner.selectedItemPosition == 2) {
            return sqrt(pow(xEnd - x, 2.0) + pow(yEnd - y, 2.0))
        }

        return 0.0
    }
}