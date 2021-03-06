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
import com.tt.oldschoolsoccer.classes.FragmentCoroutine
import com.tt.oldschoolsoccer.classes.Functions
import com.tt.oldschoolsoccer.classes.LoggedInStatus
import com.tt.oldschoolsoccer.drawable.ButtonDrawable
import com.tt.oldschoolsoccer.drawable.TileDrawable
import com.tt.oldschoolsoccer.drawable.UserIconDrawable
import kotlinx.android.synthetic.main.fragment_change_icon.view.*
import kotlinx.android.synthetic.main.fragment_statistics.view.*


class ChangeIconFragment : FragmentCoroutine() {

    private lateinit var rootView:View
    private var screenUnit = 0
    private var loggedInStatus = LoggedInStatus()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenUnit = Functions.readScreenUnit(requireContext())
        loggedInStatus = Functions.readLoggedStateFromSharedPreferences(requireContext())

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        rootView = inflater.inflate(R.layout.fragment_change_icon, container, false)

        makeUI()

        return rootView
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        rootView.fragment_change_icon_image_view.setImageDrawable(UserIconDrawable(requireContext(), (18*screenUnit).toDouble(), screenUnit.toDouble()))

    }

    private fun makeUI() {
        setBackgroundGrid()
        setSizes()
        setButtonsUI()
        makeConstraintLayout()


    }





    private fun makeConstraintLayout() {
        val set = ConstraintSet()
        set.clone(rootView.fragment_change_icon_layout)

        set.connect(rootView.fragment_change_icon_image_view.id,ConstraintSet.TOP,rootView.fragment_change_icon_layout.id,ConstraintSet.TOP,screenUnit)
        set.connect(rootView.fragment_change_icon_image_view.id,ConstraintSet.LEFT,rootView.fragment_change_icon_layout.id,ConstraintSet.LEFT,screenUnit)

        set.applyTo(rootView.fragment_change_icon_layout)
    }

    private fun setButtonsUI() {

    }

    private fun setSizes() {
        rootView.fragment_change_icon_image_view.layoutParams = ConstraintLayout.LayoutParams(18*screenUnit,18*screenUnit)
    }

    private fun setBackgroundGrid() {
        rootView.fragment_change_icon_layout.background = TileDrawable((ContextCompat.getDrawable(requireContext(),R.drawable.background)!!),
                Shader.TileMode.REPEAT,screenUnit)
    }


}