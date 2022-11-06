package com.kongregate.mobile.burritob.game

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.kongregate.mobile.burritob.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameUI(
    var contextttt: Context,
    var gameTask: GamerInterface,
    val myCar: Int,
    val enemyLogoList: List<Int>
) : View(contextttt) {

    private var myPaint: Paint? = null
    private var myPaintBonus: Paint? = null
    private var myPaintLifeText: Paint? = null
    private var speed = 8
    private var score = 0
    private var time = 0
    private var myCarPosition = 0

    private var healthPaint = Paint()
    private var life = 3


    var timer = System.currentTimeMillis()

    private var counterSpeed = 0

    private var otherCars = ArrayList<HashMap<String, Any>>()
    private var otherBonus = ArrayList<HashMap<String, Any>>()

    private var vievVidth = 0
    private var vievHeight = 0
    private var textLifeLeft = ""

    var currentEnemyAvatar = enemyLogoList.random()


    init {
        myPaint = Paint()
        healthPaint.color = Color.GREEN
        myPaintBonus = Paint()
        myPaintLifeText = Paint()
        myPaintLifeText!!.textSize = 100f
        myPaintLifeText!!.alpha = 190
        myPaintLifeText!!.color = Color.WHITE
    }

    override fun onDraw(canvas: Canvas?) {

        canvas!!.drawText(textLifeLeft, vievVidth / 2f - 220f, vievHeight / 2f, myPaintLifeText!!)


        super.onDraw(canvas)

        vievVidth = this@GameUI.measuredWidth
        vievHeight = this@GameUI.measuredHeight

        var difff = System.currentTimeMillis() - timer

        if (time % 700 < 10 + speed) {
            val map = HashMap<String, Any>()

            map["lane"] = (0..2).random()
            map["startTime"] = time
            otherCars.add(map)
        }
        time = time + 10 + speed
        val carVidth = vievVidth / 5
        val carHeight = carVidth + 10
        myPaint!!.style = Paint.Style.FILL

        //

        val d = resources.getDrawable(myCar, null)

        d.setBounds(
            myCarPosition * vievVidth / 3 + vievVidth / 15 + 25,
            vievHeight - 2 - carHeight,
            myCarPosition * vievVidth / 3 + vievVidth / 15 + carVidth - 25,
            vievHeight - 2
        )
        d.draw(canvas!!)
        myPaint!!.color = Color.GREEN
        var highScore = 0

        createBonusItem(canvas)


        if (difff >= 5000) {
            currentEnemyAvatar = enemyLogoList.random()
            timer = System.currentTimeMillis()
            speed = speed + 1 + Math.abs(score / 8)

            //here bonus about
            val mapBonus = HashMap<String, Any>()
            mapBonus["lane"] = (0..2).random()
            mapBonus["startTime"] = time
            otherBonus.add(mapBonus)
        }


        for (i in otherCars.indices) {
            try {
                val carX = otherCars[i]["lane"] as Int * vievVidth / 3 + vievVidth / 15
                var carY = time - otherCars[i]["startTime"] as Int
                //                if (counterSpeed%5 ==0 && counterSpeed != 0){
                //                    currentEnemyAvatar = enemyLogoList.random()
                //                }

                val d2 = resources.getDrawable(currentEnemyAvatar, null)

                d2.setBounds(carX + 25, carY - carHeight, carX + carVidth, carY)
                d2.draw(canvas)
                if (otherCars[i]["lane"] as Int == myCarPosition) {
                    if (carY > vievHeight - 2 - carHeight && carY < vievHeight - 2) {
                        Log.d("life", "life is ${life}")

                        when (life) {
                            0 -> {
                                gameTask.closeGame(score)
                            }
                            1 -> {
                                Log.d("life", "i am in 1 life")
                                gameTask.closeGame(score)
                            }
                            2 -> {
                                Log.d("life", "i am in 2 life")
                                textLifeLeft = "1 ♥ left"
                                CoroutineScope(Dispatchers.Main).launch {
                                    delay(800)
                                    textLifeLeft = ""
                                }
                                healthPaint.color = Color.RED
                                life--
                                otherCars.clear()
                            }
                            3 -> {
                                Log.d("life", "i am in 3 life")
                                healthPaint.color = Color.YELLOW
                                textLifeLeft = "2 ♥ left"
                                CoroutineScope(Dispatchers.Main).launch {
                                    delay(800)
                                    textLifeLeft = ""
                                }
                                myPaintLifeText!!.alpha = 200
                                life--
                                otherCars.clear()
                            }
                        }
                    }
                    if (carY > vievHeight + carHeight) {
                        otherCars.removeAt(i)
                        score++
                        counterSpeed++

                        if (score > highScore) {
                            highScore = score
                        }
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        myPaint!!.color = Color.WHITE
        myPaint!!.textSize = 40f
        canvas.drawText("Score: $score", 80f, 80f, myPaint!!)
        canvas.drawText("Speed: $speed", 380f, 80f, myPaint!!)
        canvas!!.drawRect(
            (vievVidth - 200).toFloat(), 40f,
            (vievVidth - 200 + 60 * life).toFloat(), 80f, healthPaint
        )


        invalidate()
    }

    private fun createBonusItem(canvas: Canvas): Boolean {
        val bonusVidth = vievVidth / 5
        val bonusHeight = bonusVidth + 10
        myPaintBonus!!.style = Paint.Style.FILL

        for (i in otherBonus.indices) {

            val bonusX = otherBonus[i]["lane"] as Int * vievVidth / 3 + vievVidth / 15
            var bonusY = time - otherBonus[i]["startTime"] as Int

            val bonus = resources.getDrawable(R.drawable.heartbeat, null)
            bonus.setBounds(bonusX + 25, bonusY - bonusHeight, bonusX + bonusHeight, bonusY)
            bonus.draw(canvas)

            if (otherBonus[i]["lane"] as Int == myCarPosition) {
                if (bonusY > vievHeight - 2 - bonusHeight && bonusY < vievHeight - 2) {
                    when (life) {
                        1 -> {
                            life++
                            textLifeLeft = "2 ♥ left"
                            CoroutineScope(Dispatchers.Main).launch {
                                delay(800)
                                textLifeLeft = ""
                            }
                            healthPaint.color = Color.YELLOW
                            otherBonus.clear()
                            return true
                        }
                        2 -> {
                            life++
                            textLifeLeft = "3 ♥ left"
                            CoroutineScope(Dispatchers.Main).launch {
                                delay(800)
                                textLifeLeft = ""
                            }
                            healthPaint.color = Color.GREEN
                            otherBonus.clear()
                            return true
                        }
                        3 -> {
                            textLifeLeft = "MAX health"
                            CoroutineScope(Dispatchers.Main).launch {
                                delay(800)
                                textLifeLeft = ""
                            }
                            return true
                        }
                    }
                }
            }
        }
        return true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                val x1 = event.x
                if (x1 < vievVidth / 2) {
                    if (myCarPosition > 0) {
                        myCarPosition--
                    }
                }
                if (x1 > vievVidth / 2) {
                    if (myCarPosition < 2) {
                        myCarPosition++
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {

            }
        }

        return true
    }

    private fun snackBarError() {
        Snackbar.make(
            this,
            "There is some error, try again",
            Snackbar.LENGTH_LONG
        ).show()
    }


}

