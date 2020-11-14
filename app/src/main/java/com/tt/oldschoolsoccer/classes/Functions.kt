package com.tt.oldschoolsoccer.classes

import android.content.Context

class Functions {
    companion object{
        fun saveScreenUnit(context: Context?,screenUnit:Int){
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences("ScreenUnit",Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putInt("ScreenUnit",screenUnit)
                editor.apply()
            }
        }

        fun readScreenUnit(context: Context?):Int{
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences("ScreenUnit",Context.MODE_PRIVATE)
                return sharedPreferences.getInt("ScreenUnit",-1)
            }
        return -1
        }

    }
}