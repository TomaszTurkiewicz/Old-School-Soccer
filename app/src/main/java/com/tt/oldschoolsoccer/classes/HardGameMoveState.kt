package com.tt.oldschoolsoccer.classes

class HardGameMoveState {
    private var turn = Static.CHECKING
    private var nextMyTurn = true

    fun nextMovePlayer(){
        this.turn = Static.PLAYER
    }

    fun nextMovePhone(){
        this.turn = Static.PHONE
    }

    fun nextMovePlayerAfterChecking(){
        this.turn = Static.CHECKING
        this.nextMyTurn = true
    }

    fun nextMovePhoneAfterChecking(){
        this.turn = Static.CHECKING
        this.nextMyTurn = false
    }

    fun getTurn():Int = this.turn

    fun getNextMyTurn():Boolean = this.nextMyTurn

    fun setMoveState(turn:Int,nextMyTurn:Boolean){
        when (turn) {
            Static.PLAYER -> {
                nextMovePlayer()
            }
            Static.PHONE -> {
                nextMovePhone()
            }
            else -> {
                if(nextMyTurn){
                    nextMovePlayerAfterChecking()
                }else{
                    nextMovePhoneAfterChecking()
                }
            }
        }
    }

}

