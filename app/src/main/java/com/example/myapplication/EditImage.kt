package com.example.myapplication

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.roundToInt

class EditImage {
    fun scale(mainImage: ImageView, selected: String, initialHeight: Int) {
        val scaleCoefficient = selected.removeRange(selected.length - 1, selected.length).toDouble() / 100
        val params = mainImage.layoutParams
        params.height = (initialHeight * scaleCoefficient).roundToInt()
        mainImage.layoutParams = params
    }
    fun filterClick(firstButton : Button, secondButton: Button, thirdButton: Button, cameraButton: ImageButton, imageButton: ImageButton){
        if (firstButton.visibility == View.INVISIBLE){
            cameraButton.visibility = View.INVISIBLE
            imageButton.visibility = View.INVISIBLE
            firstButton.visibility = View.VISIBLE
            secondButton.visibility = View.VISIBLE
            thirdButton.visibility = View.VISIBLE
        }
        else {
            firstButton.visibility = View.INVISIBLE
            secondButton.visibility = View.INVISIBLE
            thirdButton.visibility = View.INVISIBLE
            cameraButton.visibility = View.VISIBLE
            imageButton.visibility = View.VISIBLE
        }
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
//        bitmap.setPixel(50, 50, Color.RED)
    }
    private fun negative(oldBittmapPixelsArray: IntArray, newBitmapPixelsArray: IntArray){
        for (i in oldBittmapPixelsArray.indices){
            newBitmapPixelsArray[i] = (0xff000000 or (0xffffffff - oldBittmapPixelsArray[i])).toInt()
        }
    }
}