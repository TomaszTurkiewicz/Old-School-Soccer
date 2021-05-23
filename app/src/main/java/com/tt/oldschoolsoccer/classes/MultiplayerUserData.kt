package com.tt.oldschoolsoccer.classes

class MultiplayerUserData (var matchCounterAdded:Boolean, var time:Long, var gameReady:Boolean){

    constructor():this(
        matchCounterAdded=false,
        time = 0,
        gameReady=false
    )
}