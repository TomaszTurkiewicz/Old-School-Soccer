package com.tt.oldschoolsoccer.classes

import android.content.Context
import android.content.pm.PackageManager
import android.text.TextUtils
import android.util.Log
import androidx.annotation.NonNull
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class UpdateHelper(
        private var context:Context,
        private var onUpdateNeededListener: OnUpdateNeededListener
) {
    private val TAG:String = "UpdateHelperKT"

    companion object{
        const val KEY_UPDATE_ENABLE = "is_update_soccer_game"
        const val KEY_UPDATE_VERSION = "version_soccer_game"
        const val KEY_UPDATE_URL = "update_url_soccer_game"

        fun with(@NonNull context: Context):Builder{
            return Builder(context)
        }
    }

    interface OnUpdateNeededListener{
        fun onUpdateNeeded(updateUrl:String)
    }

    fun check(){
        val remoteConfig = FirebaseRemoteConfig.getInstance()

        if(remoteConfig.getBoolean(KEY_UPDATE_ENABLE)){
            val currentVersion = remoteConfig.getString(KEY_UPDATE_VERSION)
            val appVersion = getAppVersion(context)
            val updateUrl = remoteConfig.getString(KEY_UPDATE_URL)

            if(!TextUtils.equals(currentVersion,appVersion)){
                onUpdateNeededListener.onUpdateNeeded(updateUrl)
            }
        }
    }

    private fun getAppVersion(context: Context):String {

        var result:String = ""

        try{
            result = context.packageManager.getPackageInfo(context.packageName,0).versionName
            result=result.replace(Regex("[a-zA-Z]|-"),"")
        }
        catch(e: PackageManager.NameNotFoundException){
            Log.e(TAG,e.message)
        }

        return result
    }
}