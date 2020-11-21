package com.tt.oldschoolsoccer.classes

class MovePoint ( var moveDirection:Boolean? =false,
        var playerMove:Int=Static.NO_ONE)
{
     fun create(move:Boolean){
        moveDirection = if(move){
            false
        }
        else{
            null
        }
        playerMove = Static.NO_ONE
    }
}