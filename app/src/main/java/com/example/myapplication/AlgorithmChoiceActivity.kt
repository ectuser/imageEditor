package com.example.myapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class AlgorithmChoiceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_algorithm_choice)
    }

    // DMITRY'S FUNCTION TO FUCK SOME BITCHES USING A*
    fun transitToAStar(@Suppress("UNUSED_PARAMETER") view: View) {
        val intent = Intent(this, AStarMainActivity::class.java)
        startActivity(intent)
    }

    // INTERPOLATION
    fun transitToInterpolation(@Suppress("UNUSED_PARAMETER") view: View) {
        val intent = Intent(this, InterpolationActivity::class.java)
        startActivity(intent)
    }
}
