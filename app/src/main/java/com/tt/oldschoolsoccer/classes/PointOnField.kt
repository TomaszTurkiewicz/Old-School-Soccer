package com.tt.oldschoolsoccer.classes

class PointOnField(var moveUp:MovePoint=MovePoint(),
                   var moveUpRight:MovePoint=MovePoint(),
                   var moveRight:MovePoint=MovePoint(),
                   var moveDownRight: MovePoint=MovePoint(),
                   var moveDown:MovePoint=MovePoint(),
                   var moveDownLeft:MovePoint=MovePoint(),
                   var moveLeft:MovePoint=MovePoint(),
                   var moveUpLeft:MovePoint=MovePoint(),
                   var x:Int=0,
                   var y:Int=0,
                    var ball:Boolean = false) {

    fun setAvailableMoves(up:Boolean,upRight:Boolean,right:Boolean,downRight:Boolean,down:Boolean,downLeft:Boolean,left:Boolean,upLeft:Boolean){
        this.moveUp.create(up)
        this.moveUpRight.create(upRight)
        this.moveRight.create(right)
        this.moveDownRight.create(downRight)
        this.moveDown.create(down)
        this.moveDownLeft.create(downLeft)
        this.moveLeft.create(left)
        this.moveUpLeft.create(upLeft)
    }
}