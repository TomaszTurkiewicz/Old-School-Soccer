package com.tt.oldschoolsoccer.classes

class UserRanking(var id:String = "",
                  var userName:String = "",
                  var easyGame:Double = 0.0,
                  var normalGame:Double = 0.0,
                  var hardGame:Double = 0.0,
                  var multiGame:Double = 0.0,
                  var totalScore:Double = 0.0,
                  var playWithPeople:Boolean = false,
                  var icon:UserIconColors = UserIconColors()) {

    fun createUserRanking(user: User):UserRanking{
        this.id = user.id
        this.userName = user.userName
        this.playWithPeople = user.playWithPeople
        this.icon = user.icon
        val easyGame = calculateGame(user.easyGame)
        this.easyGame = easyGame
        val normalGame = calculateGame(user.normalGame)
        this.normalGame = normalGame
        val hardGame = calculateGame(user.hardGame)
        this.hardGame = hardGame
        val multiGame = calculateGame(user.multiGame)
        this.multiGame = multiGame
        this.totalScore = calculateTotalScore(easyGame,normalGame,hardGame,multiGame)

        return this
    }

    private fun calculateTotalScore(easyGame: Double, normalGame: Double, hardGame: Double, multiGame: Double): Double {

        var totalScore = (easyGame+2*normalGame+3*hardGame+3*multiGame)/9

        totalScore*=100
        totalScore = Math.round(totalScore).toDouble()
        totalScore/=100
        return totalScore
    }

    private fun calculateGame(game: Game): Double {
        val wins = (game.win*3).toDouble()
        val ties = game.tie.toDouble()
        val total = (game.numberOfGames*3).toDouble()
        var number:Double = ((wins+ties)/total)*100

        number*=100
        number = Math.round(number).toDouble()
        number/=100
        return number
    }


}