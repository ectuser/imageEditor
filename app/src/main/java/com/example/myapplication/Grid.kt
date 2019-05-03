package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.View
import android.widget.*

class Grid(private var clicksCount: Int = 0) {
    @SuppressLint("SetTextI18n")
    // Creating the grid
    fun createGrid(context: Context, intent: Intent, gridTable: TableLayout) {
        gridTable.setBackgroundColor(Color.BLACK)

        for (i in 0 until intent.getIntExtra("width", 2)) {
            val tableRow = TableRow(context)

            for (j in 0 until intent.getIntExtra("height", 2)) {
                val curCell = Button(context)

                // Cells' appearance
                val params = TableRow.LayoutParams(100, 100)
                params.setMargins(1, 1, 1, 1)
                curCell.layoutParams = params
                curCell.setBackgroundColor(Color.WHITE)
                curCell.text = "$i:$j"
                curCell.setTextColor(ContextCompat.getColor(context, R.color.secondaryText))
                curCell.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11F)

                curCell.setOnClickListener {
                    setEnd(curCell, context, intent, gridTable)
                }
                tableRow.addView(curCell, j)
            }

            gridTable.addView(tableRow, i)
        }
    }

    // Setting start and finish cells
    private fun setEnd(view: View, context: Context, intent: Intent, gridTable: TableLayout) {
        if (clicksCount < 2) {
            var color = Color.TRANSPARENT
            val background = view.background
            if (background is ColorDrawable) {
                color = background.color
            }

            if (color == Color.WHITE) {
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.endCell))
                clicksCount++
            }
        }
        if (clicksCount == 2) {
            for (i in 0 until intent.getIntExtra("width", 2)) {
                val curRow = gridTable.getChildAt(i) as TableRow
                for (j in 0 until intent.getIntExtra("height", 2)){
                    val curCell = curRow.getChildAt(j)
                    curCell.setOnClickListener { switchObstacle(curCell) }
                }
            }
        }
    }

    // Creating or removing obstacles
    private fun switchObstacle(view: View) {
        val color: Int
        val background = view.background as ColorDrawable
        color = background.color

        if (color == Color.WHITE) {
            view.setBackgroundColor(Color.BLACK)
        }
        else if (color == Color.BLACK) {
            view.setBackgroundColor(Color.WHITE)
        }
    }

    // Clearing the previous path
    fun clearPath(intent: Intent, gridTable: TableLayout) {
        for (i in 0 until intent.getIntExtra("width", 2)) {
            val curRow = gridTable.getChildAt(i) as TableRow

            for (j in 0 until intent.getIntExtra("height", 2)){
                val curCell = curRow.getChildAt(j)

                val color: Int
                val background = curCell.background as ColorDrawable
                color = background.color

                if (color == Color.parseColor("#FFFACD")) {
                    curCell.setBackgroundColor(Color.WHITE)
                }
            }
        }
    }

    // Making the whole grid white
    fun clearGrid(context: Context, intent: Intent, gridTable: TableLayout) {
        for (i in 0 until intent.getIntExtra("width", 2)) {
            val curRow = gridTable.getChildAt(i) as TableRow

            for (j in 0 until intent.getIntExtra("height", 2)){
                val curCell = curRow.getChildAt(j)

                val color: Int
                val background = curCell.background as ColorDrawable
                color = background.color

                if (color != Color.WHITE) {
                    curCell.setBackgroundColor(Color.WHITE)
                }

                curCell.setOnClickListener {
                    setEnd(curCell, context, intent, gridTable)
                }
            }
        }

        clicksCount = 0
    }

    // The damn algorithm itself
    fun aStar (context: Context, intent: Intent, gridTable: TableLayout, heuristicSpinner: Spinner,
               movementOption1: RadioButton, movementOption3: RadioButton) {

        if (clicksCount != 2) {
            Toast.makeText(context, "Set start and finish", Toast.LENGTH_SHORT).show()
        }
        else {
            val n = intent.getIntExtra("width", 2)
            val m = intent.getIntExtra("height", 2)
            val matrix = Array(n) {Array(m) {Cell()}}
            var nextCell = matrix[0][0]
            var x1 = 0
            var y1 = 0
            var x2 = 0
            var y2 = 0
            var numOfOpenedCells = 1
            var curMinF: Double
            var curF: Double
            val supplements = arrayOf(-1, 0, 0, 1, 1, 0, 0, -1, -1, -1, -1, 1, 1, 1, 1, -1)
            var tmp: Int
            var pathLength: Int

            // Searching for start and finish, setting some parameters, clearing the previous path
            var k = 0
            for (i in 0 until n) {
                val curRow = gridTable.getChildAt(i) as TableRow

                for (j in 0 until m){
                    val curCell = curRow.getChildAt(j)

                    val color: Int
                    val background = curCell.background as ColorDrawable
                    color = background.color
                    // Getting coordinates of the end cells
                    if (color == Color.parseColor("#F0E68C")) {
                        if (k++ == 0) {
                            x1 = i
                            y1 = j
                        }
                        else {
                            x2 = i
                            y2 = j
                        }
                    }
                    // Clearing the previous path
                    if (color == Color.parseColor("#FFFACD")) {
                        curCell.setBackgroundColor(Color.WHITE)
                    }
                    // Marking obstacles
                    if (color == Color.BLACK) {
                        matrix[i][j].isFree = false
                    }

                    matrix[i][j].x = i
                    matrix[i][j].y = j
                }
            }

            // Values for the starting cell
            matrix[x1][y1].g = 0
            matrix[x1][y1].h = matrix[x1][y1].hCalculate(x2.toDouble(), y2.toDouble(), heuristicSpinner)
            matrix[x1][y1].f = matrix[x1][y1].g + matrix[x1][y1].h
            matrix[x1][y1].isOpened = true


            // The algorithm's main loop
            while (numOfOpenedCells > 0) {
                numOfOpenedCells = 0
                curMinF = 100.0
                // Choosing next cell to check its successors
                for (i in 0 until n) {
                    for (j in 0 until m){
                        if (matrix[i][j].isOpened && matrix[i][j].f < curMinF) {
                            nextCell = matrix[i][j]
                            numOfOpenedCells++
                        }
                    }
                }

                // How many successors we'll check
                tmp = if (movementOption1.isChecked) {
                    8
                } else {
                    16
                }

                for (i in 0 until tmp step 2) {
                    // The cell is inside the grid and isn't an obstacle
                    if (nextCell.x + supplements[i] in 0 until n &&
                        nextCell.y + supplements[i + 1] in 0 until m &&
                        matrix[nextCell.x + supplements[i]][nextCell.y + supplements[i + 1]].isFree &&
                        !(i >= 8 && movementOption3.isChecked &&
                                !(matrix[nextCell.x][nextCell.y + supplements[i + 1]].isFree &&
                                matrix[nextCell.x + supplements[i]][nextCell.y].isFree))) {

                        curF = nextCell.g + 1 +
                                matrix[nextCell.x + supplements[i]][nextCell.y + supplements[i + 1]]
                                    .hCalculate(x2.toDouble(), y2.toDouble(), heuristicSpinner)

                        // This successor isn't in the opened/closed list with a smaller "f"
                        if (!(matrix[nextCell.x + supplements[i]][nextCell.y + supplements[i + 1]].isOpened &&
                                    matrix[nextCell.x + supplements[i]][nextCell.y + supplements[i + 1]].f <= curF) &&
                            !(matrix[nextCell.x + supplements[i]][nextCell.y + supplements[i + 1]].isClosed &&
                                    matrix[nextCell.x + supplements[i]][nextCell.y + supplements[i + 1]].f <= curF)) {

                            // Setting parameters for this successor:

                            matrix[nextCell.x + supplements[i]][nextCell.y + supplements[i + 1]].parentX = nextCell.x
                            matrix[nextCell.x + supplements[i]][nextCell.y + supplements[i + 1]].parentY = nextCell.y

                            matrix[nextCell.x + supplements[i]][nextCell.y + supplements[i + 1]].g =
                                (matrix[matrix[nextCell.x + supplements[i]][nextCell.y + supplements[i + 1]].parentX]
                                [matrix[nextCell.x + supplements[i]][nextCell.y + supplements[i + 1]].parentY]).g + 1

                            matrix[nextCell.x + supplements[i]][nextCell.y + supplements[i + 1]].h =
                                matrix[nextCell.x + supplements[i]][nextCell.y + supplements[i + 1]].
                                    hCalculate(x2.toDouble(), y2.toDouble(), heuristicSpinner)

                            matrix[nextCell.x + supplements[i]][nextCell.y + supplements[i + 1]].f = curF

                            matrix[nextCell.x + supplements[i]][nextCell.y + supplements[i + 1]].isOpened = true
                        }
                    }
                }

                // Moving checked cell into the closed list
                matrix[nextCell.x][nextCell.y].isOpened = false
                matrix[nextCell.x][nextCell.y].isClosed = true
            }

            // Visualization
            if (matrix[x2][y2].parentX == -1 && matrix[x2][y2].parentY == -1) {
                Toast.makeText(context, "The path doesn't exist", Toast.LENGTH_SHORT).show()
            }
            else {
                nextCell = matrix[matrix[x2][y2].parentX][matrix[x2][y2].parentY]
                pathLength = 2

                while (!(nextCell.x == x1 && nextCell.y == y1)) {
                    val curRow = gridTable.getChildAt(nextCell.x) as TableRow
                    val curCell = curRow.getChildAt(nextCell.y)

                    curCell.setBackgroundColor(ContextCompat.getColor(context, R.color.pathCell))
                    nextCell = matrix[nextCell.parentX][nextCell.parentY]
                    pathLength++
                }

                Toast.makeText(context, "Path length: $pathLength", Toast.LENGTH_SHORT).show()
            }
        }
    }
}