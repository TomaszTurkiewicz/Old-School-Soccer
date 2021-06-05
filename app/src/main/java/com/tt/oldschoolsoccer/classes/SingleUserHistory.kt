package com.tt.oldschoolsoccer.classes

class SingleUserHistory (
    var userId:String,
    var icon:UserIconColors?,
    var noOfGames:Int,
    var win:Int,
    var lose:Int,
    var tie:Int
){
    constructor():this(
        userId = "",
        icon = null,
        noOfGames = 0,
        win = 0,
        lose = 0,
        tie = 0
    )
}