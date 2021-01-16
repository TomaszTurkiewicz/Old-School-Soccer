package com.tt.oldschoolsoccer


import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.tt.oldschoolsoccer.classes.*
import com.tt.oldschoolsoccer.fragments.MainFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var screenHeight=0
    private var screenWidth=0
    private var screenUnit=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen()
        setContentView(R.layout.activity_main)
        getScreenSizeAndSaveToSharedPreferences()

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container,MainFragment())
            .commit()


    }

    private fun fullScreen(){
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )

        val decorView:View = window.decorView
        decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            if(visibility and View.SYSTEM_UI_FLAG_FULLSCREEN==0){
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            }
        }
    }

    private fun getScreenSizeAndSaveToSharedPreferences(){
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenHeight = displayMetrics.heightPixels
        screenWidth = displayMetrics.widthPixels
        val unitWidth = screenWidth/20
        val unitHeight = screenHeight/30
        screenUnit=if(unitWidth>unitHeight)unitHeight else unitWidth
        Functions.saveScreenUnit(this,screenUnit)
    }
}

/*
* todo FRAGMENTS
* todo connect to firebase (picture, user, login)
* todo add admob
* todo activity multiplayer
* todo samouczek koniecznie !!!
* todo puchary (rozgrywki)
* todo nagrody za strzelone branki i wygrane mecze
* todo ikona aplikacji
* todo update helper listener
* todo add sounds
*
*/