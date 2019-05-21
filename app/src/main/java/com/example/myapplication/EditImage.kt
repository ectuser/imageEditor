package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import kotlin.math.roundToInt
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.R.attr.x
import android.R.attr.y
import android.graphics.PointF
import android.R.attr.top
import android.R.attr.left
import android.content.Context
import android.support.constraint.solver.widgets.WidgetContainer.getBounds
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.util.DisplayMetrics




















class EditImage(BTMP: Bitmap) {
    var BACK_BITMAP = BTMP
    fun returnImage(mainImage: ImageView, savedBitmap: Bitmap){
        mainImage.setImageBitmap(savedBitmap)
    }
    fun returnBackBitmap(): Bitmap {
        return BACK_BITMAP
    }
    fun scale(mainImage: ImageView, selected: String, initialHeight: Int) {
        val scaleCoefficient = selected.removeRange(selected.length - 1, selected.length).toDouble() / 100
        val params = mainImage.layoutParams
        params.height = (initialHeight * scaleCoefficient).roundToInt()
        mainImage.layoutParams = params
    }
    // CALL FILTERS FUNCTION
    fun filter(mainImage: ImageView, number : Int){
        var oldBitmap = (mainImage.drawable as BitmapDrawable).bitmap
        BACK_BITMAP = oldBitmap
        val height = oldBitmap.height
        val width = oldBitmap.width
        var oldBittmapPixelsArray = IntArray(width * height)
        var newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        var newBitmapPixelsArray = oldBittmapPixelsArray
        oldBitmap.getPixels(oldBittmapPixelsArray, 0, width, 0, 0, width, height)

        // CALL FILTER
        if (number == 1) {negative(oldBittmapPixelsArray, newBitmapPixelsArray)}
        if (number == 2) {whiteBlack(oldBittmapPixelsArray, newBitmapPixelsArray)}
        if (number == 3) {red(oldBittmapPixelsArray, newBitmapPixelsArray)}

        newBitmap.setPixels(newBitmapPixelsArray, 0, width, 0, 0, width, height)
        mainImage.setImageBitmap(newBitmap)
    }

    // FILTERS BEGIN
    private fun negative(oldBitmapPixelsArray: IntArray, newBitmapPixelsArray: IntArray){
        for (i in oldBitmapPixelsArray.indices) {
            newBitmapPixelsArray[i] = (0xff000000 or (0xffffffff - oldBitmapPixelsArray[i])).toInt()
        }
    }
    private fun red(oldBitmapPixelsArray: IntArray, newBitmapPixelsArray: IntArray){
        for (i in oldBitmapPixelsArray.indices){
            var red = (oldBitmapPixelsArray[i] and 0x00ff0000 shr 16) // 8 0 shl
            var green = (oldBitmapPixelsArray[i] and 0x0000ff00 shr 8)
            var blue = (oldBitmapPixelsArray[i] and 0x000000ff shr 0)
            newBitmapPixelsArray[i] = ((0xff000000) or (red.toLong() shl 16)).toInt()
        }
    }
    private fun whiteBlack(oldBitmapPixelsArray: IntArray, newBitmapPixelsArray: IntArray){
        for (i in oldBitmapPixelsArray.indices){
            var red = (oldBitmapPixelsArray[i] and 0x00ff0000 shr 16) // 8 0 shl
            var green = (oldBitmapPixelsArray[i] and 0x0000ff00 shr 8)
            var blue = (oldBitmapPixelsArray[i] and 0x000000ff shr 0)

            var averageColor = (red + green + blue) / 3
            newBitmapPixelsArray[i] = ((0xff000000) or (averageColor.toLong() shl 16) or (averageColor.toLong() shl 8) or (averageColor.toLong() shl 0)).toInt()
        }
    }
    // FILTERS END

    // Fucking rotation doesn't work
    fun rotateImage(mainImage: ImageView){
        val matrix = Matrix()

        matrix.postRotate(90F)

        val bitmapOrg = (mainImage.drawable as BitmapDrawable).bitmap
        val scaledBitmap = Bitmap.createScaledBitmap(bitmapOrg, bitmapOrg.width, bitmapOrg.height, true)

        val rotatedBitmap =
            Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true)
        mainImage.setImageBitmap(rotatedBitmap)
    }

    // DAMN BLUR
    @SuppressLint("ClickableViewAccessibility")
    fun blur(mainImage: ImageView, coordinates: TextView) {
        mainImage.setOnTouchListener(View.OnTouchListener { _, event ->
            var rawX = event.x
            var rawY = event.y

            var oldBitmap = (mainImage.drawable as BitmapDrawable).bitmap
            var height = oldBitmap.height
            var width = oldBitmap.width

            val x = (rawX.toDouble() * (width.toDouble() / mainImage.width.toDouble())).toInt()
            val y = (rawY.toDouble() * (height.toDouble() / mainImage.height.toDouble())).toInt()

            coordinates.text = "$x | $y"
            var oldBittmapPixelsArray = IntArray(width * height)
            var newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            var newBitmapPixelsArray = oldBittmapPixelsArray
            oldBitmap.getPixels(oldBittmapPixelsArray, 0, width, 0, 0, width, height)
            var count = 0
            val array = oldBittmapPixelsArray //массив - донор
            val matrix = Array(height) { IntArray(width) } //будущая матрица
            for (i in matrix.indices) {
                for (j in 0 until matrix[i].size) {
                    matrix[i][j] = array[count++] //перенос элементов из донора в матрицу
                }
            }
            val ys: IntArray = intArrayOf(-1, -1, -1, 0, 0, 0, +1, +1, +1)
            val xs: IntArray = intArrayOf(-1, 0, +1, -1, 0, +1, -1, 0, +1)
            var redSum = 0
            var greenSum = 0
            var blueSum = 0
            for (i in 0..8){
                var red = 0
                var green = 0
                var blue = 0
                red = try {
                    (matrix[y + ys[i]][x + xs[i]] and 0x00ff0000 shr 16)
                } catch (e: NumberFormatException){
                    0
                }
                green = try {
                    (matrix[y + ys[i]][x + xs[i]] and 0x0000ff00 shr 8)
                } catch (e: NumberFormatException){
                    0
                }
                blue = try {
                    (matrix[y + ys[i]][x + xs[i]] and 0x000000ff shr 0)
                } catch (e: NumberFormatException){
                    0
                }
//                            var red = (matrix[y + ys[i]][x + xs[i]] and 0x00ff0000 shr 16)
//                            var green = (matrix[y + ys[i]][x + xs[i]] and 0x0000ff00 shr 8)
//                            var blue = (matrix[y + ys[i]][x + xs[i]] and 0x000000ff shr 0)
                redSum += red
                greenSum += green
                blueSum += blue


                redSum += 0
                greenSum += 0
                blueSum += 0

            }
            redSum /= 9
            greenSum /= 9
            blueSum /= 9
            for (i in 0..8){
                try {
                    matrix[y + ys[i]][x + xs[i]] = ((0xff000000) or (redSum.toLong() shl 16) or (greenSum.toLong() shl 8) or (blueSum.toLong() shl 0)).toInt()
                }
                catch (e: NumberFormatException){
                    coordinates.text = "error"
                }
            }
            for (row in 0 until height){
                for (column in 0 until width) {
                    newBitmapPixelsArray[(row * width) + column] = matrix[row][column]
                }
            }
            newBitmap.setPixels(newBitmapPixelsArray, 0, width, 0, 0, width, height)
            mainImage.setImageBitmap(newBitmap)

            return@OnTouchListener true
        })
    }
}