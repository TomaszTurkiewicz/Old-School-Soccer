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

        fun saveNormalGameToSharedPreferences(context: Context?, saved: Boolean,userId: String){
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences("NORMAL_GAME",Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean(userId,saved)
                editor.apply()
            }
        }


        fun readNormalGameFromSharedPreferences(context: Context?, userId: String): Boolean {

            var savedGame = false
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences("NORMAL_GAME", Context.MODE_PRIVATE)
                savedGame = sharedPreferences.getBoolean(userId,false)
            }
            return savedGame

        }
    }
}
