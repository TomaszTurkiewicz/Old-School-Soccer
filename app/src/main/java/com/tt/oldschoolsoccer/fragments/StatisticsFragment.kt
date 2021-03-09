package com.tt.oldschoolsoccer.fragments

import android.app.AlertDialog
import android.graphics.Shader
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tt.oldschoolsoccer.R
import com.tt.oldschoolsoccer.classes.FragmentCoroutine
import com.tt.oldschoolsoccer.classes.Functions
import com.tt.oldschoolsoccer.classes.LoggedInStatus
import com.tt.oldschoolsoccer.classes.User
import com.tt.oldschoolsoccer.database.UserDB
import com.tt.oldschoolsoccer.database.UserDBDatabase
import com.tt.oldschoolsoccer.drawable.ButtonDrawable
import com.tt.oldschoolsoccer.drawable.TileDrawable
import com.tt.oldschoolsoccer.drawable.UserIconDrawable
import kotlinx.android.synthetic.main.alert_dialog_user_name.*
import kotlinx.android.synthetic.main.alert_dialog_user_name.view.*
import kotlinx.android.synthetic.main.fragment_single_player_hard_game.view.*
import kotlinx.android.synthetic.main.fragment_statistics.view.*
import kotlinx.coroutines.launch

class StatisticsFragment : FragmentCoroutine() {

    private lateinit var rootView:View
    private var screenUnit:Int=0
    private var loggedInStatus = LoggedInStatus()
    private var tableHeight = 0
    private var tableWidthLarge = 0
    private var tableWidthNormal = 0
    private var tableWidthBig = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenUnit = Functions.readScreenUnit(requireContext())
        loggedInStatus = Functions.readLoggedStateFromSharedPreferences(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        rootView = inflater.inflate(R.layout.fragment_statistics, container, false)

        makeUI()

        rootView.fragment_statistics_back_button.setOnClickListener {
            goToMainMenu()
        }

        rootView.fragment_statistics_image_view.setOnClickListener {
            goToChangeIconFragment()
        }

        return rootView
    }

    private fun goToChangeIconFragment() {
        activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ChangeIconFragment()).commit()

    }

    private fun goToMainMenu() {
        activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MainFragment()).commit()
    }

    override fun onResume() {
        super.onResume()



        launch {
            requireContext().let {
                val user = UserDBDatabase(it).getUserDBDao().getUser(loggedInStatus.userid)
                updateUI(user)
                rootView.fragment_statistics_user_name_user.setOnClickListener {
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

                    set.connect(mView.alert_dialog_cancel_button.id,ConstraintSet.TOP,mView.alert_dialog_input_user_name.id,ConstraintSet.BOTTOM,screenUnit)
                    set.connect(mView.alert_dialog_cancel_button.id,ConstraintSet.LEFT,mView.alert_dialog_user_name.id,ConstraintSet.LEFT,screenUnit)

                    set.connect(mView.alert_dialog_ok_button.id,ConstraintSet.TOP,mView.alert_dialog_input_user_name.id,ConstraintSet.BOTTOM,screenUnit)
                    set.connect(mView.alert_dialog_ok_button.id,ConstraintSet.RIGHT,mView.alert_dialog_user_name.id,ConstraintSet.RIGHT,screenUnit)

                    set.connect(mView.alert_dialog_dummy_tv.id,ConstraintSet.TOP,mView.alert_dialog_cancel_button.id,ConstraintSet.BOTTOM,0)
                    set.connect(mView.alert_dialog_dummy_tv.id,ConstraintSet.LEFT,mView.alert_dialog_user_name.id,ConstraintSet.LEFT,0)

                    set.applyTo(mView.alert_dialog_user_name)

                    dialog.show()

                    mView.alert_dialog_cancel_button.setOnClickListener {
                        dialog.dismiss()
                    }

                    mView.alert_dialog_ok_button.setOnClickListener {
                       val newName =  mView.alert_dialog_input_user_name.text.toString().trim()
                        if(newName.isNotEmpty()){
                            user.name = newName
                            updateUser(user)
                            dialog.dismiss()
                        }
                        else{
                            mView.alert_dialog_input_user_name.error = "WRONG USER NAME"
                        }
                    }

                }

            }
        }
    }

    private fun updateUser(user: UserDB) {
        launch {
            requireContext().let {
                UserDBDatabase(it).getUserDBDao().updateUserInDB(user)
                updateUI(user)
            }
        }

    }

    private fun updateUI(user: UserDB) {
        val userNormal = User().userFromDB(user)
        rootView.fragment_statistics_user_name_user.text = user.name
        rootView.fragment_statistics_image_view.setImageDrawable(UserIconDrawable(requireContext(), (3*screenUnit).toDouble(),userNormal.icon))

        rootView.fragment_statistics_user_easy_number_of_games_user.text = user.easyGameNumberOfGame.toString()
        rootView.fragment_statistics_user_easy_win_user.text = user.easyGameWin.toString()
        rootView.fragment_statistics_user_easy_lose_user.text = user.easyGameLose.toString()
        rootView.fragment_statistics_user_easy_tie_user.text = user.easyGameTie.toString()

        rootView.fragment_statistics_user_normal_number_of_games_user.text = user.normalGameNumberOfGame.toString()
        rootView.fragment_statistics_user_normal_win_user.text = user.normalGameWin.toString()
        rootView.fragment_statistics_user_normal_lose_user.text = user.normalGameLose.toString()
        rootView.fragment_statistics_user_normal_tie_user.text = user.normalGameTie.toString()

        rootView.fragment_statistics_user_hard_number_of_games_user.text = user.hardGameNumberOfGame.toString()
        rootView.fragment_statistics_user_hard_win_user.text = user.hardGameWin.toString()
        rootView.fragment_statistics_user_hard_lose_user.text = user.hardGameLose.toString()
        rootView.fragment_statistics_user_hard_tie_user.text = user.hardGameTie.toString()

        rootView.fragment_statistics_user_multi_number_of_games_user.text = user.multiGameNumberOfGame.toString()
        rootView.fragment_statistics_user_multi_win_user.text = user.multiGameWin.toString()
        rootView.fragment_statistics_user_multi_lose_user.text = user.multiGameLose.toString()
        rootView.fragment_statistics_user_multi_tie_user.text = user.multiGameTie.toString()

    }

    private fun makeUI() {
        setBackgroundGrid()
        setSizes()
        setButtonsUI()
        makeConstraintLayout()

    }

    private fun makeConstraintLayout() {
        val set = ConstraintSet()
        set.clone(rootView.fragment_statistics_layout)

        set.connect(rootView.fragment_statistics_image_view.id,ConstraintSet.TOP,rootView.fragment_statistics_layout.id,ConstraintSet.TOP,screenUnit)
        set.connect(rootView.fragment_statistics_image_view.id,ConstraintSet.LEFT,rootView.fragment_statistics_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView.fragment_statistics_user_name_user.id,
                ConstraintSet.TOP,rootView.fragment_statistics_layout.id,ConstraintSet.TOP,screenUnit)
        set.connect(rootView.fragment_statistics_user_name_user.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_image_view.id,ConstraintSet.RIGHT,0)
        set.connect(rootView.fragment_statistics_user_name_user.id,
                ConstraintSet.RIGHT,rootView.fragment_statistics_layout.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_statistics_easy_games_title.id,ConstraintSet.TOP,rootView.fragment_statistics_user_name_user.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView.fragment_statistics_easy_games_title.id,ConstraintSet.LEFT,rootView.fragment_statistics_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView.fragment_statistics_user_easy_number_of_games_default.id,
                ConstraintSet.TOP,rootView.fragment_statistics_easy_games_title.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_easy_number_of_games_default.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_easy_games_title.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_statistics_user_easy_number_of_games_user.id,
                ConstraintSet.TOP,rootView.fragment_statistics_user_easy_number_of_games_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_easy_number_of_games_user.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_user_easy_number_of_games_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_statistics_user_easy_win_default.id,
                ConstraintSet.TOP,rootView.fragment_statistics_easy_games_title.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_easy_win_default.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_user_easy_number_of_games_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_statistics_user_easy_win_user.id,
                ConstraintSet.TOP,rootView.fragment_statistics_user_easy_win_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_easy_win_user.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_user_easy_win_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_statistics_user_easy_lose_default.id,
                ConstraintSet.TOP,rootView.fragment_statistics_easy_games_title.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_easy_lose_default.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_user_easy_win_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_statistics_user_easy_lose_user.id,
                ConstraintSet.TOP,rootView.fragment_statistics_user_easy_lose_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_easy_lose_user.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_user_easy_lose_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_statistics_user_easy_tie_default.id,
                ConstraintSet.TOP,rootView.fragment_statistics_easy_games_title.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_easy_tie_default.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_user_easy_lose_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_statistics_user_easy_tie_user.id,
                ConstraintSet.TOP,rootView.fragment_statistics_user_easy_tie_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_easy_tie_user.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_user_easy_tie_default.id,ConstraintSet.LEFT,0)



        set.connect(rootView.fragment_statistics_normal_games_title.id,ConstraintSet.TOP,rootView.fragment_statistics_easy_games_title.id,ConstraintSet.BOTTOM,6*screenUnit)
        set.connect(rootView.fragment_statistics_normal_games_title.id,ConstraintSet.LEFT,rootView.fragment_statistics_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView.fragment_statistics_user_normal_number_of_games_default.id,
                ConstraintSet.TOP,rootView.fragment_statistics_normal_games_title.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_normal_number_of_games_default.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_normal_games_title.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_statistics_user_normal_number_of_games_user.id,
                ConstraintSet.TOP,rootView.fragment_statistics_user_normal_number_of_games_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_normal_number_of_games_user.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_user_normal_number_of_games_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_statistics_user_normal_win_default.id,
                ConstraintSet.TOP,rootView.fragment_statistics_normal_games_title.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_normal_win_default.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_user_normal_number_of_games_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_statistics_user_normal_win_user.id,
                ConstraintSet.TOP,rootView.fragment_statistics_user_normal_win_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_normal_win_user.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_user_normal_win_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_statistics_user_normal_lose_default.id,
                ConstraintSet.TOP,rootView.fragment_statistics_normal_games_title.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_normal_lose_default.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_user_normal_win_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_statistics_user_normal_lose_user.id,
                ConstraintSet.TOP,rootView.fragment_statistics_user_normal_lose_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_normal_lose_user.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_user_normal_lose_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_statistics_user_normal_tie_default.id,
                ConstraintSet.TOP,rootView.fragment_statistics_normal_games_title.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_normal_tie_default.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_user_normal_lose_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_statistics_user_normal_tie_user.id,
                ConstraintSet.TOP,rootView.fragment_statistics_user_normal_tie_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_normal_tie_user.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_user_normal_tie_default.id,ConstraintSet.LEFT,0)


        set.connect(rootView.fragment_statistics_hard_games_title.id,ConstraintSet.TOP,rootView.fragment_statistics_normal_games_title.id,ConstraintSet.BOTTOM,6*screenUnit)
        set.connect(rootView.fragment_statistics_hard_games_title.id,ConstraintSet.LEFT,rootView.fragment_statistics_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView.fragment_statistics_user_hard_number_of_games_default.id,
                ConstraintSet.TOP,rootView.fragment_statistics_hard_games_title.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_hard_number_of_games_default.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_hard_games_title.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_statistics_user_hard_number_of_games_user.id,
                ConstraintSet.TOP,rootView.fragment_statistics_user_hard_number_of_games_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_hard_number_of_games_user.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_user_hard_number_of_games_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_statistics_user_hard_win_default.id,
                ConstraintSet.TOP,rootView.fragment_statistics_hard_games_title.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_hard_win_default.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_user_hard_number_of_games_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_statistics_user_hard_win_user.id,
                ConstraintSet.TOP,rootView.fragment_statistics_user_hard_win_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_hard_win_user.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_user_hard_win_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_statistics_user_hard_lose_default.id,
                ConstraintSet.TOP,rootView.fragment_statistics_hard_games_title.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_hard_lose_default.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_user_hard_win_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_statistics_user_hard_lose_user.id,
                ConstraintSet.TOP,rootView.fragment_statistics_user_hard_lose_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_hard_lose_user.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_user_hard_lose_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_statistics_user_hard_tie_default.id,
                ConstraintSet.TOP,rootView.fragment_statistics_hard_games_title.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_hard_tie_default.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_user_hard_lose_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_statistics_user_hard_tie_user.id,
                ConstraintSet.TOP,rootView.fragment_statistics_user_hard_tie_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_hard_tie_user.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_user_hard_tie_default.id,ConstraintSet.LEFT,0)


        set.connect(rootView.fragment_statistics_multi_games_title.id,ConstraintSet.TOP,rootView.fragment_statistics_hard_games_title.id,ConstraintSet.BOTTOM,6*screenUnit)
        set.connect(rootView.fragment_statistics_multi_games_title.id,ConstraintSet.LEFT,rootView.fragment_statistics_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView.fragment_statistics_user_multi_number_of_games_default.id,
                ConstraintSet.TOP,rootView.fragment_statistics_multi_games_title.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_multi_number_of_games_default.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_multi_games_title.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_statistics_user_multi_number_of_games_user.id,
                ConstraintSet.TOP,rootView.fragment_statistics_user_multi_number_of_games_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_multi_number_of_games_user.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_user_multi_number_of_games_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_statistics_user_multi_win_default.id,
                ConstraintSet.TOP,rootView.fragment_statistics_multi_games_title.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_multi_win_default.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_user_multi_number_of_games_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_statistics_user_multi_win_user.id,
                ConstraintSet.TOP,rootView.fragment_statistics_user_multi_win_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_multi_win_user.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_user_multi_win_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_statistics_user_multi_lose_default.id,
                ConstraintSet.TOP,rootView.fragment_statistics_multi_games_title.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_multi_lose_default.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_user_multi_win_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_statistics_user_multi_lose_user.id,
                ConstraintSet.TOP,rootView.fragment_statistics_user_multi_lose_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_multi_lose_user.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_user_multi_lose_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_statistics_user_multi_tie_default.id,
                ConstraintSet.TOP,rootView.fragment_statistics_multi_games_title.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_multi_tie_default.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_user_multi_lose_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_statistics_user_multi_tie_user.id,
                ConstraintSet.TOP,rootView.fragment_statistics_user_multi_tie_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_statistics_user_multi_tie_user.id,
                ConstraintSet.LEFT,rootView.fragment_statistics_user_multi_tie_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_statistics_back_button.id, ConstraintSet.TOP,rootView.fragment_statistics_multi_games_title.id, ConstraintSet.BOTTOM,6*screenUnit)
        set.connect(rootView.fragment_statistics_back_button.id, ConstraintSet.LEFT,rootView.fragment_statistics_layout.id, ConstraintSet.LEFT,15*screenUnit)




        set.applyTo(rootView.fragment_statistics_layout)
    }

    private fun setButtonsUI() {
        rootView.fragment_statistics_easy_games_title.background = ButtonDrawable(requireContext(), (tableWidthLarge).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_easy_win_default.background = ButtonDrawable(requireContext(), (tableWidthNormal).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_easy_tie_default.background = ButtonDrawable(requireContext(), (tableWidthNormal).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_easy_lose_default.background = ButtonDrawable(requireContext(), (tableWidthNormal).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_easy_number_of_games_default.background = ButtonDrawable(requireContext(), (tableWidthBig).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_easy_win_user.background = ButtonDrawable(requireContext(), (tableWidthNormal).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_easy_tie_user.background = ButtonDrawable(requireContext(), (tableWidthNormal).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_easy_lose_user.background = ButtonDrawable(requireContext(), (tableWidthNormal).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_easy_number_of_games_user.background = ButtonDrawable(requireContext(), (tableWidthBig).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())

        rootView.fragment_statistics_normal_games_title.background = ButtonDrawable(requireContext(), (tableWidthLarge).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_normal_win_default.background = ButtonDrawable(requireContext(), (tableWidthNormal).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_normal_tie_default.background = ButtonDrawable(requireContext(), (tableWidthNormal).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_normal_lose_default.background = ButtonDrawable(requireContext(), (tableWidthNormal).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_normal_number_of_games_default.background = ButtonDrawable(requireContext(), (tableWidthBig).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_normal_win_user.background = ButtonDrawable(requireContext(), (tableWidthNormal).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_normal_tie_user.background = ButtonDrawable(requireContext(), (tableWidthNormal).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_normal_lose_user.background = ButtonDrawable(requireContext(), (tableWidthNormal).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_normal_number_of_games_user.background = ButtonDrawable(requireContext(), (tableWidthBig).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())

        rootView.fragment_statistics_hard_games_title.background = ButtonDrawable(requireContext(), (tableWidthLarge).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_hard_win_default.background = ButtonDrawable(requireContext(), (tableWidthNormal).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_hard_tie_default.background = ButtonDrawable(requireContext(), (tableWidthNormal).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_hard_lose_default.background = ButtonDrawable(requireContext(), (tableWidthNormal).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_hard_number_of_games_default.background = ButtonDrawable(requireContext(), (tableWidthBig).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_hard_win_user.background = ButtonDrawable(requireContext(), (tableWidthNormal).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_hard_tie_user.background = ButtonDrawable(requireContext(), (tableWidthNormal).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_hard_lose_user.background = ButtonDrawable(requireContext(), (tableWidthNormal).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_hard_number_of_games_user.background = ButtonDrawable(requireContext(), (tableWidthBig).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())

        rootView.fragment_statistics_multi_games_title.background = ButtonDrawable(requireContext(), (tableWidthLarge).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_multi_win_default.background = ButtonDrawable(requireContext(), (tableWidthNormal).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_multi_tie_default.background = ButtonDrawable(requireContext(), (tableWidthNormal).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_multi_lose_default.background = ButtonDrawable(requireContext(), (tableWidthNormal).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_multi_number_of_games_default.background = ButtonDrawable(requireContext(), (tableWidthBig).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_multi_win_user.background = ButtonDrawable(requireContext(), (tableWidthNormal).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_multi_tie_user.background = ButtonDrawable(requireContext(), (tableWidthNormal).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_multi_lose_user.background = ButtonDrawable(requireContext(), (tableWidthNormal).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
        rootView.fragment_statistics_user_multi_number_of_games_user.background = ButtonDrawable(requireContext(), (tableWidthBig).toDouble(), (tableHeight).toDouble(),screenUnit.toDouble())
    }

    private fun setSizes() {

        tableHeight = 2*screenUnit
        tableWidthLarge = 18*screenUnit
        tableWidthBig = 6*screenUnit
        tableWidthNormal = 4*screenUnit

        rootView.fragment_statistics_user_name_user.layoutParams = ConstraintLayout.LayoutParams(14*screenUnit,3*screenUnit)
        rootView.fragment_statistics_user_name_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_statistics_image_view.layoutParams = ConstraintLayout.LayoutParams(3*screenUnit,3*screenUnit)

        rootView.fragment_statistics_easy_games_title.layoutParams = ConstraintLayout.LayoutParams(tableWidthLarge,tableHeight)
        rootView.fragment_statistics_easy_games_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        rootView.fragment_statistics_user_easy_number_of_games_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthBig,tableHeight)
        rootView.fragment_statistics_user_easy_win_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,tableHeight)
        rootView.fragment_statistics_user_easy_tie_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,tableHeight)
        rootView.fragment_statistics_user_easy_lose_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,tableHeight)
        rootView.fragment_statistics_user_easy_number_of_games_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_statistics_user_easy_win_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_statistics_user_easy_tie_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_statistics_user_easy_lose_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        rootView.fragment_statistics_user_easy_number_of_games_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthBig,tableHeight)
        rootView.fragment_statistics_user_easy_win_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,tableHeight)
        rootView.fragment_statistics_user_easy_tie_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,tableHeight)
        rootView.fragment_statistics_user_easy_lose_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,tableHeight)
        rootView.fragment_statistics_user_easy_number_of_games_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_statistics_user_easy_win_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_statistics_user_easy_tie_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_statistics_user_easy_lose_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        rootView.fragment_statistics_normal_games_title.layoutParams = ConstraintLayout.LayoutParams(tableWidthLarge,tableHeight)
        rootView.fragment_statistics_normal_games_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        rootView.fragment_statistics_user_normal_number_of_games_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthBig,tableHeight)
        rootView.fragment_statistics_user_normal_win_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,tableHeight)
        rootView.fragment_statistics_user_normal_tie_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,tableHeight)
        rootView.fragment_statistics_user_normal_lose_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,tableHeight)
        rootView.fragment_statistics_user_normal_number_of_games_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_statistics_user_normal_win_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_statistics_user_normal_tie_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_statistics_user_normal_lose_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        rootView.fragment_statistics_user_normal_number_of_games_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthBig,tableHeight)
        rootView.fragment_statistics_user_normal_win_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,tableHeight)
        rootView.fragment_statistics_user_normal_tie_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,tableHeight)
        rootView.fragment_statistics_user_normal_lose_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,tableHeight)
        rootView.fragment_statistics_user_normal_number_of_games_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_statistics_user_normal_win_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_statistics_user_normal_tie_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_statistics_user_normal_lose_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        rootView.fragment_statistics_hard_games_title.layoutParams = ConstraintLayout.LayoutParams(tableWidthLarge,tableHeight)
        rootView.fragment_statistics_hard_games_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        rootView.fragment_statistics_user_hard_number_of_games_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthBig,tableHeight)
        rootView.fragment_statistics_user_hard_win_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,tableHeight)
        rootView.fragment_statistics_user_hard_tie_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,tableHeight)
        rootView.fragment_statistics_user_hard_lose_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,tableHeight)
        rootView.fragment_statistics_user_hard_number_of_games_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_statistics_user_hard_win_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_statistics_user_hard_tie_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_statistics_user_hard_lose_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        rootView.fragment_statistics_user_hard_number_of_games_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthBig,tableHeight)
        rootView.fragment_statistics_user_hard_win_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,tableHeight)
        rootView.fragment_statistics_user_hard_tie_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,tableHeight)
        rootView.fragment_statistics_user_hard_lose_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,tableHeight)
        rootView.fragment_statistics_user_hard_number_of_games_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_statistics_user_hard_win_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_statistics_user_hard_tie_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_statistics_user_hard_lose_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        rootView.fragment_statistics_multi_games_title.layoutParams = ConstraintLayout.LayoutParams(tableWidthLarge,tableHeight)
        rootView.fragment_statistics_multi_games_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        rootView.fragment_statistics_user_multi_number_of_games_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthBig,tableHeight)
        rootView.fragment_statistics_user_multi_win_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,tableHeight)
        rootView.fragment_statistics_user_multi_tie_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,tableHeight)
        rootView.fragment_statistics_user_multi_lose_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,tableHeight)
        rootView.fragment_statistics_user_multi_number_of_games_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_statistics_user_multi_win_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_statistics_user_multi_tie_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_statistics_user_multi_lose_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        rootView.fragment_statistics_user_multi_number_of_games_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthBig,tableHeight)
        rootView.fragment_statistics_user_multi_win_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,tableHeight)
        rootView.fragment_statistics_user_multi_tie_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,tableHeight)
        rootView.fragment_statistics_user_multi_lose_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,tableHeight)
        rootView.fragment_statistics_user_multi_number_of_games_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_statistics_user_multi_win_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_statistics_user_multi_tie_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_statistics_user_multi_lose_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        rootView.fragment_statistics_back_button.layoutParams = ConstraintLayout.LayoutParams(4*screenUnit,2*screenUnit)
        rootView.fragment_statistics_back_button.background = ButtonDrawable(requireContext(), (4*screenUnit).toDouble(), (2*screenUnit).toDouble(), screenUnit.toDouble())
        rootView.fragment_statistics_back_button.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
    }

    private fun setBackgroundGrid() {
        rootView.fragment_statistics_layout.background = TileDrawable((ContextCompat.getDrawable(requireContext(),R.drawable.background)!!),
            Shader.TileMode.REPEAT,screenUnit)

    }
}