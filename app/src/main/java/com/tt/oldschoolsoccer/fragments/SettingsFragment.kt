package com.tt.oldschoolsoccer.fragments

import android.app.AlertDialog
import android.graphics.Shader
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tt.oldschoolsoccer.R
import com.tt.oldschoolsoccer.classes.FragmentCoroutine
import com.tt.oldschoolsoccer.classes.Functions
import com.tt.oldschoolsoccer.classes.LoggedInStatus
import com.tt.oldschoolsoccer.database.UserDB
import com.tt.oldschoolsoccer.database.UserDBDatabase
import com.tt.oldschoolsoccer.drawable.ButtonDrawable
import com.tt.oldschoolsoccer.drawable.TileDrawable
import kotlinx.android.synthetic.main.alert_dialog_user_name.view.*
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

        set.connect(rootView.fragment_settings_change_user_name_button.id,ConstraintSet.TOP,rootView.fragment_settings_layout.id,ConstraintSet.TOP,3*screenUnit)
        set.connect(rootView.fragment_settings_change_user_name_button.id,ConstraintSet.LEFT,rootView.fragment_settings_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView.fragment_settings_your_name_string.id,ConstraintSet.TOP,rootView.fragment_settings_layout.id,ConstraintSet.TOP,3*screenUnit)
        set.connect(rootView.fragment_settings_your_name_string.id,ConstraintSet.LEFT,rootView.fragment_settings_change_user_name_button.id,ConstraintSet.RIGHT,screenUnit)

        set.connect(rootView.fragment_settings_user_name.id,ConstraintSet.TOP,rootView.fragment_settings_layout.id,ConstraintSet.TOP,5*screenUnit)
        set.connect(rootView.fragment_settings_user_name.id,ConstraintSet.LEFT,rootView.fragment_settings_your_name_string.id,ConstraintSet.LEFT,0)



        set.applyTo(rootView.fragment_settings_layout)
    }

    private fun setViewForLoggedInNotLoggedIn() {
        if(loggedInStatus.loggedIn){
            rootView.fragment_settings_your_name_string.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
            rootView.fragment_settings_user_name.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
            rootView.fragment_settings_change_user_name_button.visibility = View.VISIBLE




            launch {
                requireContext().let{
                    val user = UserDBDatabase(it).getUserDBDao().getUser(loggedInStatus.userid)
                    rootView.fragment_settings_change_user_name_button.setOnClickListener {
                        openChangeUserNameDialog(user)
                    }


                    rootView.fragment_settings_user_name.text = user.name
                }
            }
        }
        else{
            rootView.fragment_settings_your_name_string.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_grey_medium))
            rootView.fragment_settings_user_name.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_grey_medium))
            rootView.fragment_settings_user_name.text = "---------"
            rootView.fragment_settings_change_user_name_button.visibility = View.GONE
        }


    }

    private fun openChangeUserNameDialog(user: UserDB) {

                val mBuilder = AlertDialog.Builder(requireContext())
                val mView = layoutInflater.inflate(R.layout.alert_dialog_user_name,null)
                mBuilder.setView(mView)
                val dialog = mBuilder.create()
                val flags = View.SYSTEM_UI_FLAG_IMMERSIVE or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                dialog.window!!.decorView.systemUiVisibility = flags
                dialog.setCancelable(false)
                dialog.setCanceledOnTouchOutside(false)

                mView.background = TileDrawable((ContextCompat.getDrawable(requireContext(), R.drawable.background)!!),
                        Shader.TileMode.REPEAT,screenUnit)

                mView.alert_dialog_title.layoutParams = ConstraintLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,3*screenUnit)
                mView.alert_dialog_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
                mView.alert_dialog_title.text = "SET NEW USER NAME"
                mView.alert_dialog_input_user_name.layoutParams = ConstraintLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,3*screenUnit)
                mView.alert_dialog_input_user_name.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
                mView.alert_dialog_input_user_name.setText(user.name)

                mView.alert_dialog_cancel_button.layoutParams = ConstraintLayout.LayoutParams(4*screenUnit,3*screenUnit)
                mView.alert_dialog_cancel_button.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
                mView.alert_dialog_cancel_button.background = ButtonDrawable(requireContext(), (4*screenUnit).toDouble(), (3*screenUnit).toDouble(), screenUnit.toDouble())

                mView.alert_dialog_ok_button.layoutParams = ConstraintLayout.LayoutParams(4*screenUnit,3*screenUnit)
                mView.alert_dialog_ok_button.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
                mView.alert_dialog_ok_button.background = ButtonDrawable(requireContext(), (4*screenUnit).toDouble(), (3*screenUnit).toDouble(), screenUnit.toDouble())

                val set = ConstraintSet()
                set.clone(mView.alert_dialog_user_name)

                set.connect(mView.alert_dialog_title.id,ConstraintSet.TOP,mView.alert_dialog_user_name.id,ConstraintSet.TOP,0)
                set.connect(mView.alert_dialog_title.id,ConstraintSet.LEFT,mView.alert_dialog_user_name.id,ConstraintSet.LEFT,0)

                set.connect(mView.alert_dialog_input_user_name.id,ConstraintSet.TOP,mView.alert_dialog_title.id,ConstraintSet.BOTTOM,0)
                set.connect(mView.alert_dialog_input_user_name.id,ConstraintSet.LEFT,mView.alert_dialog_user_name.id,ConstraintSet.LEFT,0)

                set.connect(mView.alert_dialog_ok_button.id,ConstraintSet.TOP,mView.alert_dialog_input_user_name.id,ConstraintSet.BOTTOM,screenUnit)
                set.connect(mView.alert_dialog_ok_button.id,ConstraintSet.RIGHT,mView.alert_dialog_user_name.id,ConstraintSet.RIGHT,screenUnit)

                set.connect(mView.alert_dialog_cancel_button.id,ConstraintSet.TOP,mView.alert_dialog_input_user_name.id,ConstraintSet.BOTTOM,screenUnit)
                set.connect(mView.alert_dialog_cancel_button.id,ConstraintSet.LEFT,mView.alert_dialog_user_name.id,ConstraintSet.LEFT,screenUnit)

                set.connect(mView.alert_dialog_dummy_tv.id,ConstraintSet.TOP,mView.alert_dialog_ok_button.id,ConstraintSet.BOTTOM,0)
                set.connect(mView.alert_dialog_dummy_tv.id,ConstraintSet.LEFT,mView.alert_dialog_user_name.id,ConstraintSet.LEFT,0)

                set.applyTo(mView.alert_dialog_user_name)

                dialog.show()

                mView.alert_dialog_cancel_button.setOnClickListener {
                    dialog.dismiss()
                }
                mView.alert_dialog_ok_button.setOnClickListener { itView ->
                    val newName = mView.alert_dialog_input_user_name.text.toString().trim()
                    if (newName.isNotEmpty()) {
                        user.name = newName
                        launch {
                            requireContext().let {
                                UserDBDatabase(it).getUserDBDao().updateUserInDB(user)
                            }
                        }
                        dialog.dismiss()

                        rootView.fragment_settings_user_name.text = newName
                    }
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