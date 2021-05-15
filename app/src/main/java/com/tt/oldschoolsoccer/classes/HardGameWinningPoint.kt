package com.tt.oldschoolsoccer.classes

import android.graphics.Point
import java.lang.Math.abs

class HardGameWinningPoint {
    private val xOne = 6
    private val xTwo = 7
    private val xThree = 8
    private val y = 21


    fun checkDistance(currentX:Int, currentY:Int): Point {
        val point = Point()
        point.y = y - currentY
        val a = abs(xOne-currentX)
        val b = abs(xTwo-currentX)
        val c = abs(xThree-currentX)

        if(a<b||a<c){
            point.x=a
        }else if(b<c){
            point.x=b
        }else{
            point.x=c
        }

        return point
    }

    fun checkDistanceToMyScore(currentX: Int,currentY: Int):Point{
        val point = Point()
        point.y = currentY
        val a = abs(xOne-currentX)
        val b = abs(xTwo-currentX)
        val c = abs(xThree-currentX)

        if(a<b||a<c){
            point.x=a
        }else if(b<c){
            point.x=b
        }else{
            point.x=c
        }
        return point
    }
}