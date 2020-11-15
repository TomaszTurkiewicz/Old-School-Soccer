package com.tt.oldschoolsoccer

import android.graphics.Shader
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tt.oldschoolsoccer.classes.Functions
import com.tt.oldschoolsoccer.drawable.FieldEasyDrawable
import com.tt.oldschoolsoccer.drawable.TileDrawable
import kotlinx.android.synthetic.main.activity_choose_game_level.*
import kotlinx.android.synthetic.main.activity_single_game_match_easy.*

class SingleGameMatchEasy : AppCompatActivity() {
    var screenUnit:Int=0
    var buttonsWidth=0
    var buttonsHeight=0
    var marginTop=0
    var marginLeft=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen()
        setContentView(R.layout.activity_single_game_match_easy)
        makeUI()
    }

    private fun makeUI() {
        screenUnit= Functions.readScreenUnit(this)
        makeBackgroundGrid()
        setViewSizes()
        setConstraintLayout()
        setDrawable()
    }

    private fun setDrawable() {
        field_easy.background = FieldEasyDrawable(this, screenUnit.toDouble())

    }

    private fun setConstraintLayout() {
        val set = ConstraintSet()
        set.clone(single_game_match_easy)

        set.connect(field_easy.id,ConstraintSet.TOP,single_game_match_easy.id,ConstraintSet.TOP,screenUnit)
        set.connect(field_easy.id,ConstraintSet.LEFT,single_game_match_easy.id,ConstraintSet.LEFT,screenUnit)

        set.connect(easyMiddle.id,ConstraintSet.TOP,single_game_match_easy.id,ConstraintSet.TOP,26*screenUnit)
        set.connect(easyMiddle.id,ConstraintSet.LEFT,single_game_match_easy.id,ConstraintSet.LEFT,9*screenUnit)

        set.connect(easyMoveUpButton.id,ConstraintSet.BOTTOM,easyMiddle.id,ConstraintSet.TOP,4*screenUnit)
        set.connect(easyMoveUpButton.id,ConstraintSet.LEFT,easyMiddle.id,ConstraintSet.LEFT,0)

        set.connect(easyMoveRightButton.id,ConstraintSet.BOTTOM,easyMiddle.id,ConstraintSet.BOTTOM,0)
        set.connect(easyMoveRightButton.id,ConstraintSet.LEFT,easyMiddle.id,ConstraintSet.RIGHT,4*screenUnit)

        set.connect(easyMoveDownButton.id,ConstraintSet.TOP,easyMiddle.id,ConstraintSet.BOTTOM,4*screenUnit)
        set.connect(easyMoveDownButton.id,ConstraintSet.LEFT,easyMiddle.id,ConstraintSet.LEFT,0)

        set.connect(easyMoveLeftButton.id,ConstraintSet.BOTTOM,easyMiddle.id,ConstraintSet.BOTTOM,0)
        set.connect(easyMoveLeftButton.id,ConstraintSet.RIGHT,easyMiddle.id,ConstraintSet.LEFT,4*screenUnit)

        set.connect(easyMoveUpRightButton.id,ConstraintSet.BOTTOM,easyMiddle.id,ConstraintSet.TOP,3*screenUnit)
        set.connect(easyMoveUpRightButton.id,ConstraintSet.LEFT,easyMiddle.id,ConstraintSet.RIGHT,3*screenUnit)

        set.connect(easyMoveDownRightButton.id,ConstraintSet.TOP,easyMiddle.id,ConstraintSet.BOTTOM,3*screenUnit)
        set.connect(easyMoveDownRightButton.id,ConstraintSet.LEFT,easyMiddle.id,ConstraintSet.RIGHT,3*screenUnit)

        set.connect(easyMoveDownLeftButton.id,ConstraintSet.TOP,easyMiddle.id,ConstraintSet.BOTTOM,3*screenUnit)
        set.connect(easyMoveDownLeftButton.id,ConstraintSet.RIGHT,easyMiddle.id,ConstraintSet.LEFT,3*screenUnit)

        set.connect(easyMoveUpLeftButton.id,ConstraintSet.BOTTOM,easyMiddle.id,ConstraintSet.TOP,3*screenUnit)
        set.connect(easyMoveUpLeftButton.id,ConstraintSet.RIGHT,easyMiddle.id,ConstraintSet.LEFT,3*screenUnit)

        set.applyTo(single_game_match_easy)

    }

    private fun setViewSizes() {
        field_easy.layoutParams = ConstraintLayout.LayoutParams(10*screenUnit,14*screenUnit)
        easyMoveUpButton.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        easyMoveUpRightButton.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        easyMoveRightButton.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        easyMoveDownRightButton.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        easyMoveDownButton.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        easyMoveDownLeftButton.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        easyMoveLeftButton.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        easyMoveUpLeftButton.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        easyMiddle.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)

    }

    private fun makeBackgroundGrid() {
        single_game_match_easy.background = TileDrawable((ContextCompat.getDrawable(this, R.drawable.background)!!),
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
}