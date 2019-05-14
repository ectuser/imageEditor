package com.example.myapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_astar_main.*

class AStarMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_astar_main)
    }

    fun transitToGrid(@Suppress("UNUSED_PARAMETER") view: View) {
        // Fool-proof included
        if (heightInput.text!!.isNotEmpty() && widthInput.text!!.isNotEmpty()) {
            if (heightInput.text.toString().toInt() >= 2 && widthInput.text.toString().toInt() >= 2 &&
                heightInput.text.toString().toInt() <= 30 && widthInput.text.toString().toInt() <= 30) {
                val intent = Intent(this, AStarGridActivity::class.java).apply {
                    putExtra("width", heightInput.text.toString().toInt())
                    putExtra("height", widthInput.text.toString().toInt())
                }
                startActivity(intent)
            }
            else {
                Toast.makeText(this, "The values must be between 2 and 30", Toast.LENGTH_SHORT).show()
            }
        }
        else {
            Toast.makeText(this, "Fill in both fields", Toast.LENGTH_SHORT).show()
        }
    }
}
