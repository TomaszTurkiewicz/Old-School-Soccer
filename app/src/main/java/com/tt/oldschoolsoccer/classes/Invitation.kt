package com.tt.oldschoolsoccer.classes

class Invitation (
        var player: String = "",
        var opponent:String = "",
        var myAccept:Boolean = false,
        var opponentAccept:Boolean = false,
        var battleName:String = "",
        var orientation:Int = Static.ORIENTATION_NOT_SET_UP
        ) {
}