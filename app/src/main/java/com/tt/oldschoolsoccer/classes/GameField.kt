package com.tt.oldschoolsoccer.classes

import android.graphics.Point
import kotlin.properties.Delegates

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

    fun moveUp(myTurn:Boolean){
        val ball = findBall()
        val i = ball.x
        val j = ball.y
                    field[i][j].ball=false
                    field[i][j].moveUp.moveDirection=true
                    field[i][j-1].ball=true
                    field[i][j-1].moveDown.moveDirection=true
                    if(myTurn){
                        field[i][j].moveUp.playerMove=Static.PLAYER
                        field[i][j-1].moveDown.playerMove=Static.PLAYER
                    }
                    else{
                        field[i][j].moveUp.playerMove=Static.PHONE
                        field[i][j-1].moveDown.playerMove=Static.PHONE
                    }
    }

    fun moveUpRight(myTurn: Boolean){
        val ball = findBall()
        val i = ball.x
        val j = ball.y
                    field[i][j].ball=false
                    field[i][j].moveUpRight.moveDirection=true
                    field[i+1][j-1].ball=true
                    field[i+1][j-1].moveDownLeft.moveDirection=true
                    if(myTurn){
                        field[i][j].moveUpRight.playerMove=Static.PLAYER
                        field[i+1][j-1].moveDownLeft.playerMove=Static.PLAYER
                    }
                    else{
                        field[i][j].moveUpRight.playerMove=Static.PHONE
                        field[i+1][j-1].moveDownLeft.playerMove=Static.PHONE
                    }
    }

    fun moveRight(myTurn: Boolean){
        val ball = findBall()
        val i = ball.x
        val j = ball.y
                    field[i][j].ball=false
                    field[i][j].moveRight.moveDirection=true
                    field[i+1][j].ball=true
                    field[i+1][j].moveLeft.moveDirection=true
                    if(myTurn){
                        field[i][j].moveRight.playerMove=Static.PLAYER
                        field[i+1][j].moveLeft.playerMove=Static.PLAYER
                    }
                    else{
                        field[i][j].moveRight.playerMove=Static.PHONE
                        field[i+1][j].moveLeft.playerMove=Static.PHONE
                    }
    }

    fun moveDownRight(myTurn: Boolean){
        val ball = findBall()
        val i = ball.x
        val j = ball.y
                    field[i][j].ball=false
                    field[i][j].moveDownRight.moveDirection=true
                    field[i+1][j+1].ball=true
                    field[i+1][j+1].moveUpLeft.moveDirection=true
                    if(myTurn){
                        field[i][j].moveDownRight.playerMove=Static.PLAYER
                        field[i+1][j+1].moveUpLeft.playerMove=Static.PLAYER
                    }
                    else{
                        field[i][j].moveDownRight.playerMove=Static.PHONE
                        field[i+1][j+1].moveUpLeft.playerMove=Static.PHONE
                    }
    }

    fun moveDown(myTurn: Boolean){
        val ball = findBall()
        val i = ball.x
        val j = ball.y
                    field[i][j].ball=false
                    field[i][j].moveDown.moveDirection=true
                    field[i][j+1].ball=true
                    field[i][j+1].moveUp.moveDirection=true
                    if(myTurn){
                        field[i][j].moveDown.playerMove=Static.PLAYER
                        field[i][j+1].moveUp.playerMove=Static.PLAYER
                    }
                    else{
                        field[i][j].moveDown.playerMove=Static.PHONE
                        field[i][j+1].moveUp.playerMove=Static.PHONE
                    }
    }

    fun moveDownLeft(myTurn: Boolean){
        val ball = findBall()
        val i = ball.x
        val j = ball.y
                    field[i][j].ball=false
                    field[i][j].moveDownLeft.moveDirection=true
                    field[i-1][j+1].ball=true
                    field[i-1][j+1].moveUpRight.moveDirection=true
                    if(myTurn){
                        field[i][j].moveDownLeft.playerMove=Static.PLAYER
                        field[i-1][j+1].moveUpRight.playerMove=Static.PLAYER
                    }
                    else{
                        field[i][j].moveDownLeft.playerMove=Static.PHONE
                        field[i-1][j+1].moveUpRight.playerMove=Static.PHONE
                    }
    }

    fun moveLeft(myTurn: Boolean){
        val ball = findBall()
        val i = ball.x
        val j = ball.y
        field[i][j].ball=false
        field[i][j].moveLeft.moveDirection=true
        field[i-1][j].ball=true
        field[i-1][j].moveRight.moveDirection=true
        if(myTurn){
            field[i][j].moveLeft.playerMove=Static.PLAYER
            field[i-1][j].moveRight.playerMove=Static.PLAYER
        }
        else{
            field[i][j].moveLeft.playerMove=Static.PHONE
            field[i-1][j].moveRight.playerMove=Static.PHONE
        }
    }

    fun moveUpLeft(myTurn: Boolean){
        val ball = findBall()
        val i = ball.x
        val j = ball.y
                    field[i][j].ball=false
                    field[i][j].moveUpLeft.moveDirection=true
                    field[i-1][j-1].ball=true
                    field[i-1][j-1].moveDownRight.moveDirection=true
                    if(myTurn){
                        field[i][j].moveUpLeft.playerMove=Static.PLAYER
                        field[i-1][j-1].moveDownRight.playerMove=Static.PLAYER
                    }
                    else{
                        field[i][j].moveUpLeft.playerMove=Static.PHONE
                        field[i-1][j-1].moveDownRight.playerMove=Static.PHONE
                    }
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
                  if(field[i][j].moveUp.moveDirection!=null){
                      up=field[i][j].moveUp.moveDirection!!
                  }
                    if(field[i][j].moveUpRight.moveDirection!=null){
                        upRight=field[i][j].moveUpRight.moveDirection!!
                    }
                    if(field[i][j].moveRight.moveDirection!=null){
                        right=field[i][j].moveRight.moveDirection!!
                    }
                    if(field[i][j].moveDownRight.moveDirection!=null){
                        downRight=field[i][j].moveDownRight.moveDirection!!
                    }
                    if(field[i][j].moveDown.moveDirection!=null){
                        down=field[i][j].moveDown.moveDirection!!
                    }
                    if(field[i][j].moveDownLeft.moveDirection!=null){
                        downLeft=field[i][j].moveDownLeft.moveDirection!!
                    }
                    if(field[i][j].moveLeft.moveDirection!=null){
                        left=field[i][j].moveLeft.moveDirection!!
                    }
                    if(field[i][j].moveUpLeft.moveDirection!=null){
                        upLeft=field[i][j].moveUpLeft.moveDirection!!
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

    fun checkIfMoveInDirectionIsAvailable(pointOnField: PointOnField,direction: Int):Boolean{
        if(direction==Static.DOWN){
            if(pointOnField.moveDown.moveDirection!=null){
                if(pointOnField.moveDown.moveDirection==false){
                    return true
                }
            }
        }
        if(direction==Static.DOWN_LEFT){
            if(pointOnField.moveDownLeft.moveDirection!=null){
                if(pointOnField.moveDownLeft.moveDirection==false){
                    return true
                }
            }
        }
        if(direction==Static.DOWN_RIGHT){
            if(pointOnField.moveDownRight.moveDirection!=null){
                if(pointOnField.moveDownRight.moveDirection==false){
                    return true
                }
            }
        }
        if(direction==Static.LEFT){
            if(pointOnField.moveLeft.moveDirection!=null){
                if(pointOnField.moveLeft.moveDirection==false){
                    return true
                }
            }
        }
        if(direction==Static.RIGHT){
            if(pointOnField.moveRight.moveDirection!=null){
                if(pointOnField.moveRight.moveDirection==false){
                    return true
                }
            }
        }
        if(direction==Static.UP){
            if(pointOnField.moveUp.moveDirection!=null){
                if(pointOnField.moveUp.moveDirection==false){
                    return true
                }
            }
        }
        if(direction==Static.UP_RIGHT){
            if(pointOnField.moveUpRight.moveDirection!=null){
                if(pointOnField.moveUpRight.moveDirection==false){
                    return true
                }
            }
        }
        if(direction==Static.UP_LEFT){
            if(pointOnField.moveUpLeft.moveDirection!=null){
                if(pointOnField.moveUpLeft.moveDirection==false){
                    return true
                }
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




}