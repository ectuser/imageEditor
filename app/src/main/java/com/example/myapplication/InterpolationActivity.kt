package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_interpolation.*
import kotlin.math.pow


class InterpolationActivity : AppCompatActivity() {
    private val maxNumberOfPoints = 10
    private val xCoordinates = arrayOfNulls<Float>(maxNumberOfPoints)
    private val yCoordinates = arrayOfNulls<Float>(maxNumberOfPoints)
    // Coefficients for splines
    val a = arrayOfNulls<Float>(maxNumberOfPoints)
    val b = arrayOfNulls<Float>(maxNumberOfPoints)
    val c = arrayOfNulls<Float>(maxNumberOfPoints)
    val d = arrayOfNulls<Float>(maxNumberOfPoints)
    // Defines what to draw
    var drawMode = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interpolation)
        createDrawView()
    }

    // Creates a blank view to draw something
    @SuppressLint("ClickableViewAccessibility")
    fun createDrawView() {
        val background = DrawView(this)
        drawView.addView(background)

        // What to do when the screen is touched
        background.setOnTouchListener { _, event ->
            // If we touched the screen (not moved a finger)
            if (event.action == 0) {
                if (xCoordinates[maxNumberOfPoints - 1] != null) {
                    Toast.makeText(this, "Limit of points exceeded", Toast.LENGTH_SHORT).show()
                }
                for (i in 0 until maxNumberOfPoints) {
                    if (xCoordinates[i] == null) {
                        xCoordinates[i] = event.x
                        yCoordinates[i] = event.y
                        break
                    }
                }
                background.invalidate()
            }

            true
        }
    }

    // What to draw
    inner class DrawView(context: Context) : View(context) {
        @SuppressLint("DrawAllocation")
        override fun onDraw(canvas: Canvas) {
            val paint = Paint()
            paint.setARGB(255, 0, 0, 0)
            paint.strokeWidth = 1f
            paint.style = Paint.Style.FILL
            var numberOfPoints = -1

            // Points
            for (i in 0 until maxNumberOfPoints) {
                if (xCoordinates[i] == null) {
                    break
                }
                else {
                    canvas.drawCircle(xCoordinates[i]!!, yCoordinates[i]!!, 10f, paint)
                    numberOfPoints++
                }
            }

            if (drawMode == 1) {
                // Chain between points
                if (xCoordinates[1] != null) {
                    for (i in 0 until numberOfPoints) {
                        canvas.drawLine(xCoordinates[i]!!, yCoordinates[i]!!,
                            xCoordinates[i + 1]!!, yCoordinates[i + 1]!!, paint)
                    }
                }
                drawMode = 0
            }

            if (drawMode == 2) {
                // Interpolation
                val step = 0.01f
                if (a[1] != null) {
                    for (i in 0 until numberOfPoints) {
                        var tmp = xCoordinates[i]
                        while (tmp!! < xCoordinates[i + 1]!! - step) {
                            canvas.drawLine(tmp, a[i]!! + b[i]!! * (tmp - xCoordinates[i]!!) +
                                    c[i]!! * (tmp - xCoordinates[i]!!).pow(2) +
                                    d[i]!! * (tmp - xCoordinates[i]!!).pow(3),
                                tmp + step, a[i]!! + b[i]!! * (tmp + step - xCoordinates[i]!!) +
                                        c[i]!! * (tmp + step - xCoordinates[i]!!).pow(2) +
                                        d[i]!! * (tmp + step - xCoordinates[i]!!).pow(3), paint)
                            tmp += step
                        }
                    }
                }
                drawMode = 0
            }
        }
    }

    // The chain between points
    fun createChain(@Suppress("UNUSED_PARAMETER") view: View) {
        // Counting the amount of points
        var numberOfPoints = 0
        for (i in 0 until maxNumberOfPoints) {
            if (xCoordinates[i] == null) {
                numberOfPoints = i
                break
            }
        }
        if (numberOfPoints == 0 && xCoordinates[0] != null) {
            numberOfPoints = maxNumberOfPoints
        }

        if (numberOfPoints > 1) {
            sortPoints(numberOfPoints)

            drawMode = 1
            drawView.removeView(drawView.getChildAt(0))
            createDrawView()
        }
        else {
            Toast.makeText(this, "Add more points", Toast.LENGTH_SHORT).show()
        }
    }

    // Interpolation, great and powerful
    fun interpolation(@Suppress("UNUSED_PARAMETER") view: View) {
        // Number of intervals between points
        var n = -1
        while (n < maxNumberOfPoints - 1 && xCoordinates[n + 1] != null) {
            n++
        }

        if (n > 0) {
            sortPoints(n + 1)

            for (i in 0..n) {
                a[i] = yCoordinates[i]
            }

            val mu = arrayOfNulls<Float>(n)
            val h = arrayOfNulls<Float>(n)
            for (i in 0 until n) {
                h[i] = xCoordinates[i + 1]!! - xCoordinates[i]!!
            }

            val beta = arrayOfNulls<Float>(n)
            for (i in 1 until n) {
                beta[i] = 3 * ((a[i + 1]!! - a[i]!!) / h[i]!! - (a[i]!! - a[i - 1]!!) / h[i - 1]!!)
            }

            val l = arrayOfNulls<Float>(n + 1)
            val z = arrayOfNulls<Float>(n + 1)
            l[0] = 1f
            mu[0] = 0f
            z[0] = 0f

            for (i in 1 until n) {
                l[i] = 2 * (xCoordinates[i + 1]!! - xCoordinates[i - 1]!!) - h[i - 1]!! * mu[i - 1]!!
                mu[i] = h[i]!! / l[i]!!
                z[i] = (beta[i]!! - h[i - 1]!! * z[i - 1]!!) / l[i]!!
            }

            l[n] = 1f
            z[n] = 0f
            c[n] = 0f

            for (i in n - 1 downTo 0) {
                c[i] = z[i]!! - mu[i]!! * c[i + 1]!!
                b[i] = (a[i + 1]!! - a[i]!!) / h[i]!! - (h[i]!! * (c[i + 1]!! + 2 * c[i]!!)) / 3
                d[i] = (c[i + 1]!! - c[i]!!) / (3 * h[i]!!)
            }

            drawMode = 2
            drawView.removeView(drawView.getChildAt(0))
            createDrawView()
        }
        else {
            Toast.makeText(this, "Add more points", Toast.LENGTH_SHORT).show()
        }
    }

    // What do you think this function does? Yeah, it clears the screen. You're damn right!
    @SuppressLint("ClickableViewAccessibility")
    fun clear(@Suppress("UNUSED_PARAMETER") view: View) {
        drawView.removeView(drawView.getChildAt(0))
        for (i in 0 until maxNumberOfPoints) {
            xCoordinates[i] = null
            yCoordinates[i] = null
            a[i] = null
            b[i] = null
            c[i] = null
            d[i] = null
        }
        createDrawView()
    }

    // Sorting points from left to right
    private fun sortPoints(numberOfPoints: Int) {
        for (i in 0 until numberOfPoints) {
            for (j in i + 1 until numberOfPoints) {
                if (xCoordinates[i]!! > xCoordinates[j]!!) {
                    xCoordinates[i] = xCoordinates[j].also { xCoordinates[j] = xCoordinates[i] }
                    yCoordinates[i] = yCoordinates[j].also { yCoordinates[j] = yCoordinates[i] }
                }
            }
        }
    }
}
