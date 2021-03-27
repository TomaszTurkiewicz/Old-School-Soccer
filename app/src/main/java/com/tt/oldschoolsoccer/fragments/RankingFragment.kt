package com.tt.oldschoolsoccer.fragments

import android.graphics.Shader
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tt.oldschoolsoccer.R
import com.tt.oldschoolsoccer.classes.FragmentCoroutine
import com.tt.oldschoolsoccer.classes.Functions
import com.tt.oldschoolsoccer.classes.LoggedInStatus
import com.tt.oldschoolsoccer.database.UserDB
import com.tt.oldschoolsoccer.drawable.TileDrawable
import kotlinx.android.synthetic.main.fragment_choose_game_type.view.*
import kotlinx.android.synthetic.main.fragment_ranking.view.*


class RankingFragment : FragmentCoroutine() {

    private var screenUnit = 0
    private var loggedInStatus = LoggedInStatus()
    private lateinit var userDB: UserDB
    private lateinit var rootView: View
    private var iconSize = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenUnit = Functions.readScreenUnit(requireContext())
        loggedInStatus = Functions.readLoggedStateFromSharedPreferences(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_ranking, container, false)

        makeUI()

        return rootView
    }

    private fun makeUI() {

        setBackgroundGrid()
        setSizes()
        setButtonsUI()
        setViewForLoggedInNotLoggedIn()
        makeConstraintLayout()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        rootView.fragment_ranking_back_button.setOnClickListener {
            goToMainMenu()
        }

    }

    private fun goToMainMenu() {
        activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MainFragment()).commit()

    }

    private fun makeConstraintLayout() {
        val set = ConstraintSet()
        set.clone(rootView.fragment_ranking_layout)

        set.connect(rootView.fragment_ranking_back_button.id,ConstraintSet.TOP,rootView.fragment_ranking_layout.id,ConstraintSet.TOP,screenUnit)
        set.connect(rootView.fragment_ranking_back_button.id,ConstraintSet.LEFT,rootView.fragment_ranking_layout.id,ConstraintSet.LEFT,16*screenUnit)

        set.applyTo(rootView.fragment_ranking_layout)
    }

    private fun setViewForLoggedInNotLoggedIn() {

    }

    private fun setButtonsUI() {
        rootView.fragment_ranking_back_button.layoutParams = ConstraintLayout.LayoutParams(iconSize,iconSize)
        rootView.fragment_ranking_back_button.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.close))
    }

    private fun setSizes() {
        iconSize = 2*screenUnit
    }

    private fun setBackgroundGrid() {
        rootView.fragment_ranking_layout.background = TileDrawable((ContextCompat.getDrawable(requireContext(),R.drawable.background)!!),
                Shader.TileMode.REPEAT,screenUnit)
    }

}