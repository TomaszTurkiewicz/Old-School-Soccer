package com.tt.oldschoolsoccer.classes

import android.graphics.Point
import com.tt.oldschoolsoccer.database.PointOnField
import java.lang.Math.abs

class MoveNode(parentNode: MoveNode?,direction:Int?,field: Array<Array<PointOnField>>,ball:Point,bestMove: BestMove)
{
        var comingDirection:Int?=null
        private var availableMoves = AvailableMoves()
        var parentNode:MoveNode? = null
        private var nextMove = false

        private var moveDownNode:MoveNode? = null
        private var moveDownLeftNode:MoveNode? = null
        private var moveDownRightNode:MoveNode? = null
        private var moveLeftNode:MoveNode? = null
        private var moveRightNode:MoveNode? = null
        private var moveUpLeftNode:MoveNode? = null
        private var moveUpRightNode:MoveNode? = null
        private var moveUpNode:MoveNode? = null

        private var distance:Int = (12-ball.y)+(abs(4-ball.x))




        init {


                direction?.let { comingDirection=direction }
                availableMoves = GameField().checkIfMoveInDirectionIsAvailable(field[ball.x][ball.y])
            parentNode?.let { this.parentNode = parentNode }
                nextMove = checkNextMove(field[ball.x][ball.y])
                if(nextMove){
                        if(availableMoves.down){
                                val tmpBall = GameField().testMoveDown(field,ball)
                                moveDownNode = MoveNode(this,Static.DOWN,field,tmpBall,bestMove)
                        }
                        if(availableMoves.downLeft){
                                val tmpBall = GameField().testMoveDownLeft(field,ball)
                                moveDownLeftNode = MoveNode(this,Static.DOWN_LEFT,field,tmpBall,bestMove)
                        }
                        if(availableMoves.downRight){
                                val tmpBall = GameField().testMoveDownRight(field,ball)
                                moveDownRightNode = MoveNode(this,Static.DOWN_RIGHT,field,tmpBall,bestMove)
                        }
                        if(availableMoves.left){
                                val tmpBall = GameField().testMoveLeft(field,ball)
                                moveLeftNode = MoveNode(this,Static.LEFT,field,tmpBall,bestMove)
                        }
                        if(availableMoves.right){
                                val tmpBall = GameField().testMoveRight(field,ball)
                                moveRightNode = MoveNode(this,Static.RIGHT,field,tmpBall,bestMove)
                        }
                        if(availableMoves.upLeft){
                                val tmpBall = GameField().testMoveUpLeft(field,ball)
                                moveUpLeftNode = MoveNode(this,Static.UP_LEFT,field,tmpBall,bestMove)
                        }
                        if(availableMoves.upRight){
                                val tmpBall = GameField().testMoveUpRight(field,ball)
                                moveUpRightNode = MoveNode(this,Static.UP_RIGHT,field,tmpBall,bestMove)
                        }
                        if(availableMoves.up){
                                val tmpBall = GameField().testMoveUp(field,ball)
                                moveUpNode = MoveNode(this,Static.UP,field,tmpBall,bestMove)
                        }

                }
                else{
                        bestMove.updateBestMove(distance,this)
                }




        }



        private fun checkNextMove(pointOnField: PointOnField):Boolean {
                if(pointOnField.moveDown==Static.MOVE_FORBIDDEN){
                        return true
                }
                if(pointOnField.moveDown==Static.MOVE_DONE_BY_PHONE){
                        return true
                }
                if(pointOnField.moveDown==Static.MOVE_DONE_BY_ME){
                        return true
                }

                if(pointOnField.moveDownLeft==Static.MOVE_FORBIDDEN){
                        return true
                }
                if(pointOnField.moveDownLeft==Static.MOVE_DONE_BY_PHONE){
                        return true
                }
                if(pointOnField.moveDownLeft==Static.MOVE_DONE_BY_ME){
                        return true
                }
                if(pointOnField.moveDownRight==Static.MOVE_FORBIDDEN){
                        return true
                }
                if(pointOnField.moveDownRight==Static.MOVE_DONE_BY_PHONE){
                        return true
                }
                if(pointOnField.moveDownRight==Static.MOVE_DONE_BY_ME){
                        return true
                }
                if(pointOnField.moveLeft==Static.MOVE_FORBIDDEN){
                        return true
                }
                if(pointOnField.moveLeft==Static.MOVE_DONE_BY_PHONE){
                        return true
                }
                if(pointOnField.moveLeft==Static.MOVE_DONE_BY_ME){
                        return true
                }
                if(pointOnField.moveRight==Static.MOVE_FORBIDDEN){
                        return true
                }
                if(pointOnField.moveRight==Static.MOVE_DONE_BY_PHONE){
                        return true
                }
                if(pointOnField.moveRight==Static.MOVE_DONE_BY_ME){
                        return true
                }
                if(pointOnField.moveUpLeft==Static.MOVE_FORBIDDEN){
                        return true
                }
                if(pointOnField.moveUpLeft==Static.MOVE_DONE_BY_PHONE){
                        return true
                }
                if(pointOnField.moveUpLeft==Static.MOVE_DONE_BY_ME){
                        return true
                }
                if(pointOnField.moveUpRight==Static.MOVE_FORBIDDEN){
                        return true
                }
                if(pointOnField.moveUpRight==Static.MOVE_DONE_BY_PHONE){
                        return true
                }
                if(pointOnField.moveUpRight==Static.MOVE_DONE_BY_ME){
                        return true
                }
                if(pointOnField.moveUp==Static.MOVE_FORBIDDEN){
                        return true
                }
                if(pointOnField.moveUp==Static.MOVE_DONE_BY_PHONE){
                        return true
                }
                if(pointOnField.moveUp==Static.MOVE_DONE_BY_ME){
                        return true
                }
                return false
        }


}