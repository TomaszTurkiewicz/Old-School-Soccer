package com.tt.oldschoolsoccer


import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.tt.oldschoolsoccer.classes.*
import com.tt.oldschoolsoccer.fragments.MainFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), UpdateHelper.OnUpdateNeededListener{

    private var screenHeight=0
    private var screenWidth=0
    private val screenSize = ScreenSize()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen()
        setContentView(R.layout.activity_main)
        getScreenSizeAndSaveToSharedPreferences()

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container,MainFragment())
            .commit()

        Functions.saveUpdateToSharedPreferences(context = this,isUpdate = false)
        UpdateHelper.with(this).onUpdateNeeded(this).check()


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
        val size = Point()
        windowManager.defaultDisplay.getRealSize(size)
        screenHeight = size.y
        screenWidth = size.x
        val unitHeight = screenHeight/30
        val unitWidth = screenWidth/20
        screenSize.screenUnit=if(unitWidth>unitHeight)unitHeight else unitWidth
        screenSize.horizontalCount = screenWidth/screenSize.screenUnit
        screenSize.verticalCount = screenHeight/screenSize.screenUnit
        screenSize.verticalOffset=screenHeight%screenSize.screenUnit
        screenSize.horizontalOffset=screenWidth%screenSize.screenUnit


        Functions.saveScreenSize(this,screenSize)
    }

    override fun onUpdateNeeded(updateUrl: String) {
        Functions.saveUpdateToSharedPreferences(context = this,isUpdate = true,url = updateUrl)
    }
}

/*

* todo add admob
* todo activity multiplayer
todo ranking
* todo samouczek koniecznie !!!
* todo puchary (rozgrywki)
* todo nagrody za strzelone branki i wygrane mecze
* todo ikona aplikacji
* todo add sounds
*
*/
