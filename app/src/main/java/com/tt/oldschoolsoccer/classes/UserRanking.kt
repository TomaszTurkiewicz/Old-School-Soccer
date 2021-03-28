package com.tt.oldschoolsoccer.classes

import com.tt.oldschoolsoccer.database.UserDB

class UserRanking(var id:String = "",
                  var userName:String = "",
                  var easyGame:Double = 0.0,
                  var easyNoOfGames:Int = 0,
                  var normalGame:Double = 0.0,
                  var normalNoOfGames:Int = 0,
                  var hardGame:Double = 0.0,
                  var hardNoOfGames:Int = 0,
                  var multiGame:Double = 0.0,
                  var multiNoOfGames:Int = 0,
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
        this.easyNoOfGames = user.easyGame.numberOfGames
        val normalGame = calculateGame(user.normalGame)
        this.normalGame = normalGame
        this.normalNoOfGames = user.normalGame.numberOfGames
        val hardGame = calculateGame(user.hardGame)
        this.hardGame = hardGame
        this.hardNoOfGames = user.hardGame.numberOfGames
        val multiGame = calculateGame(user.multiGame)
        this.multiGame = multiGame
        this.multiNoOfGames = user.multiGame.numberOfGames
        this.totalScore = calculateTotalScore(easyGame,normalGame,hardGame,multiGame)

        return this
    }

    fun createUserRankingFromDB(userDB: UserDB):UserRanking{
        this.id = userDB.id
        this.userName = userDB.name
        this.playWithPeople = userDB.playWithPeople
        this.icon.setBackgroundColor(userDB.backgroundColor)
        this.icon.setBodyLeftColor(userDB.bodyLeftColor)
        this.icon.setBodyRightColor(userDB.bodyRightColor)
        this.icon.setLeftArmColor(userDB.leftArmColor)
        this.icon.setRightArmColor(userDB.rightArmColor)
        this.icon.setOverArmColor(userDB.overArmsColor)
        this.icon.setTrousersBodyColor(userDB.trousersBody)
        this.icon.setTrousersExternalColor(userDB.trouserExternalColor)
        this.icon.setTrousersInternalColor(userDB.trousersInternalColor)
        val easyGameO = Game(userDB.easyGameNumberOfGame,userDB.easyGameWin,userDB.easyGameTie,userDB.easyGameLose)
        val easyGame = calculateGame(easyGameO)
        this.easyGame = easyGame
        this.easyNoOfGames = userDB.easyGameNumberOfGame

        val normalGameO = Game(userDB.normalGameNumberOfGame,userDB.normalGameWin,userDB.normalGameTie,userDB.normalGameLose)
        val normalGame = calculateGame(normalGameO)
        this.normalGame = normalGame
        this.normalNoOfGames = userDB.normalGameNumberOfGame

        val hardGameO = Game(userDB.hardGameNumberOfGame,userDB.hardGameWin,userDB.hardGameTie,userDB.hardGameLose)
        val hardGame = calculateGame(hardGameO)
        this.hardGame = hardGame
        this.hardNoOfGames = userDB.hardGameNumberOfGame

        val multiGameO = Game(userDB.multiGameNumberOfGame,userDB.multiGameWin,userDB.multiGameTie,userDB.multiGameLose)
        val multiGame = calculateGame(multiGameO)
        this.multiGame = multiGame
        this.multiNoOfGames = userDB.multiGameNumberOfGame


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