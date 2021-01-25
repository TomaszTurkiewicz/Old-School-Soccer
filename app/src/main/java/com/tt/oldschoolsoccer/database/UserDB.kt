package com.tt.oldschoolsoccer.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tt.oldschoolsoccer.classes.User


/**
 * class for holding user statistic in room database
 *
 */

@Entity
data class UserDB(
        @PrimaryKey(autoGenerate = false)
        var id:String = "",
        var name: String = "",
        var easyGameWin: Int = 0,
        var easyGameLose: Int = 0,
        var easyGameTie: Int = 0,
        var easyGameNumberOfGame: Int = 0,
        var normalGameWin: Int = 0,
        var normalGameLose: Int = 0,
        var normalGameTie: Int = 0,
        var normalGameNumberOfGame: Int = 0,
        var hardGameWin: Int = 0,
        var hardGameLose: Int = 0,
        var hardGameTie: Int = 0,
        var hardGameNumberOfGame: Int = 0,
        var multiGameWin: Int = 0,
        var multiGameLose: Int = 0,
        var multiGameTie: Int = 0,
        var multiGameNumberOfGame: Int = 0,
) {
    fun dbUser(user: User): UserDB{

       this.id = user.id
       this.name = user.userName
       this.easyGameWin = user.easyGame.win
        this.easyGameLose = user.easyGame.lose
        this.easyGameTie = user.easyGame.tie
        this.easyGameNumberOfGame = user.easyGame.numberOfGames
        this.normalGameWin = user.normalGame.win
        this.normalGameLose = user.normalGame.lose
        this.normalGameTie = user.normalGame.tie
        this.normalGameNumberOfGame = user.normalGame.numberOfGames
        this.hardGameWin = user.hardGame.win
        this.hardGameLose = user.hardGame.lose
        this.hardGameTie = user.hardGame.tie
        this.hardGameNumberOfGame = user.hardGame.numberOfGames
        this.multiGameWin = user.multiGame.win
        this.multiGameLose = user.multiGame.lose
        this.multiGameTie = user.multiGame.tie
        this.multiGameNumberOfGame = user.multiGame.numberOfGames
        return this
    }
}