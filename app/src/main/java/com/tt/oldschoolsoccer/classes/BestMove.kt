package com.tt.oldschoolsoccer.classes

class BestMove() {
    var distance:Int?=null
    lateinit var moveNode:MoveNode

    fun updateBestMove(newDistance:Int,newMoveNode:MoveNode){
        if(this.distance==null){
            this.distance=newDistance
            this.moveNode=newMoveNode
        }else{
            if(this.distance!!>newDistance){
                this.distance=newDistance
                this.moveNode=newMoveNode
            }
        }
    }
}