package com.tt.oldschoolsoccer.classes

import android.graphics.Point

class EasyGameField {
    lateinit var field:Array<Array<PointOnField>>

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
                    field[i][j-1].ball=true
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
                    field[i+1][j-1].ball=true
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
                    field[i+1][j].ball=true
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
                    field[i+1][j+1].ball=true
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
                    field[i][j+1].ball=true
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
                    field[i-1][j+1].ball=true
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
                    field[i-1][j].ball=true
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
                    field[i-1][j-1].ball=true
                    return true
                }
            }
        }
        return false
    }



}