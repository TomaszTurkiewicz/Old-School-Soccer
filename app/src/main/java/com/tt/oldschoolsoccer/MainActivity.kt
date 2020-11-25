package com.tt.oldschoolsoccer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import com.tt.oldschoolsoccer.classes.Functions
import android.content.Intent
import android.graphics.Shader
import android.util.TypedValue
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tt.oldschoolsoccer.R.drawable
import com.tt.oldschoolsoccer.drawable.ButtonDrawable
import com.tt.oldschoolsoccer.drawable.TileDrawable
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var screenHeight=0
    private var screenWidth=0
    private var screenUnit=0
    private var buttonsHeight=0
    private var buttonsWidth=0
    private var marginLeft=0
    private var marginTop=0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen()
        setContentView(R.layout.activity_main)
        makeUI()

//        val intent = Intent(this,SingleGameMatchEasy::class.java)
//        startActivity(intent)
//        finish()




    }

    private fun makeUI() {
        getScreenSizeAndSaveToSharedPreferences()
        setBackgroundGrid()
        setButtonsUI()
        makeConstraintLayout()




        // todo make ui for single player activity
        // todo samouczek
        // todo different types of games (single, liga, tournament)
        // todo make logic for single player game normal (choosing best moves and checking if phone can score)!!!
        // todo make logic for single player game (checking if phone can score if not blocking user moves)!!!
    }



    private fun setButtonsUI() {
        buttonsWidth=16*screenUnit
        buttonsHeight=4*screenUnit
        marginTop=buttonsHeight/2
        marginLeft=2*screenUnit
        choose_game_type_button_main_activity.layoutParams = ConstraintLayout.LayoutParams(buttonsWidth,buttonsHeight)
        choose_game_type_button_main_activity.background=ButtonDrawable(this, (buttonsWidth).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
        choose_game_type_button_main_activity.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
    }

    private fun makeConstraintLayout() {
        val set:ConstraintSet = ConstraintSet()
        set.clone(main_layout)

        set.connect(choose_game_type_button_main_activity.id,ConstraintSet.TOP,main_layout.id,ConstraintSet.TOP,marginTop)
        set.connect(choose_game_type_button_main_activity.id,ConstraintSet.LEFT,main_layout.id,ConstraintSet.LEFT,marginLeft)

        set.applyTo(main_layout)
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

    fun goToChooseGameTypeActivityOnClick(view: View) {
        val intent = Intent(this,ChooseGameType::class.java)
        startActivity(intent)
        finish()
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