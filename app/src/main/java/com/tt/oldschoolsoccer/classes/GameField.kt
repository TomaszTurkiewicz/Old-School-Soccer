package com.tt.oldschoolsoccer.classes

import android.graphics.Point
import com.tt.oldschoolsoccer.database.PointOnField

class GameField {
    lateinit var field: Array<Array<PointOnField>>
    var myMove: Boolean = true
    var level = -1


    /**
     * generate empty football field with ball in the middle
     * EASY and NORMAL are small
     * HARD is bigger
     */
    fun generate(gameLevel: Int) {
        if (gameLevel == Static.EASY || gameLevel == Static.NORMAL) {
            field = Array(11) { Array(15) { PointOnField() } }
            for (i in 0..10) {
                for (j in 0..14) {
                    field[i][j].x = i
                    field[i][j].y = j
                    field[i][j].position = 11 * j + i
                }
            }
            field[5][7].ball = true
            setAvailableMoves()

        } else {
            field = Array(13) { Array(21) { PointOnField() } }
            for (i in 0..12) {
                for (j in 0..20) {
                    field[i][j].x = i
                    field[i][j].y = j
                    field[i][j].position = 13 * j + i
                }
            }
            field[6][10].ball = true
            setAvailableMovesHard()
        }
        level = gameLevel
    }

    /**
     * setting available moves so ball cannot go outside borders
     * HARD
     */
    private fun setAvailableMovesHard() {
        field[0][0].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)
        field[1][0].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)
        field[2][0].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)
        field[3][0].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)
        field[4][0].setAvailableMoves(up = false, upRight = false, right = false, downRight = true, down = false, downLeft = false, left = false, upLeft = false)
        field[5][0].setAvailableMoves(up = false, upRight = false, right = false, downRight = true, down = true, downLeft = true, left = false, upLeft = false)
        field[6][0].setAvailableMoves(up = false, upRight = false, right = false, downRight = true, down = true, downLeft = true, left = false, upLeft = false)
        field[7][0].setAvailableMoves(up = false, upRight = false, right = false, downRight = true, down = true, downLeft = true, left = false, upLeft = false)
        field[8][0].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = true, left = false, upLeft = false)
        field[9][0].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)
        field[10][0].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)
        field[11][0].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)
        field[12][0].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)

        field[0][1].setAvailableMoves(up = false, upRight = false, right = false, downRight = true, down = false, downLeft = false, left = false, upLeft = false)
        field[1][1].setAvailableMoves(up = false, upRight = false, right = false, downRight = true, down = true, downLeft = true, left = false, upLeft = false)
        field[2][1].setAvailableMoves(up = false, upRight = false, right = false, downRight = true, down = true, downLeft = true, left = false, upLeft = false)
        field[3][1].setAvailableMoves(up = false, upRight = false, right = false, downRight = true, down = true, downLeft = true, left = false, upLeft = false)
        field[4][1].setAvailableMoves(false, upRight = true, right = true, downRight = true, down = true, downLeft = true, left = false, upLeft = false)
        field[8][1].setAvailableMoves(up = false, upRight = false, right = false, downRight = true, down = true, downLeft = true, left = true, upLeft = true)
        field[9][1].setAvailableMoves(up = false, upRight = false, right = false, downRight = true, down = true, downLeft = true, left = false, upLeft = false)
        field[10][1].setAvailableMoves(up = false, upRight = false, right = false, downRight = true, down = true, downLeft = true, left = false, upLeft = false)
        field[11][1].setAvailableMoves(up = false, upRight = false, right = false, downRight = true, down = true, downLeft = true, left = false, upLeft = false)
        field[12][1].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = true, left = false, upLeft = false)

        field[0][2].setAvailableMoves(false, upRight = true, right = true, downRight = true, down = false, downLeft = false, left = false, upLeft = false)
        field[0][3].setAvailableMoves(false, upRight = true, right = true, downRight = true, down = false, downLeft = false, left = false, upLeft = false)
        field[0][4].setAvailableMoves(false, upRight = true, right = true, downRight = true, down = false, downLeft = false, left = false, upLeft = false)
        field[0][5].setAvailableMoves(false, upRight = true, right = true, downRight = true, down = false, downLeft = false, left = false, upLeft = false)
        field[0][6].setAvailableMoves(false, upRight = true, right = true, downRight = true, down = false, downLeft = false, left = false, upLeft = false)
        field[0][7].setAvailableMoves(false, upRight = true, right = true, downRight = true, down = false, downLeft = false, left = false, upLeft = false)
        field[0][8].setAvailableMoves(false, upRight = true, right = true, downRight = true, down = false, downLeft = false, left = false, upLeft = false)
        field[0][9].setAvailableMoves(false, upRight = true, right = true, downRight = true, down = false, downLeft = false, left = false, upLeft = false)
        field[0][10].setAvailableMoves(false, upRight = true, right = true, downRight = true, down = false, downLeft = false, left = false, upLeft = false)
        field[0][11].setAvailableMoves(false, upRight = true, right = true, downRight = true, down = false, downLeft = false, left = false, upLeft = false)
        field[0][12].setAvailableMoves(false, upRight = true, right = true, downRight = true, down = false, downLeft = false, left = false, upLeft = false)
        field[0][13].setAvailableMoves(false, upRight = true, right = true, downRight = true, down = false, downLeft = false, left = false, upLeft = false)
        field[0][14].setAvailableMoves(false, upRight = true, right = true, downRight = true, down = false, downLeft = false, left = false, upLeft = false)
        field[0][15].setAvailableMoves(false, upRight = true, right = true, downRight = true, down = false, downLeft = false, left = false, upLeft = false)
        field[0][16].setAvailableMoves(false, upRight = true, right = true, downRight = true, down = false, downLeft = false, left = false, upLeft = false)
        field[0][17].setAvailableMoves(false, upRight = true, right = true, downRight = true, down = false, downLeft = false, left = false, upLeft = false)
        field[0][18].setAvailableMoves(false, upRight = true, right = true, downRight = true, down = false, downLeft = false, left = false, upLeft = false)


        field[12][2].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = true, left = true, upLeft = true)
        field[12][3].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = true, left = true, upLeft = true)
        field[12][4].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = true, left = true, upLeft = true)
        field[12][5].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = true, left = true, upLeft = true)
        field[12][6].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = true, left = true, upLeft = true)
        field[12][7].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = true, left = true, upLeft = true)
        field[12][8].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = true, left = true, upLeft = true)
        field[12][9].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = true, left = true, upLeft = true)
        field[12][10].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = true, left = true, upLeft = true)
        field[12][11].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = true, left = true, upLeft = true)
        field[12][12].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = true, left = true, upLeft = true)
        field[12][13].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = true, left = true, upLeft = true)
        field[12][14].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = true, left = true, upLeft = true)
        field[12][15].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = true, left = true, upLeft = true)
        field[12][16].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = true, left = true, upLeft = true)
        field[12][17].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = true, left = true, upLeft = true)
        field[12][18].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = true, left = true, upLeft = true)

        field[0][19].setAvailableMoves(false, upRight = true, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)
        field[1][19].setAvailableMoves(up = true, upRight = true, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = true)
        field[2][19].setAvailableMoves(up = true, upRight = true, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = true)
        field[3][19].setAvailableMoves(up = true, upRight = true, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = true)
        field[4][19].setAvailableMoves(up = true, upRight = true, right = true, downRight = true, down = false, downLeft = false, left = false, upLeft = true)
        field[8][19].setAvailableMoves(up = true, upRight = true, right = false, downRight = false, down = false, downLeft = true, left = true, upLeft = true)
        field[9][19].setAvailableMoves(up = true, upRight = true, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = true)
        field[10][19].setAvailableMoves(up = true, upRight = true, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = true)
        field[11][19].setAvailableMoves(up = true, upRight = true, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = true)
        field[12][19].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = true)

        field[0][20].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)
        field[1][20].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)
        field[2][20].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)
        field[3][20].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)
        field[4][20].setAvailableMoves(false, upRight = true, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)
        field[5][20].setAvailableMoves(up = true, upRight = true, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = true)
        field[6][20].setAvailableMoves(up = true, upRight = true, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = true)
        field[7][20].setAvailableMoves(up = true, upRight = true, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = true)
        field[8][20].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = true)
        field[9][20].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)
        field[10][20].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)
        field[11][20].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)
        field[12][20].setAvailableMoves(up = false, upRight = false, right = false, downRight = false, down = false, downLeft = false, left = false, upLeft = false)

    }


    /**
     * setting available moves so ball cannot go outside borders
     * EASY and NORMAL
     */
    private fun setAvailableMoves() {

        field[0][0].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[1][0].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[2][0].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[3][0].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[4][0].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[5][0].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[6][0].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[7][0].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[8][0].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[9][0].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[10][0].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)

        field[0][1].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[1][1].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[2][1].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[3][1].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[4][1].setAvailableMoves(up=false,upRight = false,right = false,downRight = true,down = false,downLeft = false,left = false,upLeft = false)
        field[5][1].setAvailableMoves(up=false,upRight = false,right = false,downRight = true,down = true,downLeft = true,left = false,upLeft = false)
        field[6][1].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = true,left = false,upLeft = false)
        field[7][1].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[8][1].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[9][1].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[10][1].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)

        field[0][2].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[1][2].setAvailableMoves(up=false,upRight = false,right = false,downRight = true,down = false,downLeft = false,left = false,upLeft = false)
        field[2][2].setAvailableMoves(up=false,upRight = false,right = false,downRight = true,down = true,downLeft = true,left = false,upLeft = false)
        field[3][2].setAvailableMoves(up=false,upRight = false,right = false,downRight = true,down = true,downLeft = true,left = false,upLeft = false)
        field[4][2].setAvailableMoves(up=false,upRight = true,right = true,downRight = true,down = true,downLeft = true,left = false,upLeft = false)
        field[6][2].setAvailableMoves(up=false,upRight = false,right = false,downRight = true,down = true,downLeft = true,left = true,upLeft = true)
        field[7][2].setAvailableMoves(up=false,upRight = false,right = false,downRight = true,down = true,downLeft = true,left = false,upLeft = false)
        field[8][2].setAvailableMoves(up=false,upRight = false,right = false,downRight = true,down = true,downLeft = true,left = false,upLeft = false)
        field[9][2].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = true,left = false,upLeft = false)
        field[10][2].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)

        field[0][3].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[1][3].setAvailableMoves(up=false,upRight = true,right = true,downRight = true,down = false,downLeft = false,left = false,upLeft = false)
        field[9][3].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = true,left = true,upLeft = true)
        field[10][3].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)

        field[0][4].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[1][4].setAvailableMoves(up=false,upRight = true,right = true,downRight = true,down = false,downLeft = false,left = false,upLeft = false)
        field[9][4].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = true,left = true,upLeft = true)
        field[10][4].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)

        field[0][5].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[1][5].setAvailableMoves(up=false,upRight = true,right = true,downRight = true,down = false,downLeft = false,left = false,upLeft = false)
        field[9][5].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = true,left = true,upLeft = true)
        field[10][5].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)

        field[0][6].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[1][6].setAvailableMoves(up=false,upRight = true,right = true,downRight = true,down = false,downLeft = false,left = false,upLeft = false)
        field[9][6].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = true,left = true,upLeft = true)
        field[10][6].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)

        field[0][7].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[1][7].setAvailableMoves(up=false,upRight = true,right = true,downRight = true,down = false,downLeft = false,left = false,upLeft = false)
        field[9][7].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = true,left = true,upLeft = true)
        field[10][7].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)

        field[0][8].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[1][8].setAvailableMoves(up=false,upRight = true,right = true,downRight = true,down = false,downLeft = false,left = false,upLeft = false)
        field[9][8].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = true,left = true,upLeft = true)
        field[10][8].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)

        field[0][9].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[1][9].setAvailableMoves(up=false,upRight = true,right = true,downRight = true,down = false,downLeft = false,left = false,upLeft = false)
        field[9][9].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = true,left = true,upLeft = true)
        field[10][9].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)

        field[0][10].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[1][10].setAvailableMoves(up=false,upRight = true,right = true,downRight = true,down = false,downLeft = false,left = false,upLeft = false)
        field[9][10].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = true,left = true,upLeft = true)
        field[10][10].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)

        field[0][11].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[1][11].setAvailableMoves(up=false,upRight = true,right = true,downRight = true,down = false,downLeft = false,left = false,upLeft = false)
        field[9][11].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = true,left = true,upLeft = true)
        field[10][11].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)

        field[0][12].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[1][12].setAvailableMoves(up=false,upRight = true,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[2][12].setAvailableMoves(up=true,upRight = true,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = true)
        field[3][12].setAvailableMoves(up=true,upRight = true,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = true)
        field[4][12].setAvailableMoves(up=true,upRight = true,right = true,downRight = true,down = false,downLeft = false,left = false,upLeft = true)
        field[6][12].setAvailableMoves(up=true,upRight = true,right = false,downRight = false,down = false,downLeft = true,left = true,upLeft = true)
        field[7][12].setAvailableMoves(up=true,upRight = true,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = true)
        field[8][12].setAvailableMoves(up=true,upRight = true,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = true)
        field[9][12].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = true)
        field[10][12].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)

        field[0][13].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[1][13].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[2][13].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[3][13].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[4][13].setAvailableMoves(up=false,upRight = true,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[5][13].setAvailableMoves(up=true,upRight = true,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = true)
        field[6][13].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = true)
        field[7][13].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[8][13].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[9][13].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[10][13].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)

        field[0][14].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[1][14].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[2][14].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[3][14].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[4][14].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[5][14].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[6][14].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[7][14].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[8][14].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[9][14].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)
        field[10][14].setAvailableMoves(up=false,upRight = false,right = false,downRight = false,down = false,downLeft = false,left = false,upLeft = false)

    }

    /**
     * move up (player move)
     * returning two points before and after move so they can be stored in database later
     */
    fun moveUp(myTurn: Boolean): PointsAfterMove {
        val pointsAfterMove = PointsAfterMove()
        val ball = findBall()
        val i = ball.x
        val j = ball.y
        field[i][j].ball = false
        field[i][j - 1].ball = true
        if (myTurn) {
            field[i][j].moveUp = Static.MOVE_DONE_BY_ME
            field[i][j - 1].moveDown = Static.MOVE_DONE_BY_ME
        } else {
            field[i][j].moveUp = Static.MOVE_DONE_BY_PHONE
            field[i][j - 1].moveDown = Static.MOVE_DONE_BY_PHONE
        }
        pointsAfterMove.beforeMovePoint = field[i][j]
        pointsAfterMove.afterMovePoint = field[i][j - 1]

        return pointsAfterMove
    }

    /**
     * move up right (player move)
     * returning two points before and after move so they can be stored in database later
     */
    fun moveUpRight(myTurn: Boolean): PointsAfterMove {
        val pointsAfterMove = PointsAfterMove()
        val ball = findBall()
        val i = ball.x
        val j = ball.y
        field[i][j].ball = false
        field[i + 1][j - 1].ball = true
        if (myTurn) {
            field[i][j].moveUpRight = Static.MOVE_DONE_BY_ME
            field[i + 1][j - 1].moveDownLeft = Static.MOVE_DONE_BY_ME
        } else {
            field[i][j].moveUpRight = Static.MOVE_DONE_BY_PHONE
            field[i + 1][j - 1].moveDownLeft = Static.MOVE_DONE_BY_PHONE
        }
        pointsAfterMove.beforeMovePoint = field[i][j]
        pointsAfterMove.afterMovePoint = field[i + 1][j - 1]

        return pointsAfterMove
    }

    /**
     * move right (player move)
     * returning two points before and after move so they can be stored in database later
     */
    fun moveRight(myTurn: Boolean): PointsAfterMove {
        val pointsAfterMove = PointsAfterMove()
        val ball = findBall()
        val i = ball.x
        val j = ball.y
        field[i][j].ball = false
        field[i + 1][j].ball = true
        if (myTurn) {
            field[i][j].moveRight = Static.MOVE_DONE_BY_ME
            field[i + 1][j].moveLeft = Static.MOVE_DONE_BY_ME
        } else {
            field[i][j].moveRight = Static.MOVE_DONE_BY_PHONE
            field[i + 1][j].moveLeft = Static.MOVE_DONE_BY_PHONE
        }
        pointsAfterMove.beforeMovePoint = field[i][j]
        pointsAfterMove.afterMovePoint = field[i + 1][j]

        return pointsAfterMove
    }

    /**
     * move down right (player move)
     * returning two points before and after move so they can be stored in database later
     */
    fun moveDownRight(myTurn: Boolean): PointsAfterMove {
        val pointsAfterMove = PointsAfterMove()
        val ball = findBall()
        val i = ball.x
        val j = ball.y
        field[i][j].ball = false
        field[i + 1][j + 1].ball = true
        if (myTurn) {
            field[i][j].moveDownRight = Static.MOVE_DONE_BY_ME
            field[i + 1][j + 1].moveUpLeft = Static.MOVE_DONE_BY_ME
        } else {
            field[i][j].moveDownRight = Static.MOVE_DONE_BY_PHONE
            field[i + 1][j + 1].moveUpLeft = Static.MOVE_DONE_BY_PHONE
        }
        pointsAfterMove.beforeMovePoint = field[i][j]
        pointsAfterMove.afterMovePoint = field[i + 1][j + 1]

        return pointsAfterMove
    }

    /**
     * move down (player move)
     * returning two points before and after move so they can be stored in database later
     */
    fun moveDown(myTurn: Boolean): PointsAfterMove {
        val pointsAfterMove = PointsAfterMove()
        val ball = findBall()
        val i = ball.x
        val j = ball.y
        field[i][j].ball = false
        field[i][j + 1].ball = true
        if (myTurn) {
            field[i][j].moveDown = Static.MOVE_DONE_BY_ME
            field[i][j + 1].moveUp = Static.MOVE_DONE_BY_ME
        } else {
            field[i][j].moveDown = Static.MOVE_DONE_BY_PHONE
            field[i][j + 1].moveUp = Static.MOVE_DONE_BY_PHONE
        }
        pointsAfterMove.beforeMovePoint = field[i][j]
        pointsAfterMove.afterMovePoint = field[i][j + 1]

        return pointsAfterMove
    }

    /**
     * move down left (player move)
     * returning two points before and after move so they can be stored in database later
     */
    fun moveDownLeft(myTurn: Boolean): PointsAfterMove {
        val pointsAfterMove = PointsAfterMove()
        val ball = findBall()
        val i = ball.x
        val j = ball.y
        field[i][j].ball = false
        field[i - 1][j + 1].ball = true
        if (myTurn) {
            field[i][j].moveDownLeft = Static.MOVE_DONE_BY_ME
            field[i - 1][j + 1].moveUpRight = Static.MOVE_DONE_BY_ME
        } else {
            field[i][j].moveDownLeft = Static.MOVE_DONE_BY_PHONE
            field[i - 1][j + 1].moveUpRight = Static.MOVE_DONE_BY_PHONE
        }
        pointsAfterMove.beforeMovePoint = field[i][j]
        pointsAfterMove.afterMovePoint = field[i - 1][j + 1]

        return pointsAfterMove
    }

    /**
     * move left (player move)
     * returning two points before and after move so they can be stored in database later
     */
    fun moveLeft(myTurn: Boolean): PointsAfterMove {
        val pointsAfterMove = PointsAfterMove()
        val ball = findBall()
        val i = ball.x
        val j = ball.y
        field[i][j].ball = false
        field[i - 1][j].ball = true
        if (myTurn) {
            field[i][j].moveLeft = Static.MOVE_DONE_BY_ME
            field[i - 1][j].moveRight = Static.MOVE_DONE_BY_ME
        } else {
            field[i][j].moveLeft = Static.MOVE_DONE_BY_PHONE
            field[i - 1][j].moveRight = Static.MOVE_DONE_BY_PHONE
        }
        pointsAfterMove.beforeMovePoint = field[i][j]
        pointsAfterMove.afterMovePoint = field[i - 1][j]

        return pointsAfterMove
    }


    /**
     * move up left (player move)
     * returning two points before and after move so they can be stored in database later
     */
    fun moveUpLeft(myTurn: Boolean): PointsAfterMove {
        val pointsAfterMove = PointsAfterMove()
        val ball = findBall()
        val i = ball.x
        val j = ball.y
        field[i][j].ball = false
        field[i - 1][j - 1].ball = true
        if (myTurn) {
            field[i][j].moveUpLeft = Static.MOVE_DONE_BY_ME
            field[i - 1][j - 1].moveDownRight = Static.MOVE_DONE_BY_ME
        } else {
            field[i][j].moveUpLeft = Static.MOVE_DONE_BY_PHONE
            field[i - 1][j - 1].moveDownRight = Static.MOVE_DONE_BY_PHONE
        }
        pointsAfterMove.beforeMovePoint = field[i][j]
        pointsAfterMove.afterMovePoint = field[i - 1][j - 1]

        return pointsAfterMove
    }

    /**
     * checks if ball stuck (no move available in any direction) and if another move is available
     * point is at the border of field or someone already made move to/from this point in any direction
     */
    fun checkIfStuckAndNextMove(direction: Int): StuckAndNextMove {
        var up = true
        var upRight = true
        var right = true
        var downRight = true
        var down = true
        var downLeft = true
        var left = true
        var upLeft = true
        val ball = findBall()

        if (field[ball.x][ball.y].moveUp == Static.MOVE_AVAILABLE) {
            up = false
        }
        if (field[ball.x][ball.y].moveUpRight == Static.MOVE_AVAILABLE) {
            upRight = false
        }
        if (field[ball.x][ball.y].moveRight == Static.MOVE_AVAILABLE) {
            right = false
        }
        if (field[ball.x][ball.y].moveDownRight == Static.MOVE_AVAILABLE) {
            downRight = false
        }
        if (field[ball.x][ball.y].moveDown == Static.MOVE_AVAILABLE) {
            down = false
        }
        if (field[ball.x][ball.y].moveDownLeft == Static.MOVE_AVAILABLE) {
            downLeft = false
        }
        if (field[ball.x][ball.y].moveLeft == Static.MOVE_AVAILABLE) {
            left = false
        }
        if (field[ball.x][ball.y].moveUpLeft == Static.MOVE_AVAILABLE) {
            upLeft = false
        }


        /**
         * if everything is true - stuck is true
         */
        val stuck = up and (upRight and (right and (downRight and (down and (downLeft and (left and (upLeft)))))))

        if (direction == Static.UP) {
            down = false
        }
        if (direction == Static.UP_RIGHT) {
            downLeft = false
        }
        if (direction == Static.RIGHT) {
            left = false
        }
        if (direction == Static.DOWN_RIGHT) {
            upLeft = false
        }
        if (direction == Static.DOWN) {
            up = false
        }
        if (direction == Static.DOWN_LEFT) {
            upRight = false
        }
        if (direction == Static.LEFT) {
            right = false
        }
        if (direction == Static.UP_LEFT) {
            downRight = false
        }

        /**
         * if any is true (except the last one we did) we have another move
         */
        val nextMove = up or (upRight or (right or (downRight or (down or (downLeft or (left or (upLeft)))))))

        return StuckAndNextMove(nextMove, stuck)
    }

    /**
     * checking if from given point on field we can make moves in every direction
     */
    fun checkIfMoveInDirectionIsAvailable(pointOnField: PointOnField): AvailableMoves {
        val availableMoves = AvailableMoves()
        if (pointOnField.moveDown == Static.MOVE_AVAILABLE) {
            availableMoves.down = true
        }
        if (pointOnField.moveDownLeft == Static.MOVE_AVAILABLE) {
            availableMoves.downLeft = true
        }
        if (pointOnField.moveDownRight == Static.MOVE_AVAILABLE) {
            availableMoves.downRight = true
        }
        if (pointOnField.moveLeft == Static.MOVE_AVAILABLE) {
            availableMoves.left = true
        }
        if (pointOnField.moveRight == Static.MOVE_AVAILABLE) {
            availableMoves.right = true
        }
        if (pointOnField.moveUp == Static.MOVE_AVAILABLE) {
            availableMoves.up = true
        }
        if (pointOnField.moveUpRight == Static.MOVE_AVAILABLE) {
            availableMoves.upRight = true
        }
        if (pointOnField.moveUpLeft == Static.MOVE_AVAILABLE) {
            availableMoves.upLeft = true
        }
        return availableMoves
    }

    /**
     * finding ball coordinates
     */
    fun findBall(): Point {
        val ball = Point()
        if (level != Static.HARD) {
            for (i in 0..10) {
                for (j in 0..14) {
                    if (field[i][j].ball) {
                        ball.x=i
                        ball.y=j
                        return ball
                    }
                }
            }
        } else {
            for (i in 0..12) {
                for (j in 0..20) {
                    if (field[i][j].ball) {
                        ball.x=i
                        ball.y=j
                        return ball
                    }
                }
            }
        }
        return ball

    }

    /**
     * returning point on field
     */
    fun getPoint(i: Int, j: Int): PointOnField {
        return field[i][j]
    }

    fun testMoveUp(field: Array<Array<PointOnField>>, ball: Point): Point {
        field[ball.x][ball.y].moveUp = Static.MOVE_CHECKING
        field[ball.x][ball.y - 1].moveDown = Static.MOVE_CHECKING
        return Point(ball.x, ball.y - 1)
    }

    fun testMoveUpRight(field: Array<Array<PointOnField>>, ball: Point): Point {
        field[ball.x][ball.y].moveUpRight = Static.MOVE_CHECKING
        field[ball.x + 1][ball.y - 1].moveDownLeft = Static.MOVE_CHECKING
        return Point(ball.x + 1, ball.y - 1)
    }

    fun testMoveRight(field: Array<Array<PointOnField>>, ball: Point): Point {
        field[ball.x][ball.y].moveRight = Static.MOVE_CHECKING
        field[ball.x + 1][ball.y].moveLeft = Static.MOVE_CHECKING
        return Point(ball.x + 1, ball.y)
    }

    fun testMoveDownRight(field: Array<Array<PointOnField>>, ball: Point): Point {
        field[ball.x][ball.y].moveDownRight = Static.MOVE_CHECKING
        field[ball.x + 1][ball.y + 1].moveUpLeft = Static.MOVE_CHECKING
        return Point(ball.x + 1, ball.y + 1)
    }

    fun testMoveDown(field: Array<Array<PointOnField>>, ball: Point): Point {
        field[ball.x][ball.y].moveDown = Static.MOVE_CHECKING
        field[ball.x][ball.y + 1].moveUp = Static.MOVE_CHECKING
        return Point(ball.x, ball.y + 1)
    }

    fun testMoveDownLeft(field: Array<Array<PointOnField>>, ball: Point): Point {
        field[ball.x][ball.y].moveDownLeft = Static.MOVE_CHECKING
        field[ball.x - 1][ball.y + 1].moveUpRight = Static.MOVE_CHECKING
        return Point(ball.x - 1, ball.y + 1)
    }

    fun testMoveLeft(field: Array<Array<PointOnField>>, ball: Point): Point {
        field[ball.x][ball.y].moveLeft = Static.MOVE_CHECKING
        field[ball.x - 1][ball.y].moveRight = Static.MOVE_CHECKING
        return Point(ball.x - 1, ball.y)
    }

    fun testMoveUpLeft(field: Array<Array<PointOnField>>, ball: Point): Point {
        field[ball.x][ball.y].moveUpLeft = Static.MOVE_CHECKING
        field[ball.x - 1][ball.y - 1].moveDownRight = Static.MOVE_CHECKING
        return Point(ball.x - 1, ball.y - 1)
    }

    fun bestMoveUp(field: Array<Array<PointOnField>>, ball: Point): Point {
        field[ball.x][ball.y].moveUp = Static.MOVE_BEST
        field[ball.x][ball.y - 1].moveDown = Static.MOVE_BEST
        return Point(ball.x, ball.y - 1)
    }

    fun bestMoveUpRight(field: Array<Array<PointOnField>>, ball: Point): Point {
        field[ball.x][ball.y].moveUpRight = Static.MOVE_BEST
        field[ball.x + 1][ball.y - 1].moveDownLeft = Static.MOVE_BEST
        return Point(ball.x + 1, ball.y - 1)
    }

    fun bestMoveRight(field: Array<Array<PointOnField>>, ball: Point): Point {
        field[ball.x][ball.y].moveRight = Static.MOVE_BEST
        field[ball.x + 1][ball.y].moveLeft = Static.MOVE_BEST
        return Point(ball.x + 1, ball.y)
    }

    fun bestMoveDownRight(field: Array<Array<PointOnField>>, ball: Point): Point {
        field[ball.x][ball.y].moveDownRight = Static.MOVE_BEST
        field[ball.x + 1][ball.y + 1].moveUpLeft = Static.MOVE_BEST
        return Point(ball.x + 1, ball.y + 1)
    }

    fun bestMoveDown(field: Array<Array<PointOnField>>, ball: Point): Point {
        field[ball.x][ball.y].moveDown = Static.MOVE_BEST
        field[ball.x][ball.y + 1].moveUp = Static.MOVE_BEST
        return Point(ball.x, ball.y + 1)
    }

    fun bestMoveDownLeft(field: Array<Array<PointOnField>>, ball: Point): Point {
        field[ball.x][ball.y].moveDownLeft = Static.MOVE_BEST
        field[ball.x - 1][ball.y + 1].moveUpRight = Static.MOVE_BEST
        return Point(ball.x - 1, ball.y + 1)
    }

    fun bestMoveLeft(field: Array<Array<PointOnField>>, ball: Point): Point {
        field[ball.x][ball.y].moveLeft = Static.MOVE_BEST
        field[ball.x - 1][ball.y].moveRight = Static.MOVE_BEST
        return Point(ball.x - 1, ball.y)
    }

    fun bestMoveUpLeft(field: Array<Array<PointOnField>>, ball: Point): Point {
        field[ball.x][ball.y].moveUpLeft = Static.MOVE_BEST
        field[ball.x - 1][ball.y - 1].moveDownRight = Static.MOVE_BEST
        return Point(ball.x - 1, ball.y - 1)
    }

    fun checkIfNextMoveAvailable(pointOnField:PointOnField):Boolean{
        val availableMoves = checkIfMoveInDirectionIsAvailable(pointOnField)

        return availableMoves.up or
                (availableMoves.upRight or
                        (availableMoves.upLeft or
                                (availableMoves.left or
                                        (availableMoves.right or
                                                (availableMoves.downLeft or
                                                        (availableMoves.downRight or
                                                                (availableMoves.down)))))))

    }

    fun clearTestMoves() {
        if (level != Static.HARD) {
            for (i in 0..8) {
                for (j in 0..12) {
                    if (field[i][j].moveUp == Static.MOVE_CHECKING || field[i][j].moveUp == Static.MOVE_BEST) {
                        field[i][j].moveUp = Static.MOVE_AVAILABLE
                    }
                    if (field[i][j].moveUpRight == Static.MOVE_CHECKING || field[i][j].moveUp == Static.MOVE_BEST) {
                        field[i][j].moveUpRight = Static.MOVE_AVAILABLE
                    }
                    if (field[i][j].moveRight == Static.MOVE_CHECKING || field[i][j].moveUp == Static.MOVE_BEST) {
                        field[i][j].moveRight = Static.MOVE_AVAILABLE
                    }
                    if (field[i][j].moveDownRight == Static.MOVE_CHECKING || field[i][j].moveUp == Static.MOVE_BEST) {
                        field[i][j].moveDownRight = Static.MOVE_AVAILABLE
                    }
                    if (field[i][j].moveDown == Static.MOVE_CHECKING || field[i][j].moveUp == Static.MOVE_BEST) {
                        field[i][j].moveDown = Static.MOVE_AVAILABLE
                    }
                    if (field[i][j].moveDownLeft == Static.MOVE_CHECKING || field[i][j].moveUp == Static.MOVE_BEST) {
                        field[i][j].moveDownLeft = Static.MOVE_AVAILABLE
                    }
                    if (field[i][j].moveLeft == Static.MOVE_CHECKING || field[i][j].moveUp == Static.MOVE_BEST) {
                        field[i][j].moveLeft = Static.MOVE_AVAILABLE
                    }
                    if (field[i][j].moveUpLeft == Static.MOVE_CHECKING || field[i][j].moveUp == Static.MOVE_BEST) {
                        field[i][j].moveUpLeft = Static.MOVE_AVAILABLE
                    }

                }
            }

        } else {
            for (i in 0..12) {
                for (j in 0..20) {
                    if (field[i][j].moveUp == Static.MOVE_CHECKING || field[i][j].moveUp == Static.MOVE_BEST) {
                        field[i][j].moveUp = Static.MOVE_AVAILABLE
                    }
                    if (field[i][j].moveUpRight == Static.MOVE_CHECKING || field[i][j].moveUp == Static.MOVE_BEST) {
                        field[i][j].moveUpRight = Static.MOVE_AVAILABLE
                    }
                    if (field[i][j].moveRight == Static.MOVE_CHECKING || field[i][j].moveUp == Static.MOVE_BEST) {
                        field[i][j].moveRight = Static.MOVE_AVAILABLE
                    }
                    if (field[i][j].moveDownRight == Static.MOVE_CHECKING || field[i][j].moveUp == Static.MOVE_BEST) {
                        field[i][j].moveDownRight = Static.MOVE_AVAILABLE
                    }
                    if (field[i][j].moveDown == Static.MOVE_CHECKING || field[i][j].moveUp == Static.MOVE_BEST) {
                        field[i][j].moveDown = Static.MOVE_AVAILABLE
                    }
                    if (field[i][j].moveDownLeft == Static.MOVE_CHECKING || field[i][j].moveUp == Static.MOVE_BEST) {
                        field[i][j].moveDownLeft = Static.MOVE_AVAILABLE
                    }
                    if (field[i][j].moveLeft == Static.MOVE_CHECKING || field[i][j].moveUp == Static.MOVE_BEST) {
                        field[i][j].moveLeft = Static.MOVE_AVAILABLE
                    }
                    if (field[i][j].moveUpLeft == Static.MOVE_CHECKING || field[i][j].moveUp == Static.MOVE_BEST) {
                        field[i][j].moveUpLeft = Static.MOVE_AVAILABLE
                    }

                }
            }

        }
    }

    fun checkIfMoveInDirectionAvailable(direction:Int,ball:Point):Boolean {
        return when (direction) {
            Static.UP -> field[ball.x][ball.y].moveUp == Static.MOVE_AVAILABLE
            Static.UP_RIGHT -> field[ball.x][ball.y].moveUpRight == Static.MOVE_AVAILABLE
            Static.RIGHT -> field[ball.x][ball.y].moveRight == Static.MOVE_AVAILABLE
            Static.DOWN_RIGHT -> field[ball.x][ball.y].moveDownRight == Static.MOVE_AVAILABLE
            Static.DOWN ->  field[ball.x][ball.y].moveDown == Static.MOVE_AVAILABLE
            Static.DOWN_LEFT -> field[ball.x][ball.y].moveDownLeft == Static.MOVE_AVAILABLE
            Static.LEFT -> field[ball.x][ball.y].moveLeft == Static.MOVE_AVAILABLE
            Static.UP_LEFT -> field[ball.x][ball.y].moveUpLeft == Static.MOVE_AVAILABLE
            else -> false
        }
    }

    fun makeOpponentMoveInDirection(direction:Int){
        when(direction){
            Static.UP -> moveUp(false)
            Static.UP_RIGHT -> moveUpRight(false)
            Static.RIGHT -> moveRight(false)
            Static.DOWN_RIGHT -> moveDownRight(false)
            Static.DOWN -> moveDown(false)
            Static.DOWN_LEFT -> moveDownLeft(false)
            Static.LEFT -> moveLeft(false)
            Static.UP_LEFT -> moveUpLeft(false)
        }
    }

}