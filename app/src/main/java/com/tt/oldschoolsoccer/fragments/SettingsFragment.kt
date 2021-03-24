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
import com.tt.oldschoolsoccer.database.UserDB
import com.tt.oldschoolsoccer.database.UserDBDatabase
import com.tt.oldschoolsoccer.drawable.TileDrawable
import kotlinx.android.synthetic.main.fragment_choose_game_type.view.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
import kotlinx.coroutines.launch


class SettingsFragment : FragmentCoroutine() {

    private var screenUnit = 0
    private var loggedInStatus = LoggedInStatus()
    private lateinit var rootView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screenUnit = Functions.readScreenUnit(requireContext())
        loggedInStatus = Functions.readLoggedStateFromSharedPreferences(requireContext())

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        rootView = inflater.inflate(R.layout.fragment_settings, container, false)

        makeUI()

        rootView.fragment_settings_back_button.setOnClickListener {
            goToMainMenu()
        }

        return rootView
    }

    private fun goToMainMenu() {
        activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MainFragment()).commit()
    }

    private fun makeUI() {
        setBackgroundGrid()
        setSizes()
        setButtonsUI()
        setViewForLoggedInNotLoggedIn()
        makeConstraintLayout()


    }

    private fun makeConstraintLayout() {
        val set = ConstraintSet()
        set.clone(rootView.fragment_settings_layout)

        set.connect(rootView.fragment_settings_back_button.id, ConstraintSet.TOP,rootView.fragment_settings_layout.id, ConstraintSet.TOP,screenUnit)
        set.connect(rootView.fragment_settings_back_button.id, ConstraintSet.LEFT,rootView.fragment_settings_layout.id, ConstraintSet.LEFT,16*screenUnit)

        set.connect(rootView.fragment_settings_your_name_string.id,ConstraintSet.TOP,rootView.fragment_settings_layout.id,ConstraintSet.TOP,3*screenUnit)
        set.connect(rootView.fragment_settings_your_name_string.id,ConstraintSet.LEFT,rootView.fragment_settings_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView.fragment_settings_user_name.id,ConstraintSet.TOP,rootView.fragment_settings_layout.id,ConstraintSet.TOP,5*screenUnit)
        set.connect(rootView.fragment_settings_user_name.id,ConstraintSet.LEFT,rootView.fragment_settings_layout.id,ConstraintSet.LEFT,screenUnit)

        set.applyTo(rootView.fragment_settings_layout)
    }

    private fun setViewForLoggedInNotLoggedIn() {
        if(loggedInStatus.loggedIn){
            rootView.fragment_settings_your_name_string.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
            rootView.fragment_settings_user_name.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))


            var user = UserDB()

            launch {
                requireContext().let{
                    user = UserDBDatabase(it).getUserDBDao().getUser(loggedInStatus.userid)


                    rootView.fragment_settings_user_name.text = user.name
                }
            }



        }
        else{
            rootView.fragment_settings_your_name_string.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_grey_medium))
            rootView.fragment_settings_user_name.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_grey_medium))
            rootView.fragment_settings_user_name.text = "---------"
        }


    }

    private fun setButtonsUI() {
        rootView.fragment_settings_back_button.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_settings_back_button.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.close))

        rootView.fragment_settings_your_name_string.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenUnit.toFloat())
        rootView.fragment_settings_user_name.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenUnit.toFloat())

        rootView.fragment_settings_change_user_name_button.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_settings_change_user_name_button.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.settings))
    }

    private fun setSizes() {

    }

    private fun setBackgroundGrid() {
        rootView.fragment_settings_layout.background = TileDrawable((ContextCompat.getDrawable(requireContext(),R.drawable.background)!!),
                Shader.TileMode.REPEAT,screenUnit)
    }


}