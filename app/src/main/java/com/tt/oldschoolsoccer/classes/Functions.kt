package com.tt.oldschoolsoccer.classes

import android.content.Context
import android.view.inspector.StaticInspectionCompanionProvider

class Functions {
    companion object{

        /**
         * saves ScreenUnit to shared preferences for making UI in different activities
         */
        fun saveScreenUnit(context: Context?,screenUnit:Int){
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences("ScreenUnit",Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putInt("ScreenUnit",screenUnit)
                editor.apply()
            }
        }

        /**
         * saves user statistics to shared preferences
         */
        fun saveUserToSharedPreferences(context: Context?, user:User){
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences(user.id,Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("USER_NAME",user.userName)
                editor.putInt("EASY_NO_OF_GAMES",user.easyGame.numberOfGames)
                editor.putInt("EASY_LOSE",user.easyGame.lose)
                editor.putInt("EASY_TIE",user.easyGame.tie)
                editor.putInt("EASY_WIN",user.easyGame.win)
                editor.putInt("NORMAL_NO_OF_GAMES",user.normalGame.numberOfGames)
                editor.putInt("NORMAL_LOSE",user.normalGame.lose)
                editor.putInt("NORMAL_TIE",user.normalGame.tie)
                editor.putInt("NORMAL_WIN",user.normalGame.win)
                editor.putInt("HARD_NO_OF_GAMES",user.hardGame.numberOfGames)
                editor.putInt("HARD_LOSE",user.hardGame.lose)
                editor.putInt("HARD_TIE",user.hardGame.tie)
                editor.putInt("HARD_WIN",user.hardGame.win)
                editor.putInt("MULTI_NO_OF_GAMES",user.multiGame.numberOfGames)
                editor.putInt("MULTI_LOSE",user.multiGame.lose)
                editor.putInt("MULTI_TIE",user.multiGame.tie)
                editor.putInt("MULTI_WIN",user.multiGame.win)
                editor.apply()
            }
        }

        /**
         * reads screenUnit from shared preferences
         */
        fun readScreenUnit(context: Context?):Int{
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences("ScreenUnit",Context.MODE_PRIVATE)
                return sharedPreferences.getInt("ScreenUnit",-1)
            }
        return -1
        }

        /**
         * reads user statistics from shared preferences
         */
        fun readUserFromSharedPreferences(context: Context?,userId:String):User{
            var userName = ""
            val easyGame:Game = Game()
            val normalGame:Game = Game()
            val hardGame:Game = Game()
            val multiGame:Game = Game()
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences(userId,Context.MODE_PRIVATE)
                userName = sharedPreferences.getString("USER_NAME", "").toString()
                easyGame.numberOfGames = sharedPreferences.getInt("EASY_NO_OF_GAMES",0)
                easyGame.lose = sharedPreferences.getInt("EASY_LOSE",0)
                easyGame.tie = sharedPreferences.getInt("EASY_TIE",0)
                easyGame.win = sharedPreferences.getInt("EASY_WIN",0)
                normalGame.numberOfGames = sharedPreferences.getInt("NORMAL_NO_OF_GAMES",0)
                normalGame.lose = sharedPreferences.getInt("NORMAL_LOSE",0)
                normalGame.tie = sharedPreferences.getInt("NORMAL_TIE",0)
                normalGame.win = sharedPreferences.getInt("NORMAL_WIN",0)
                hardGame.numberOfGames = sharedPreferences.getInt("HARD_NO_OF_GAMES",0)
                hardGame.lose = sharedPreferences.getInt("HARD_LOSE",0)
                hardGame.tie = sharedPreferences.getInt("HARD_TIE",0)
                hardGame.win = sharedPreferences.getInt("HARD_WIN",0)
                multiGame.numberOfGames = sharedPreferences.getInt("MULTI_NO_OF_GAMES",0)
                multiGame.lose = sharedPreferences.getInt("MULTI_LOSE",0)
                multiGame.tie = sharedPreferences.getInt("MULTI_TIE",0)
                multiGame.win = sharedPreferences.getInt("MULTI_WIN",0)
            }
            return User(userId,userName,easyGame, normalGame, hardGame, multiGame)
        }

        /**
         * saves info of last logged in user
         * so even without internet still you can update your statistics
         */
        fun saveLoggedStateToSharedPreferences(context: Context?,loggedInStatus: LoggedInStatus){
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences("LOGGED_IN",Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("LOGGED_IN",loggedInStatus.loggedIn)
                editor.putString("USER_ID",loggedInStatus.userid)
                editor.apply()
            }
        }

        /**
         * reads info about last logged in user
         */
        fun readLoggedStateFromSharedPreferences(context: Context?):LoggedInStatus{
            val loggedInStatus = LoggedInStatus()
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences("LOGGED_IN", Context.MODE_PRIVATE)
                loggedInStatus.loggedIn = sharedPreferences.getBoolean("LOGGED_IN",false)
                loggedInStatus.userid = sharedPreferences.getString("USER_ID","").toString()
            }
            return loggedInStatus
        }

        /**
         * saves info if easy game has been saved (for user)
         */
        fun saveEasyGameToSharedPreferences(context: Context?, saved: Boolean,userId: String){
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences("EASY_GAME",Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean(userId,saved)
                editor.apply()
            }
        }

        /**
         * reads info if easy game has been saved to Room Database (for user)
         */
        fun readEasyGameFromSharedPreferences(context: Context?,userId: String):Boolean{
            var savedGame = false
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences("EASY_GAME", Context.MODE_PRIVATE)
                savedGame = sharedPreferences.getBoolean(userId,false)
            }
            return savedGame
        }

        /**
         * reads if Room database was already created
         */
        fun isRoomDatabaseCreatedFromSharedPreferences(context: Context?,userId: String):Boolean{
            var created = false
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences("ROOM", Context.MODE_PRIVATE)
                created = sharedPreferences.getBoolean(userId,false)
            }
            return created
        }

        /**
         * saves info that Room database was already created
         */
        fun databaseCreatedToSharedPreferences(context: Context?, created: Boolean, userId: String){
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences("ROOM",Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean(userId,created)
                editor.apply()
            }
        }

}
}
