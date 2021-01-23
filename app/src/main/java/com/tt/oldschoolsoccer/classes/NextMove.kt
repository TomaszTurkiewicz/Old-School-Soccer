package com.tt.oldschoolsoccer.classes

class NextMove(private var direction:Int? = null,
                private var distance:Int? = null ) {

    fun setNextMove(direction: Int,distance: Int){
        this.direction = direction
        this.distance = distance
    }

    fun isNextMoveSet():Boolean{
        return this.direction != null
    }

    fun getDistance():Int?{
        return this.distance
    }

    fun getDirection():Int?{
        return this.direction
    }
}