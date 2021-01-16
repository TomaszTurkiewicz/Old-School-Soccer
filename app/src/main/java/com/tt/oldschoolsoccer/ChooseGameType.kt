package com.tt.oldschoolsoccer

import android.content.Intent
import android.graphics.Shader
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.core.content.ContextCompat
import com.tt.oldschoolsoccer.classes.Functions
import com.tt.oldschoolsoccer.drawable.TileDrawable
import kotlinx.android.synthetic.main.activity_choose_game_type.*
import kotlinx.android.synthetic.main.activity_main.*


/**
 * activity for choosing game type
 * single player
 * multi player
 * league
 */
class ChooseGameType : AppCompatActivity() {

    private var screenHeight=0
    private var screenWidth=0
    private var screenUnit=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen()
        setContentView(R.layout.activity_choose_game_type)
        makeUI()
    }

    private fun makeUI() {
        getScreenSizeAndSaveToSharedPreferences()
        setBackgroundGrid()

    }

    private fun setBackgroundGrid() {
        choose_game_type.background = TileDrawable((ContextCompat.getDrawable(this, R.drawable.background)!!),
            Shader.TileMode.REPEAT,screenUnit)

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

        val decorView: View = window.decorView
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

    fun goToSinglePlayerActivityOnClick(view: View) {
        val intent = Intent(this,ChooseGameLevelActivity::class.java)
        startActivity(intent)
        finish()
    }
}