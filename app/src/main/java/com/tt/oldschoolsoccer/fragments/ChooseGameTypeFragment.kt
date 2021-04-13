package com.tt.oldschoolsoccer.fragments

import android.app.AlertDialog
import android.graphics.Shader
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tt.oldschoolsoccer.R
import com.tt.oldschoolsoccer.classes.*
import com.tt.oldschoolsoccer.database.UserDB
import com.tt.oldschoolsoccer.database.UserDBDatabase
import com.tt.oldschoolsoccer.drawable.ButtonDrawable
import com.tt.oldschoolsoccer.drawable.ButtonGreyDrawable
import com.tt.oldschoolsoccer.drawable.CheckBoxDrawable
import com.tt.oldschoolsoccer.drawable.TileDrawable
import com.tt.oldschoolsoccer.fragments.multiPlayer.MultiPlayerListFragment
import com.tt.oldschoolsoccer.fragments.singlePlayer.SinglePlayerEasyGameFragment
import com.tt.oldschoolsoccer.fragments.singlePlayer.SinglePlayerHardGameFragment
import com.tt.oldschoolsoccer.fragments.singlePlayer.SinglePlayerNormalGameFragment
import kotlinx.android.synthetic.main.alert_dialog_play_with_people.view.*
import kotlinx.android.synthetic.main.alert_dialog_user_name.view.*
import kotlinx.android.synthetic.main.fragment_choose_game_type.view.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
import kotlinx.coroutines.launch


class ChooseGameTypeFragment : FragmentCoroutine() {

    private var tableWidthNormal = 0
    private var screenUnit=0
    private var buttonsHeight=0
    private var marginLeft=0
    private var marginTop=0
    private var loggedInStatus = LoggedInStatus()
    private var easyGameSaved = false
    private var normalGameSaved = false
    private var hardGameSaved = false
    private lateinit var rootView: View
    private var totalScoreString = 0
    private val multiGameHandler = Handler()
    private var multiGameState = Static.MULTI_GAME_NOT_SET_UP




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenUnit = Functions.readScreenUnit(requireContext())
        loggedInStatus = Functions.readLoggedStateFromSharedPreferences(requireContext())

        if(loggedInStatus.loggedIn){
            easyGameSaved = Functions.readEasyGameFromSharedPreferences(requireContext(),loggedInStatus.userid)
            normalGameSaved = Functions.readNormalGameFromSharedPreferences(requireContext(),loggedInStatus.userid)
            hardGameSaved = Functions.readHardGameFromSharedPreferences(requireContext(),loggedInStatus.userid)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        rootView = inflater.inflate(R.layout.fragment_choose_game_type, container, false)

        makeUI()

        setOnClickListeners()

        return rootView
    }

    private fun setOnClickListeners(){

        rootView.fragment_choose_game_type_back_button.setOnClickListener {
            goToMainMenu()
        }

    }



    private fun goToMainMenu() {

        activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MainFragment()).commit()

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

    private fun makeUI(){
        setBackgroundGrid()
        setSizes()
        setButtonsUI()
        setViewForLoggedInNotLoggedIn()
        makeConstraintLayout()

    }







    override fun onPause() {
        super.onPause()
        multiGameHandler.removeCallbacksAndMessages(null)

    }


    private fun setViewForLoggedInNotLoggedIn() {
        if(loggedInStatus.loggedIn){
            launch {
                requireContext().let {
                    val userDB = UserDBDatabase(it).getUserDBDao().getUser(loggedInStatus.userid)
                    val user = User().userFromDB(userDB)

                    rootView.fragment_choose_game_type_total_score_string.background = ButtonDrawable(requireContext(),(totalScoreString).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                    rootView.fragment_choose_game_type_total_score_string.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_black))
                    rootView.fragment_choose_game_type_total_score_user.background = ButtonDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                    rootView.fragment_choose_game_type_total_score_user.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_black))
                    rootView.fragment_choose_game_type_total_score_user.text = calculateTotalScore(user)


                    rootView.fragment_choose_game_type_easy_btn.background = ButtonDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                    rootView.fragment_choose_game_type_easy_dummy.background = ButtonDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                    rootView.fragment_choose_game_type_easy_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                    setStatistic(rootView.fragment_choose_game_type_easy_number_of_games_default,tableWidthNormal,buttonsHeight,true,null)
                    setStatistic(rootView.fragment_choose_game_type_easy_number_of_games_user,tableWidthNormal,buttonsHeight,true,user.easyGame.numberOfGames.toString())
                    setStatistic(rootView.fragment_choose_game_type_easy_score_default,tableWidthNormal,buttonsHeight,true,null)
                    setStatistic(rootView.fragment_choose_game_type_easy_score_user,tableWidthNormal,buttonsHeight,true,getScore(user.easyGame))
                    if(easyGameSaved){
                        rootView.fragment_choose_game_type_easy_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.pause))
                    }else{
                        rootView.fragment_choose_game_type_easy_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.play))
                    }

                    rootView.fragment_choose_game_type_easy_play_pause_btn.setOnClickListener {
                        goToSinglePlayerEasyGame()
                    }

                    if(userDB.easyGameNumberOfGame>=10){
                        rootView.fragment_choose_game_type_normal_btn.background = ButtonDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                        rootView.fragment_choose_game_type_normal_dummy.background = ButtonDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                        rootView.fragment_choose_game_type_normal_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                        setStatistic(rootView.fragment_choose_game_type_normal_number_of_games_default,tableWidthNormal,buttonsHeight,true,null)
                        setStatistic(rootView.fragment_choose_game_type_normal_number_of_games_user,tableWidthNormal,buttonsHeight,true,user.normalGame.numberOfGames.toString())
                        setStatistic(rootView.fragment_choose_game_type_normal_score_default,tableWidthNormal,buttonsHeight,true,null)
                        setStatistic(rootView.fragment_choose_game_type_normal_score_user,tableWidthNormal,buttonsHeight,true,getScore(user.normalGame))
                        if(normalGameSaved){
                            rootView.fragment_choose_game_type_normal_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.pause))
                        }else{
                            rootView.fragment_choose_game_type_normal_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.play))
                        }
                        rootView.fragment_choose_game_type_normal_play_pause_btn.setOnClickListener {
                            goToSinglePlayerNormalGame()
                        }
                    }else{
                        rootView.fragment_choose_game_type_normal_btn.background = ButtonGreyDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                        rootView.fragment_choose_game_type_normal_dummy.background = ButtonGreyDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                        rootView.fragment_choose_game_type_normal_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_grey_buttons))
                        setStatistic(rootView.fragment_choose_game_type_normal_number_of_games_default,tableWidthNormal,buttonsHeight,false,null)
                        setStatistic(rootView.fragment_choose_game_type_normal_number_of_games_user,tableWidthNormal,buttonsHeight,false,user.normalGame.numberOfGames.toString())
                        setStatistic(rootView.fragment_choose_game_type_normal_score_default,tableWidthNormal,buttonsHeight,false,null)
                        setStatistic(rootView.fragment_choose_game_type_normal_score_user,tableWidthNormal,buttonsHeight,false,getScore(user.normalGame))
                        rootView.fragment_choose_game_type_normal_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.play_grey))
                    }

                    if(userDB.normalGameNumberOfGame>=10){
                        rootView.fragment_choose_game_type_hard_btn.background = ButtonDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                        rootView.fragment_choose_game_type_hard_dummy.background = ButtonDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                        rootView.fragment_choose_game_type_hard_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                        setStatistic(rootView.fragment_choose_game_type_hard_number_of_games_default,tableWidthNormal,buttonsHeight,true,null)
                        setStatistic(rootView.fragment_choose_game_type_hard_number_of_games_user,tableWidthNormal,buttonsHeight,true,user.hardGame.numberOfGames.toString())
                        setStatistic(rootView.fragment_choose_game_type_hard_score_default,tableWidthNormal,buttonsHeight,true,null)
                        setStatistic(rootView.fragment_choose_game_type_hard_score_user,tableWidthNormal,buttonsHeight,true,getScore(user.hardGame))

                        if(hardGameSaved){
                            rootView.fragment_choose_game_type_hard_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.pause))
                        }else{
                            rootView.fragment_choose_game_type_hard_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.play))
                        }

                        rootView.fragment_choose_game_type_hard_play_pause_btn.setOnClickListener {
                            goToSinglePlayerHardGame()
                        }
                    }else{
                        rootView.fragment_choose_game_type_hard_btn.background = ButtonGreyDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                        rootView.fragment_choose_game_type_hard_dummy.background = ButtonGreyDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                        rootView.fragment_choose_game_type_hard_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_grey_buttons))
                        setStatistic(rootView.fragment_choose_game_type_hard_number_of_games_default,tableWidthNormal,buttonsHeight,false,null)
                        setStatistic(rootView.fragment_choose_game_type_hard_number_of_games_user,tableWidthNormal,buttonsHeight,false,user.hardGame.numberOfGames.toString())
                        setStatistic(rootView.fragment_choose_game_type_hard_score_default,tableWidthNormal,buttonsHeight,false,null)
                        setStatistic(rootView.fragment_choose_game_type_hard_score_user,tableWidthNormal,buttonsHeight,false,getScore(user.hardGame))
                        rootView.fragment_choose_game_type_hard_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.play_grey))
                    }

                    if(userDB.hardGameNumberOfGame>=10){
                        rootView.fragment_choose_game_type_multi_btn.background = ButtonDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                        rootView.fragment_choose_game_type_multi_dummy.background = ButtonDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                        rootView.fragment_choose_game_type_multi_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                        setStatistic(rootView.fragment_choose_game_type_multi_number_of_games_default,tableWidthNormal,buttonsHeight,true,null)
                        setStatistic(rootView.fragment_choose_game_type_multi_number_of_games_user,tableWidthNormal,buttonsHeight,true,user.multiGame.numberOfGames.toString())
                        setStatistic(rootView.fragment_choose_game_type_multi_score_default,tableWidthNormal,buttonsHeight,true,null)
                        setStatistic(rootView.fragment_choose_game_type_multi_score_user,tableWidthNormal,buttonsHeight,true,getScore(user.multiGame))

                        multiGameButtonOptions(userDB.playWithPeople)

                        rootView.fragment_choose_game_type_multi_play_pause_btn.setOnClickListener {
                            if (userDB.playWithPeople) {
                                when (multiGameState) {
                                    Static.MULTI_GAME_MATCH_READY -> playMultiPlayer()
                                    Static.MULTI_GAME_RECEIVED_INVITATION -> acceptOrNotInvitation()
                                    Static.MULTI_GAME_SENT_INVITATION -> waitForAcceptationFromOpponent()
                                    Static.MULTI_GAME_NOT_SET_UP -> chooseOpponent()
                                }
                            }
                            else{
                                alertDialogEnablePlayWithPeople(userDB)
                            }
                        }


                    }else{
                        rootView.fragment_choose_game_type_multi_btn.background = ButtonGreyDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                        rootView.fragment_choose_game_type_multi_dummy.background = ButtonGreyDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                        rootView.fragment_choose_game_type_multi_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_grey_buttons))
                        setStatistic(rootView.fragment_choose_game_type_multi_number_of_games_default,tableWidthNormal,buttonsHeight,false,null)
                        setStatistic(rootView.fragment_choose_game_type_multi_number_of_games_user,tableWidthNormal,buttonsHeight,false,user.multiGame.numberOfGames.toString())
                        setStatistic(rootView.fragment_choose_game_type_multi_score_default,tableWidthNormal,buttonsHeight,false,null)
                        setStatistic(rootView.fragment_choose_game_type_multi_score_user,tableWidthNormal,buttonsHeight,false,getScore(user.multiGame))
                        rootView.fragment_choose_game_type_multi_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.play_grey))


                    }


                }
            }
        }else{

            rootView.fragment_choose_game_type_easy_btn.background = ButtonDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
            rootView.fragment_choose_game_type_easy_dummy.background = ButtonDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
            rootView.fragment_choose_game_type_easy_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
            setStatistic(rootView.fragment_choose_game_type_easy_number_of_games_default,tableWidthNormal,buttonsHeight,false,null)
            setStatistic(rootView.fragment_choose_game_type_easy_number_of_games_user,tableWidthNormal,buttonsHeight,false,"0%")
            setStatistic(rootView.fragment_choose_game_type_easy_score_default,tableWidthNormal,buttonsHeight,false,null)
            setStatistic(rootView.fragment_choose_game_type_easy_score_user,tableWidthNormal,buttonsHeight,false,"0%")
            rootView.fragment_choose_game_type_easy_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.play))


            rootView.fragment_choose_game_type_normal_btn.background = ButtonDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
            rootView.fragment_choose_game_type_normal_dummy.background = ButtonDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
            rootView.fragment_choose_game_type_normal_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
            setStatistic(rootView.fragment_choose_game_type_normal_number_of_games_default,tableWidthNormal,buttonsHeight,false,null)
            setStatistic(rootView.fragment_choose_game_type_normal_number_of_games_user,tableWidthNormal,buttonsHeight,false,"0%")
            setStatistic(rootView.fragment_choose_game_type_normal_score_default,tableWidthNormal,buttonsHeight,false,null)
            setStatistic(rootView.fragment_choose_game_type_normal_score_user,tableWidthNormal,buttonsHeight,false,"0%")
            rootView.fragment_choose_game_type_normal_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.play))

            rootView.fragment_choose_game_type_hard_btn.background = ButtonDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
            rootView.fragment_choose_game_type_hard_dummy.background = ButtonDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
            rootView.fragment_choose_game_type_hard_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
            setStatistic(rootView.fragment_choose_game_type_hard_number_of_games_default,tableWidthNormal,buttonsHeight,false,null)
            setStatistic(rootView.fragment_choose_game_type_hard_number_of_games_user,tableWidthNormal,buttonsHeight,false,"0%")
            setStatistic(rootView.fragment_choose_game_type_hard_score_default,tableWidthNormal,buttonsHeight,false,null)
            setStatistic(rootView.fragment_choose_game_type_hard_score_user,tableWidthNormal,buttonsHeight,false,"0%")
            rootView.fragment_choose_game_type_hard_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.play))

            rootView.fragment_choose_game_type_multi_btn.background = ButtonGreyDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
            rootView.fragment_choose_game_type_multi_dummy.background = ButtonGreyDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
            rootView.fragment_choose_game_type_multi_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_grey_buttons))
            setStatistic(rootView.fragment_choose_game_type_multi_number_of_games_default,tableWidthNormal,buttonsHeight,false,null)
            setStatistic(rootView.fragment_choose_game_type_multi_number_of_games_user,tableWidthNormal,buttonsHeight,false,"0%")
            setStatistic(rootView.fragment_choose_game_type_multi_score_default,tableWidthNormal,buttonsHeight,false,null)
            setStatistic(rootView.fragment_choose_game_type_multi_score_user,tableWidthNormal,buttonsHeight,false,"0%")
            rootView.fragment_choose_game_type_multi_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.play_grey))

            rootView.fragment_choose_game_type_total_score_string.background = ButtonGreyDrawable(requireContext(),(totalScoreString).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
            rootView.fragment_choose_game_type_total_score_string.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_grey_buttons))
            rootView.fragment_choose_game_type_total_score_user.background = ButtonGreyDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
            rootView.fragment_choose_game_type_total_score_user.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_grey_buttons))
            rootView.fragment_choose_game_type_total_score_user.text = "0%"

            rootView.fragment_choose_game_type_easy_play_pause_btn.setOnClickListener {
                goToSinglePlayerEasyGame()
            }

            rootView.fragment_choose_game_type_normal_play_pause_btn.setOnClickListener {
                goToSinglePlayerNormalGame()
            }

            rootView.fragment_choose_game_type_hard_play_pause_btn.setOnClickListener {
                goToSinglePlayerHardGame()
            }
        }

    }

    private fun alertDialogEnablePlayWithPeople(userDB: UserDB) {
        val mBuilder = AlertDialog.Builder(requireContext())
        val mView = layoutInflater.inflate(R.layout.alert_dialog_play_with_people,null)
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

        mView.alert_dialog_play_with_people_title.layoutParams = ConstraintLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,3*screenUnit)
        mView.alert_dialog_play_with_people_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        mView.alert_dialog_play_with_people_title.text = "PLAY WITH PEOPLE?"

        mView.alert_dialog_play_with_people_message.layoutParams = ConstraintLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        mView.alert_dialog_play_with_people_message.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        mView.alert_dialog_play_with_people_message.setLineSpacing(screenUnit.toFloat(),0.85f)
        mView.alert_dialog_play_with_people_message.text = "If You want to play with other people you have to enable this function. As soon as You do it other people can also invite You to play"

        mView.alert_dialog_play_with_people_check_box.layoutParams = ConstraintLayout.LayoutParams(screenUnit,screenUnit)
        mView.alert_dialog_play_with_people_check_box.background = CheckBoxDrawable(requireContext(),screenUnit.toDouble(),screenUnit.toDouble(),true)

        displayCheckBox(userDB,mView)


        mView.alert_dialog_play_with_people_string.layoutParams = ConstraintLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        mView.alert_dialog_play_with_people_string.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        mView.alert_dialog_play_with_people_ok_button.layoutParams = ConstraintLayout.LayoutParams(4*screenUnit,3*screenUnit)
        mView.alert_dialog_play_with_people_ok_button.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        mView.alert_dialog_play_with_people_ok_button.background = ButtonDrawable(requireContext(), (4*screenUnit).toDouble(), (3*screenUnit).toDouble(), screenUnit.toDouble())

        val set = ConstraintSet()
        set.clone(mView.alert_dialog_play_with_people_layout)

        set.connect(mView.alert_dialog_play_with_people_title.id,ConstraintSet.TOP,mView.alert_dialog_play_with_people_layout.id,ConstraintSet.TOP,0)
        set.connect(mView.alert_dialog_play_with_people_title.id,ConstraintSet.LEFT,mView.alert_dialog_play_with_people_layout.id,ConstraintSet.LEFT,0)

        set.connect(mView.alert_dialog_play_with_people_message.id,ConstraintSet.TOP,mView.alert_dialog_play_with_people_title.id,ConstraintSet.BOTTOM,0)
        set.connect(mView.alert_dialog_play_with_people_message.id,ConstraintSet.LEFT,mView.alert_dialog_play_with_people_title.id,ConstraintSet.LEFT,0)

        set.connect(mView.alert_dialog_play_with_people_check_box.id,ConstraintSet.TOP,mView.alert_dialog_play_with_people_layout.id,ConstraintSet.TOP,12 * screenUnit)
        set.connect(mView.alert_dialog_play_with_people_check_box.id,ConstraintSet.LEFT,mView.alert_dialog_play_with_people_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(mView.alert_dialog_play_with_people_string.id,ConstraintSet.TOP,mView.alert_dialog_play_with_people_check_box.id,ConstraintSet.TOP,0)
        set.connect(mView.alert_dialog_play_with_people_string.id,ConstraintSet.LEFT,mView.alert_dialog_play_with_people_check_box.id,ConstraintSet.RIGHT,screenUnit)

        set.connect(mView.alert_dialog_play_with_people_ok_button.id,ConstraintSet.TOP,mView.alert_dialog_play_with_people_string.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(mView.alert_dialog_play_with_people_ok_button.id,ConstraintSet.RIGHT,mView.alert_dialog_play_with_people_layout.id,ConstraintSet.RIGHT,screenUnit)


        set.connect(mView.alert_dialog_play_with_people_dummy_tv.id,ConstraintSet.TOP,mView.alert_dialog_play_with_people_ok_button.id,ConstraintSet.BOTTOM,0)
        set.connect(mView.alert_dialog_play_with_people_dummy_tv.id,ConstraintSet.LEFT,mView.alert_dialog_play_with_people_layout.id,ConstraintSet.LEFT,0)

        set.applyTo(mView.alert_dialog_play_with_people_layout)


        mView.alert_dialog_play_with_people_check_box.setOnClickListener {
            userDB.playWithPeople = !userDB.playWithPeople
            launch {
                requireContext().let {
                    UserDBDatabase(it).getUserDBDao().updateUserInDB(userDB)
                }
            }
            displayCheckBox(userDB,mView)
        }

        mView.alert_dialog_play_with_people_ok_button.setOnClickListener {
            multiGameButtonOptions(userDB.playWithPeople)
            dialog.dismiss()
        }


        dialog.show()




    }

    private fun displayCheckBox(userDB: UserDB, mView: View) {
        if(userDB.playWithPeople){
            mView.alert_dialog_play_with_people_check_box.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.check))
        }else{
            mView.alert_dialog_play_with_people_check_box.setImageDrawable(null)
        }

    }

    private fun chooseOpponent() {
        activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MultiPlayerListFragment()).commit()

    }

    private fun waitForAcceptationFromOpponent() {
        // todo alert dialog z informacja ze jeszcze nie zaakceptowane przez przeciwnika wiec czekac czy rezygnowac?

    }

    private fun acceptOrNotInvitation() {
        // todo alert dialog z zaakceptuj lub odrzuc zaproszenie - najlepiej ze statystykami przeciwnika

    }

    private fun playMultiPlayer() {
        // todo go to multi player match fragment

    }



    private fun multiGameButtonOptions(playWithPeople:Boolean) {
        if(playWithPeople){
            multiGame().run()
        }
        else{
            rootView.fragment_choose_game_type_multi_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.play_grey))
        }

    }

    private fun multiGame():Runnable = Runnable {
        val dbRef = Firebase.database.getReference("Invitation").child(loggedInStatus.userid)
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(!snapshot.exists()){
                    val invitation = Invitation()
                    dbRef.setValue(invitation)
                    multiGameHandler.postDelayed(multiGame(),1000)
                }else{
                    val invitation = snapshot.getValue(Invitation::class.java)
                    multiGameState = checkMultiGameState(invitation!!)

                    if (isAdded) {
                        when (multiGameState) {
                            Static.MULTI_GAME_NOT_SET_UP -> {
                                rootView.fragment_choose_game_type_multi_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.invite))
                            }
                            Static.MULTI_GAME_SENT_INVITATION -> rootView.fragment_choose_game_type_multi_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.waiting))
                            Static.MULTI_GAME_RECEIVED_INVITATION -> rootView.fragment_choose_game_type_multi_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.invitation))
                            Static.MULTI_GAME_MATCH_READY -> rootView.fragment_choose_game_type_multi_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.play))
                        }

                    }


                    multiGameHandler.postDelayed(multiGame(),1000)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun checkMultiGameState(invitation: Invitation): Int {
        var state = 0
        state = when {
            !invitation.myAccept and (!invitation.opponentAccept) -> {
                Static.MULTI_GAME_NOT_SET_UP
            }
            invitation.myAccept and (!invitation.opponentAccept) -> {
                Static.MULTI_GAME_SENT_INVITATION
            }
            !invitation.myAccept and (invitation.opponentAccept) -> {
                Static.MULTI_GAME_RECEIVED_INVITATION
            }
            else -> {
                Static.MULTI_GAME_MATCH_READY
            }
        }

        return  state
    }

    private fun calculateTotalScore(user: User): CharSequence {
        var totalScore = 0.0
        val easyScore = calculatePercent(user.easyGame)
        val normalScore = calculatePercent(user.normalGame)
        val hardScore = calculatePercent(user.hardGame)
        val multiScore = calculatePercent(user.multiGame)

        totalScore = (easyScore+2*normalScore+3*hardScore+3*multiScore)/9

        totalScore*=100
        totalScore = Math.round(totalScore).toDouble()
        totalScore/=100



        return "$totalScore%"
    }

    private fun getScore(game: Game):String{

        return if(game.numberOfGames==0){
            "0%"
        }else{

            val number = calculatePercent(game)


            "$number%"
        }
    }

    private fun calculatePercent(game: Game):Double {
        val wins = (game.win*3).toDouble()
        val ties = game.tie.toDouble()
        val total = (game.numberOfGames*3).toDouble()
        var number:Double = ((wins+ties)/total)*100

        number*=100
        number = Math.round(number).toDouble()
        number/=100

        return number
    }

    private fun setStatistic(view:TextView,width:Int,height:Int,black:Boolean,text:String?){
        if(black){
            view.background = ButtonDrawable(requireContext(),width.toDouble(),height.toDouble(),screenUnit.toDouble())
            view.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
            text?.let {
                view.text = text
            }
        }
        else{
            view.background = ButtonGreyDrawable(requireContext(),width.toDouble(),height.toDouble(),screenUnit.toDouble())
            view.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_grey_buttons))
            text?.let {
                view.text = text
            }
        }

    }

    private fun makeConstraintLayout(){
        val set = ConstraintSet()
        set.clone(rootView.fragment_choose_game_type_layout)

        set.connect(rootView.fragment_choose_game_type_easy_number_of_games_default.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_layout.id, ConstraintSet.TOP,marginTop+2*screenUnit)
        set.connect(rootView.fragment_choose_game_type_easy_number_of_games_default.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_layout.id, ConstraintSet.LEFT,marginLeft)

        set.connect(rootView.fragment_choose_game_type_easy_number_of_games_user.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_easy_number_of_games_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_easy_number_of_games_user.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_easy_number_of_games_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_choose_game_type_easy_score_default.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_easy_number_of_games_default.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_choose_game_type_easy_score_default.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_easy_number_of_games_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_choose_game_type_easy_score_user.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_easy_score_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_easy_score_user.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_easy_score_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_choose_game_type_easy_btn.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_easy_score_default.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_choose_game_type_easy_btn.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_easy_score_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_choose_game_type_easy_dummy.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_easy_btn.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_easy_dummy.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_easy_btn.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_choose_game_type_easy_play_pause_btn.id,ConstraintSet.TOP,rootView.fragment_choose_game_type_easy_dummy.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_choose_game_type_easy_play_pause_btn.id,ConstraintSet.LEFT,rootView.fragment_choose_game_type_easy_dummy.id,ConstraintSet.LEFT,2*screenUnit)



        set.connect(rootView.fragment_choose_game_type_normal_number_of_games_default.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_easy_number_of_games_user.id, ConstraintSet.BOTTOM,marginTop)
        set.connect(rootView.fragment_choose_game_type_normal_number_of_games_default.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_layout.id, ConstraintSet.LEFT,marginLeft)

        set.connect(rootView.fragment_choose_game_type_normal_number_of_games_user.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_normal_number_of_games_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_normal_number_of_games_user.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_normal_number_of_games_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_choose_game_type_normal_score_default.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_normal_number_of_games_default.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_choose_game_type_normal_score_default.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_normal_number_of_games_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_choose_game_type_normal_score_user.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_normal_score_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_normal_score_user.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_normal_score_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_choose_game_type_normal_btn.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_normal_score_default.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_choose_game_type_normal_btn.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_normal_score_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_choose_game_type_normal_dummy.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_normal_btn.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_normal_dummy.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_normal_btn.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_choose_game_type_normal_play_pause_btn.id,ConstraintSet.TOP,rootView.fragment_choose_game_type_normal_dummy.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_choose_game_type_normal_play_pause_btn.id,ConstraintSet.LEFT,rootView.fragment_choose_game_type_normal_dummy.id,ConstraintSet.LEFT,2*screenUnit)


        set.connect(rootView.fragment_choose_game_type_hard_number_of_games_default.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_normal_number_of_games_user.id, ConstraintSet.BOTTOM,marginTop)
        set.connect(rootView.fragment_choose_game_type_hard_number_of_games_default.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_layout.id, ConstraintSet.LEFT,marginLeft)

        set.connect(rootView.fragment_choose_game_type_hard_number_of_games_user.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_hard_number_of_games_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_hard_number_of_games_user.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_hard_number_of_games_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_choose_game_type_hard_score_default.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_hard_number_of_games_default.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_choose_game_type_hard_score_default.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_hard_number_of_games_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_choose_game_type_hard_score_user.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_hard_score_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_hard_score_user.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_hard_score_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_choose_game_type_hard_btn.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_hard_score_default.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_choose_game_type_hard_btn.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_hard_score_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_choose_game_type_hard_dummy.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_hard_btn.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_hard_dummy.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_hard_btn.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_choose_game_type_hard_play_pause_btn.id,ConstraintSet.TOP,rootView.fragment_choose_game_type_hard_dummy.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_choose_game_type_hard_play_pause_btn.id,ConstraintSet.LEFT,rootView.fragment_choose_game_type_hard_dummy.id,ConstraintSet.LEFT,2*screenUnit)



        set.connect(rootView.fragment_choose_game_type_multi_number_of_games_default.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_hard_number_of_games_user.id,ConstraintSet.BOTTOM,marginTop)
        set.connect(rootView.fragment_choose_game_type_multi_number_of_games_default.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView.fragment_choose_game_type_multi_number_of_games_user.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_multi_number_of_games_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_multi_number_of_games_user.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_multi_number_of_games_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_choose_game_type_multi_score_default.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_multi_number_of_games_default.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_choose_game_type_multi_score_default.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_multi_number_of_games_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_choose_game_type_multi_score_user.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_multi_score_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_multi_score_user.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_multi_score_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_choose_game_type_multi_btn.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_multi_score_default.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_choose_game_type_multi_btn.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_multi_score_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_choose_game_type_multi_dummy.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_multi_btn.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_multi_dummy.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_multi_btn.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_choose_game_type_multi_play_pause_btn.id,ConstraintSet.TOP,rootView.fragment_choose_game_type_multi_dummy.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_choose_game_type_multi_play_pause_btn.id,ConstraintSet.LEFT,rootView.fragment_choose_game_type_multi_dummy.id,ConstraintSet.LEFT,2*screenUnit)


        set.connect(rootView.fragment_choose_game_type_back_button.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_layout.id, ConstraintSet.TOP,screenUnit)
        set.connect(rootView.fragment_choose_game_type_back_button.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_layout.id, ConstraintSet.LEFT,16*screenUnit)

        set.connect(rootView.fragment_choose_game_type_total_score_string.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_multi_number_of_games_user.id,ConstraintSet.BOTTOM,marginTop)
        set.connect(rootView.fragment_choose_game_type_total_score_string.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView.fragment_choose_game_type_total_score_user.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_total_score_string.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_choose_game_type_total_score_user.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_total_score_string.id,ConstraintSet.RIGHT,0)

        set.applyTo(rootView.fragment_choose_game_type_layout)

    }

    private fun setButtonsUI(){
        rootView.fragment_choose_game_type_easy_btn.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_easy_dummy.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)

        rootView.fragment_choose_game_type_easy_btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenUnit.toFloat())

        rootView.fragment_choose_game_type_easy_play_pause_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)

        rootView.fragment_choose_game_type_easy_number_of_games_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_easy_score_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)

        rootView.fragment_choose_game_type_easy_number_of_games_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_easy_score_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        rootView.fragment_choose_game_type_easy_number_of_games_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_easy_score_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)

        rootView.fragment_choose_game_type_easy_number_of_games_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_easy_score_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())


        rootView.fragment_choose_game_type_normal_btn.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_normal_dummy.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_normal_btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenUnit.toFloat())
        rootView.fragment_choose_game_type_normal_play_pause_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)

        rootView.fragment_choose_game_type_normal_number_of_games_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_normal_score_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)

        rootView.fragment_choose_game_type_normal_number_of_games_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_normal_score_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())


        rootView.fragment_choose_game_type_normal_number_of_games_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_normal_score_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)

        rootView.fragment_choose_game_type_normal_number_of_games_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_normal_score_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())



        rootView.fragment_choose_game_type_hard_btn.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_hard_dummy.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_hard_btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenUnit.toFloat())
        rootView.fragment_choose_game_type_hard_play_pause_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)

        rootView.fragment_choose_game_type_hard_number_of_games_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_hard_score_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)

        rootView.fragment_choose_game_type_hard_number_of_games_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_hard_score_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        rootView.fragment_choose_game_type_hard_number_of_games_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_hard_score_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)

        rootView.fragment_choose_game_type_hard_number_of_games_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_hard_score_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        rootView.fragment_choose_game_type_multi_btn.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_multi_dummy.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_multi_btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenUnit.toFloat())
        rootView.fragment_choose_game_type_multi_play_pause_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)

        rootView.fragment_choose_game_type_multi_number_of_games_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_multi_score_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)

        rootView.fragment_choose_game_type_multi_number_of_games_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_multi_score_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        rootView.fragment_choose_game_type_multi_number_of_games_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_multi_score_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)

        rootView.fragment_choose_game_type_multi_number_of_games_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_multi_score_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        rootView.fragment_choose_game_type_back_button.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_choose_game_type_back_button.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.close))

        rootView.fragment_choose_game_type_total_score_string.layoutParams = ConstraintLayout.LayoutParams(totalScoreString,buttonsHeight)
        rootView.fragment_choose_game_type_total_score_string.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        rootView.fragment_choose_game_type_total_score_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_total_score_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

    }

    private fun setSizes(){

        buttonsHeight=2*screenUnit
        marginLeft = screenUnit
        marginTop=2*screenUnit
        tableWidthNormal = 6*screenUnit
        totalScoreString = 12 * screenUnit
    }

    private fun setBackgroundGrid(){
        rootView.fragment_choose_game_type_layout.background = TileDrawable((ContextCompat.getDrawable(requireContext(),R.drawable.background)!!),
            Shader.TileMode.REPEAT,screenUnit)
    }

}
