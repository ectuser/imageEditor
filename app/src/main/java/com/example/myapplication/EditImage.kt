package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import kotlin.math.roundToInt
import android.view.MotionEvent
import android.view.View
import android.widget.Toast


class EditImage {
    fun returnImage(mainImage: ImageView, savedBitmap: Bitmap){
        mainImage.setImageBitmap(savedBitmap)
    }
    fun enlarge(mainImage: ImageView, selected: String, initialHeight: Int) {
        val scaleCoefficient = selected.removeRange(selected.length - 1, selected.length).toDouble() / 100
        val params = mainImage.layoutParams
        params.height = (initialHeight * scaleCoefficient).roundToInt()
        mainImage.layoutParams = params
    }

    fun scale05x(context: Context, mainImage: ImageView) {
        val oldBitmap = (mainImage.drawable as BitmapDrawable).bitmap
        val height = oldBitmap.height
        val width = oldBitmap.width
        val oldBitmapPixelsArray = IntArray(width * height)
        val newBitmap = Bitmap.createBitmap(width / 2, height / 2, Bitmap.Config.ARGB_8888)
        val newBitmapPixelsArray = IntArray((width / 2) * (height / 2))
        oldBitmap.getPixels(oldBitmapPixelsArray, 0, width, 0, 0, width, height)

        if (width % 2 == 0) {
            for (i in newBitmapPixelsArray.indices) {
                var redSum = (oldBitmapPixelsArray[i * 2 + (i / (width / 2)) * width] and 0x00ff0000 shr 16) +
                        (oldBitmapPixelsArray[i * 2 + (i / (width / 2)) * width + 1] and 0x00ff0000 shr 16) +
                        (oldBitmapPixelsArray[i * 2 + (i / (width / 2)) * width + width] and 0x00ff0000 shr 16) +
                        (oldBitmapPixelsArray[i * 2 + (i / (width / 2)) * width + width + 1] and 0x00ff0000 shr 16)
                var greenSum = (oldBitmapPixelsArray[i * 2 + (i / (width / 2)) * width] and 0x0000ff00 shr 8) +
                        (oldBitmapPixelsArray[i * 2 + (i / (width / 2)) * width + 1] and 0x0000ff00 shr 8) +
                        (oldBitmapPixelsArray[i * 2 + (i / (width / 2)) * width + width] and 0x0000ff00 shr 8) +
                        (oldBitmapPixelsArray[i * 2 + (i / (width / 2)) * width + width + 1] and 0x0000ff00 shr 8)
                var blueSum = (oldBitmapPixelsArray[i * 2 + (i / (width / 2)) * width] and 0x000000ff shr 0) +
                        (oldBitmapPixelsArray[i * 2 + (i / (width / 2)) * width + 1] and 0x000000ff shr 0) +
                        (oldBitmapPixelsArray[i * 2 + (i / (width / 2)) * width + width] and 0x000000ff shr 0) +
                        (oldBitmapPixelsArray[i * 2 + (i / (width / 2)) * width + width + 1] and 0x000000ff shr 0)

                redSum /= 4
                greenSum /= 4
                blueSum /= 4

                newBitmapPixelsArray[i] = ((0xff000000) or (redSum.toLong() shl 16) or
                        (greenSum.toLong() shl 8) or (blueSum.toLong() shl 0)).toInt()
            }
        }
        else {
            for (i in newBitmapPixelsArray.indices) {
                var redSum = (oldBitmapPixelsArray[i * 2 + (i / (width / 2)) * (width + 1)] and 0x00ff0000 shr 16) +
                        (oldBitmapPixelsArray[i * 2 + (i / (width / 2)) * (width + 1) + 1] and 0x00ff0000 shr 16) +
                        (oldBitmapPixelsArray[i * 2 + (i / (width / 2)) * (width + 1) + (width + 1)] and 0x00ff0000 shr 16) +
                        (oldBitmapPixelsArray[i * 2 + (i / (width / 2)) * (width + 1) + (width + 1) + 1] and 0x00ff0000 shr 16)
                var greenSum = (oldBitmapPixelsArray[i * 2 + (i / (width / 2)) * (width + 1)] and 0x0000ff00 shr 8) +
                        (oldBitmapPixelsArray[i * 2 + (i / (width / 2)) * (width + 1) + 1] and 0x0000ff00 shr 8) +
                        (oldBitmapPixelsArray[i * 2 + (i / (width / 2)) * (width + 1) + (width + 1)] and 0x0000ff00 shr 8) +
                        (oldBitmapPixelsArray[i * 2 + (i / (width / 2)) * (width + 1) + (width + 1) + 1] and 0x0000ff00 shr 8)
                var blueSum = (oldBitmapPixelsArray[i * 2 + (i / (width / 2)) * (width + 1)] and 0x000000ff shr 0) +
                        (oldBitmapPixelsArray[i * 2 + (i / (width / 2)) * (width + 1) + 1] and 0x000000ff shr 0) +
                        (oldBitmapPixelsArray[i * 2 + (i / (width / 2)) * (width + 1) + (width + 1)] and 0x000000ff shr 0) +
                        (oldBitmapPixelsArray[i * 2 + (i / (width / 2)) * (width + 1) + (width + 1) + 1] and 0x000000ff shr 0)

                redSum /= 4
                greenSum /= 4
                blueSum /= 4

                newBitmapPixelsArray[i] = ((0xff000000) or (redSum.toLong() shl 16) or
                        (greenSum.toLong() shl 8) or (blueSum.toLong() shl 0)).toInt()
            }
        }

        newBitmap.setPixels(newBitmapPixelsArray, 0, width / 2, 0, 0, width / 2, height / 2)
        mainImage.setImageBitmap(newBitmap)
        Toast.makeText(context, "${newBitmap.width} x ${newBitmap.height}", Toast.LENGTH_SHORT).show()
    }

    fun scale2x(context: Context, mainImage: ImageView) {
        try {
            val oldBitmap = (mainImage.drawable as BitmapDrawable).bitmap
            val height = oldBitmap.height
            val width = oldBitmap.width
            val oldBitmapPixelsArray = IntArray(width * height)
            val newBitmap = Bitmap.createBitmap(width * 2, height * 2, Bitmap.Config.ARGB_8888)
            val newBitmapPixelsArray = IntArray(width * 2 * height * 2)
            oldBitmap.getPixels(oldBitmapPixelsArray, 0, width, 0, 0, width, height)

            for (i in oldBitmapPixelsArray.indices) {
                newBitmapPixelsArray[i * 2 + (i / width) * width * 2] = oldBitmapPixelsArray[i]
                newBitmapPixelsArray[i * 2 + (i / width) * width * 2 + 1] = oldBitmapPixelsArray[i]
                newBitmapPixelsArray[i * 2 + (i / width) * width * 2 + width * 2] = oldBitmapPixelsArray[i]
                newBitmapPixelsArray[i * 2 + (i / width) * width * 2 + width * 2 + 1] = oldBitmapPixelsArray[i]

                // smoothing
                if (i >= width && i <= width * (height - 1) && i % width != 0 && i % width != width - 1) {
                    val a = oldBitmapPixelsArray[i - width]
                    val b = oldBitmapPixelsArray[i + 1]
                    val c = oldBitmapPixelsArray[i - 1]
                    val d = oldBitmapPixelsArray[i + width]

                    if (c == a && c != d && a != b) {
                        newBitmapPixelsArray[i * 2 + (i / width) * width * 2] = a
                    }
                    if (a == b && a != c && b != d) {
                        newBitmapPixelsArray[i * 2 + (i / width) * width * 2 + 1] = b
                    }
                    if (d == c && d != b && c != a) {
                        newBitmapPixelsArray[i * 2 + (i / width) * width * 2 + width * 2] = c
                    }
                    if (b == d && b != a && d != c) {
                        newBitmapPixelsArray[i * 2 + (i / width) * width * 2 + width * 2 + 1] = d
                    }
                }
            }

            newBitmap.setPixels(newBitmapPixelsArray, 0, width * 2, 0, 0, width * 2, height * 2)
            mainImage.setImageBitmap(newBitmap)
            Toast.makeText(context, "${newBitmap.width} x ${newBitmap.height}", Toast.LENGTH_SHORT).show()
        }
        catch (e: OutOfMemoryError) {
            Toast.makeText(context, "The image is too large", Toast.LENGTH_SHORT).show()
        }
    }

    fun scale3x(context: Context, mainImage: ImageView) {
        try {
            val oldBitmap = (mainImage.drawable as BitmapDrawable).bitmap
            val height = oldBitmap.height
            val width = oldBitmap.width
            val oldBitmapPixelsArray = IntArray(width * height)
            val newBitmap = Bitmap.createBitmap(width * 3, height * 3, Bitmap.Config.ARGB_8888)
            val newBitmapPixelsArray = IntArray(width * 3 * height * 3)
            oldBitmap.getPixels(oldBitmapPixelsArray, 0, width, 0, 0, width, height)

            for (i in oldBitmapPixelsArray.indices) {
                newBitmapPixelsArray[i * 3 + (i / width) * width * 6] = oldBitmapPixelsArray[i]
                newBitmapPixelsArray[i * 3 + (i / width) * width * 6 + 1] = oldBitmapPixelsArray[i]
                newBitmapPixelsArray[i * 3 + (i / width) * width * 6 + 2] = oldBitmapPixelsArray[i]
                newBitmapPixelsArray[i * 3 + (i / width) * width * 6 + width * 3] = oldBitmapPixelsArray[i]
                newBitmapPixelsArray[i * 3 + (i / width) * width * 6 + width * 3 + 1] = oldBitmapPixelsArray[i]
                newBitmapPixelsArray[i * 3 + (i / width) * width * 6 + width * 3 + 2] = oldBitmapPixelsArray[i]
                newBitmapPixelsArray[i * 3 + (i / width) * width * 6 + width * 3 * 2] = oldBitmapPixelsArray[i]
                newBitmapPixelsArray[i * 3 + (i / width) * width * 6 + width * 3 * 2 + 1] = oldBitmapPixelsArray[i]
                newBitmapPixelsArray[i * 3 + (i / width) * width * 6 + width * 3 * 2 + 2] = oldBitmapPixelsArray[i]

                // smoothing
                if (i >= width && i <= width * (height - 1) && i % width != 0 && i % width != width - 1) {
                    val a = oldBitmapPixelsArray[i - width - 1]
                    val b = oldBitmapPixelsArray[i - width]
                    val c = oldBitmapPixelsArray[i - width + 1]
                    val d = oldBitmapPixelsArray[i - 1]
                    val e = oldBitmapPixelsArray[i]
                    val f = oldBitmapPixelsArray[i + 1]
                    val g = oldBitmapPixelsArray[i + width - 1]
                    val h = oldBitmapPixelsArray[i + width]
                    val j = oldBitmapPixelsArray[i + width + 1]

                    if (d == b && d != h && b != f) {
                        newBitmapPixelsArray[i * 3 + (i / width) * width * 6] = d
                    }
                    if ((d == b && d != h && b != f && e != c) ||
                        (b == f && b != d && f != h && e != a)) {
                        newBitmapPixelsArray[i * 3 + (i / width) * width * 6 + 1] = b
                    }
                    if (b == f && b != d && f != h) {
                        newBitmapPixelsArray[i * 3 + (i / width) * width * 6 + 2] = f
                    }
                    if ((h == d && h != f && d != b && e != a) ||
                        (d == b && d != h && b != f && e != g)) {
                        newBitmapPixelsArray[i * 3 + (i / width) * width * 6 + width * 3] = d
                    }
                    if ((b == f && b != d && f != h && e != j) ||
                        (f == h && f != b && h != d && e != c)) {
                        newBitmapPixelsArray[i * 3 + (i / width) * width * 6 + width * 3 + 2] = f
                    }
                    if (h == d && h != f && d != b) {
                        newBitmapPixelsArray[i * 3 + (i / width) * width * 6 + width * 3 * 2] = d
                    }
                    if ((f == h && f != b && h != d && e != g) ||
                        (h == d && h != f && d != b && e != j)) {
                        newBitmapPixelsArray[i * 3 + (i / width) * width * 6 + width * 3 * 2 + 1] = h
                    }
                    if (f == h && f != b && h != d) {
                        newBitmapPixelsArray[i * 3 + (i / width) * width * 6 + width * 3 * 2 + 2] = f
                    }
                }
            }

            newBitmap.setPixels(newBitmapPixelsArray, 0, width * 3, 0, 0, width * 3, height * 3)
            mainImage.setImageBitmap(newBitmap)
            Toast.makeText(context, "${newBitmap.width} x ${newBitmap.height}", Toast.LENGTH_SHORT).show()
        }
        catch (e: OutOfMemoryError) {
            Toast.makeText(context, "The image is too large", Toast.LENGTH_SHORT).show()
        }
    }

    // CALL FILTERS FUNCTION
    fun filter(mainImage: ImageView, number : Int){
        var oldBitmap = (mainImage.drawable as BitmapDrawable).bitmap
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
    fun blur(mainImage: ImageView){
        mainImage.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        var x = event.x
                        var y = event.y
//                        textView.text = "$x | $y"
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
                        matrix
//                        for (row in 0..height)
//                            for (column in 0..width){
//                                newBitmapPixelsArray[(row * width) + column] = matrix[row][column]
//                            }
//                        newBitmap.setPixels(newBitmapPixelsArray, 0, width, 0, 0, width, height)
//                        mainImage.setImageBitmap(newBitmap)
//                        oldBittmapPixelsArray[x.toInt() * y.toInt()] =

                    }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })
    }
}