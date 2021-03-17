package com.tt.oldschoolsoccer.fragments

import android.graphics.Shader
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tt.oldschoolsoccer.R
import com.tt.oldschoolsoccer.classes.FragmentCoroutine
import com.tt.oldschoolsoccer.classes.Functions
import com.tt.oldschoolsoccer.classes.LoggedInStatus
import com.tt.oldschoolsoccer.database.UserDBDatabase
import com.tt.oldschoolsoccer.drawable.ButtonDrawable
import com.tt.oldschoolsoccer.drawable.ButtonGreyDrawable
import com.tt.oldschoolsoccer.drawable.TileDrawable
import com.tt.oldschoolsoccer.fragments.singlePlayer.SinglePlayerEasyGameFragment
import com.tt.oldschoolsoccer.fragments.singlePlayer.SinglePlayerHardGameFragment
import com.tt.oldschoolsoccer.fragments.singlePlayer.SinglePlayerNormalGameFragment
import kotlinx.android.synthetic.main.fragment_choose_game_type.view.*
import kotlinx.android.synthetic.main.fragment_statistics.view.*
import kotlinx.coroutines.launch


class ChooseGameTypeFragment : FragmentCoroutine() {

    private var tableWidthNormal = 0
    private var tableWidthBig = 0
    private var screenUnit=0
    private var buttonsHeight=0
    private var buttonsWidth=0
    private var marginLeft=0
    private var marginTop=0
    private var loggedInStatus = LoggedInStatus()
    private var easyGameSaved = false
    private var normalGameSaved = false
    private var hardGameSaved = false
    private lateinit var rootView: View



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

    private fun setViewForLoggedInNotLoggedIn() {
        if(loggedInStatus.loggedIn){
            launch {
                requireContext().let {
                    val user = UserDBDatabase(it).getUserDBDao().getUser(loggedInStatus.userid)
                    rootView.fragment_choose_game_type_easy_btn.background = ButtonDrawable(requireContext(),(buttonsWidth).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                    rootView.fragment_choose_game_type_easy_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                    setStatistic(rootView.fragment_choose_game_type_easy_number_of_games_default,tableWidthBig,buttonsHeight,true,null)
                    setStatistic(rootView.fragment_choose_game_type_easy_number_of_games_user,tableWidthBig,buttonsHeight,true,user.easyGameNumberOfGame.toString())
                    setStatistic(rootView.fragment_choose_game_type_easy_win_default,tableWidthNormal,buttonsHeight,true,null)
                    setStatistic(rootView.fragment_choose_game_type_easy_win_user,tableWidthNormal,buttonsHeight,true,user.easyGameWin.toString())
                    setStatistic(rootView.fragment_choose_game_type_easy_lose_default,tableWidthNormal,buttonsHeight,true,null)
                    setStatistic(rootView.fragment_choose_game_type_easy_lose_user,tableWidthNormal,buttonsHeight,true,user.easyGameLose.toString())
                    setStatistic(rootView.fragment_choose_game_type_easy_tie_default,tableWidthNormal,buttonsHeight,true,null)
                    setStatistic(rootView.fragment_choose_game_type_easy_tie_user,tableWidthNormal,buttonsHeight,true,user.easyGameTie.toString())

                    rootView.fragment_choose_game_type_easy_btn.setOnClickListener {
                        goToSinglePlayerEasyGame()
                    }

                    if(user.easyGameNumberOfGame>=10){
                        rootView.fragment_choose_game_type_normal_btn.background = ButtonDrawable(requireContext(),(buttonsWidth).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                        rootView.fragment_choose_game_type_normal_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                        setStatistic(rootView.fragment_choose_game_type_normal_number_of_games_default,tableWidthBig,buttonsHeight,true,null)
                        setStatistic(rootView.fragment_choose_game_type_normal_number_of_games_user,tableWidthBig,buttonsHeight,true,user.normalGameNumberOfGame.toString())
                        setStatistic(rootView.fragment_choose_game_type_normal_win_default,tableWidthNormal,buttonsHeight,true,null)
                        setStatistic(rootView.fragment_choose_game_type_normal_win_user,tableWidthNormal,buttonsHeight,true,user.normalGameWin.toString())
                        setStatistic(rootView.fragment_choose_game_type_normal_lose_default,tableWidthNormal,buttonsHeight,true,null)
                        setStatistic(rootView.fragment_choose_game_type_normal_lose_user,tableWidthNormal,buttonsHeight,true,user.normalGameLose.toString())
                        setStatistic(rootView.fragment_choose_game_type_normal_tie_default,tableWidthNormal,buttonsHeight,true,null)
                        setStatistic(rootView.fragment_choose_game_type_normal_tie_user,tableWidthNormal,buttonsHeight,true,user.normalGameTie.toString())
                        rootView.fragment_choose_game_type_normal_btn.setOnClickListener {
                            goToSinglePlayerNormalGame()
                        }
                    }else{
                        rootView.fragment_choose_game_type_normal_btn.background = ButtonGreyDrawable(requireContext(),(buttonsWidth).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                        rootView.fragment_choose_game_type_normal_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_grey_buttons))
                        setStatistic(rootView.fragment_choose_game_type_normal_number_of_games_default,tableWidthBig,buttonsHeight,false,null)
                        setStatistic(rootView.fragment_choose_game_type_normal_number_of_games_user,tableWidthBig,buttonsHeight,false,user.normalGameNumberOfGame.toString())
                        setStatistic(rootView.fragment_choose_game_type_normal_win_default,tableWidthNormal,buttonsHeight,false,null)
                        setStatistic(rootView.fragment_choose_game_type_normal_win_user,tableWidthNormal,buttonsHeight,false,user.normalGameWin.toString())
                        setStatistic(rootView.fragment_choose_game_type_normal_lose_default,tableWidthNormal,buttonsHeight,false,null)
                        setStatistic(rootView.fragment_choose_game_type_normal_lose_user,tableWidthNormal,buttonsHeight,false,user.normalGameLose.toString())
                        setStatistic(rootView.fragment_choose_game_type_normal_tie_default,tableWidthNormal,buttonsHeight,false,null)
                        setStatistic(rootView.fragment_choose_game_type_normal_tie_user,tableWidthNormal,buttonsHeight,false,user.normalGameTie.toString())
                    }

                    if(user.normalGameNumberOfGame>=10){
                        rootView.fragment_choose_game_type_hard_btn.background = ButtonDrawable(requireContext(),(buttonsWidth).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                        rootView.fragment_choose_game_type_hard_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                        setStatistic(rootView.fragment_choose_game_type_hard_number_of_games_default,tableWidthBig,buttonsHeight,true,null)
                        setStatistic(rootView.fragment_choose_game_type_hard_number_of_games_user,tableWidthBig,buttonsHeight,true,user.hardGameNumberOfGame.toString())
                        setStatistic(rootView.fragment_choose_game_type_hard_win_default,tableWidthNormal,buttonsHeight,true,null)
                        setStatistic(rootView.fragment_choose_game_type_hard_win_user,tableWidthNormal,buttonsHeight,true,user.hardGameWin.toString())
                        setStatistic(rootView.fragment_choose_game_type_hard_lose_default,tableWidthNormal,buttonsHeight,true,null)
                        setStatistic(rootView.fragment_choose_game_type_hard_lose_user,tableWidthNormal,buttonsHeight,true,user.hardGameLose.toString())
                        setStatistic(rootView.fragment_choose_game_type_hard_tie_default,tableWidthNormal,buttonsHeight,true,null)
                        setStatistic(rootView.fragment_choose_game_type_hard_tie_user,tableWidthNormal,buttonsHeight,true,user.hardGameTie.toString())
                        rootView.fragment_choose_game_type_hard_btn.setOnClickListener {
                            goToSinglePlayerHardGame()
                        }
                    }else{
                        rootView.fragment_choose_game_type_hard_btn.background = ButtonGreyDrawable(requireContext(),(buttonsWidth).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                        rootView.fragment_choose_game_type_hard_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_grey_buttons))
                        setStatistic(rootView.fragment_choose_game_type_hard_number_of_games_default,tableWidthBig,buttonsHeight,false,null)
                        setStatistic(rootView.fragment_choose_game_type_hard_number_of_games_user,tableWidthBig,buttonsHeight,false,user.hardGameNumberOfGame.toString())
                        setStatistic(rootView.fragment_choose_game_type_hard_win_default,tableWidthNormal,buttonsHeight,false,null)
                        setStatistic(rootView.fragment_choose_game_type_hard_win_user,tableWidthNormal,buttonsHeight,false,user.hardGameWin.toString())
                        setStatistic(rootView.fragment_choose_game_type_hard_lose_default,tableWidthNormal,buttonsHeight,false,null)
                        setStatistic(rootView.fragment_choose_game_type_hard_lose_user,tableWidthNormal,buttonsHeight,false,user.hardGameLose.toString())
                        setStatistic(rootView.fragment_choose_game_type_hard_tie_default,tableWidthNormal,buttonsHeight,false,null)
                        setStatistic(rootView.fragment_choose_game_type_hard_tie_user,tableWidthNormal,buttonsHeight,false,user.hardGameTie.toString())
                    }

                    if(user.hardGameNumberOfGame>=10){
                        rootView.fragment_choose_game_type_multi_btn.background = ButtonDrawable(requireContext(),(buttonsWidth).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                        rootView.fragment_choose_game_type_multi_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                        setStatistic(rootView.fragment_choose_game_type_multi_number_of_games_default,tableWidthBig,buttonsHeight,true,null)
                        setStatistic(rootView.fragment_choose_game_type_multi_number_of_games_user,tableWidthBig,buttonsHeight,true,user.multiGameNumberOfGame.toString())
                        setStatistic(rootView.fragment_choose_game_type_multi_win_default,tableWidthNormal,buttonsHeight,true,null)
                        setStatistic(rootView.fragment_choose_game_type_multi_win_user,tableWidthNormal,buttonsHeight,true,user.multiGameWin.toString())
                        setStatistic(rootView.fragment_choose_game_type_multi_lose_default,tableWidthNormal,buttonsHeight,true,null)
                        setStatistic(rootView.fragment_choose_game_type_multi_lose_user,tableWidthNormal,buttonsHeight,true,user.multiGameLose.toString())
                        setStatistic(rootView.fragment_choose_game_type_multi_tie_default,tableWidthNormal,buttonsHeight,true,null)
                        setStatistic(rootView.fragment_choose_game_type_multi_tie_user,tableWidthNormal,buttonsHeight,true,user.multiGameTie.toString())
                    }else{
                        rootView.fragment_choose_game_type_multi_btn.background = ButtonGreyDrawable(requireContext(),(buttonsWidth).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                        rootView.fragment_choose_game_type_multi_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_grey_buttons))
                        setStatistic(rootView.fragment_choose_game_type_multi_number_of_games_default,tableWidthBig,buttonsHeight,false,null)
                        setStatistic(rootView.fragment_choose_game_type_multi_number_of_games_user,tableWidthBig,buttonsHeight,false,user.multiGameNumberOfGame.toString())
                        setStatistic(rootView.fragment_choose_game_type_multi_win_default,tableWidthNormal,buttonsHeight,false,null)
                        setStatistic(rootView.fragment_choose_game_type_multi_win_user,tableWidthNormal,buttonsHeight,false,user.multiGameWin.toString())
                        setStatistic(rootView.fragment_choose_game_type_multi_lose_default,tableWidthNormal,buttonsHeight,false,null)
                        setStatistic(rootView.fragment_choose_game_type_multi_lose_user,tableWidthNormal,buttonsHeight,false,user.multiGameLose.toString())
                        setStatistic(rootView.fragment_choose_game_type_multi_tie_default,tableWidthNormal,buttonsHeight,false,null)
                        setStatistic(rootView.fragment_choose_game_type_multi_tie_user,tableWidthNormal,buttonsHeight,false,user.multiGameTie.toString())
                    }


                }
            }
        }else{

            rootView.fragment_choose_game_type_easy_btn.background = ButtonDrawable(requireContext(),(buttonsWidth).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
            rootView.fragment_choose_game_type_easy_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
            setStatistic(rootView.fragment_choose_game_type_easy_number_of_games_default,tableWidthBig,buttonsHeight,false,null)
            setStatistic(rootView.fragment_choose_game_type_easy_number_of_games_user,tableWidthBig,buttonsHeight,false,"0")
            setStatistic(rootView.fragment_choose_game_type_easy_win_default,tableWidthNormal,buttonsHeight,false,null)
            setStatistic(rootView.fragment_choose_game_type_easy_win_user,tableWidthNormal,buttonsHeight,false,"0")
            setStatistic(rootView.fragment_choose_game_type_easy_lose_default,tableWidthNormal,buttonsHeight,false,null)
            setStatistic(rootView.fragment_choose_game_type_easy_lose_user,tableWidthNormal,buttonsHeight,false,"0")
            setStatistic(rootView.fragment_choose_game_type_easy_tie_default,tableWidthNormal,buttonsHeight,false,null)
            setStatistic(rootView.fragment_choose_game_type_easy_tie_user,tableWidthNormal,buttonsHeight,false,"0")

            rootView.fragment_choose_game_type_normal_btn.background = ButtonDrawable(requireContext(),(buttonsWidth).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
            rootView.fragment_choose_game_type_normal_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
            setStatistic(rootView.fragment_choose_game_type_normal_number_of_games_default,tableWidthBig,buttonsHeight,false,null)
            setStatistic(rootView.fragment_choose_game_type_normal_number_of_games_user,tableWidthBig,buttonsHeight,false,"0")
            setStatistic(rootView.fragment_choose_game_type_normal_win_default,tableWidthNormal,buttonsHeight,false,null)
            setStatistic(rootView.fragment_choose_game_type_normal_win_user,tableWidthNormal,buttonsHeight,false,"0")
            setStatistic(rootView.fragment_choose_game_type_normal_lose_default,tableWidthNormal,buttonsHeight,false,null)
            setStatistic(rootView.fragment_choose_game_type_normal_lose_user,tableWidthNormal,buttonsHeight,false,"0")
            setStatistic(rootView.fragment_choose_game_type_normal_tie_default,tableWidthNormal,buttonsHeight,false,null)
            setStatistic(rootView.fragment_choose_game_type_normal_tie_user,tableWidthNormal,buttonsHeight,false,"0")

            rootView.fragment_choose_game_type_hard_btn.background = ButtonDrawable(requireContext(),(buttonsWidth).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
            rootView.fragment_choose_game_type_hard_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
            setStatistic(rootView.fragment_choose_game_type_hard_number_of_games_default,tableWidthBig,buttonsHeight,false,null)
            setStatistic(rootView.fragment_choose_game_type_hard_number_of_games_user,tableWidthBig,buttonsHeight,false,"0")
            setStatistic(rootView.fragment_choose_game_type_hard_win_default,tableWidthNormal,buttonsHeight,false,null)
            setStatistic(rootView.fragment_choose_game_type_hard_win_user,tableWidthNormal,buttonsHeight,false,"0")
            setStatistic(rootView.fragment_choose_game_type_hard_lose_default,tableWidthNormal,buttonsHeight,false,null)
            setStatistic(rootView.fragment_choose_game_type_hard_lose_user,tableWidthNormal,buttonsHeight,false,"0")
            setStatistic(rootView.fragment_choose_game_type_hard_tie_default,tableWidthNormal,buttonsHeight,false,null)
            setStatistic(rootView.fragment_choose_game_type_hard_tie_user,tableWidthNormal,buttonsHeight,false,"0")

            rootView.fragment_choose_game_type_multi_btn.background = ButtonGreyDrawable(requireContext(),(buttonsWidth).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
            rootView.fragment_choose_game_type_multi_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_grey_buttons))
            setStatistic(rootView.fragment_choose_game_type_multi_number_of_games_default,tableWidthBig,buttonsHeight,false,null)
            setStatistic(rootView.fragment_choose_game_type_multi_number_of_games_user,tableWidthBig,buttonsHeight,false,"0")
            setStatistic(rootView.fragment_choose_game_type_multi_win_default,tableWidthNormal,buttonsHeight,false,null)
            setStatistic(rootView.fragment_choose_game_type_multi_win_user,tableWidthNormal,buttonsHeight,false,"0")
            setStatistic(rootView.fragment_choose_game_type_multi_lose_default,tableWidthNormal,buttonsHeight,false,null)
            setStatistic(rootView.fragment_choose_game_type_multi_lose_user,tableWidthNormal,buttonsHeight,false,"0")
            setStatistic(rootView.fragment_choose_game_type_multi_tie_default,tableWidthNormal,buttonsHeight,false,null)
            setStatistic(rootView.fragment_choose_game_type_multi_tie_user,tableWidthNormal,buttonsHeight,false,"0")

            rootView.fragment_choose_game_type_easy_btn.setOnClickListener {
                goToSinglePlayerEasyGame()
            }

            rootView.fragment_choose_game_type_normal_btn.setOnClickListener {
                goToSinglePlayerNormalGame()
            }

            rootView.fragment_choose_game_type_hard_btn.setOnClickListener {
                goToSinglePlayerHardGame()
            }
        }

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

        set.connect(rootView.fragment_choose_game_type_easy_btn.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_layout.id, ConstraintSet.TOP,marginTop+2*screenUnit)
        set.connect(rootView.fragment_choose_game_type_easy_btn.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_layout.id, ConstraintSet.LEFT,marginLeft)

        set.connect(rootView.fragment_choose_game_type_easy_number_of_games_default.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_easy_btn.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_easy_number_of_games_default.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_easy_btn.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_choose_game_type_easy_number_of_games_user.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_easy_number_of_games_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_easy_number_of_games_user.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_easy_number_of_games_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_choose_game_type_easy_win_default.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_easy_btn.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_easy_win_default.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_easy_number_of_games_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_choose_game_type_easy_win_user.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_easy_win_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_easy_win_user.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_easy_win_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_choose_game_type_easy_lose_default.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_easy_btn.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_easy_lose_default.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_easy_win_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_choose_game_type_easy_lose_user.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_easy_lose_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_easy_lose_user.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_easy_lose_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_choose_game_type_easy_tie_default.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_easy_btn.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_easy_tie_default.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_easy_lose_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_choose_game_type_easy_tie_user.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_easy_tie_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_easy_tie_user.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_easy_tie_default.id,ConstraintSet.LEFT,0)



        set.connect(rootView.fragment_choose_game_type_normal_btn.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_easy_number_of_games_user.id, ConstraintSet.BOTTOM,marginTop)
        set.connect(rootView.fragment_choose_game_type_normal_btn.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_layout.id, ConstraintSet.LEFT,marginLeft)

        set.connect(rootView.fragment_choose_game_type_normal_number_of_games_default.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_normal_btn.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_normal_number_of_games_default.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_normal_btn.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_choose_game_type_normal_number_of_games_user.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_normal_number_of_games_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_normal_number_of_games_user.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_normal_number_of_games_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_choose_game_type_normal_win_default.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_normal_btn.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_normal_win_default.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_normal_number_of_games_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_choose_game_type_normal_win_user.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_normal_win_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_normal_win_user.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_normal_win_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_choose_game_type_normal_lose_default.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_normal_btn.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_normal_lose_default.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_normal_win_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_choose_game_type_normal_lose_user.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_normal_lose_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_normal_lose_user.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_normal_lose_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_choose_game_type_normal_tie_default.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_normal_btn.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_normal_tie_default.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_normal_lose_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_choose_game_type_normal_tie_user.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_normal_tie_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_normal_tie_user.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_normal_tie_default.id,ConstraintSet.LEFT,0)




        set.connect(rootView.fragment_choose_game_type_hard_btn.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_normal_number_of_games_user.id, ConstraintSet.BOTTOM,marginTop)
        set.connect(rootView.fragment_choose_game_type_hard_btn.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_layout.id, ConstraintSet.LEFT,marginLeft)

        set.connect(rootView.fragment_choose_game_type_hard_number_of_games_default.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_hard_btn.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_hard_number_of_games_default.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_hard_btn.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_choose_game_type_hard_number_of_games_user.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_hard_number_of_games_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_hard_number_of_games_user.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_hard_number_of_games_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_choose_game_type_hard_win_default.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_hard_btn.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_hard_win_default.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_hard_number_of_games_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_choose_game_type_hard_win_user.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_hard_win_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_hard_win_user.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_hard_win_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_choose_game_type_hard_lose_default.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_hard_btn.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_hard_lose_default.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_hard_win_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_choose_game_type_hard_lose_user.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_hard_lose_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_hard_lose_user.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_hard_lose_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_choose_game_type_hard_tie_default.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_hard_btn.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_hard_tie_default.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_hard_lose_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_choose_game_type_hard_tie_user.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_hard_tie_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_hard_tie_user.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_hard_tie_default.id,ConstraintSet.LEFT,0)



        set.connect(rootView.fragment_choose_game_type_multi_btn.id,ConstraintSet.TOP,rootView.fragment_choose_game_type_hard_number_of_games_user.id,ConstraintSet.BOTTOM,marginTop)
        set.connect(rootView.fragment_choose_game_type_multi_btn.id,ConstraintSet.LEFT,rootView.fragment_choose_game_type_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView.fragment_choose_game_type_multi_number_of_games_default.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_multi_btn.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_multi_number_of_games_default.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_multi_btn.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_choose_game_type_multi_number_of_games_user.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_multi_number_of_games_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_multi_number_of_games_user.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_multi_number_of_games_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_choose_game_type_multi_win_default.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_multi_btn.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_multi_win_default.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_multi_number_of_games_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_choose_game_type_multi_win_user.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_multi_win_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_multi_win_user.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_multi_win_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_choose_game_type_multi_lose_default.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_multi_btn.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_multi_lose_default.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_multi_win_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_choose_game_type_multi_lose_user.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_multi_lose_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_multi_lose_user.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_multi_lose_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_choose_game_type_multi_tie_default.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_multi_btn.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_multi_tie_default.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_multi_lose_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_choose_game_type_multi_tie_user.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_multi_tie_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_choose_game_type_multi_tie_user.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_multi_tie_default.id,ConstraintSet.LEFT,0)


        set.connect(rootView.fragment_choose_game_type_back_button.id, ConstraintSet.TOP,rootView.fragment_choose_game_type_layout.id, ConstraintSet.TOP,screenUnit)
        set.connect(rootView.fragment_choose_game_type_back_button.id, ConstraintSet.LEFT,rootView.fragment_choose_game_type_layout.id, ConstraintSet.LEFT,16*screenUnit)

        set.applyTo(rootView.fragment_choose_game_type_layout)

    }

    private fun setButtonsUI(){
        rootView.fragment_choose_game_type_easy_btn.layoutParams = ConstraintLayout.LayoutParams(buttonsWidth,buttonsHeight)

        rootView.fragment_choose_game_type_easy_btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenUnit.toFloat())

        rootView.fragment_choose_game_type_easy_number_of_games_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthBig,buttonsHeight)
        rootView.fragment_choose_game_type_easy_win_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_easy_tie_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_easy_lose_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_easy_number_of_games_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_easy_win_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_easy_tie_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_easy_lose_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_easy_number_of_games_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthBig,buttonsHeight)
        rootView.fragment_choose_game_type_easy_win_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_easy_tie_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_easy_lose_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_easy_number_of_games_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_easy_win_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_easy_tie_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_easy_lose_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        rootView.fragment_choose_game_type_normal_btn.layoutParams = ConstraintLayout.LayoutParams(buttonsWidth,buttonsHeight)

        rootView.fragment_choose_game_type_normal_btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenUnit.toFloat())

        rootView.fragment_choose_game_type_normal_number_of_games_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthBig,buttonsHeight)
        rootView.fragment_choose_game_type_normal_win_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_normal_tie_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_normal_lose_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_normal_number_of_games_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_normal_win_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_normal_tie_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_normal_lose_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        rootView.fragment_choose_game_type_normal_number_of_games_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthBig,buttonsHeight)
        rootView.fragment_choose_game_type_normal_win_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_normal_tie_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_normal_lose_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_normal_number_of_games_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_normal_win_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_normal_tie_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_normal_lose_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())



        rootView.fragment_choose_game_type_hard_btn.layoutParams = ConstraintLayout.LayoutParams(buttonsWidth,buttonsHeight)

        rootView.fragment_choose_game_type_hard_btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenUnit.toFloat())

        rootView.fragment_choose_game_type_hard_number_of_games_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthBig,buttonsHeight)
        rootView.fragment_choose_game_type_hard_win_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_hard_tie_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_hard_lose_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_hard_number_of_games_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_hard_win_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_hard_tie_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_hard_lose_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_hard_number_of_games_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthBig,buttonsHeight)
        rootView.fragment_choose_game_type_hard_win_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_hard_tie_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_hard_lose_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_hard_number_of_games_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_hard_win_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_hard_tie_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_hard_lose_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        rootView.fragment_choose_game_type_multi_btn.layoutParams = ConstraintLayout.LayoutParams(buttonsWidth,buttonsHeight)

        rootView.fragment_choose_game_type_multi_btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenUnit.toFloat())

        rootView.fragment_choose_game_type_multi_number_of_games_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthBig,buttonsHeight)
        rootView.fragment_choose_game_type_multi_win_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_multi_tie_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_multi_lose_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_multi_number_of_games_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_multi_win_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_multi_tie_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_multi_lose_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_multi_number_of_games_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthBig,buttonsHeight)
        rootView.fragment_choose_game_type_multi_win_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_multi_tie_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_multi_lose_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView.fragment_choose_game_type_multi_number_of_games_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_multi_win_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_multi_tie_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_choose_game_type_multi_lose_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())


        rootView.fragment_choose_game_type_back_button.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_choose_game_type_back_button.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.close))


        if(easyGameSaved){
            rootView.fragment_choose_game_type_easy_btn.text = "EASY (SAVED GAME)"
        }
        if(normalGameSaved){
            rootView.fragment_choose_game_type_normal_btn.text = "NORMAL (SAVED GAME)"
        }
        if(hardGameSaved){
            rootView.fragment_choose_game_type_hard_btn.text = "HARD (SAVED GAME)"
        }

    }

    private fun setSizes(){
        buttonsWidth=18*screenUnit
        buttonsHeight=2*screenUnit
        marginLeft = screenUnit
        marginTop=2*screenUnit
        tableWidthBig = 6*screenUnit
        tableWidthNormal = 4*screenUnit
    }

    private fun setBackgroundGrid(){
        rootView.fragment_choose_game_type_layout.background = TileDrawable((ContextCompat.getDrawable(requireContext(),R.drawable.background)!!),
            Shader.TileMode.REPEAT,screenUnit)
    }

}
