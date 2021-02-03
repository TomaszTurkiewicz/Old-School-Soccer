package com.tt.oldschoolsoccer.fragments.singlePlayer

import android.graphics.Shader
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tt.oldschoolsoccer.R
import com.tt.oldschoolsoccer.classes.FragmentCoroutine
import com.tt.oldschoolsoccer.classes.Functions
import com.tt.oldschoolsoccer.classes.LoggedInStatus
import com.tt.oldschoolsoccer.drawable.TileDrawable
import kotlinx.android.synthetic.main.fragment_single_player_hard_game.view.*
import kotlinx.android.synthetic.main.fragment_single_player_normal_game.view.*
import kotlinx.android.synthetic.main.fragment_single_player_normal_game.view.fragment_single_player_normal_game_layout


class SinglePlayerHardGameFragment : FragmentCoroutine() {
    private var screenUnit:Int = 0
    private var loggedInStatus = LoggedInStatus()
    private lateinit var rootView:View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenUnit = Functions.readScreenUnit(requireContext())
        loggedInStatus = Functions.readLoggedStateFromSharedPreferences(requireContext())

        createField()

    }

    private fun createField() {
        //todo!!!

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_single_player_hard_game, container, false)

        makeUI()

        return rootView
    }

    private fun makeUI() {
        makeBackgroundGrid()
        setViewSizes()
        setDrawable()
        setConstraintLayout()
        setOnClickListeners()
        disableButtons()

    }

    private fun disableButtons() {
        TODO("Not yet implemented")
    }

    private fun setOnClickListeners() {
        TODO("Not yet implemented")

    }

    private fun setDrawable() {
        TODO("Not yet implemented")

    }

    private fun setConstraintLayout() {
        TODO("Not yet implemented")

    }

    private fun setViewSizes() {
        TODO("Not yet implemented")

    }

    private fun makeBackgroundGrid() {
        rootView.fragment_single_player_hard_game_layout.background = TileDrawable((ContextCompat.getDrawable(requireContext(), R.drawable.background)!!),
            Shader.TileMode.REPEAT,screenUnit)

    }


}