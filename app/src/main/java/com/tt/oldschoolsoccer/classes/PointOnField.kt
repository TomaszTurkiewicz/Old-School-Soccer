package com.tt.oldschoolsoccer.classes

class PointOnField(var moveUp:Boolean?=false,
                   var moveUpRight:Boolean?=false,
                   var moveRight:Boolean?=false,
                   var moveDownRight: Boolean?=false,
                   var moveDown:Boolean?=false,
                   var moveDownLeft:Boolean?=false,
                   var moveLeft:Boolean?=false,
                   var moveUpLeft:Boolean?=false,
                   var x:Int=0,
                   var y:Int=0,
                    var ball:Boolean = false) {

    fun setAvailableMoves(up:Boolean?,upRight:Boolean?,right:Boolean?,downRight:Boolean?,down:Boolean?,downLeft:Boolean?,left:Boolean?,upLeft:Boolean?){
        this.moveUp=up
        this.moveUpRight=upRight
        this.moveRight=right
        this.moveDownRight=downRight
        this.moveDown=down
        this.moveDownLeft=downLeft
        this.moveLeft=left
        this.moveUpLeft=upLeft
    }
}