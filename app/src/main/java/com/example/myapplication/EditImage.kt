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
    fun blur(mainImage: ImageView, coordinates: TextView){
        mainImage.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        var x = event.x.toInt()
                        var y = event.y.toInt()
                        x = 50
                        y = 50
                        coordinates.text = "$x | $y"
                        var eps = 30F

                        var oldBitmap = (mainImage.drawable as BitmapDrawable).bitmap
                        val height = oldBitmap.height
                        val width = oldBitmap.width
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
                            var red = (matrix[y + ys[i]][x + xs[i]] and 0x00ff0000 shr 16)
                            var green = (matrix[y + ys[i]][x + xs[i]] and 0x0000ff00 shr 8)
                            var blue = (matrix[y + ys[i]][x + xs[i]] and 0x000000ff shr 0)
                            redSum += red
                            greenSum += green
                            blueSum += blue
                        }
                        redSum /= 9
                        greenSum /= 9
                        blueSum /= 9
//                        var average = (
//                                matrix[y - 1][x - 1] + matrix[y - 1][x] + matrix[y - 1][x + 1] +
//                                        matrix[y][x - 1] + matrix[y][x] + matrix[y][x + 1] +
//                                        matrix[y + 1][x - 1] + matrix[y + 1][x] + matrix[y + 1][x + 1]
//                                ) / 9
//                        var average = matrix[y][x]
//                        var red = (matrix[y][x] and 0x00ff0000 shr 16) // 8 0 shl
//                        var green = (matrix[y][x] and 0x0000ff00 shr 8)
//                        var blue = (matrix[y][x] and 0x000000ff shr 0)
                        matrix[y - 1][x - 1] = ((0xff000000) or (redSum.toLong() shl 16) or (greenSum.toLong() shl 8) or (blueSum.toLong() shl 0)).toInt()
                        matrix[y - 1][x] = ((0xff000000) or (redSum.toLong() shl 16) or (greenSum.toLong() shl 8) or (blueSum.toLong() shl 0)).toInt()
                        matrix[y - 1][x + 1] = ((0xff000000) or (redSum.toLong() shl 16) or (greenSum.toLong() shl 8) or (blueSum.toLong() shl 0)).toInt()
                        matrix[y][x - 1] = ((0xff000000) or (redSum.toLong() shl 16) or (greenSum.toLong() shl 8) or (blueSum.toLong() shl 0)).toInt()
                        matrix[y][x] = ((0xff000000) or (redSum.toLong() shl 16) or (greenSum.toLong() shl 8) or (blueSum.toLong() shl 0)).toInt()
                        matrix[y][x + 1] = ((0xff000000) or (redSum.toLong() shl 16) or (greenSum.toLong() shl 8) or (blueSum.toLong() shl 0)).toInt()
                        matrix[y + 1][x - 1] = ((0xff000000) or (redSum.toLong() shl 16) or (greenSum.toLong() shl 8) or (blueSum.toLong() shl 0)).toInt()
                        matrix[y + 1][x] = ((0xff000000) or (redSum.toLong() shl 16) or (greenSum.toLong() shl 8) or (blueSum.toLong() shl 0)).toInt()
                        matrix[y + 1][x + 1] = ((0xff000000) or (redSum.toLong() shl 16) or (greenSum.toLong() shl 8) or (blueSum.toLong() shl 0)).toInt()
                        for (row in 0 until height){
                            for (column in 0 until width) {
                                newBitmapPixelsArray[(row * width) + column] = matrix[row][column]
                            }
                        }
                        newBitmap.setPixels(newBitmapPixelsArray, 0, width, 0, 0, width, height)
                        mainImage.setImageBitmap(newBitmap)
//                        oldBittmapPixelsArray[x.toInt() * y.toInt()] =

                    }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })
    }
}