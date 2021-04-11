package com.tt.oldschoolsoccer.fragments

import android.graphics.Shader
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import com.tt.oldschoolsoccer.database.UserDBDatabase
import com.tt.oldschoolsoccer.drawable.ButtonDrawable
import com.tt.oldschoolsoccer.drawable.ButtonGreyDrawable
import com.tt.oldschoolsoccer.drawable.TileDrawable
import com.tt.oldschoolsoccer.fragments.singlePlayer.SinglePlayerEasyGameFragment
import com.tt.oldschoolsoccer.fragments.singlePlayer.SinglePlayerHardGameFragment
import com.tt.oldschoolsoccer.fragments.singlePlayer.SinglePlayerNormalGameFragment
import kotlinx.android.synthetic.main.fragment_choose_game_type.view.*
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
    private var rootView: View? = null
    private var totalScoreString = 0
    private val multiGameHandler = Handler()
    private var multiGameState = Static.MULTI_GAME_NOT_SET_UP
    private val multiPlayerIconHandler = Handler()



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
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_choose_game_type, container, false)

        makeUI()

        setOnClickListeners()

        return rootView
    }

    private fun setOnClickListeners(){

        rootView!!.fragment_choose_game_type_back_button.setOnClickListener {
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
        displayMultiPlayerIcon().run()

    }

    private fun displayMultiPlayerIcon():Runnable = Runnable {
        if (isAdded) {
            when (multiGameState) {
                Static.MULTI_GAME_NOT_SET_UP -> {
                    rootView?.fragment_choose_game_type_multi_play_pause_btn?.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.invite))
                }
                Static.MULTI_GAME_SENT_INVITATION -> rootView?.fragment_choose_game_type_multi_play_pause_btn?.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.waiting))
                Static.MULTI_GAME_RECEIVED_INVITATION -> rootView?.fragment_choose_game_type_multi_play_pause_btn?.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.invitation))
                Static.MULTI_GAME_MATCH_READY -> rootView?.fragment_choose_game_type_multi_play_pause_btn?.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.play))
            }
            multiPlayerIconHandler.postDelayed(displayMultiPlayerIcon(), 1000)
        }
    }



    override fun onPause() {
        super.onPause()
        multiGameHandler.removeCallbacks(null)
        multiPlayerIconHandler.removeCallbacks(null)
    }


    private fun setViewForLoggedInNotLoggedIn() {
        if(loggedInStatus.loggedIn){
            launch {
                requireContext().let {
                    val userDB = UserDBDatabase(it).getUserDBDao().getUser(loggedInStatus.userid)
                    val user = User().userFromDB(userDB)

                    rootView!!.fragment_choose_game_type_total_score_string.background = ButtonDrawable(requireContext(),(totalScoreString).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                    rootView!!.fragment_choose_game_type_total_score_string.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_black))
                    rootView!!.fragment_choose_game_type_total_score_user.background = ButtonDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                    rootView!!.fragment_choose_game_type_total_score_user.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_black))
                    rootView!!.fragment_choose_game_type_total_score_user.text = calculateTotalScore(user)


                    rootView!!.fragment_choose_game_type_easy_btn.background = ButtonDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                    rootView!!.fragment_choose_game_type_easy_dummy.background = ButtonDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                    rootView!!.fragment_choose_game_type_easy_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                    setStatistic(rootView!!.fragment_choose_game_type_easy_number_of_games_default,tableWidthNormal,buttonsHeight,true,null)
                    setStatistic(rootView!!.fragment_choose_game_type_easy_number_of_games_user,tableWidthNormal,buttonsHeight,true,user.easyGame.numberOfGames.toString())
                    setStatistic(rootView!!.fragment_choose_game_type_easy_score_default,tableWidthNormal,buttonsHeight,true,null)
                    setStatistic(rootView!!.fragment_choose_game_type_easy_score_user,tableWidthNormal,buttonsHeight,true,getScore(user.easyGame))
                    if(easyGameSaved){
                        rootView!!.fragment_choose_game_type_easy_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.pause))
                    }else{
                        rootView!!.fragment_choose_game_type_easy_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.play))
                    }

                    rootView!!.fragment_choose_game_type_easy_play_pause_btn.setOnClickListener {
                        goToSinglePlayerEasyGame()
                    }

                    if(userDB.easyGameNumberOfGame>=10){
                        rootView!!.fragment_choose_game_type_normal_btn.background = ButtonDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                        rootView!!.fragment_choose_game_type_normal_dummy.background = ButtonDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                        rootView!!.fragment_choose_game_type_normal_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                        setStatistic(rootView!!.fragment_choose_game_type_normal_number_of_games_default,tableWidthNormal,buttonsHeight,true,null)
                        setStatistic(rootView!!.fragment_choose_game_type_normal_number_of_games_user,tableWidthNormal,buttonsHeight,true,user.normalGame.numberOfGames.toString())
                        setStatistic(rootView!!.fragment_choose_game_type_normal_score_default,tableWidthNormal,buttonsHeight,true,null)
                        setStatistic(rootView!!.fragment_choose_game_type_normal_score_user,tableWidthNormal,buttonsHeight,true,getScore(user.normalGame))
                        if(normalGameSaved){
                            rootView!!.fragment_choose_game_type_normal_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.pause))
                        }else{
                            rootView!!.fragment_choose_game_type_normal_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.play))
                        }
                        rootView!!.fragment_choose_game_type_normal_play_pause_btn.setOnClickListener {
                            goToSinglePlayerNormalGame()
                        }
                    }else{
                        rootView!!.fragment_choose_game_type_normal_btn.background = ButtonGreyDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                        rootView!!.fragment_choose_game_type_normal_dummy.background = ButtonGreyDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                        rootView!!.fragment_choose_game_type_normal_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_grey_buttons))
                        setStatistic(rootView!!.fragment_choose_game_type_normal_number_of_games_default,tableWidthNormal,buttonsHeight,false,null)
                        setStatistic(rootView!!.fragment_choose_game_type_normal_number_of_games_user,tableWidthNormal,buttonsHeight,false,user.normalGame.numberOfGames.toString())
                        setStatistic(rootView!!.fragment_choose_game_type_normal_score_default,tableWidthNormal,buttonsHeight,false,null)
                        setStatistic(rootView!!.fragment_choose_game_type_normal_score_user,tableWidthNormal,buttonsHeight,false,getScore(user.normalGame))
                        rootView!!.fragment_choose_game_type_normal_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.play_grey))
                    }

                    if(userDB.normalGameNumberOfGame>=10){
                        rootView!!.fragment_choose_game_type_hard_btn.background = ButtonDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                        rootView!!.fragment_choose_game_type_hard_dummy.background = ButtonDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                        rootView!!.fragment_choose_game_type_hard_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                        setStatistic(rootView!!.fragment_choose_game_type_hard_number_of_games_default,tableWidthNormal,buttonsHeight,true,null)
                        setStatistic(rootView!!.fragment_choose_game_type_hard_number_of_games_user,tableWidthNormal,buttonsHeight,true,user.hardGame.numberOfGames.toString())
                        setStatistic(rootView!!.fragment_choose_game_type_hard_score_default,tableWidthNormal,buttonsHeight,true,null)
                        setStatistic(rootView!!.fragment_choose_game_type_hard_score_user,tableWidthNormal,buttonsHeight,true,getScore(user.hardGame))

                        if(hardGameSaved){
                            rootView!!.fragment_choose_game_type_hard_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.pause))
                        }else{
                            rootView!!.fragment_choose_game_type_hard_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.play))
                        }

                        rootView!!.fragment_choose_game_type_hard_play_pause_btn.setOnClickListener {
                            goToSinglePlayerHardGame()
                        }
                    }else{
                        rootView!!.fragment_choose_game_type_hard_btn.background = ButtonGreyDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                        rootView!!.fragment_choose_game_type_hard_dummy.background = ButtonGreyDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                        rootView!!.fragment_choose_game_type_hard_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_grey_buttons))
                        setStatistic(rootView!!.fragment_choose_game_type_hard_number_of_games_default,tableWidthNormal,buttonsHeight,false,null)
                        setStatistic(rootView!!.fragment_choose_game_type_hard_number_of_games_user,tableWidthNormal,buttonsHeight,false,user.hardGame.numberOfGames.toString())
                        setStatistic(rootView!!.fragment_choose_game_type_hard_score_default,tableWidthNormal,buttonsHeight,false,null)
                        setStatistic(rootView!!.fragment_choose_game_type_hard_score_user,tableWidthNormal,buttonsHeight,false,getScore(user.hardGame))
                        rootView!!.fragment_choose_game_type_hard_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.play_grey))
                    }

                    if(userDB.hardGameNumberOfGame>=10){
                        rootView!!.fragment_choose_game_type_multi_btn.background = ButtonDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                        rootView!!.fragment_choose_game_type_multi_dummy.background = ButtonDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                        rootView!!.fragment_choose_game_type_multi_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                        setStatistic(rootView!!.fragment_choose_game_type_multi_number_of_games_default,tableWidthNormal,buttonsHeight,true,null)
                        setStatistic(rootView!!.fragment_choose_game_type_multi_number_of_games_user,tableWidthNormal,buttonsHeight,true,user.multiGame.numberOfGames.toString())
                        setStatistic(rootView!!.fragment_choose_game_type_multi_score_default,tableWidthNormal,buttonsHeight,true,null)
                        setStatistic(rootView!!.fragment_choose_game_type_multi_score_user,tableWidthNormal,buttonsHeight,true,getScore(user.multiGame))

                        multiGameButtonOptions()


                    }else{
                        rootView!!.fragment_choose_game_type_multi_btn.background = ButtonGreyDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                        rootView!!.fragment_choose_game_type_multi_dummy.background = ButtonGreyDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
                        rootView!!.fragment_choose_game_type_multi_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_grey_buttons))
                        setStatistic(rootView!!.fragment_choose_game_type_multi_number_of_games_default,tableWidthNormal,buttonsHeight,false,null)
                        setStatistic(rootView!!.fragment_choose_game_type_multi_number_of_games_user,tableWidthNormal,buttonsHeight,false,user.multiGame.numberOfGames.toString())
                        setStatistic(rootView!!.fragment_choose_game_type_multi_score_default,tableWidthNormal,buttonsHeight,false,null)
                        setStatistic(rootView!!.fragment_choose_game_type_multi_score_user,tableWidthNormal,buttonsHeight,false,getScore(user.multiGame))
                        rootView!!.fragment_choose_game_type_multi_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.play_grey))


                    }


                }
            }
        }else{

            rootView!!.fragment_choose_game_type_easy_btn.background = ButtonDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
            rootView!!.fragment_choose_game_type_easy_dummy.background = ButtonDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
            rootView!!.fragment_choose_game_type_easy_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
            setStatistic(rootView!!.fragment_choose_game_type_easy_number_of_games_default,tableWidthNormal,buttonsHeight,false,null)
            setStatistic(rootView!!.fragment_choose_game_type_easy_number_of_games_user,tableWidthNormal,buttonsHeight,false,"0%")
            setStatistic(rootView!!.fragment_choose_game_type_easy_score_default,tableWidthNormal,buttonsHeight,false,null)
            setStatistic(rootView!!.fragment_choose_game_type_easy_score_user,tableWidthNormal,buttonsHeight,false,"0%")
            rootView!!.fragment_choose_game_type_easy_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.play))


            rootView!!.fragment_choose_game_type_normal_btn.background = ButtonDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
            rootView!!.fragment_choose_game_type_normal_dummy.background = ButtonDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
            rootView!!.fragment_choose_game_type_normal_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
            setStatistic(rootView!!.fragment_choose_game_type_normal_number_of_games_default,tableWidthNormal,buttonsHeight,false,null)
            setStatistic(rootView!!.fragment_choose_game_type_normal_number_of_games_user,tableWidthNormal,buttonsHeight,false,"0%")
            setStatistic(rootView!!.fragment_choose_game_type_normal_score_default,tableWidthNormal,buttonsHeight,false,null)
            setStatistic(rootView!!.fragment_choose_game_type_normal_score_user,tableWidthNormal,buttonsHeight,false,"0%")
            rootView!!.fragment_choose_game_type_normal_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.play))

            rootView!!.fragment_choose_game_type_hard_btn.background = ButtonDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
            rootView!!.fragment_choose_game_type_hard_dummy.background = ButtonDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
            rootView!!.fragment_choose_game_type_hard_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
            setStatistic(rootView!!.fragment_choose_game_type_hard_number_of_games_default,tableWidthNormal,buttonsHeight,false,null)
            setStatistic(rootView!!.fragment_choose_game_type_hard_number_of_games_user,tableWidthNormal,buttonsHeight,false,"0%")
            setStatistic(rootView!!.fragment_choose_game_type_hard_score_default,tableWidthNormal,buttonsHeight,false,null)
            setStatistic(rootView!!.fragment_choose_game_type_hard_score_user,tableWidthNormal,buttonsHeight,false,"0%")
            rootView!!.fragment_choose_game_type_hard_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.play))

            rootView!!.fragment_choose_game_type_multi_btn.background = ButtonGreyDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
            rootView!!.fragment_choose_game_type_multi_dummy.background = ButtonGreyDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
            rootView!!.fragment_choose_game_type_multi_btn.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_grey_buttons))
            setStatistic(rootView!!.fragment_choose_game_type_multi_number_of_games_default,tableWidthNormal,buttonsHeight,false,null)
            setStatistic(rootView!!.fragment_choose_game_type_multi_number_of_games_user,tableWidthNormal,buttonsHeight,false,"0%")
            setStatistic(rootView!!.fragment_choose_game_type_multi_score_default,tableWidthNormal,buttonsHeight,false,null)
            setStatistic(rootView!!.fragment_choose_game_type_multi_score_user,tableWidthNormal,buttonsHeight,false,"0%")
            rootView!!.fragment_choose_game_type_multi_play_pause_btn.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.play_grey))

            rootView!!.fragment_choose_game_type_total_score_string.background = ButtonGreyDrawable(requireContext(),(totalScoreString).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
            rootView!!.fragment_choose_game_type_total_score_string.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_grey_buttons))
            rootView!!.fragment_choose_game_type_total_score_user.background = ButtonGreyDrawable(requireContext(),(tableWidthNormal).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
            rootView!!.fragment_choose_game_type_total_score_user.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_grey_buttons))
            rootView!!.fragment_choose_game_type_total_score_user.text = "0%"

            rootView!!.fragment_choose_game_type_easy_play_pause_btn.setOnClickListener {
                goToSinglePlayerEasyGame()
            }

            rootView!!.fragment_choose_game_type_normal_play_pause_btn.setOnClickListener {
                goToSinglePlayerNormalGame()
            }

            rootView!!.fragment_choose_game_type_hard_play_pause_btn.setOnClickListener {
                goToSinglePlayerHardGame()
            }
        }

    }

    private fun multiGameButtonOptions() {
        multiGame().run()

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
        set.clone(rootView!!.fragment_choose_game_type_layout)

        set.connect(rootView!!.fragment_choose_game_type_easy_number_of_games_default.id, ConstraintSet.TOP,rootView!!.fragment_choose_game_type_layout.id, ConstraintSet.TOP,marginTop+2*screenUnit)
        set.connect(rootView!!.fragment_choose_game_type_easy_number_of_games_default.id, ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_layout.id, ConstraintSet.LEFT,marginLeft)

        set.connect(rootView!!.fragment_choose_game_type_easy_number_of_games_user.id, ConstraintSet.TOP,rootView!!.fragment_choose_game_type_easy_number_of_games_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView!!.fragment_choose_game_type_easy_number_of_games_user.id, ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_easy_number_of_games_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView!!.fragment_choose_game_type_easy_score_default.id, ConstraintSet.TOP,rootView!!.fragment_choose_game_type_easy_number_of_games_default.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_choose_game_type_easy_score_default.id, ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_easy_number_of_games_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView!!.fragment_choose_game_type_easy_score_user.id, ConstraintSet.TOP,rootView!!.fragment_choose_game_type_easy_score_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView!!.fragment_choose_game_type_easy_score_user.id, ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_easy_score_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView!!.fragment_choose_game_type_easy_btn.id, ConstraintSet.TOP,rootView!!.fragment_choose_game_type_easy_score_default.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_choose_game_type_easy_btn.id, ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_easy_score_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView!!.fragment_choose_game_type_easy_dummy.id, ConstraintSet.TOP,rootView!!.fragment_choose_game_type_easy_btn.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView!!.fragment_choose_game_type_easy_dummy.id, ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_easy_btn.id,ConstraintSet.LEFT,0)

        set.connect(rootView!!.fragment_choose_game_type_easy_play_pause_btn.id,ConstraintSet.TOP,rootView!!.fragment_choose_game_type_easy_dummy.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_choose_game_type_easy_play_pause_btn.id,ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_easy_dummy.id,ConstraintSet.LEFT,2*screenUnit)



        set.connect(rootView!!.fragment_choose_game_type_normal_number_of_games_default.id, ConstraintSet.TOP,rootView!!.fragment_choose_game_type_easy_number_of_games_user.id, ConstraintSet.BOTTOM,marginTop)
        set.connect(rootView!!.fragment_choose_game_type_normal_number_of_games_default.id, ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_layout.id, ConstraintSet.LEFT,marginLeft)

        set.connect(rootView!!.fragment_choose_game_type_normal_number_of_games_user.id, ConstraintSet.TOP,rootView!!.fragment_choose_game_type_normal_number_of_games_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView!!.fragment_choose_game_type_normal_number_of_games_user.id, ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_normal_number_of_games_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView!!.fragment_choose_game_type_normal_score_default.id, ConstraintSet.TOP,rootView!!.fragment_choose_game_type_normal_number_of_games_default.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_choose_game_type_normal_score_default.id, ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_normal_number_of_games_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView!!.fragment_choose_game_type_normal_score_user.id, ConstraintSet.TOP,rootView!!.fragment_choose_game_type_normal_score_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView!!.fragment_choose_game_type_normal_score_user.id, ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_normal_score_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView!!.fragment_choose_game_type_normal_btn.id, ConstraintSet.TOP,rootView!!.fragment_choose_game_type_normal_score_default.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_choose_game_type_normal_btn.id, ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_normal_score_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView!!.fragment_choose_game_type_normal_dummy.id, ConstraintSet.TOP,rootView!!.fragment_choose_game_type_normal_btn.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView!!.fragment_choose_game_type_normal_dummy.id, ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_normal_btn.id,ConstraintSet.LEFT,0)

        set.connect(rootView!!.fragment_choose_game_type_normal_play_pause_btn.id,ConstraintSet.TOP,rootView!!.fragment_choose_game_type_normal_dummy.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_choose_game_type_normal_play_pause_btn.id,ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_normal_dummy.id,ConstraintSet.LEFT,2*screenUnit)


        set.connect(rootView!!.fragment_choose_game_type_hard_number_of_games_default.id, ConstraintSet.TOP,rootView!!.fragment_choose_game_type_normal_number_of_games_user.id, ConstraintSet.BOTTOM,marginTop)
        set.connect(rootView!!.fragment_choose_game_type_hard_number_of_games_default.id, ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_layout.id, ConstraintSet.LEFT,marginLeft)

        set.connect(rootView!!.fragment_choose_game_type_hard_number_of_games_user.id, ConstraintSet.TOP,rootView!!.fragment_choose_game_type_hard_number_of_games_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView!!.fragment_choose_game_type_hard_number_of_games_user.id, ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_hard_number_of_games_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView!!.fragment_choose_game_type_hard_score_default.id, ConstraintSet.TOP,rootView!!.fragment_choose_game_type_hard_number_of_games_default.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_choose_game_type_hard_score_default.id, ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_hard_number_of_games_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView!!.fragment_choose_game_type_hard_score_user.id, ConstraintSet.TOP,rootView!!.fragment_choose_game_type_hard_score_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView!!.fragment_choose_game_type_hard_score_user.id, ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_hard_score_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView!!.fragment_choose_game_type_hard_btn.id, ConstraintSet.TOP,rootView!!.fragment_choose_game_type_hard_score_default.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_choose_game_type_hard_btn.id, ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_hard_score_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView!!.fragment_choose_game_type_hard_dummy.id, ConstraintSet.TOP,rootView!!.fragment_choose_game_type_hard_btn.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView!!.fragment_choose_game_type_hard_dummy.id, ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_hard_btn.id,ConstraintSet.LEFT,0)

        set.connect(rootView!!.fragment_choose_game_type_hard_play_pause_btn.id,ConstraintSet.TOP,rootView!!.fragment_choose_game_type_hard_dummy.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_choose_game_type_hard_play_pause_btn.id,ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_hard_dummy.id,ConstraintSet.LEFT,2*screenUnit)



        set.connect(rootView!!.fragment_choose_game_type_multi_number_of_games_default.id, ConstraintSet.TOP,rootView!!.fragment_choose_game_type_hard_number_of_games_user.id,ConstraintSet.BOTTOM,marginTop)
        set.connect(rootView!!.fragment_choose_game_type_multi_number_of_games_default.id, ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView!!.fragment_choose_game_type_multi_number_of_games_user.id, ConstraintSet.TOP,rootView!!.fragment_choose_game_type_multi_number_of_games_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView!!.fragment_choose_game_type_multi_number_of_games_user.id, ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_multi_number_of_games_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView!!.fragment_choose_game_type_multi_score_default.id, ConstraintSet.TOP,rootView!!.fragment_choose_game_type_multi_number_of_games_default.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_choose_game_type_multi_score_default.id, ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_multi_number_of_games_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView!!.fragment_choose_game_type_multi_score_user.id, ConstraintSet.TOP,rootView!!.fragment_choose_game_type_multi_score_default.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView!!.fragment_choose_game_type_multi_score_user.id, ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_multi_score_default.id,ConstraintSet.LEFT,0)

        set.connect(rootView!!.fragment_choose_game_type_multi_btn.id, ConstraintSet.TOP,rootView!!.fragment_choose_game_type_multi_score_default.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_choose_game_type_multi_btn.id, ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_multi_score_default.id,ConstraintSet.RIGHT,0)

        set.connect(rootView!!.fragment_choose_game_type_multi_dummy.id, ConstraintSet.TOP,rootView!!.fragment_choose_game_type_multi_btn.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView!!.fragment_choose_game_type_multi_dummy.id, ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_multi_btn.id,ConstraintSet.LEFT,0)

        set.connect(rootView!!.fragment_choose_game_type_multi_play_pause_btn.id,ConstraintSet.TOP,rootView!!.fragment_choose_game_type_multi_dummy.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_choose_game_type_multi_play_pause_btn.id,ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_multi_dummy.id,ConstraintSet.LEFT,2*screenUnit)


        set.connect(rootView!!.fragment_choose_game_type_back_button.id, ConstraintSet.TOP,rootView!!.fragment_choose_game_type_layout.id, ConstraintSet.TOP,screenUnit)
        set.connect(rootView!!.fragment_choose_game_type_back_button.id, ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_layout.id, ConstraintSet.LEFT,16*screenUnit)

        set.connect(rootView!!.fragment_choose_game_type_total_score_string.id, ConstraintSet.TOP,rootView!!.fragment_choose_game_type_multi_number_of_games_user.id,ConstraintSet.BOTTOM,marginTop)
        set.connect(rootView!!.fragment_choose_game_type_total_score_string.id, ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView!!.fragment_choose_game_type_total_score_user.id, ConstraintSet.TOP,rootView!!.fragment_choose_game_type_total_score_string.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_choose_game_type_total_score_user.id, ConstraintSet.LEFT,rootView!!.fragment_choose_game_type_total_score_string.id,ConstraintSet.RIGHT,0)

        set.applyTo(rootView!!.fragment_choose_game_type_layout)

    }

    private fun setButtonsUI(){
        rootView!!.fragment_choose_game_type_easy_btn.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView!!.fragment_choose_game_type_easy_dummy.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)

        rootView!!.fragment_choose_game_type_easy_btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenUnit.toFloat())

        rootView!!.fragment_choose_game_type_easy_play_pause_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)

        rootView!!.fragment_choose_game_type_easy_number_of_games_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView!!.fragment_choose_game_type_easy_score_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)

        rootView!!.fragment_choose_game_type_easy_number_of_games_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView!!.fragment_choose_game_type_easy_score_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        rootView!!.fragment_choose_game_type_easy_number_of_games_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView!!.fragment_choose_game_type_easy_score_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)

        rootView!!.fragment_choose_game_type_easy_number_of_games_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView!!.fragment_choose_game_type_easy_score_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())


        rootView!!.fragment_choose_game_type_normal_btn.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView!!.fragment_choose_game_type_normal_dummy.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView!!.fragment_choose_game_type_normal_btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenUnit.toFloat())
        rootView!!.fragment_choose_game_type_normal_play_pause_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)

        rootView!!.fragment_choose_game_type_normal_number_of_games_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView!!.fragment_choose_game_type_normal_score_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)

        rootView!!.fragment_choose_game_type_normal_number_of_games_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView!!.fragment_choose_game_type_normal_score_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())


        rootView!!.fragment_choose_game_type_normal_number_of_games_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView!!.fragment_choose_game_type_normal_score_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)

        rootView!!.fragment_choose_game_type_normal_number_of_games_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView!!.fragment_choose_game_type_normal_score_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())



        rootView!!.fragment_choose_game_type_hard_btn.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView!!.fragment_choose_game_type_hard_dummy.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView!!.fragment_choose_game_type_hard_btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenUnit.toFloat())
        rootView!!.fragment_choose_game_type_hard_play_pause_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)

        rootView!!.fragment_choose_game_type_hard_number_of_games_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView!!.fragment_choose_game_type_hard_score_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)

        rootView!!.fragment_choose_game_type_hard_number_of_games_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView!!.fragment_choose_game_type_hard_score_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        rootView!!.fragment_choose_game_type_hard_number_of_games_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView!!.fragment_choose_game_type_hard_score_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)

        rootView!!.fragment_choose_game_type_hard_number_of_games_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView!!.fragment_choose_game_type_hard_score_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        rootView!!.fragment_choose_game_type_multi_btn.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView!!.fragment_choose_game_type_multi_dummy.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView!!.fragment_choose_game_type_multi_btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenUnit.toFloat())
        rootView!!.fragment_choose_game_type_multi_play_pause_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)

        rootView!!.fragment_choose_game_type_multi_number_of_games_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView!!.fragment_choose_game_type_multi_score_default.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)

        rootView!!.fragment_choose_game_type_multi_number_of_games_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView!!.fragment_choose_game_type_multi_score_default.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        rootView!!.fragment_choose_game_type_multi_number_of_games_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView!!.fragment_choose_game_type_multi_score_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)

        rootView!!.fragment_choose_game_type_multi_number_of_games_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView!!.fragment_choose_game_type_multi_score_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        rootView!!.fragment_choose_game_type_back_button.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView!!.fragment_choose_game_type_back_button.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.close))

        rootView!!.fragment_choose_game_type_total_score_string.layoutParams = ConstraintLayout.LayoutParams(totalScoreString,buttonsHeight)
        rootView!!.fragment_choose_game_type_total_score_string.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        rootView!!.fragment_choose_game_type_total_score_user.layoutParams = ConstraintLayout.LayoutParams(tableWidthNormal,buttonsHeight)
        rootView!!.fragment_choose_game_type_total_score_user.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

    }

    private fun setSizes(){

        buttonsHeight=2*screenUnit
        marginLeft = screenUnit
        marginTop=2*screenUnit
        tableWidthNormal = 6*screenUnit
        totalScoreString = 12 * screenUnit
    }

    private fun setBackgroundGrid(){
        rootView!!.fragment_choose_game_type_layout.background = TileDrawable((ContextCompat.getDrawable(requireContext(),R.drawable.background)!!),
            Shader.TileMode.REPEAT,screenUnit)
    }

}
