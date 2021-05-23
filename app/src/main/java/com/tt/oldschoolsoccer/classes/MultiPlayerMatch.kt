package com.tt.oldschoolsoccer.classes

class MultiPlayerMatch (var turn:Int,
                        var moveList:ArrayList<MultiPlayerMove>,
                        var time:Long,
                        var endGame:Int){

    constructor() : this (
        turn = 0,
        moveList = ArrayList<MultiPlayerMove>(),
        time = 0,
        endGame = Static.NOT_SET_UP
    )
}