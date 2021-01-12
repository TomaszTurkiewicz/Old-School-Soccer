package com.tt.oldschoolsoccer.classes

import android.graphics.Point
import com.tt.oldschoolsoccer.database.PointOnField

class GameField {
    lateinit var field:Array<Array<PointOnField>>
    var myMove:Boolean = true
    var level = -1

    fun generate(gameLevel:Int){
        if(gameLevel!=Static.HARD) {
            field = Array(9) { Array(13) { PointOnField() } }
            for (i in 0..8) {
                for (j in 0..12) {
                    field[i][j].x = i + 1
                    field[i][j].y = j + 1
                    field[i][j].position = 9*j + i
                }
            }
            field[4][6].ball = true
            setAvaileblesMoves()

        }
        level=gameLevel
    }

    private fun setAvaileblesMoves() {
        field[0][0].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)
        field[1][0].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)
        field[2][0].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)
        field[3][0].setAvailableMoves(up = false, upRight = false, right = false,downRight = true, down = false, downLeft = false, left = false, upLeft = false)
        field[4][0].setAvailableMoves(up = false, upRight = false, right = false,downRight = true,down = true,downLeft = true, left = false, upLeft = false)
        field[5][0].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false,downLeft = true, left = false, upLeft = false)
        field[6][0].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)
        field[7][0].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)
        field[8][0].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)

        field[0][1].setAvailableMoves(up = false, upRight = false, right = false,downRight = true, down = false, downLeft = false, left = false, upLeft = false)
        field[1][1].setAvailableMoves(up = false, upRight = false, right = false,downRight = true,down = true,downLeft = true, left = false, upLeft = false)
        field[2][1].setAvailableMoves(up = false, upRight = false, right = false,downRight = true,down = true,downLeft = true, left = false, upLeft = false)
        field[3][1].setAvailableMoves(false,upRight = true,right = true,downRight = true,down = true,downLeft = true, left = false, upLeft = false)
        field[5][1].setAvailableMoves(up = false, upRight = false, right = false,downRight = true,down = true,downLeft = true,left = true,upLeft = true)
        field[6][1].setAvailableMoves(up = false, upRight = false, right = false,downRight = true,down = true,downLeft = true, left = false, upLeft = false)
        field[7][1].setAvailableMoves(up = false, upRight = false, right = false,downRight = true,down = true,downLeft = true, left = false, upLeft = false)
        field[8][1].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false,downLeft = true, left = false, upLeft = false)

        field[0][2].setAvailableMoves(false,upRight = true,right = true,downRight = true, down = false, downLeft = false, left = false, upLeft = false)
        field[0][3].setAvailableMoves(false,upRight = true,right = true,downRight = true, down = false, downLeft = false, left = false, upLeft = false)
        field[0][4].setAvailableMoves(false,upRight = true,right = true,downRight = true, down = false, downLeft = false, left = false, upLeft = false)
        field[0][5].setAvailableMoves(false,upRight = true,right = true,downRight = true, down = false, downLeft = false, left = false, upLeft = false)
        field[0][6].setAvailableMoves(false,upRight = true,right = true,downRight = true, down = false, downLeft = false, left = false, upLeft = false)
        field[0][7].setAvailableMoves(false,upRight = true,right = true,downRight = true, down = false, downLeft = false, left = false, upLeft = false)
        field[0][8].setAvailableMoves(false,upRight = true,right = true,downRight = true, down = false, downLeft = false, left = false, upLeft = false)
        field[0][9].setAvailableMoves(false,upRight = true,right = true,downRight = true, down = false, downLeft = false, left = false, upLeft = false)
        field[0][10].setAvailableMoves(false,upRight = true,right = true,downRight = true, down = false, downLeft = false, left = false, upLeft = false)

        field[8][2].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false,downLeft = true,left = true,upLeft = true)
        field[8][3].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false,downLeft = true,left = true,upLeft = true)
        field[8][4].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false,downLeft = true,left = true,upLeft = true)
        field[8][5].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false,downLeft = true,left = true,upLeft = true)
        field[8][6].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false,downLeft = true,left = true,upLeft = true)
        field[8][7].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false,downLeft = true,left = true,upLeft = true)
        field[8][8].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false,downLeft = true,left = true,upLeft = true)
        field[8][9].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false,downLeft = true,left = true,upLeft = true)
        field[8][10].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false,downLeft = true,left = true,upLeft = true)

        field[0][11].setAvailableMoves(false,upRight = true, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)
        field[1][11].setAvailableMoves(up = true,upRight = true, right = false, downRight = false, down = false, downLeft = false, left = false,upLeft = true)
        field[2][11].setAvailableMoves(up = true,upRight = true, right = false, downRight = false, down = false, downLeft = false, left = false,upLeft = true)
        field[3][11].setAvailableMoves(up = true,upRight = true,right = true,downRight = true, down = false, downLeft = false, left = false,upLeft = true)
        field[5][11].setAvailableMoves(up = true,upRight = true, right = false, downRight = false, down = false,downLeft = true,left = true,upLeft = true)
        field[6][11].setAvailableMoves(up = true,upRight = true, right = false, downRight = false, down = false, downLeft = false, left = false,upLeft = true)
        field[7][11].setAvailableMoves(up = true,upRight = true, right = false, downRight = false, down = false, downLeft = false, left = false,upLeft = true)
        field[8][11].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false,upLeft = true)

        field[0][12].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)
        field[1][12].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)
        field[2][12].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)
        field[3][12].setAvailableMoves(false,upRight = true, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)
        field[4][12].setAvailableMoves(up = true,upRight = true, right = false, downRight = false, down = false, downLeft = false, left = false,upLeft = true)
        field[5][12].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false,upLeft = true)
        field[6][12].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)
        field[7][12].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)
        field[8][12].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)

    }

    fun moveUp(myTurn:Boolean):PointsAfterMove{
        val pointsAfterMove = PointsAfterMove()
        val ball = findBall()
        val i = ball.x
        val j = ball.y
                    field[i][j].ball=false
                    field[i][j-1].ball=true
                    if(myTurn){
                        field[i][j].moveUp = Static.MOVE_DONE_BY_ME
                        field[i][j-1].moveDown = Static.MOVE_DONE_BY_ME
                    }
                    else{
                        field[i][j].moveUp = Static.MOVE_DONE_BY_PHONE
                        field[i][j-1].moveDown = Static.MOVE_DONE_BY_PHONE
                    }
        pointsAfterMove.beforeMovePoint = field[i][j]
        pointsAfterMove.afterMovePoint = field[i][j-1]

        return pointsAfterMove
    }

    fun moveUpRight(myTurn: Boolean):PointsAfterMove{
        val pointsAfterMove = PointsAfterMove()
        val ball = findBall()
        val i = ball.x
        val j = ball.y
                    field[i][j].ball=false
                    field[i+1][j-1].ball=true
                    if(myTurn){
                        field[i][j].moveUpRight = Static.MOVE_DONE_BY_ME
                        field[i+1][j-1].moveDownLeft = Static.MOVE_DONE_BY_ME
                    }
                    else{
                        field[i][j].moveUpRight = Static.MOVE_DONE_BY_PHONE
                        field[i+1][j-1].moveDownLeft = Static.MOVE_DONE_BY_PHONE
                    }
        pointsAfterMove.beforeMovePoint = field[i][j]
        pointsAfterMove.afterMovePoint = field[i+1][j-1]

        return pointsAfterMove
    }

    fun moveRight(myTurn: Boolean):PointsAfterMove{
        val pointsAfterMove = PointsAfterMove()
        val ball = findBall()
        val i = ball.x
        val j = ball.y
                    field[i][j].ball=false
                    field[i+1][j].ball=true
                    if(myTurn){
                        field[i][j].moveRight = Static.MOVE_DONE_BY_ME
                        field[i+1][j].moveLeft = Static.MOVE_DONE_BY_ME
                    }
                    else{
                        field[i][j].moveRight = Static.MOVE_DONE_BY_PHONE
                        field[i+1][j].moveLeft = Static.MOVE_DONE_BY_PHONE
                    }
        pointsAfterMove.beforeMovePoint = field[i][j]
        pointsAfterMove.afterMovePoint = field[i+1][j]

        return pointsAfterMove
    }

    fun moveDownRight(myTurn: Boolean):PointsAfterMove{
        val pointsAfterMove = PointsAfterMove()
        val ball = findBall()
        val i = ball.x
        val j = ball.y
                    field[i][j].ball=false
                    field[i+1][j+1].ball=true
                    if(myTurn){
                        field[i][j].moveDownRight = Static.MOVE_DONE_BY_ME
                        field[i+1][j+1].moveUpLeft = Static.MOVE_DONE_BY_ME
                    }
                    else{
                        field[i][j].moveDownRight = Static.MOVE_DONE_BY_PHONE
                        field[i+1][j+1].moveUpLeft = Static.MOVE_DONE_BY_PHONE
                    }
        pointsAfterMove.beforeMovePoint = field[i][j]
        pointsAfterMove.afterMovePoint = field[i+1][j+1]

        return pointsAfterMove
    }

    fun moveDown(myTurn: Boolean):PointsAfterMove{
        val pointsAfterMove = PointsAfterMove()
        val ball = findBall()
        val i = ball.x
        val j = ball.y
                    field[i][j].ball=false
                    field[i][j+1].ball=true
                    if(myTurn){
                        field[i][j].moveDown = Static.MOVE_DONE_BY_ME
                        field[i][j+1].moveUp = Static.MOVE_DONE_BY_ME
                    }
                    else{
                        field[i][j].moveDown = Static.MOVE_DONE_BY_PHONE
                        field[i][j+1].moveUp = Static.MOVE_DONE_BY_PHONE
                    }
        pointsAfterMove.beforeMovePoint = field[i][j]
        pointsAfterMove.afterMovePoint = field[i][j+1]

        return pointsAfterMove
    }

    fun moveDownLeft(myTurn: Boolean):PointsAfterMove{
        val pointsAfterMove = PointsAfterMove()
        val ball = findBall()
        val i = ball.x
        val j = ball.y
                    field[i][j].ball=false
                    field[i-1][j+1].ball=true
                    if(myTurn){
                        field[i][j].moveDownLeft = Static.MOVE_DONE_BY_ME
                        field[i-1][j+1].moveUpRight = Static.MOVE_DONE_BY_ME
                    }
                    else{
                        field[i][j].moveDownLeft = Static.MOVE_DONE_BY_PHONE
                        field[i-1][j+1].moveUpRight = Static.MOVE_DONE_BY_PHONE
                    }
        pointsAfterMove.beforeMovePoint = field[i][j]
        pointsAfterMove.afterMovePoint = field[i-1][j+1]

        return pointsAfterMove
    }

    fun moveLeft(myTurn: Boolean):PointsAfterMove{
        val pointsAfterMove = PointsAfterMove()
        val ball = findBall()
        val i = ball.x
        val j = ball.y
        field[i][j].ball=false
        field[i-1][j].ball=true
        if(myTurn){
            field[i][j].moveLeft = Static.MOVE_DONE_BY_ME
            field[i-1][j].moveRight = Static.MOVE_DONE_BY_ME
        }
        else{
            field[i][j].moveLeft = Static.MOVE_DONE_BY_PHONE
            field[i-1][j].moveRight = Static.MOVE_DONE_BY_PHONE
        }
        pointsAfterMove.beforeMovePoint = field[i][j]
        pointsAfterMove.afterMovePoint = field[i-1][j]

        return pointsAfterMove
    }

    fun moveUpLeft(myTurn: Boolean):PointsAfterMove{
        val pointsAfterMove = PointsAfterMove()
        val ball = findBall()
        val i = ball.x
        val j = ball.y
                    field[i][j].ball=false
                    field[i-1][j-1].ball=true
                    if(myTurn){
                        field[i][j].moveUpLeft = Static.MOVE_DONE_BY_ME
                        field[i-1][j-1].moveDownRight = Static.MOVE_DONE_BY_ME
                    }
                    else{
                        field[i][j].moveUpLeft = Static.MOVE_DONE_BY_PHONE
                        field[i-1][j-1].moveDownRight = Static.MOVE_DONE_BY_PHONE
                    }
        pointsAfterMove.beforeMovePoint = field[i][j]
        pointsAfterMove.afterMovePoint = field[i-1][j-1]

        return pointsAfterMove
    }

    fun checkIfStuckAndNextMove(direction:Int):StuckAndNextMove{
        var up=true
        var upRight=true
        var right=true
        var downRight=true
        var down=true
        var downLeft=true
        var left=true
        var upLeft=true
        for(i in 0..8){
            for(j in 0..12){
                if(field[i][j].ball){
                    if(field[i][j].moveUp==Static.MOVE_AVAILABLE){
                        up = false
                    }
                    if(field[i][j].moveUpRight==Static.MOVE_AVAILABLE){
                        upRight= false
                    }
                    if(field[i][j].moveRight==Static.MOVE_AVAILABLE){
                        right=false
                    }
                    if(field[i][j].moveDownRight==Static.MOVE_AVAILABLE){
                        downRight=false
                    }
                    if(field[i][j].moveDown==Static.MOVE_AVAILABLE){
                        down=false
                    }
                    if(field[i][j].moveDownLeft==Static.MOVE_AVAILABLE){
                        downLeft=false
                    }
                    if(field[i][j].moveLeft==Static.MOVE_AVAILABLE){
                        left=false
                    }
                    if(field[i][j].moveUpLeft==Static.MOVE_AVAILABLE){
                        upLeft=false
                    }
                }
            }
        }

        val stuck = up and (upRight and (right and (downRight and (down and(downLeft and(left and (upLeft)))))))
        if(direction==Static.UP){
            down=false
        }
        if(direction==Static.UP_RIGHT){
            downLeft=false
        }
        if(direction==Static.RIGHT){
            left=false
        }
        if(direction==Static.DOWN_RIGHT){
            upLeft=false
        }
        if(direction==Static.DOWN){
            up=false
        }
        if(direction==Static.DOWN_LEFT){
            upRight=false
        }
        if(direction==Static.LEFT){
            right=false
        }
        if(direction==Static.UP_LEFT){
            downRight=false
        }
        val nextMove = up or (upRight or (right or (downRight or (down or(downLeft or(left or (upLeft)))))))

        return StuckAndNextMove(nextMove,stuck)
    }

    fun checkIfMoveInDirectionIsAvailable(pointOnField: PointOnField, direction: Int):Boolean{
        if(direction==Static.DOWN){
            if(pointOnField.moveDown==Static.MOVE_AVAILABLE){
                return true
            }
        }
        if(direction==Static.DOWN_LEFT){
            if(pointOnField.moveDownLeft==Static.MOVE_AVAILABLE){
                    return true
            }
        }
        if(direction==Static.DOWN_RIGHT){
            if(pointOnField.moveDownRight==Static.MOVE_AVAILABLE){
                    return true
            }
        }
        if(direction==Static.LEFT){
            if(pointOnField.moveLeft==Static.MOVE_AVAILABLE){
                    return true
            }
        }
        if(direction==Static.RIGHT){
            if(pointOnField.moveRight==Static.MOVE_AVAILABLE){
                    return true
            }
        }
        if(direction==Static.UP){
            if(pointOnField.moveUp==Static.MOVE_AVAILABLE){
                    return true
            }
        }
        if(direction==Static.UP_RIGHT){
            if(pointOnField.moveUpRight==Static.MOVE_AVAILABLE){
                    return true
            }
        }
        if(direction==Static.UP_LEFT){
            if(pointOnField.moveUpLeft==Static.MOVE_AVAILABLE){
                    return true
            }
        }
        return false
    }

    fun findBall():Point {
        if (level != Static.HARD) {
            for (i in 0..8) {
                for (j in 0..12) {
                    if (field[i][j].ball) {
                        return Point(i, j)
                    }
                }
            }
        }
            return Point(-1, -1)

    }

    fun getPoint(i:Int,j:Int):PointOnField{
        return field[i][j]
    }




}