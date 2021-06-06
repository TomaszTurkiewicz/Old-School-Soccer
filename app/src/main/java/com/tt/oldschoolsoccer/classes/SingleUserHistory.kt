package com.tt.oldschoolsoccer.classes

class SingleUserHistory (
    var userId:String,
    var name:String?,
    var icon:UserIconColors?,
    var noOfGames:Int,
    var win:Int,
    var lose:Int,
    var tie:Int
){
    constructor():this(
        userId = "",
        name = null,
        icon = null,
        noOfGames = 0,
        win = 0,
        lose = 0,
        tie = 0
    )



    fun score():Double{
        var score:Double = (this.win/this.noOfGames).toDouble()
        score*=100
        score = Math.round(score).toDouble()
        score/100
        return score
    }


}