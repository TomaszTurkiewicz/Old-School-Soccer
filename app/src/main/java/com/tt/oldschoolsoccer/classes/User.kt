package com.tt.oldschoolsoccer.classes

import com.tt.oldschoolsoccer.database.UserDB

/**
 * holding user info
 */
 data class User (var id:String = "",
                    var userName:String = "ANONYMOUS",
                    var easyGame:Game = Game(),
                    var normalGame:Game = Game(),
                    var hardGame:Game = Game(),
                    var multiGame:Game = Game())   {

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
   return this
  }

}