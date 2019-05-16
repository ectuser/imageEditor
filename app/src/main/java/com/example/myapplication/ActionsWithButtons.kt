package com.example.myapplication

import android.view.View
import android.widget.Button
import android.widget.ImageButton

class ActionsWithButtons {
    fun filterClick(firstButton : Button, secondButton: Button, thirdButton: Button, cameraButton: ImageButton, imageButton: ImageButton, backButton: Button, returnButton: Button){
        if (firstButton.visibility == View.INVISIBLE){
            cameraButton.visibility = View.INVISIBLE
            imageButton.visibility = View.INVISIBLE
            firstButton.visibility = View.VISIBLE
            secondButton.visibility = View.VISIBLE
            thirdButton.visibility = View.VISIBLE
            backButton.visibility = View.VISIBLE
            returnButton.visibility = View.VISIBLE

        }
        else {
            firstButton.visibility = View.INVISIBLE
            secondButton.visibility = View.INVISIBLE
            thirdButton.visibility = View.INVISIBLE
            backButton.visibility = View.INVISIBLE
            returnButton.visibility = View.INVISIBLE
            cameraButton.visibility = View.VISIBLE
            imageButton.visibility = View.VISIBLE
        }
    }
}