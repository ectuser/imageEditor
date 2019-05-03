package com.example.myapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_astar_grid.*

class AStarGridActivity : AppCompatActivity() {
    private val grid = Grid()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_astar_grid)

        grid.createGrid(this, intent, gridTable)
    }

    fun runAStar(@Suppress("UNUSED_PARAMETER") view: View) {
        grid.aStar(this, intent, gridTable, heuristicSpinner, movementOption1, movementOption3)
    }

    fun launchClearPath(@Suppress("UNUSED_PARAMETER") view: View) {
        grid.clearPath(intent, gridTable)
    }

    fun launchClearGrid(@Suppress("UNUSED_PARAMETER") view: View) {
        grid.clearGrid(this, intent, gridTable)
    }
}
