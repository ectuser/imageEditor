package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import kotlin.math.roundToInt
import android.graphics.ColorMatrixColorFilter
import android.graphics.ColorFilter
import android.graphics.Matrix
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*


class EditImage{
    fun returnImage(mainImage: ImageView, savedBitmap: Bitmap){
        mainImage.setImageBitmap(savedBitmap)
    }
    fun scale(mainImage: ImageView, selected: String, initialHeight: Int) {
        val scaleCoefficient = selected.removeRange(selected.length - 1, selected.length).toDouble() / 100
        val params = mainImage.layoutParams
        params.height = (initialHeight * scaleCoefficient).roundToInt()
        mainImage.layoutParams = params
    }
    fun filter(mainImage: ImageView, number : Int){
        var oldBitmap = (mainImage.drawable as BitmapDrawable).bitmap
        val height = oldBitmap.height
        val width = oldBitmap.width
        var oldBittmapPixelsArray = IntArray(width * height)
        var newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        var newBitmapPixelsArray = oldBittmapPixelsArray
        oldBitmap.getPixels(oldBittmapPixelsArray, 0, width, 0, 0, width, height)

        if (number == 1) {negative(oldBittmapPixelsArray, newBitmapPixelsArray)}
        newBitmap.setPixels(newBitmapPixelsArray, 0, width, 0, 0, width, height)
        mainImage.setImageBitmap(newBitmap)
    }
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
//            var r =
        }
    }
    fun whiteBlack(mainImage: ImageView){
        val colorMatrix = floatArrayOf(
            0.33f, 0.33f, 0.33f, 0f, 1F,
            0.33f, 0.33f, 0.33f, 0f, 1F,
            0.33f, 0.33f, 0.33f, 0f, 1F,
            0f, 0f, 0f, 1f, 0f
        )
        val colorFilter = ColorMatrixColorFilter(colorMatrix)
        mainImage.setColorFilter(colorFilter)
    }
    fun negative(mainImage: ImageView){
        val colorMatrix = floatArrayOf(1f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 1f, 0f)
        val colorFilter = ColorMatrixColorFilter(colorMatrix)
        mainImage.setColorFilter(colorFilter)
    }

    fun rotateImage(mainImage: ImageView){
        val matrix = Matrix()

        matrix.postRotate(-90F)

        val bitmapOrg = (mainImage.drawable as BitmapDrawable).bitmap
        val scaledBitmap = Bitmap.createScaledBitmap(bitmapOrg, bitmapOrg.width, bitmapOrg.height, true)

        val rotatedBitmap =
            Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true)
        mainImage.setImageBitmap(rotatedBitmap)
    }
    @SuppressLint("ClickableViewAccessibility")
    fun blur(mainImage: ImageView, textView: TextView){
        mainImage.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        var x = event.x
                        var y = event.y
                        var eps = 30F

                        

                        textView.text = x.toString()
                    }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })
    }
}