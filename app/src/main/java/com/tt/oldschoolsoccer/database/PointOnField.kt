package com.tt.oldschoolsoccer.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tt.oldschoolsoccer.classes.Static

/**
 * Entity for database
 * point on field with info about available moves, moves which have been already done and ball
 */
@Entity
data class PointOnField(
                        @PrimaryKey(autoGenerate = false)
                        var position: Int = 0,
                        var moveUp: Int = Static.MOVE_AVAILABLE,
                        var moveUpRight: Int = Static.MOVE_AVAILABLE,
                        var moveRight: Int = Static.MOVE_AVAILABLE,
                        var moveDownRight: Int = Static.MOVE_AVAILABLE,
                        var moveDown: Int = Static.MOVE_AVAILABLE,
                        var moveDownLeft: Int = Static.MOVE_AVAILABLE,
                        var moveLeft: Int = Static.MOVE_AVAILABLE,
                        var moveUpLeft: Int = Static.MOVE_AVAILABLE,
                        var x:Int=0,
                        var y:Int=0,
                        var ball:Boolean = false) {

    /**
     * creating available moves for a point
     */
    fun setAvailableMoves(up:Boolean,upRight:Boolean,right:Boolean,downRight:Boolean,down:Boolean,downLeft:Boolean,left:Boolean,upLeft:Boolean){
        this.moveUp = setMove(up)
        this.moveUpRight = setMove(upRight)
        this.moveRight = setMove(right)
        this.moveDownRight = setMove(downRight)
        this.moveDown = setMove(down)
        this.moveDownLeft = setMove(downLeft)
        this.moveLeft = setMove(left)
        this.moveUpLeft = setMove(upLeft)


    }

    /**
     * setting if move is available or not
     */
    private fun setMove(move:Boolean):Int{
        return if(move) Static.MOVE_AVAILABLE else Static.MOVE_FORBIDDEN
    }


}