package com.tt.oldschoolsoccer.classes

import android.graphics.Point
import com.tt.oldschoolsoccer.database.PointOnField
import java.lang.Math.abs

class PointOnFieldToCheck (val incomingDirection:Int?, val currentBall:Point, val previousBall:Point?) {
    val availableMoves = AvailableMoves()
    private var checked = false
    private var nextMove = false

    fun setAvailableMoves(field: GameField){

        if(field.field[currentBall.x][currentBall.y].moveDown==Static.MOVE_AVAILABLE ||
                field.field[currentBall.x][currentBall.y].moveDown==Static.MOVE_CHECKING){
            availableMoves.down=true
        }
        if(field.field[currentBall.x][currentBall.y].moveDownLeft==Static.MOVE_AVAILABLE ||
                field.field[currentBall.x][currentBall.y].moveDownLeft==Static.MOVE_CHECKING){
            availableMoves.downLeft=true
        }
        if(field.field[currentBall.x][currentBall.y].moveDownRight==Static.MOVE_AVAILABLE ||
                field.field[currentBall.x][currentBall.y].moveDownRight==Static.MOVE_CHECKING){
            availableMoves.downRight=true
        }
        if(field.field[currentBall.x][currentBall.y].moveRight==Static.MOVE_AVAILABLE ||
                field.field[currentBall.x][currentBall.y].moveRight==Static.MOVE_CHECKING){
            availableMoves.right=true
        }
        if(field.field[currentBall.x][currentBall.y].moveLeft==Static.MOVE_AVAILABLE ||
                field.field[currentBall.x][currentBall.y].moveLeft==Static.MOVE_CHECKING){
            availableMoves.left=true
        }
        if(field.field[currentBall.x][currentBall.y].moveUpLeft==Static.MOVE_AVAILABLE ||
                field.field[currentBall.x][currentBall.y].moveUpLeft==Static.MOVE_CHECKING){
            availableMoves.upLeft=true
        }
        if(field.field[currentBall.x][currentBall.y].moveUpRight==Static.MOVE_AVAILABLE ||
                field.field[currentBall.x][currentBall.y].moveUpRight==Static.MOVE_CHECKING){
            availableMoves.upRight=true
        }
        if(field.field[currentBall.x][currentBall.y].moveUp==Static.MOVE_AVAILABLE ||
                field.field[currentBall.x][currentBall.y].moveUp==Static.MOVE_CHECKING){
            availableMoves.up=true
        }
        setNextMove(field)
    }

    fun setChecked(){
        checked = true
    }
    fun isChecked():Boolean{
        return checked
    }

    fun setNextMove(field: GameField){

        if(field.field[currentBall.x][currentBall.y].moveDown==Static.MOVE_FORBIDDEN ||
                field.field[currentBall.x][currentBall.y].moveDown==Static.MOVE_DONE_BY_ME||
                field.field[currentBall.x][currentBall.y].moveDown==Static.MOVE_DONE_BY_PHONE||
                field.field[currentBall.x][currentBall.y].moveDownLeft==Static.MOVE_FORBIDDEN ||
                field.field[currentBall.x][currentBall.y].moveDownLeft==Static.MOVE_DONE_BY_ME||
                field.field[currentBall.x][currentBall.y].moveDownLeft==Static.MOVE_DONE_BY_PHONE||
                field.field[currentBall.x][currentBall.y].moveDownRight==Static.MOVE_FORBIDDEN ||
                field.field[currentBall.x][currentBall.y].moveDownRight==Static.MOVE_DONE_BY_ME||
                field.field[currentBall.x][currentBall.y].moveDownRight==Static.MOVE_DONE_BY_PHONE||
                field.field[currentBall.x][currentBall.y].moveLeft==Static.MOVE_FORBIDDEN ||
                field.field[currentBall.x][currentBall.y].moveLeft==Static.MOVE_DONE_BY_ME||
                field.field[currentBall.x][currentBall.y].moveLeft==Static.MOVE_DONE_BY_PHONE||
                field.field[currentBall.x][currentBall.y].moveRight==Static.MOVE_FORBIDDEN ||
                field.field[currentBall.x][currentBall.y].moveRight==Static.MOVE_DONE_BY_ME||
                field.field[currentBall.x][currentBall.y].moveRight==Static.MOVE_DONE_BY_PHONE||
                field.field[currentBall.x][currentBall.y].moveUpLeft==Static.MOVE_FORBIDDEN ||
                field.field[currentBall.x][currentBall.y].moveUpLeft==Static.MOVE_DONE_BY_ME||
                field.field[currentBall.x][currentBall.y].moveUpLeft==Static.MOVE_DONE_BY_PHONE||
                field.field[currentBall.x][currentBall.y].moveUpRight==Static.MOVE_FORBIDDEN ||
                field.field[currentBall.x][currentBall.y].moveUpRight==Static.MOVE_DONE_BY_ME||
                field.field[currentBall.x][currentBall.y].moveUpRight==Static.MOVE_DONE_BY_PHONE||
                field.field[currentBall.x][currentBall.y].moveUp==Static.MOVE_FORBIDDEN ||
                field.field[currentBall.x][currentBall.y].moveUp==Static.MOVE_DONE_BY_ME||
                field.field[currentBall.x][currentBall.y].moveUp==Static.MOVE_DONE_BY_PHONE){
            nextMove=true
        }
    }

    fun isNextMove():Boolean{
        return nextMove
    }

}
