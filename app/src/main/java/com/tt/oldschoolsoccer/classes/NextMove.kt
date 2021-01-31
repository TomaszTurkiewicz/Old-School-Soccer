package com.tt.oldschoolsoccer.classes

import android.graphics.Point

class NextMove(private var direction:Int? = null,
                private var distance:Int? = null,
                private var distancePoint: Point? = null) {



    fun setNextMoveWithDistancePoint(direction:Int,distancePoint:Point){
        this.direction = direction
        this.distancePoint = distancePoint
    }

    fun isNextMoveSet():Boolean{
        return this.direction != null
    }


    fun getDirection():Int?{
        return this.direction
    }

    fun getDistancePoint():Point?{
        return this.distancePoint
    }
}