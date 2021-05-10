package com.tt.oldschoolsoccer.classes

class MultiPlayerMatch (var turn:Int,
                        var moveList:ArrayList<MultiPlayerMove>,
                        var time:Long){

    constructor() : this (
        turn = 0,
        moveList = ArrayList<MultiPlayerMove>(),
        time = 0
    )
}