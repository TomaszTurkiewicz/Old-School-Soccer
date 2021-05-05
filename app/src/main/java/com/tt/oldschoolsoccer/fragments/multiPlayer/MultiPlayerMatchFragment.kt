package com.tt.oldschoolsoccer.fragments.multiPlayer

import android.graphics.Shader
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tt.oldschoolsoccer.R
import com.tt.oldschoolsoccer.classes.Functions
import com.tt.oldschoolsoccer.classes.LoggedInStatus
import com.tt.oldschoolsoccer.classes.ScreenSize
import com.tt.oldschoolsoccer.drawable.TileDrawable
import kotlinx.android.synthetic.main.fragment_choose_game_type.view.*
import kotlinx.android.synthetic.main.fragment_multi_player_match.view.*


class MultiPlayerMatchFragment : Fragment() {

    private var screenSize = ScreenSize()
    private var loggedInStatus = LoggedInStatus()
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenSize = Functions.readScreenSize(requireContext())
        loggedInStatus = Functions.readLoggedStateFromSharedPreferences(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_multi_player_match, container, false)

        makeUI()


        return rootView
    }

    private fun makeUI() {
        setBackgroundGrid()
   //     setSizes()
   //     setButtonsUI()
   //     makeConstraintLayout()

    }

    private fun setBackgroundGrid() {
        rootView.fragment_multi_player_match_layout.background = TileDrawable((ContextCompat.getDrawable(requireContext(),R.drawable.background)!!),
                Shader.TileMode.REPEAT,screenSize.screenUnit)

    }

}