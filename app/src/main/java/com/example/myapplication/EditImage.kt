package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import kotlin.math.roundToInt
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class EditImage(BTMP: Bitmap) {
    var BACK_BITMAP = BTMP
    fun returnImage(mainImage: ImageView, savedBitmap: Bitmap){
        mainImage.setImageBitmap(savedBitmap)
    }
    fun enlarge(mainImage: ImageView, selected: String, initialHeight: Int) {
        val scaleCoefficient = selected.removeRange(selected.length - 1, selected.length).toDouble() / 100
        val params = mainImage.layoutParams
        params.height = (initialHeight * scaleCoefficient).roundToInt()
        mainImage.layoutParams = params
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
        var oldBitmap = (mainImage.drawable as BitmapDrawable).bitmap // создаем битмап из imageview
        val height = oldBitmap.height // высота картинки и битмапа
        val width = oldBitmap.width // ширина
        var oldBittmapPixelsArray = IntArray(width * height) // массив его пикселей (пока просто массив, не двумерный, и пока он пустой, то есть ничего не содержит, т.е. пока это просто массив длиной width * height)
        var newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)  // ноздаем новый битмап (пока пустой, но шириной и высотой такой же, как и прошлый)
        var newBitmapPixelsArray = oldBittmapPixelsArray // создаем массив пикселей нового битмапа
        oldBitmap.getPixels(oldBittmapPixelsArray, 0, width, 0, 0, width, height) // заполняем старый массив пикселей пикселями из старого битмапа
// а тут мы первращаем массив пикселей в матрицу пикселей
        val array = oldBittmapPixelsArray // массив-донор - массив пикселей старого битмапа
        val matrix = Array(height) { IntArray(width) } //будущая матрица
        var count = 0
        for (i in matrix.indices) {
            for (j in 0 until matrix[i].size) {
                matrix[i][j] = array[count++] //перенос элементов из донора в матрицу
            }
        }
        val newMatrix = Array(width) { IntArray(height) }

        // ALGORITHM BEGIN
        var x0 : Int = (width / 2)
        var y0 : Int = (height / 2)
        var rotationDeg = 90.0
        var rotationRad = Math.toRadians(rotationDeg)

        for (y1 in matrix.indices) {
            for (x1 in 0 until matrix[y1].size) {
                var el = matrix[y1][x1]
                var x2 = cos(rotationRad) * (x1 - x0) - sin(rotationRad) * (y1 - y0) + x0
                var y2 = sin(rotationRad) * (x1 - x0) + cos(rotationRad) * (y1 - y0) + y0
                newMatrix[y2.toInt()][x2.toInt()] = el
            }
        }



        // ALGORITHM END

//        for (row in 0 until height){
//            for (column in 0 until width) {
//                newBitmapPixelsArray[(row * width) + column] = matrix[row][column] // забиваем новый массив пикселей по формуле соответствующими значениями из матрицы
//            }
//        }
//        newBitmap.setPixels(newBitmapPixelsArray, 0, width, 0, 0, width, height) // добавляем полученные пиксели в новый битмап
//        mainImage.setImageBitmap(newBitmap) // ставим новый битмап в imageview
    }

    // DAMN BLUR
    @SuppressLint("ClickableViewAccessibility")
    fun blur(mainImage: ImageView, coordinates: TextView) {
        mainImage.setOnTouchListener(View.OnTouchListener { _, event ->
            // click coordinates
            var rawX = event.x
            var rawY = event.y

            var oldBitmap = (mainImage.drawable as BitmapDrawable).bitmap
            var height = oldBitmap.height
            var width = oldBitmap.width
            // get bitmap coordinates
            val x = (rawX.toDouble() * (width.toDouble() / mainImage.width.toDouble())).toInt()
            val y = (rawY.toDouble() * (height.toDouble() / mainImage.height.toDouble())).toInt()

//            coordinates.text = "$x | $y"
            var oldBittmapPixelsArray = IntArray(width * height) // empty pixels array
            var newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888) // new bitmap
            var newBitmapPixelsArray = oldBittmapPixelsArray // the same for new bitmap
            oldBitmap.getPixels(oldBittmapPixelsArray, 0, width, 0, 0, width, height) // filling pixels
            var count = 0
            val array = oldBittmapPixelsArray //массив - донор
            val matrix = Array(height) { IntArray(width) } //будущая матрица
            for (i in matrix.indices) {
                for (j in 0 until matrix[i].size) {
                    matrix[i][j] = array[count++] //перенос элементов из донора в матрицу
                }
            }
            var redSum = 0
            var greenSum = 0
            var blueSum = 0

            var eps = ((sqrt(height.toDouble() * width.toDouble())) / (100)).toInt() // ЭТО ОКРЕСТНОСТЬ БЛЮРА (тип радиус), КОРОЧЕ НАДО ПРИДУМАТЬ, как ее менять в зависимости от разрешения

            for (i in y - eps..y + eps){
                for (j in x - eps..x + eps){
                    if (i in 0..(height - 1) && j >= 0 && j < width){
                        var red = 0
                        var green = 0
                        var blue = 0
                        red = matrix[i][j] and 0x00ff0000 shr 16
                        green = matrix[i][j] and 0x0000ff00 shr 8
                        blue = matrix[i][j] and 0x000000ff shr 0
                        redSum += red
                        greenSum += green
                        blueSum += blue
                    }
                }
            }
            redSum /= ((1 + 2 * eps) * (1 + 2 * eps))
            greenSum /= ((1 + 2 * eps) * (1 + 2 * eps))
            blueSum /= ((1 + 2 * eps) * (1 + 2 * eps))
            for (i in y - eps..y + eps){
                for (j in x - eps..x + eps){
                    if (i in 0..(height - 1) && j >= 0 && j < width){
                        matrix[i][j] = ((0xff000000) or (redSum.toLong() shl 16) or (greenSum.toLong() shl 8) or (blueSum.toLong() shl 0)).toInt()
                    }
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