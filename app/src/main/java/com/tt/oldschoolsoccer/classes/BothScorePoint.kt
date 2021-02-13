package com.tt.oldschoolsoccer.classes

import android.graphics.Point

class BothScorePoint (ball:Point,field:GameField){
    var currentBall = Point()
    var availableMoves = AvailableMoves()
    var distanceToMyScore = Point()
    var distanceToScore = Point()
    var checked = false


    init {
        this.currentBall = ball
        this.availableMoves=GameField().checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y])
        this.distanceToScore = HardGameWinningPoint().checkDistance(ball.x,ball.y)
        this.distanceToMyScore = HardGameWinningPoint().checkDistanceToMyScore(ball.x,ball.y)
    }

    fun setCheck(){
        this.checked=true
    }

    fun isChecked():Boolean{
        return this.checked
    }
}