package com.tt.oldschoolsoccer.classes

class MultiPlayerHistory (var history:ArrayList<SingleUserHistory>) {
    constructor():this(
        history = ArrayList<SingleUserHistory>()
    )

    fun addWin(userId: String){
        for(i in history.indices){
            if(history[i].userId==userId) {
                var win = history[i].win
                win+=1
                history[i].win = win
            }
        }
    }

    fun addTie(userId: String){
        for(i in history.indices){
            if(history[i].userId==userId) {
                var tie = history[i].tie
                tie+=1
                history[i].tie = tie
            }
        }
    }

    fun addLose(userId: String){
        for(i in history.indices){
            if(history[i].userId==userId) {
                var lose = history[i].lose
                lose+=1
                history[i].lose = lose
            }
        }
    }

    fun updateNoOfGames(userId:String){
        val contain = contains(userId)
        if(contain){
            updateGamesForUser(userId)
        }
        else{
            addUSerAndAddGame(userId)
        }
    }

    private fun addUSerAndAddGame(userId: String) {
        val singleHistory = SingleUserHistory()
        singleHistory.userId = userId
        singleHistory.noOfGames=1
        history.add(singleHistory)

    }

    private fun updateGamesForUser(userId: String) {
        for(i in history.indices){
            if(history[i].userId==userId) {
                var noOfGames = history[i].noOfGames
                noOfGames+=1
                history[i].noOfGames = noOfGames
            }
        }
    }

    fun contains(userId: String):Boolean{
        var contain = false
        for(i in history.indices){
            if(history[i].userId == userId){
                contain = true
            }
        }
        return contain
    }
}