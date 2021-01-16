package com.tt.oldschoolsoccer

import android.content.Intent
import android.graphics.Shader
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tt.oldschoolsoccer.classes.Functions
import com.tt.oldschoolsoccer.drawable.ButtonDrawable
import com.tt.oldschoolsoccer.drawable.TileDrawable
import kotlinx.android.synthetic.main.activity_choose_game_level.*


/**
 * choosing game level (easy, normal, hard)
 */
class ChooseGameLevelActivity : AppCompatActivity() {
    var screenUnit:Int=0
    var buttonsWidth=0
    var buttonsHeight=0
    var marginTop=0
    var marginLeft=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen()
        setContentView(R.layout.activity_choose_game_level)
        makeUI()

    }

    private fun makeUI() {
        screenUnit=Functions.readScreenUnit(this)
        makeBackgroundGrid()
        makeButtonsUI()
        makeConstraintLayout()
    }

    private fun makeConstraintLayout() {
        val set: ConstraintSet = ConstraintSet()
        set.clone(choose_game_level)

        set.connect(easy_game.id,
            ConstraintSet.TOP,choose_game_level.id,
            ConstraintSet.TOP,marginTop)
        set.connect(easy_game.id,
            ConstraintSet.LEFT,choose_game_level.id,
            ConstraintSet.LEFT,marginLeft)

        set.applyTo(choose_game_level)

    }

    private fun makeButtonsUI() {
        buttonsWidth=16*screenUnit
        buttonsHeight=4*screenUnit
        marginTop=buttonsHeight/2
        marginLeft=2*screenUnit
        easy_game.layoutParams = ConstraintLayout.LayoutParams(buttonsWidth,buttonsHeight)
        easy_game.background= ButtonDrawable(this, (buttonsWidth).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
        easy_game.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

    }

    private fun makeBackgroundGrid() {
        choose_game_level.background = TileDrawable((ContextCompat.getDrawable(this, R.drawable.background)!!),
            Shader.TileMode.REPEAT,screenUnit)

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

    fun goToEasyGameSinglePlayer(view: View) {

        val intent = Intent(this,SingleGameMatchEasy::class.java)
        startActivity(intent)
        finish()
    }
}