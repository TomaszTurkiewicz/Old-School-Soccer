package com.tt.oldschoolsoccer.classes

import com.tt.oldschoolsoccer.database.UserDB

/**
 * holding user info
 */
 data class User (var id:String = "",
                    var userName:String = "",
                    var easyGame:Game = Game(),
                    var normalGame:Game = Game(),
                    var hardGame:Game = Game(),
                    var multiGame:Game = Game(),
                    var playWithPeople:Boolean = false,
                    var icon:UserIconColors = UserIconColors())   {

   fun userFromDB(userDB: UserDB):User{
   this.id = userDB.id
   this.userName = userDB.name
   this.easyGame.numberOfGames = userDB.easyGameNumberOfGame
   this.easyGame.win = userDB.easyGameWin
   this.easyGame.lose = userDB.easyGameLose
   this.easyGame.tie = userDB.easyGameTie
   this.normalGame.numberOfGames = userDB.normalGameNumberOfGame
   this.normalGame.win = userDB.normalGameWin
   this.normalGame.lose = userDB.normalGameLose
   this.normalGame.tie = userDB.normalGameTie
   this.hardGame.numberOfGames = userDB.hardGameNumberOfGame
   this.hardGame.win = userDB.hardGameWin
   this.hardGame.lose = userDB.hardGameLose
   this.hardGame.tie = userDB.hardGameTie
   this.multiGame.numberOfGames = userDB.multiGameNumberOfGame
   this.multiGame.win = userDB.multiGameWin
   this.multiGame.lose = userDB.multiGameLose
   this.multiGame.tie = userDB.multiGameTie
   this.playWithPeople = userDB.playWithPeople
   this.icon.setBackgroundColor(userDB.backgroundColor)
   this.icon.setLeftArmColor(userDB.leftArmColor)
   this.icon.setRightArmColor(userDB.rightArmColor)
   this.icon.setOverArmColor(userDB.overArmsColor)
   this.icon.setBodyLeftColor(userDB.bodyLeftColor)
   this.icon.setBodyRightColor(userDB.bodyRightColor)
   this.icon.setTrousersExternalColor(userDB.trouserExternalColor)
   this.icon.setTrousersInternalColor(userDB.trousersInternalColor)
   this.icon.setTrousersBodyColor(userDB.trousersBody)
   return this
  }

}