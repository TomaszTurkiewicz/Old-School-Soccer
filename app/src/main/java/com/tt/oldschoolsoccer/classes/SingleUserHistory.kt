package com.tt.oldschoolsoccer.classes

class SingleUserHistory (
    var userId:String,
    var noOfGames:Int,
    var win:Int,
    var lose:Int,
    var tie:Int
){
    constructor():this(
        userId = "",
        noOfGames = 0,
        win = 0,
        lose = 0,
        tie = 0
    )
}