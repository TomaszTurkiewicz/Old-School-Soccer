package com.tt.oldschoolsoccer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.constraintlayout.solver.state.State
import androidx.constraintlayout.widget.ConstraintSet
import com.tt.oldschoolsoccer.classes.Functions
import android.content.Intent
import android.graphics.Shader
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tt.oldschoolsoccer.R.drawable
import com.tt.oldschoolsoccer.drawable.TileDrawable
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var screenHeight=0
    private var screenWidth=0
    private var screenUnit=0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen()
        setContentView(R.layout.activity_main)
        makeUI()




    }

    private fun makeUI() {
        getScreenSizeAndSaveToSharedPreferences()
        setBackgroundGrid()
    }

    private fun setBackgroundGrid() {
        main_layout.background = TileDrawable((ContextCompat.getDrawable(this,drawable.background)!!),Shader.TileMode.REPEAT,screenUnit)

    }

    private fun getScreenSizeAndSaveToSharedPreferences() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenHeight = displayMetrics.heightPixels
        screenWidth = displayMetrics.widthPixels
        val unitWidth = screenWidth/20
        val unitHeight = screenHeight/30
        screenUnit=if(unitWidth>unitHeight)unitHeight else unitWidth
        Functions.saveScreenUnit(this,screenUnit)

    }

    private fun fullScreen() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

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
}

/*
* todo connect to firebase (picture, user, login)
* todo add admob
* todo odczyt z shared preferences kratki
* todo activity singleplayer
* todo activity multiplayer
* todo login logout
* todo user class - store only points plus name
* todo create kratka
* todo create field
* todo create game logic (move, checking if next move available, check if droga na bramkę dostępna)
* todo samouczek koniecznie !!!
* todo puchary (rozgrywki)
* todo nagrody za strzelone branki i wygrane mecze
* todo ikona aplikacji
* todo update helper listener
* todo add sounds
*
*/