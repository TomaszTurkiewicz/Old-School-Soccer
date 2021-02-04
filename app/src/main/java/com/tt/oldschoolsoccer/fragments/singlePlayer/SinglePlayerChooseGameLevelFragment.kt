package com.tt.oldschoolsoccer.fragments.singlePlayer

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
import kotlinx.android.synthetic.main.fragment_single_player_choose_game_level.view.*


class SinglePlayerChooseGameLevelFragment : Fragment() {

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
        val rootView = inflater.inflate(R.layout.fragment_single_player_choose_game_level, container, false)

        makeUI(rootView)

        setOnClickListeners(rootView)

        return rootView
    }

    private fun setOnClickListeners(rootView: View){
        rootView.fragment_single_player_choose_game_level_easy_btn.setOnClickListener {
            goToSinglePlayerEasyGame()
        }

        rootView.fragment_single_player_choose_game_level_normal_btn.setOnClickListener {
            goToSinglePlayerNormalGame()
        }

        rootView.fragment_single_player_choose_game_level_hard_btn.setOnClickListener {
            goToSinglePlayerHardGame()
        }
    }

    private fun goToSinglePlayerHardGame() {
        activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, SinglePlayerHardGameFragment()).commit()
    }

    private fun goToSinglePlayerNormalGame() {
        activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, SinglePlayerNormalGameFragment()).commit()
    }

    private fun goToSinglePlayerEasyGame() {
        activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, SinglePlayerEasyGameFragment()).commit()
    }

    private fun makeUI(rootView: View){
        setBackgroundGrid(rootView)
        setSizes()
        setButtonsUI(rootView)
        makeConstraintLayout(rootView)
    }

    private fun makeConstraintLayout(rootView: View){
        val set = ConstraintSet()
        set.clone(rootView.fragment_single_player_choose_game_level_layout)

        set.connect(rootView.fragment_single_player_choose_game_level_easy_btn.id,
            ConstraintSet.TOP,rootView.fragment_single_player_choose_game_level_layout.id,
            ConstraintSet.TOP,marginTop)
        set.connect(rootView.fragment_single_player_choose_game_level_easy_btn.id,
            ConstraintSet.LEFT,rootView.fragment_single_player_choose_game_level_layout.id,
            ConstraintSet.LEFT,marginLeft)

        set.connect(rootView.fragment_single_player_choose_game_level_normal_btn.id,
            ConstraintSet.TOP,rootView.fragment_single_player_choose_game_level_easy_btn.id,
            ConstraintSet.BOTTOM,marginTop)
        set.connect(rootView.fragment_single_player_choose_game_level_normal_btn.id,
            ConstraintSet.LEFT,rootView.fragment_single_player_choose_game_level_layout.id,
            ConstraintSet.LEFT,marginLeft)

        set.connect(rootView.fragment_single_player_choose_game_level_hard_btn.id,
                ConstraintSet.TOP,rootView.fragment_single_player_choose_game_level_normal_btn.id,
                ConstraintSet.BOTTOM,marginTop)
        set.connect(rootView.fragment_single_player_choose_game_level_hard_btn.id,
                ConstraintSet.LEFT,rootView.fragment_single_player_choose_game_level_layout.id,
                ConstraintSet.LEFT,marginLeft)


        set.applyTo(rootView.fragment_single_player_choose_game_level_layout)

    }

    private fun setButtonsUI(rootView: View){
        rootView.fragment_single_player_choose_game_level_easy_btn.layoutParams = ConstraintLayout.LayoutParams(buttonsWidth,buttonsHeight)
        rootView.fragment_single_player_choose_game_level_easy_btn.background = ButtonDrawable(requireContext(),(buttonsWidth).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
        rootView.fragment_single_player_choose_game_level_easy_btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenUnit.toFloat())

        rootView.fragment_single_player_choose_game_level_normal_btn.layoutParams = ConstraintLayout.LayoutParams(buttonsWidth,buttonsHeight)
        rootView.fragment_single_player_choose_game_level_normal_btn.background = ButtonDrawable(requireContext(),(buttonsWidth).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
        rootView.fragment_single_player_choose_game_level_normal_btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenUnit.toFloat())

        rootView.fragment_single_player_choose_game_level_hard_btn.layoutParams = ConstraintLayout.LayoutParams(buttonsWidth,buttonsHeight)
        rootView.fragment_single_player_choose_game_level_hard_btn.background = ButtonDrawable(requireContext(),(buttonsWidth).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
        rootView.fragment_single_player_choose_game_level_hard_btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenUnit.toFloat())
    }

    private fun setSizes(){
        buttonsWidth=16*screenUnit
        buttonsHeight=4*screenUnit
        marginLeft = 2*screenUnit
        marginTop=buttonsHeight/2
    }

    private fun setBackgroundGrid(rootView: View){
        rootView.fragment_single_player_choose_game_level_layout.background = TileDrawable((ContextCompat.getDrawable(requireContext(),R.drawable.background)!!),
            Shader.TileMode.REPEAT,screenUnit)
    }

}

//TODO add button to hard game!!!