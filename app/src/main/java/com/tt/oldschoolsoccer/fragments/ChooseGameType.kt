package com.tt.oldschoolsoccer.fragments

import android.graphics.Shader
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tt.oldschoolsoccer.R
import com.tt.oldschoolsoccer.classes.Functions
import com.tt.oldschoolsoccer.drawable.ButtonDrawable
import com.tt.oldschoolsoccer.drawable.TileDrawable
import kotlinx.android.synthetic.main.fragment_choose_game_type.view.*
import kotlinx.android.synthetic.main.fragment_main.view.*

class ChooseGameType : Fragment() {

    private var screenUnit:Int=0
    private var buttonsHeight=0
    private var buttonsWidth=0
    private var marginLeft=0
    private var marginTop=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenUnit = Functions.readScreenUnit(requireContext())

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_choose_game_type, container, false)

        makeUI(rootView)

        return rootView
    }

    private fun makeUI(rootView: View) {
        setBackgroundGrid(rootView)
        setSizes()
        setButtonUI(rootView)
        makeConstraintLayout(rootView)
    }

    private fun makeConstraintLayout(rootView: View) {
        val set = ConstraintSet()
        set.clone(rootView.fragment_choose_game_type_layout)

        set.connect(rootView.fragment_choose_game_type_single_player_btn.id,ConstraintSet.TOP,rootView.fragment_choose_game_type_layout.id,ConstraintSet.TOP,marginTop)
        set.connect(rootView.fragment_choose_game_type_single_player_btn.id,ConstraintSet.LEFT,rootView.fragment_choose_game_type_layout.id,ConstraintSet.LEFT,marginLeft)

        set.applyTo(rootView.fragment_choose_game_type_layout)

    }

    private fun setButtonUI(rootView: View) {
        rootView.fragment_choose_game_type_single_player_btn.layoutParams = ConstraintLayout.LayoutParams(buttonsWidth,buttonsHeight)
        rootView.fragment_choose_game_type_single_player_btn.background = ButtonDrawable(requireContext(),(buttonsWidth).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
        rootView.fragment_choose_game_type_single_player_btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenUnit.toFloat())
    }

    private fun setSizes() {
        buttonsWidth=16*screenUnit
        buttonsHeight=4*screenUnit
        marginLeft = 2*screenUnit
        marginTop=buttonsHeight/2

    }

    private fun setBackgroundGrid(rootView: View?) {
        rootView!!.fragment_choose_game_type_layout.background = TileDrawable((ContextCompat.getDrawable(requireContext(),R.drawable.background)!!),
            Shader.TileMode.REPEAT,screenUnit)

    }

}