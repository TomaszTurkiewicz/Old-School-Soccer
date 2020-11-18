package com.tt.oldschoolsoccer.classes

import android.graphics.Point
import kotlin.properties.Delegates

class EasyGameField {
    lateinit var field:Array<Array<PointOnField>>
    var myMove:Boolean = true

    fun generate(){

        field = Array(9) { Array(13) { PointOnField() } }

        for(i in 0..8){
            for(j in 0..12){
                field[i][j].x=i+1
                field[i][j].y=j+1
            }
        }
        field[4][6].ball=true

        setAvaileblesMoves()



    }

    private fun setAvaileblesMoves() {
        field[0][0].setAvailableMoves(null,null,null,null,null,null,null,null)
        field[1][0].setAvailableMoves(null,null,null,null,null,null,null,null)
        field[2][0].setAvailableMoves(null,null,null,null,null,null,null,null)
        field[3][0].setAvailableMoves(null,null,null,downRight = false,null,null,null,null)
        field[4][0].setAvailableMoves(null,null,null,downRight = false,down = false,downLeft = false,null,null)
        field[5][0].setAvailableMoves(null,null,null,null,null,downLeft = false,null,null)
        field[6][0].setAvailableMoves(null,null,null,null,null,null,null,null)
        field[7][0].setAvailableMoves(null,null,null,null,null,null,null,null)
        field[8][0].setAvailableMoves(null,null,null,null,null,null,null,null)

        field[0][1].setAvailableMoves(null,null,null,downRight = false,null,null,null,null)
        field[1][1].setAvailableMoves(null,null,null,downRight = false,down = false,downLeft = false,null,null)
        field[2][1].setAvailableMoves(null,null,null,downRight = false,down = false,downLeft = false,null,null)
        field[3][1].setAvailableMoves(null,upRight = false,right = false,downRight = false,down = false,downLeft = false,null,null)
        field[5][1].setAvailableMoves(null,null,null,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[6][1].setAvailableMoves(null,null,null,downRight = false,down = false,downLeft = false,null,null)
        field[7][1].setAvailableMoves(null,null,null,downRight = false,down = false,downLeft = false,null,null)
        field[8][1].setAvailableMoves(null,null,null,null,null,downLeft = false,null,null)

        field[0][2].setAvailableMoves(null,upRight = false,right = false,downRight = false,null,null,null,null)
        field[0][3].setAvailableMoves(null,upRight = false,right = false,downRight = false,null,null,null,null)
        field[0][4].setAvailableMoves(null,upRight = false,right = false,downRight = false,null,null,null,null)
        field[0][5].setAvailableMoves(null,upRight = false,right = false,downRight = false,null,null,null,null)
        field[0][6].setAvailableMoves(null,upRight = false,right = false,downRight = false,null,null,null,null)
        field[0][7].setAvailableMoves(null,upRight = false,right = false,downRight = false,null,null,null,null)
        field[0][8].setAvailableMoves(null,upRight = false,right = false,downRight = false,null,null,null,null)
        field[0][9].setAvailableMoves(null,upRight = false,right = false,downRight = false,null,null,null,null)
        field[0][10].setAvailableMoves(null,upRight = false,right = false,downRight = false,null,null,null,null)

        field[8][2].setAvailableMoves(null,null,null,null,null,downLeft = false,left = false,upLeft = false)
        field[8][3].setAvailableMoves(null,null,null,null,null,downLeft = false,left = false,upLeft = false)
        field[8][4].setAvailableMoves(null,null,null,null,null,downLeft = false,left = false,upLeft = false)
        field[8][5].setAvailableMoves(null,null,null,null,null,downLeft = false,left = false,upLeft = false)
        field[8][6].setAvailableMoves(null,null,null,null,null,downLeft = false,left = false,upLeft = false)
        field[8][7].setAvailableMoves(null,null,null,null,null,downLeft = false,left = false,upLeft = false)
        field[8][8].setAvailableMoves(null,null,null,null,null,downLeft = false,left = false,upLeft = false)
        field[8][9].setAvailableMoves(null,null,null,null,null,downLeft = false,left = false,upLeft = false)
        field[8][10].setAvailableMoves(null,null,null,null,null,downLeft = false,left = false,upLeft = false)

        field[0][11].setAvailableMoves(null,upRight = false,null,null,null,null,null,null)
        field[1][11].setAvailableMoves(up = false,upRight = false,null,null,null,null,null,upLeft = false)
        field[2][11].setAvailableMoves(up = false,upRight = false,null,null,null,null,null,upLeft = false)
        field[3][11].setAvailableMoves(up = false,upRight = false,right = false,downRight = false,null,null,null,upLeft = false)
        field[5][11].setAvailableMoves(up = false,upRight = false,null,null,null,downLeft = false,left = false,upLeft = false)
        field[6][11].setAvailableMoves(up = false,upRight = false,null,null,null,null,null,upLeft = false)
        field[7][11].setAvailableMoves(up = false,upRight = false,null,null,null,null,null,upLeft = false)
        field[8][11].setAvailableMoves(null,null,null,null,null,null,null,upLeft = false)

        field[0][12].setAvailableMoves(null,null,null,null,null,null,null,null)
        field[1][12].setAvailableMoves(null,null,null,null,null,null,null,null)
        field[2][12].setAvailableMoves(null,null,null,null,null,null,null,null)
        field[3][12].setAvailableMoves(null,upRight = false,null,null,null,null,null,null)
        field[4][12].setAvailableMoves(up = false,upRight = false,null,null,null,null,null,upLeft = false)
        field[5][12].setAvailableMoves(null,null,null,null,null,null,null,upLeft = false)
        field[6][12].setAvailableMoves(null,null,null,null,null,null,null,null)
        field[7][12].setAvailableMoves(null,null,null,null,null,null,null,null)
        field[8][12].setAvailableMoves(null,null,null,null,null,null,null,null)

    }

    fun moveUp():Boolean{
        for(i in 0..8){
            for(j in 0..12){
                if(field[i][j].ball){
                    field[i][j].ball=false
                    field[i][j].moveUp=true
                    field[i][j-1].ball=true
                    field[i][j-1].moveDown=true
                    return true
                }
            }
        }
        return false
    }

    fun moveUpRight():Boolean{
        for(i in 0..8){
            for(j in 0..12){
                if(field[i][j].ball){
                    field[i][j].ball=false
                    field[i][j].moveUpRight=true
                    field[i+1][j-1].ball=true
                    field[i+1][j-1].moveDownLeft=true
                    return true
                }
            }
        }
        return false
    }

    fun moveRight():Boolean{
        for(i in 0..8){
            for(j in 0..12){
                if(field[i][j].ball){
                    field[i][j].ball=false
                    field[i][j].moveRight=true
                    field[i+1][j].ball=true
                    field[i+1][j].moveLeft=true
                    return true
                }
            }
        }
        return false
    }

    fun moveDownRight():Boolean{
        for(i in 0..8){
            for(j in 0..12){
                if(field[i][j].ball){
                    field[i][j].ball=false
                    field[i][j].moveDownRight=true
                    field[i+1][j+1].ball=true
                    field[i+1][j+1].moveUpLeft=true
                    return true
                }
            }
        }
        return false
    }

    fun moveDown():Boolean{
        for(i in 0..8){
            for(j in 0..12){
                if(field[i][j].ball){
                    field[i][j].ball=false
                    field[i][j].moveDown=true
                    field[i][j+1].ball=true
                    field[i][j+1].moveUp=true
                    return true
                }
            }
        }
        return false
    }

    fun moveDownLeft():Boolean{
        for(i in 0..8){
            for(j in 0..12){
                if(field[i][j].ball){
                    field[i][j].ball=false
                    field[i][j].moveDownLeft=true
                    field[i-1][j+1].ball=true
                    field[i-1][j+1].moveUpRight=true
                    return true
                }
            }
        }
        return false
    }

    fun moveLeft():Boolean{
        for(i in 0..8){
            for(j in 0..12){
                if(field[i][j].ball){
                    field[i][j].ball=false
                    field[i][j].moveLeft=true
                    field[i-1][j].ball=true
                    field[i-1][j].moveRight=true
                    return true
                }
            }
        }
        return false
    }

    fun moveUpLeft():Boolean{
        for(i in 0..8){
            for(j in 0..12){
                if(field[i][j].ball){
                    field[i][j].ball=false
                    field[i][j].moveUpLeft=true
                    field[i-1][j-1].ball=true
                    field[i-1][j-1].moveDownRight=true
                    return true
                }
            }
        }
        return false
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
                  if(field[i][j].moveUp!=null){
                      up=field[i][j].moveUp!!
                  }
                    if(field[i][j].moveUpRight!=null){
                        upRight=field[i][j].moveUpRight!!
                    }
                    if(field[i][j].moveRight!=null){
                        right=field[i][j].moveRight!!
                    }
                    if(field[i][j].moveDownRight!=null){
                        downRight=field[i][j].moveDownRight!!
                    }
                    if(field[i][j].moveDown!=null){
                        down=field[i][j].moveDown!!
                    }
                    if(field[i][j].moveDownLeft!=null){
                        downLeft=field[i][j].moveDownLeft!!
                    }
                    if(field[i][j].moveLeft!=null){
                        left=field[i][j].moveLeft!!
                    }
                    if(field[i][j].moveUpLeft!=null){
                        upLeft=field[i][j].moveUpLeft!!
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



}