package com.tt.oldschoolsoccer.classes

import android.content.Context
import android.view.inspector.StaticInspectionCompanionProvider

class Functions {
    companion object{

        /**
         * saves ScreenUnit to shared preferences for making UI in different activities
         */
        fun saveScreenSize(context: Context?,screenSize: ScreenSize){
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences("ScreenSize",Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putInt("ScreenUnit",screenSize.screenUnit)
                editor.putInt("VerticalCount",screenSize.verticalCount)
                editor.putInt("VerticalOffSet",screenSize.verticalOffset)
                editor.putInt("HorizontalCount",screenSize.horizontalCount)
                editor.putInt("HorizontalOffset",screenSize.horizontalOffset)
                editor.apply()
            }
        }


        /**
         * reads screenUnit from shared preferences
         */
        fun readScreenSize(context: Context?):ScreenSize{
            val screenSize = ScreenSize()
            context?.let {
                val sharedPreferences = context.getSharedPreferences("ScreenSize",Context.MODE_PRIVATE)
                screenSize.screenUnit = sharedPreferences.getInt("ScreenUnit",0)
                screenSize.verticalCount = sharedPreferences.getInt("VerticalCount",0)
                screenSize.verticalOffset = sharedPreferences.getInt("VerticalOffSet",0)
                screenSize.horizontalCount = sharedPreferences.getInt("HorizontalCount",0)
                screenSize.horizontalOffset = sharedPreferences.getInt("HorizontalOffset",0)
            }
        return screenSize
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

        fun saveMyMoveEasyToSharedPreferences(context: Context?,myMove: Boolean, userId: String){
            context?.let {
                val sharedPreferences = context.getSharedPreferences("EASY_GAME_MY_MOVE",Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean(userId,myMove)
                editor.commit()
            }
        }

        fun readMyMoveEasyGameFromSharedPreferences(context: Context?,userId: String):Boolean{
            var myMove = false
            context?.let {
                val sharedPreferences = context.getSharedPreferences("EASY_GAME_MY_MOVE",Context.MODE_PRIVATE)
                myMove = sharedPreferences.getBoolean(userId,false)
            }
            return myMove
        }

        fun saveMyMoveNormalToSharedPreferences(context: Context?,myMove: Boolean, userId: String){
            context?.let {
                val sharedPreferences = context.getSharedPreferences("NORMAL_GAME_MY_MOVE",Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean(userId,myMove)
                editor.commit()
            }
        }

        fun readMyMoveNormalGameFromSharedPreferences(context: Context?,userId: String):Boolean{
            var myMove = false
            context?.let {
                val sharedPreferences = context.getSharedPreferences("NORMAL_GAME_MY_MOVE",Context.MODE_PRIVATE)
                myMove = sharedPreferences.getBoolean(userId,false)
            }
            return myMove
        }

        fun saveMyStartNormalToSharedPreferences(context: Context?,myMove: Boolean, userId: String){
            context?.let {
                val sharedPreferences = context.getSharedPreferences("NORMAL_GAME_MY_START",Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean(userId,myMove)
                editor.commit()
            }
        }

        fun readMyStartNormalGameFromSharedPreferences(context: Context?,userId: String):Boolean{
            var myMove = false
            context?.let {
                val sharedPreferences = context.getSharedPreferences("NORMAL_GAME_MY_START",Context.MODE_PRIVATE)
                myMove = sharedPreferences.getBoolean(userId,false)
            }
            return myMove
        }

        fun readHardGameFromSharedPreferences(context: Context, userId: String): Boolean {
            var savedGame = false
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences("HARD_GAME", Context.MODE_PRIVATE)
                savedGame = sharedPreferences.getBoolean(userId,false)
            }
            return savedGame

        }



        fun saveHardGameToSharedPreferences(context: Context, saved: Boolean, userId: String) {
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences("HARD_GAME",Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean(userId,saved)
                editor.apply()
            }

        }

        fun readMyStartHardGameFromSharedPreferences(context: Context, userId: String): Boolean {
            var myMove = false
            context?.let {
                val sharedPreferences = context.getSharedPreferences("HARD_GAME_MY_START",Context.MODE_PRIVATE)
                myMove = sharedPreferences.getBoolean(userId,false)
            }
            return myMove

        }

        fun saveMyStartHardToSharedPreferences(context: Context, myMove: Boolean, userId: String) {
            context?.let {
                val sharedPreferences = context.getSharedPreferences("HARD_GAME_MY_START",Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean(userId,myMove)
                editor.commit()
            }

        }

        fun saveGameMoveStateHardToSharedPreferences(context: Context, turn:HardGameMoveState, userId: String) {
            context?.let {
                val sharedPreferences = context.getSharedPreferences("HARD_GAME_MY_MOVE$userId",Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putInt("turn",turn.getTurn())
                editor.putBoolean("nextMyTurn",turn.getNextMyTurn())
                editor.commit()
            }

        }
        fun readGameMoveStateHardGameFromSharedPreferences(context: Context, userId: String): HardGameMoveState {
            val turn = HardGameMoveState()
            context?.let {
                val sharedPreferences = context.getSharedPreferences("HARD_GAME_MY_MOVE$userId",Context.MODE_PRIVATE)
                val turnInt = sharedPreferences.getInt("turn",Static.CHECKING)
                val nextMyTurn = sharedPreferences.getBoolean("nextMyTurn",true)
                turn.setMoveState(turnInt,nextMyTurn)
            }
            return turn

        }

        fun saveUpdateToSharedPreferences(context: Context?, isUpdate:Boolean, url:String = ""){
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences("UPDATE",Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                if(isUpdate){
                    editor.putBoolean("update",isUpdate)
                    editor.putString("url",url)
                    editor.apply()
                }else{
                    editor.putBoolean("update",isUpdate)
                    editor.putString("url","")
                    editor.apply()
                }
            }
        }

        fun readUpdateFromSharedPreferences(context: Context?):Update{
            val update = Update()
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences("UPDATE", Context.MODE_PRIVATE)
                update.isUpdate = sharedPreferences.getBoolean("update",false)
                update.url = sharedPreferences.getString("url","").toString()
            }
            return update
        }

        fun saveRandomIconToSharedPreferences(context: Context,gameType:String, userIconColors: UserIconColors){
            context.let {
                val sharedPreferences = context.getSharedPreferences("$gameType PHONE ICON",Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putInt("background",userIconColors.getBackgroundColor())
                editor.putInt("leftArm",userIconColors.getLeftArmColor())
                editor.putInt("rightArm",userIconColors.getRightArmColor())
                editor.putInt("overArms",userIconColors.getOverArmsColor())
                editor.putInt("bodyLeft",userIconColors.getBodyLeftColor())
                editor.putInt("bodyRight",userIconColors.getBodyRightColor())
                editor.putInt("trousersExternal",userIconColors.getTrousersExternalColor())
                editor.putInt("trousersInternal",userIconColors.getTrousersInternalColor())
                editor.putInt("trousersBody",userIconColors.getTrousersBodyColor())
                editor.apply()
            }
        }

        fun readRandomIconFromSharedPreferences(context: Context,gameType:String):UserIconColors{
            val phoneIconColors = UserIconColors()
            context.let {
             val sharedPreferences = context.getSharedPreferences("$gameType PHONE ICON",Context.MODE_PRIVATE)
             phoneIconColors.setBackgroundColor(sharedPreferences.getInt("background",-1))
             phoneIconColors.setLeftArmColor(sharedPreferences.getInt("leftArm",-1))
             phoneIconColors.setRightArmColor(sharedPreferences.getInt("rightArm",-1))
             phoneIconColors.setOverArmColor(sharedPreferences.getInt("overArms",-1))
             phoneIconColors.setBodyLeftColor(sharedPreferences.getInt("bodyLeft",-1))
             phoneIconColors.setBodyRightColor(sharedPreferences.getInt("bodyRight",-1))
             phoneIconColors.setTrousersExternalColor(sharedPreferences.getInt("trousersExternal",-1))
             phoneIconColors.setTrousersInternalColor(sharedPreferences.getInt("trousersInternal",-1))
             phoneIconColors.setTrousersBodyColor(sharedPreferences.getInt("trousersBody",-1))
            }
            return phoneIconColors
        }

        fun readSoundFromSharedPreferences(context: Context):Boolean{
            var sound = false
            context.let {
                val sharedPreferences = context.getSharedPreferences("SOUND",Context.MODE_PRIVATE)
                sound = sharedPreferences.getBoolean("SOUND",false)
            }
            return sound
        }

        fun saveSoundToSharedPreferences(context: Context,sound:Boolean){
            context.let {
                val sharedPreferences = context.getSharedPreferences("SOUND",Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("SOUND",sound)
                editor.apply()
            }
        }

    }
}
