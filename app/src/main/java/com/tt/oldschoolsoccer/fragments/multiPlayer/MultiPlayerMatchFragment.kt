package com.tt.oldschoolsoccer.fragments.multiPlayer

import android.app.AlertDialog
import android.graphics.Point
import android.graphics.Shader
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tt.oldschoolsoccer.R
import com.tt.oldschoolsoccer.classes.*
import com.tt.oldschoolsoccer.database.UserDBDatabase
import com.tt.oldschoolsoccer.drawable.*
import com.tt.oldschoolsoccer.fragments.MainFragment
import kotlinx.android.synthetic.main.alert_dialog_end_game.view.*
import kotlinx.android.synthetic.main.fragment_multi_player_match.view.*
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class MultiPlayerMatchFragment : FragmentCoroutine() {

    /*
    * todo
    *   add "WAITING FOR OPPONENT"
    *   add green dot when opponent active
    *
     */

    private var screenSize = ScreenSize()
    private var loggedInStatus = LoggedInStatus()
    private lateinit var rootView: View
    private var field = GameField()
    private var invitation = Invitation()
    private var invitationReady = false
    private val prepareMatchHandler = Handler()
    private val playMatchHandler = Handler()
    private var multiPlayerMatch = MultiPlayerMatch()
    private var counter = 0
    private val moveList = ArrayList<MultiPlayerMove>()
    private val tmpMoveList = ArrayList<MultiPlayerMove>()
    private val endGameWinHandler = Handler()
    private val endGameLoseHandler = Handler()
    private val endGameTieHandler = Handler()
    private var endGameLoopCounter=0
    private lateinit var userName:String
    private lateinit var dialog: AlertDialog
    private var historyRef: DatabaseReference? = null
    private var history = MultiPlayerHistory()


    /**----------------- LIFE CYCLE ------------------------**/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screenSize = Functions.readScreenSize(requireContext())
        loggedInStatus = Functions.readLoggedStateFromSharedPreferences(requireContext())
        historyRef = Firebase.database.getReference("History").child(loggedInStatus.userid)

        historyRef!!.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    history = snapshot.getValue(MultiPlayerHistory::class.java)!!
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        createField()
    }

    override fun onResume() {
        super.onResume()
        prepareMatch().run()
    }

    override fun onPause() {
        super.onPause()
        prepareMatchHandler.removeCallbacksAndMessages(null)
        playMatchHandler.removeCallbacksAndMessages(null)
        endGameWinHandler.removeCallbacksAndMessages(null)
        endGameLoseHandler.removeCallbacksAndMessages(null)
        endGameTieHandler.removeCallbacksAndMessages(null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_multi_player_match, container, false)
        makeUI()
        return rootView
    }


    /**----------------- MAKE UI -------------------------**/

    private fun makeUI() {
        setBackgroundGrid()
        setViewSizes()
        setDrawable()
        setConstraintLayout()
        setOnClickListeners()
        disableButtons()
        makeIcons()
    }

    private fun setBackgroundGrid() {
        rootView.fragment_multi_player_match_layout.background = TileDrawable((ContextCompat.getDrawable(requireContext(),R.drawable.background)!!),
            Shader.TileMode.REPEAT,screenSize.screenUnit)
    }

    private fun setViewSizes() {
        rootView.fragment_multi_player_match_game_field.layoutParams = ConstraintLayout.LayoutParams(14*screenSize.screenUnit,22*screenSize.screenUnit)
        rootView.fragment_multi_player_match_move_up_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenSize.screenUnit,2*screenSize.screenUnit)
        rootView.fragment_multi_player_match_move_up_right_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenSize.screenUnit,2*screenSize.screenUnit)
        rootView.fragment_multi_player_match_move_right_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenSize.screenUnit,2*screenSize.screenUnit)
        rootView.fragment_multi_player_match_move_down_right_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenSize.screenUnit,2*screenSize.screenUnit)
        rootView.fragment_multi_player_match_move_down_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenSize.screenUnit,2*screenSize.screenUnit)
        rootView.fragment_multi_player_match_move_down_left_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenSize.screenUnit,2*screenSize.screenUnit)
        rootView.fragment_multi_player_match_move_left_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenSize.screenUnit,2*screenSize.screenUnit)
        rootView.fragment_multi_player_match_move_up_left_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenSize.screenUnit,2*screenSize.screenUnit)
        rootView.fragment_multi_player_match_game_middle.layoutParams = ConstraintLayout.LayoutParams(2*screenSize.screenUnit,2*screenSize.screenUnit)
        rootView.fragment_multi_player_match_back_button.layoutParams = ConstraintLayout.LayoutParams(2*screenSize.screenUnit,2*screenSize.screenUnit)
        rootView.fragment_multi_player_match_user_icon.layoutParams = ConstraintLayout.LayoutParams(3*screenSize.screenUnit,3*screenSize.screenUnit)
        rootView.fragment_multi_player_match_opponent_icon.layoutParams = ConstraintLayout.LayoutParams(3*screenSize.screenUnit,3*screenSize.screenUnit)
        rootView.fragment_multi_player_match_vs_tv.layoutParams = ConstraintLayout.LayoutParams(3*screenSize.screenUnit,3*screenSize.screenUnit)
        rootView.fragment_multi_player_match_vs_tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenSize.screenUnit.toFloat())
    }

    private fun setDrawable() {
        rootView.fragment_multi_player_match_game_field.background = FieldHardDrawable(requireContext(), screenSize.screenUnit.toDouble())
        rootView.fragment_multi_player_match_move_down_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.down)
        rootView.fragment_multi_player_match_move_down_right_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.down_right)
        rootView.fragment_multi_player_match_move_right_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.right)
        rootView.fragment_multi_player_match_move_up_right_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.up_right)
        rootView.fragment_multi_player_match_move_up_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.up)
        rootView.fragment_multi_player_match_move_up_left_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.up_left)
        rootView.fragment_multi_player_match_move_left_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.left)
        rootView.fragment_multi_player_match_move_down_left_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.down_left)
        rootView.fragment_multi_player_match_back_button.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.close))
    }

    private fun setConstraintLayout() {
        val set = ConstraintSet()
        set.clone(rootView.fragment_multi_player_match_layout)

        set.connect(rootView.fragment_multi_player_match_game_field.id, ConstraintSet.TOP,rootView.fragment_multi_player_match_layout.id, ConstraintSet.TOP,screenSize.screenUnit)
        set.connect(rootView.fragment_multi_player_match_game_field.id, ConstraintSet.LEFT,rootView.fragment_multi_player_match_layout.id, ConstraintSet.LEFT,screenSize.screenUnit)

        set.connect(rootView.fragment_multi_player_match_game_middle.id, ConstraintSet.TOP,rootView.fragment_multi_player_match_layout.id, ConstraintSet.TOP,26*screenSize.screenUnit)
        set.connect(rootView.fragment_multi_player_match_game_middle.id, ConstraintSet.LEFT,rootView.fragment_multi_player_match_layout.id, ConstraintSet.LEFT,9*screenSize.screenUnit)

        set.connect(rootView.fragment_multi_player_match_move_up_btn.id, ConstraintSet.BOTTOM,rootView.fragment_multi_player_match_game_middle.id, ConstraintSet.TOP,screenSize.screenUnit)
        set.connect(rootView.fragment_multi_player_match_move_up_btn.id, ConstraintSet.LEFT,rootView.fragment_multi_player_match_game_middle.id, ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_multi_player_match_move_right_btn.id, ConstraintSet.BOTTOM,rootView.fragment_multi_player_match_game_middle.id, ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_multi_player_match_move_right_btn.id, ConstraintSet.LEFT,rootView.fragment_multi_player_match_game_middle.id, ConstraintSet.RIGHT,screenSize.screenUnit)

        set.connect(rootView.fragment_multi_player_match_move_down_btn.id, ConstraintSet.TOP,rootView.fragment_multi_player_match_game_middle.id, ConstraintSet.BOTTOM,screenSize.screenUnit)
        set.connect(rootView.fragment_multi_player_match_move_down_btn.id, ConstraintSet.LEFT,rootView.fragment_multi_player_match_game_middle.id, ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_multi_player_match_move_left_btn.id, ConstraintSet.BOTTOM,rootView.fragment_multi_player_match_game_middle.id, ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_multi_player_match_move_left_btn.id, ConstraintSet.RIGHT,rootView.fragment_multi_player_match_game_middle.id, ConstraintSet.LEFT,screenSize.screenUnit)

        set.connect(rootView.fragment_multi_player_match_move_up_right_btn.id, ConstraintSet.BOTTOM,rootView.fragment_multi_player_match_game_middle.id, ConstraintSet.TOP,screenSize.screenUnit)
        set.connect(rootView.fragment_multi_player_match_move_up_right_btn.id, ConstraintSet.LEFT,rootView.fragment_multi_player_match_game_middle.id, ConstraintSet.RIGHT,screenSize.screenUnit)

        set.connect(rootView.fragment_multi_player_match_move_down_right_btn.id, ConstraintSet.TOP,rootView.fragment_multi_player_match_game_middle.id, ConstraintSet.BOTTOM,screenSize.screenUnit)
        set.connect(rootView.fragment_multi_player_match_move_down_right_btn.id, ConstraintSet.LEFT,rootView.fragment_multi_player_match_game_middle.id, ConstraintSet.RIGHT,screenSize.screenUnit)

        set.connect(rootView.fragment_multi_player_match_move_down_left_btn.id, ConstraintSet.TOP,rootView.fragment_multi_player_match_game_middle.id, ConstraintSet.BOTTOM,screenSize.screenUnit)
        set.connect(rootView.fragment_multi_player_match_move_down_left_btn.id, ConstraintSet.RIGHT,rootView.fragment_multi_player_match_game_middle.id, ConstraintSet.LEFT,screenSize.screenUnit)

        set.connect(rootView.fragment_multi_player_match_move_up_left_btn.id, ConstraintSet.BOTTOM,rootView.fragment_multi_player_match_game_middle.id, ConstraintSet.TOP,screenSize.screenUnit)
        set.connect(rootView.fragment_multi_player_match_move_up_left_btn.id, ConstraintSet.RIGHT,rootView.fragment_multi_player_match_game_middle.id, ConstraintSet.LEFT,screenSize.screenUnit)

        set.connect(rootView.fragment_multi_player_match_back_button.id, ConstraintSet.TOP,rootView.fragment_multi_player_match_layout.id, ConstraintSet.TOP,screenSize.screenUnit)
        set.connect(rootView.fragment_multi_player_match_back_button.id, ConstraintSet.LEFT,rootView.fragment_multi_player_match_layout.id, ConstraintSet.LEFT,16*screenSize.screenUnit)



        set.connect(rootView.fragment_multi_player_match_opponent_icon.id,
            ConstraintSet.TOP,rootView.fragment_multi_player_match_game_field.id,
            ConstraintSet.TOP,4*screenSize.screenUnit)
        set.connect(rootView.fragment_multi_player_match_opponent_icon.id,
            ConstraintSet.LEFT,rootView.fragment_multi_player_match_game_field.id,
            ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_multi_player_match_vs_tv.id,
            ConstraintSet.TOP,rootView.fragment_multi_player_match_opponent_icon.id,
            ConstraintSet.BOTTOM, (2.5*screenSize.screenUnit).toInt())
        set.connect(rootView.fragment_multi_player_match_vs_tv.id,
            ConstraintSet.LEFT,rootView.fragment_multi_player_match_opponent_icon.id,
            ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_multi_player_match_user_icon.id,
            ConstraintSet.TOP,rootView.fragment_multi_player_match_vs_tv.id,
            ConstraintSet.BOTTOM, (2.5*screenSize.screenUnit).toInt())
        set.connect(rootView.fragment_multi_player_match_user_icon.id,
            ConstraintSet.LEFT,rootView.fragment_multi_player_match_vs_tv.id,
            ConstraintSet.LEFT,0)

        set.applyTo(rootView.fragment_multi_player_match_layout)

    }

    private fun setOnClickListeners() {

        rootView.fragment_multi_player_match_move_up_btn.setOnClickListener {
            if(invitation.orientation==Static.ORIENTATION_NORMAL){
                normalPress(field.moveUp(true), Static.UP)
            }else{
                normalPress(field.moveDown(true), Static.DOWN)
            }

        }
        rootView.fragment_multi_player_match_move_up_right_btn.setOnClickListener {
            if(invitation.orientation==Static.ORIENTATION_NORMAL){
                normalPress(field.moveUpRight(true), Static.UP_RIGHT)
            }else{
                normalPress(field.moveDownLeft(true), Static.DOWN_LEFT)
            }
        }
        rootView.fragment_multi_player_match_move_right_btn.setOnClickListener {
            if(invitation.orientation==Static.ORIENTATION_NORMAL){
                normalPress(field.moveRight(true), Static.RIGHT)
            }else{
                normalPress(field.moveLeft(true), Static.LEFT)
            }
        }
        rootView.fragment_multi_player_match_move_down_right_btn.setOnClickListener {
            if(invitation.orientation==Static.ORIENTATION_NORMAL){
                normalPress(field.moveDownRight(true), Static.DOWN_RIGHT)
            }else{
                normalPress(field.moveUpLeft(true), Static.UP_LEFT)
            }
        }
        rootView.fragment_multi_player_match_move_down_btn.setOnClickListener {
            if(invitation.orientation==Static.ORIENTATION_NORMAL){
                normalPress(field.moveDown(true), Static.DOWN)
            }else{
                normalPress(field.moveUp(true), Static.UP)
            }
        }
        rootView.fragment_multi_player_match_move_down_left_btn.setOnClickListener {
            if(invitation.orientation==Static.ORIENTATION_NORMAL){
                normalPress(field.moveDownLeft(true), Static.DOWN_LEFT)
            }else{
                normalPress(field.moveUpRight(true), Static.UP_RIGHT)
            }
        }
        rootView.fragment_multi_player_match_move_left_btn.setOnClickListener {
            if(invitation.orientation==Static.ORIENTATION_NORMAL){
                normalPress(field.moveLeft(true), Static.LEFT)
            }else{
                normalPress(field.moveRight(true), Static.RIGHT)
            }
        }
        rootView.fragment_multi_player_match_move_up_left_btn.setOnClickListener {
            if(invitation.orientation==Static.ORIENTATION_NORMAL){
                normalPress(field.moveUpLeft(true), Static.UP_LEFT)
            }else{
                normalPress(field.moveDownRight(true), Static.DOWN_RIGHT)
            }
        }


        rootView.fragment_multi_player_match_back_button.setOnClickListener {
            goToMainMenu()
        }

    }

    private fun disableButtons() {
        rootView.fragment_multi_player_match_move_up_btn.visibility=View.GONE
        rootView.fragment_multi_player_match_move_up_right_btn.visibility=View.GONE
        rootView.fragment_multi_player_match_move_right_btn.visibility=View.GONE
        rootView.fragment_multi_player_match_move_down_right_btn.visibility=View.GONE
        rootView.fragment_multi_player_match_move_down_btn.visibility=View.GONE
        rootView.fragment_multi_player_match_move_down_left_btn.visibility=View.GONE
        rootView.fragment_multi_player_match_move_left_btn.visibility=View.GONE
        rootView.fragment_multi_player_match_move_up_left_btn.visibility=View.GONE
    }

    private fun makeIcons() {
        launch {
            requireContext().let {
                val user = User().userFromDB(UserDBDatabase(it).getUserDBDao().getUser(loggedInStatus.userid))
                rootView.fragment_multi_player_match_user_icon.setImageDrawable(UserIconDrawable(requireContext(),
                    (3*screenSize.screenUnit).toDouble(),user.icon
                ))
            }
        }

        val opponentRef = Firebase.database.getReference("Invitations").child(loggedInStatus.userid)
        opponentRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    invitation = snapshot.getValue(Invitation::class.java)!!
                    invitationReady = true
                    val opponent = invitation.opponent
                    val opponentIconRef = Firebase.database.getReference("Ranking").child(opponent)
                    opponentIconRef.addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(snapshot1: DataSnapshot) {
                            if (snapshot1.exists()){
                                val userOpponent = snapshot1.getValue(UserRanking::class.java)
                                rootView.fragment_multi_player_match_opponent_icon.setImageDrawable(UserIconDrawable(requireContext(), (3*screenSize.screenUnit).toDouble(),userOpponent!!.icon))
                            }
                        }
                        override fun onCancelled(error1: DatabaseError) {
                        }
                    })
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


    /**------------------------ PREPARE GAME -----------------------**/

    private fun createField() {
        field.generate(Static.MULTI)
    }

    private fun prepareMatch(): Runnable = Runnable {
        if(invitationReady){
            prepareMatchHandler.removeCallbacksAndMessages(null)
            val matchRef = Firebase.database.getReference("Match").child(invitation.battleName)
            matchRef.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        multiPlayerMatch = snapshot.getValue(MultiPlayerMatch::class.java)!!
                        moveList.clear()
                        tmpMoveList.clear()
                        if(multiPlayerMatch.moveList.size>0){
                            for(i in multiPlayerMatch.moveList.indices){
                                moveList.add(multiPlayerMatch.moveList[i])
                            }
                            if(moveList.size>0){
                                for(i in moveList.indices){

                                    field.makeMoves(moveList[i],invitation.orientation)
                                }
                            }
                            displayField()
                        }
                        val gameCounterAdded = checkIfPointAlreadyAdded()
                        if(!gameCounterAdded){
                            updateUserData()
                            addGameCounter()

                        }
                        matchRef.setValue(multiPlayerMatch)
                        when(multiPlayerMatch.endGame){
                            Static.TIE -> tieAnimationWithDeletingFirebase()
                            invitation.orientation -> winAnimationWithDeletingFirebase()
                            setOpponentTurn() -> lostAnimationWithDeletingFirebase()
                        }
                        prepareMatchHandler.removeCallbacksAndMessages(null)
                        play().run()
                    }else{
                        multiPlayerMatch.turn = Static.ORIENTATION_NORMAL
                        val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
                        multiPlayerMatch.time = calendar.timeInMillis
                        updateUserData()
                        addGameCounter()
                        matchRef.setValue(multiPlayerMatch)
                        prepareMatchHandler.removeCallbacksAndMessages(null)
                        play().run()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
        else{
            prepareMatchHandler.postDelayed(prepareMatch(),500)
        }
    }


    private fun addOpponent(history: MultiPlayerHistory,historyRef:DatabaseReference) {
        history.updateNoOfGames(invitation.opponent)

        historyRef.setValue(history)

    }

    private fun addGameCounter() {
        addGameCounterInDatabase()
        addOpponent(history,historyRef!!)
    }


    private fun addGameCounterInDatabase() {
        launch {
            requireContext().let {
                val user = UserDBDatabase(it).getUserDBDao().getUser(loggedInStatus.userid)
                user.multiGameNumberOfGame+=1
                UserDBDatabase(it).getUserDBDao().updateUserInDB(user)
            }
        }
    }

    private fun checkIfPointAlreadyAdded():Boolean {
        return if(invitation.orientation==Static.NORMAL){
            multiPlayerMatch.playerOne.matchCounterAdded
        }else{
            multiPlayerMatch.playerTwo.matchCounterAdded
        }

    }

    private fun updateUserData() {
        if(invitation.orientation==Static.NORMAL){
            multiPlayerMatch.playerOne.matchCounterAdded=true
            multiPlayerMatch.playerOne.gameReady=true
        }else{
            multiPlayerMatch.playerTwo.matchCounterAdded=true
            multiPlayerMatch.playerTwo.gameReady=true
        }

    }


    /**-------------------------- PLAY ----------------------------**/

    private fun play(): Runnable = Runnable {
        playMatchHandler.removeCallbacksAndMessages(null)
        val dbRef = Firebase.database.getReference("Match").child(invitation.battleName)
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    multiPlayerMatch = snapshot.getValue(MultiPlayerMatch::class.java)!!
                    if(multiPlayerMatch.playerTwo.gameReady&&multiPlayerMatch.playerOne.gameReady){
                    if(multiPlayerMatch.turn==invitation.orientation){
                        // my move
                        readDatabaseAndEnableButtons()
                    }else{
                        readDatabase()
                    }
                    }
                    else{
                     playMatchHandler.postDelayed(play(),1000)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun readDatabaseAndEnableButtons() {
        tmpMoveList.clear()
        if(multiPlayerMatch.moveList.size>moveList.size){
            for(i in moveList.size until multiPlayerMatch.moveList.size){
                tmpMoveList.add(multiPlayerMatch.moveList[i])
            }
            counter=0
            displayMovesAndEnableButtons().run()
        }else{
            enableButtons()
        }
    }

    private fun readDatabase() {
        tmpMoveList.clear()
        if(multiPlayerMatch.moveList.size>moveList.size){
            for(i in moveList.size until multiPlayerMatch.moveList.size){
                tmpMoveList.add(multiPlayerMatch.moveList[i])
            }
            counter=0
            displayMoves().run()
        }else{
            playMatchHandler.postDelayed(play(),1000)
        }
    }



    /** ----------------------- WIN ----------------------------**/

    private fun winAnimation() {
        multiPlayerMatch.turn = setOpponentTurn()
        multiPlayerMatch.endGame = invitation.orientation
        val dbRef = Firebase.database.getReference("Match").child(invitation.battleName)
        dbRef.setValue(multiPlayerMatch)
        clearInvitation()
        displayWinAnimationAndAddPoints()
    }

    private fun winAnimationWithDeletingFirebase() {
        val dbRef = Firebase.database.getReference("Match").child(invitation.battleName)
        dbRef.removeValue()
        clearInvitation()
        displayWinAnimationAndAddPoints()
    }


    private fun displayWinAnimationAndAddPoints() {
        playMatchHandler.removeCallbacksAndMessages(null)
        launch {
            requireContext().let {
                val user = UserDBDatabase(it).getUserDBDao().getUser(loggedInStatus.userid)
                user.multiGameWin+=1
                UserDBDatabase(it).getUserDBDao().updateUserInDB(user)
            }
        }
        history.addWin(invitation.opponent)
        historyRef!!.setValue(history)
        endGameWinRunnable().run()
    }

    private fun endGameWinRunnable()= Runnable {
        when(endGameLoopCounter){
            0 -> prepareUserNameWin()
            1 -> displayWinAlertDialog()
            2 -> doNothingWin()
            3 -> removeDialogWin()
            4 -> goToMainMenu()
        }
    }

    private fun prepareUserNameWin() {
        launch {
            requireContext().let {
                val user = UserDBDatabase(it).getUserDBDao().getUser(loggedInStatus.userid)
                userName = user.name
                endGameLoopCounter+=1
                endGameWinHandler.postDelayed(endGameWinRunnable(),1000)
            }
        }
    }

    private fun displayWinAlertDialog() {
        val mBuilder = AlertDialog.Builder(requireContext())
        val mView = layoutInflater.inflate(R.layout.alert_dialog_end_game,null)
        mBuilder.setView(mView)
        dialog = mBuilder.create()
        val flags = View.SYSTEM_UI_FLAG_IMMERSIVE or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        dialog.window!!.decorView.systemUiVisibility = flags
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        mView.background = TileDrawable((ContextCompat.getDrawable(requireContext(), R.drawable.background_green)!!),
            Shader.TileMode.REPEAT,screenSize.screenUnit)

        mView.alert_dialog_end_game_title.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,3*screenSize.screenUnit)
        mView.alert_dialog_end_game_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,(1.5*screenSize.screenUnit).toFloat())
        mView.alert_dialog_end_game_title.text = "CONGRATULATION"
        mView.alert_dialog_end_game_title.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_green_dark))

        mView.alert_dialog_end_game_message.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,3*screenSize.screenUnit)
        mView.alert_dialog_end_game_message.setTextSize(TypedValue.COMPLEX_UNIT_PX,(1.5*screenSize.screenUnit).toFloat())
        mView.alert_dialog_end_game_message.text = "$userName WINS"
        mView.alert_dialog_end_game_message.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_green_dark))


        val set = ConstraintSet()
        set.clone(mView.alert_dialog_end_game)

        set.connect(mView.alert_dialog_end_game_title.id,ConstraintSet.TOP,mView.alert_dialog_end_game.id,ConstraintSet.TOP,0)
        set.connect(mView.alert_dialog_end_game_title.id,ConstraintSet.LEFT,mView.alert_dialog_end_game.id,ConstraintSet.LEFT,0)

        set.connect(mView.alert_dialog_end_game_message.id,ConstraintSet.TOP,mView.alert_dialog_end_game_title.id,ConstraintSet.BOTTOM,0)
        set.connect(mView.alert_dialog_end_game_message.id,ConstraintSet.LEFT,mView.alert_dialog_end_game.id,ConstraintSet.LEFT,0)

        set.connect(mView.alert_dialog_end_game_dummy_tv.id,ConstraintSet.TOP,mView.alert_dialog_end_game_message.id,ConstraintSet.BOTTOM,0)
        set.connect(mView.alert_dialog_end_game_dummy_tv.id,ConstraintSet.LEFT,mView.alert_dialog_end_game.id,ConstraintSet.LEFT,0)

        set.applyTo(mView.alert_dialog_end_game)

        dialog.show()

        endGameLoopCounter +=1
        endGameWinHandler.postDelayed(endGameWinRunnable(),1000)
    }

    private fun doNothingWin() {
        endGameLoopCounter+=1
        endGameWinHandler.postDelayed(endGameWinRunnable(),1000)
    }

    private fun removeDialogWin() {
        dialog.dismiss()
        endGameLoopCounter+=1
        endGameWinHandler.postDelayed(endGameWinRunnable(),100)
    }


    /**------------------------- LOSE ---------------------------**/

    private fun lostAnimation() {
        multiPlayerMatch.turn = setOpponentTurn()
        multiPlayerMatch.endGame = setOpponentTurn()
        val dbRef = Firebase.database.getReference("Match").child(invitation.battleName)
        dbRef.setValue(multiPlayerMatch)

        clearInvitation()
        displayLoseAnimationAndAddPoints()
    }

    private fun lostAnimationWithDeletingFirebase() {
        val dbRef = Firebase.database.getReference("Match").child(invitation.battleName)
        dbRef.removeValue()
        clearInvitation()
        displayLoseAnimationAndAddPoints()
    }

    private fun displayLoseAnimationAndAddPoints() {
        playMatchHandler.removeCallbacksAndMessages(null)
        launch {
            requireContext().let {
                val user = UserDBDatabase(it).getUserDBDao().getUser(loggedInStatus.userid)
                user.multiGameLose+=1
                UserDBDatabase(it).getUserDBDao().updateUserInDB(user)
            }
        }
        history.addLose(invitation.opponent)
        historyRef!!.setValue(history)
        endGameLoseRunnable().run()
    }

    private fun endGameLoseRunnable() = Runnable {
        when(endGameLoopCounter){
            0 -> prepareUserNameLose()
            1 -> displayLoseAlertDialog()
            2 -> doNothingLose()
            3 -> removeDialogLose()
            4 -> goToMainMenu()
        }
    }

    private fun prepareUserNameLose() {
        val dbRefOpponent = Firebase.database.getReference("Ranking").child(invitation.opponent)
        dbRefOpponent.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val opponent = snapshot.getValue(UserRanking::class.java)
                    userName = opponent!!.userName
                    endGameLoopCounter += 1
                    endGameLoseHandler.postDelayed(endGameLoseRunnable(),1000)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun displayLoseAlertDialog() {
        val mBuilder = AlertDialog.Builder(requireContext())
        val mView = layoutInflater.inflate(R.layout.alert_dialog_end_game,null)
        mBuilder.setView(mView)
        dialog = mBuilder.create()
        val flags = View.SYSTEM_UI_FLAG_IMMERSIVE or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        dialog.window!!.decorView.systemUiVisibility = flags
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        mView.background = TileDrawable((ContextCompat.getDrawable(requireContext(), R.drawable.background_red)!!),
            Shader.TileMode.REPEAT,screenSize.screenUnit)

        mView.alert_dialog_end_game_title.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,3*screenSize.screenUnit)
        mView.alert_dialog_end_game_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,(1.5*screenSize.screenUnit).toFloat())
        mView.alert_dialog_end_game_title.text = "SORRY"
        mView.alert_dialog_end_game_title.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_red_dark))

        mView.alert_dialog_end_game_message.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,3*screenSize.screenUnit)
        mView.alert_dialog_end_game_message.setTextSize(TypedValue.COMPLEX_UNIT_PX,(1.5*screenSize.screenUnit).toFloat())
        mView.alert_dialog_end_game_message.text = "$userName WINS"
        mView.alert_dialog_end_game_message.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_red_dark))


        val set = ConstraintSet()
        set.clone(mView.alert_dialog_end_game)

        set.connect(mView.alert_dialog_end_game_title.id,ConstraintSet.TOP,mView.alert_dialog_end_game.id,ConstraintSet.TOP,0)
        set.connect(mView.alert_dialog_end_game_title.id,ConstraintSet.LEFT,mView.alert_dialog_end_game.id,ConstraintSet.LEFT,0)

        set.connect(mView.alert_dialog_end_game_message.id,ConstraintSet.TOP,mView.alert_dialog_end_game_title.id,ConstraintSet.BOTTOM,0)
        set.connect(mView.alert_dialog_end_game_message.id,ConstraintSet.LEFT,mView.alert_dialog_end_game.id,ConstraintSet.LEFT,0)

        set.connect(mView.alert_dialog_end_game_dummy_tv.id,ConstraintSet.TOP,mView.alert_dialog_end_game_message.id,ConstraintSet.BOTTOM,0)
        set.connect(mView.alert_dialog_end_game_dummy_tv.id,ConstraintSet.LEFT,mView.alert_dialog_end_game.id,ConstraintSet.LEFT,0)

        set.applyTo(mView.alert_dialog_end_game)

        dialog.show()

        endGameLoopCounter +=1
        endGameLoseHandler.postDelayed(endGameLoseRunnable(),1000)

    }

    private fun doNothingLose() {
        endGameLoopCounter+=1
        endGameLoseHandler.postDelayed(endGameLoseRunnable(),1000)
    }

    private fun removeDialogLose() {
        dialog.dismiss()
        endGameLoopCounter+=1
        endGameLoseHandler.postDelayed(endGameLoseRunnable(),100)
    }



    /**------------------------ GAME LOGIC ------------------------**/

    private fun displayMovesAndEnableButtons(): Runnable = Runnable {
        if(counter>=tmpMoveList.size){
                when(multiPlayerMatch.endGame){
                    Static.TIE -> tieAnimationWithDeletingFirebase()
                    invitation.orientation -> winAnimationWithDeletingFirebase()
                    setOpponentTurn() -> lostAnimationWithDeletingFirebase()
                    else -> playMatchHandler.postDelayed(play(),100)
                }
        }
        else{
            moveList.add(tmpMoveList[counter])
            field.makeOpponentMoveInDirection(tmpMoveList[counter].direction)
            displayField()
            counter+=1
            val mHandler = Handler()
            mHandler.postDelayed(displayMovesAndEnableButtons(),1000)
        }
    }

    private fun tieAnimationWithDeletingFirebase() {
        val dbRef = Firebase.database.getReference("Match").child(invitation.battleName)
        dbRef.removeValue()
        clearInvitation()
        displayTieAnimationAndAddPoints()
    }

    private fun displayTieAnimationAndAddPoints() {
        playMatchHandler.removeCallbacksAndMessages(null)
        launch {
            requireContext().let {
                val user = UserDBDatabase(it).getUserDBDao().getUser(loggedInStatus.userid)
                user.multiGameTie+=1
                UserDBDatabase(it).getUserDBDao().updateUserInDB(user)
            }
        }
        history.addTie(invitation.opponent)
        historyRef!!.setValue(history)
        endGameTieRunnable().run()

    }

    private fun endGameTieRunnable()= Runnable {
        when(endGameLoopCounter){
            0 -> prepareUserNameTie()
            1 -> displayTieAlertDialog()
            2 -> doNothingTie()
            3 -> removeDialogTie()
            4 -> goToMainMenu()
        }
    }

    private fun removeDialogTie() {
        dialog.dismiss()
        endGameLoopCounter+=1
        endGameTieHandler.postDelayed(endGameTieRunnable(),100)
    }

    private fun doNothingTie() {
        endGameLoopCounter+=1
        endGameTieHandler.postDelayed(endGameTieRunnable(),1000)

    }

    private fun displayTieAlertDialog() {
        val mBuilder = AlertDialog.Builder(requireContext())
        val mView = layoutInflater.inflate(R.layout.alert_dialog_end_game,null)
        mBuilder.setView(mView)
        dialog = mBuilder.create()
        val flags = View.SYSTEM_UI_FLAG_IMMERSIVE or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        dialog.window!!.decorView.systemUiVisibility = flags
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        mView.background = TileDrawable((ContextCompat.getDrawable(requireContext(), R.drawable.background_yellow)!!),
            Shader.TileMode.REPEAT,screenSize.screenUnit)

        mView.alert_dialog_end_game_title.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,3*screenSize.screenUnit)
        mView.alert_dialog_end_game_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,(1.5*screenSize.screenUnit).toFloat())
        mView.alert_dialog_end_game_title.text = "UNFORTUNATELY"
        mView.alert_dialog_end_game_title.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_yellow_dark))

        mView.alert_dialog_end_game_message.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,3*screenSize.screenUnit)
        mView.alert_dialog_end_game_message.setTextSize(TypedValue.COMPLEX_UNIT_PX,(1.5*screenSize.screenUnit).toFloat())
        mView.alert_dialog_end_game_message.text = "$userName WINS"
        mView.alert_dialog_end_game_message.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_yellow_dark))


        val set = ConstraintSet()
        set.clone(mView.alert_dialog_end_game)

        set.connect(mView.alert_dialog_end_game_title.id,ConstraintSet.TOP,mView.alert_dialog_end_game.id,ConstraintSet.TOP,0)
        set.connect(mView.alert_dialog_end_game_title.id,ConstraintSet.LEFT,mView.alert_dialog_end_game.id,ConstraintSet.LEFT,0)

        set.connect(mView.alert_dialog_end_game_message.id,ConstraintSet.TOP,mView.alert_dialog_end_game_title.id,ConstraintSet.BOTTOM,0)
        set.connect(mView.alert_dialog_end_game_message.id,ConstraintSet.LEFT,mView.alert_dialog_end_game.id,ConstraintSet.LEFT,0)

        set.connect(mView.alert_dialog_end_game_dummy_tv.id,ConstraintSet.TOP,mView.alert_dialog_end_game_message.id,ConstraintSet.BOTTOM,0)
        set.connect(mView.alert_dialog_end_game_dummy_tv.id,ConstraintSet.LEFT,mView.alert_dialog_end_game.id,ConstraintSet.LEFT,0)

        set.applyTo(mView.alert_dialog_end_game)

        dialog.show()

        endGameLoopCounter +=1
        endGameTieHandler.postDelayed(endGameTieRunnable(),1000)


    }

    private fun prepareUserNameTie() {
        userName = "NOBODY"
        endGameLoopCounter += 1
        endGameTieHandler.postDelayed(endGameTieRunnable(),1000)

    }

    private fun displayMoves(): Runnable = Runnable {

        if(counter>=tmpMoveList.size){
            playMatchHandler.postDelayed(play(),1000)
        }
        else{
            moveList.add(tmpMoveList[counter])
            field.makeOpponentMoveInDirection(tmpMoveList[counter].direction)
            displayField()
            counter+=1
            val mHandler = Handler()
            mHandler.postDelayed(displayMoves(),1000)
        }
    }

    private fun enableButtons() {
        disableButtons()
        val ball = field.findBall()
        val availableMoves = field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y])

        if(invitation.orientation==Static.ORIENTATION_NORMAL) {

            if (availableMoves.up) {
                rootView.fragment_multi_player_match_move_up_btn.visibility = View.VISIBLE
            }
            if (availableMoves.upRight) {
                rootView.fragment_multi_player_match_move_up_right_btn.visibility = View.VISIBLE
            }
            if (availableMoves.right) {
                rootView.fragment_multi_player_match_move_right_btn.visibility = View.VISIBLE
            }
            if (availableMoves.downRight) {
                rootView.fragment_multi_player_match_move_down_right_btn.visibility = View.VISIBLE
            }
            if (availableMoves.down) {
                rootView.fragment_multi_player_match_move_down_btn.visibility = View.VISIBLE
            }
            if (availableMoves.downLeft) {
                rootView.fragment_multi_player_match_move_down_left_btn.visibility = View.VISIBLE
            }
            if (availableMoves.left) {
                rootView.fragment_multi_player_match_move_left_btn.visibility = View.VISIBLE
            }
            if (availableMoves.upLeft) {
                rootView.fragment_multi_player_match_move_up_left_btn.visibility = View.VISIBLE
            }
        }
        else{

            if (availableMoves.up) {
                rootView.fragment_multi_player_match_move_down_btn.visibility = View.VISIBLE
            }
            if (availableMoves.upRight) {
                rootView.fragment_multi_player_match_move_down_left_btn.visibility = View.VISIBLE
            }
            if (availableMoves.right) {
                rootView.fragment_multi_player_match_move_left_btn.visibility = View.VISIBLE
            }
            if (availableMoves.downRight) {
                rootView.fragment_multi_player_match_move_up_left_btn.visibility = View.VISIBLE
            }
            if (availableMoves.down) {
                rootView.fragment_multi_player_match_move_up_btn.visibility = View.VISIBLE
            }
            if (availableMoves.downLeft) {
                rootView.fragment_multi_player_match_move_up_right_btn.visibility = View.VISIBLE
            }
            if (availableMoves.left) {
                rootView.fragment_multi_player_match_move_right_btn.visibility = View.VISIBLE
            }
            if (availableMoves.upLeft) {
                rootView.fragment_multi_player_match_move_down_right_btn.visibility = View.VISIBLE
            }

        }

    }

    private fun normalPress(move:PointsAfterMove, direction:Int){
        val points = move
        displayField()

        val multiPlayerMove = MultiPlayerMove()
        multiPlayerMove.user = invitation.orientation
        multiPlayerMove.direction = direction
        multiPlayerMove.ball = Point(points.beforeMovePoint.x,points.beforeMovePoint.y)

        multiPlayerMatch.moveList.add(multiPlayerMove)
        moveList.add(multiPlayerMove)
        val multiRef = Firebase.database.getReference("Match").child(invitation.battleName)
        multiRef.setValue(multiPlayerMatch)

        disableButtons()
         val endGame = checkWin()
        if (endGame){
            playMatchHandler.removeCallbacksAndMessages(null)
        }else{

            checkNextMove(direction)





        }
    }

    private fun checkingBothScores(nextMove:StuckAndNextMove) {
        var myScoreAvailable = false
        var phoneScoreAvailable = false
        var tie = false

        val list = ArrayList<BothScorePoint>()
        val ball = field.findBall()
        val firstPoint = BothScorePoint(ball,field)

        list.add(firstPoint)

        val tmpList = ArrayList<BothScorePoint>()
        var loopChecker = true

        while(loopChecker){
            for(x in list){
                if(!x.isChecked()){
                    x.setCheck()
                    val myScore = x.isMyScore()
                    val phoneScore = x.isPhoneScore()

                    if(myScore){
                        myScoreAvailable=true
                    }
                    if(phoneScore){
                        phoneScoreAvailable=true
                    }
                    if(myScore or (phoneScore)){
                        //don't check availablemoves
                    }else {
                        if (x.availableMoves.down) {
                            val newBall = field.testMoveDown(field.field, x.currentBall)
                            val newPoint = BothScorePoint(newBall, field)
                            tmpList.add(newPoint)
                        }
                        if (x.availableMoves.downLeft) {
                            val newBall = field.testMoveDownLeft(field.field, x.currentBall)
                            val newPoint = BothScorePoint(newBall, field)
                            tmpList.add(newPoint)
                        }
                        if (x.availableMoves.downRight) {
                            val newBall = field.testMoveDownRight(field.field, x.currentBall)
                            val newPoint = BothScorePoint(newBall, field)
                            tmpList.add(newPoint)
                        }
                        if (x.availableMoves.left) {
                            val newBall = field.testMoveLeft(field.field, x.currentBall)
                            val newPoint = BothScorePoint(newBall, field)
                            tmpList.add(newPoint)
                        }
                        if (x.availableMoves.right) {
                            val newBall = field.testMoveRight(field.field, x.currentBall)
                            val newPoint = BothScorePoint(newBall, field)
                            tmpList.add(newPoint)
                        }
                        if (x.availableMoves.upLeft) {
                            val newBall = field.testMoveUpLeft(field.field, x.currentBall)
                            val newPoint = BothScorePoint(newBall, field)
                            tmpList.add(newPoint)
                        }
                        if (x.availableMoves.upRight) {
                            val newBall = field.testMoveUpRight(field.field, x.currentBall)
                            val newPoint = BothScorePoint(newBall, field)
                            tmpList.add(newPoint)
                        }
                        if (x.availableMoves.up) {
                            val newBall = field.testMoveUp(field.field, x.currentBall)
                            val newPoint = BothScorePoint(newBall, field)
                            tmpList.add(newPoint)
                        }
                    }
                }
            }

            //      list.clear()

            if(myScoreAvailable or (phoneScoreAvailable)){
                loopChecker=false
            }
            else {

                if (tmpList.size == 0) {
                    loopChecker = false
                    tie=true
                } else {
                    for (x in tmpList) {
                        var shouldBeAdded = true

                        for(y in list){
                            if(y.currentBall.x==x.currentBall.x && y.currentBall.y == x.currentBall.y){
                                shouldBeAdded = false
                            }
                        }

                        if(shouldBeAdded) {
                            list.add(x)
                        }
                    }
                    tmpList.clear()
                }
            }
        }

        field.clearTestMoves()

        if(tie){
            tieAnimation()
        }
        else{
            if(nextMove.nextMove){
                enableButtons()
            }else{
                multiPlayerMatch.turn = setOpponentTurn()
                val multiRef = Firebase.database.getReference("Match").child(invitation.battleName)
                multiRef.setValue(multiPlayerMatch)

                checkFirebase().run()
            }
        }

    }

    private fun tieAnimation() {
        multiPlayerMatch.turn = setOpponentTurn()
        multiPlayerMatch.endGame = Static.TIE
        val dbRef = Firebase.database.getReference("Match").child(invitation.battleName)
        dbRef.setValue(multiPlayerMatch)
        clearInvitation()
        displayTieAnimationAndAddPoints()

    }

    private fun checkNextMove(direction: Int) {
        val nextMove = field.checkIfStuckAndNextMove(direction)
        if(nextMove.stuck){
            playMatchHandler.removeCallbacksAndMessages(null)
            lostAnimation()
        }else {

            checkingBothScores(nextMove)

        }
    }

    private fun checkFirebase():Runnable= Runnable {
        val dbRef = Firebase.database.getReference("Match").child(invitation.battleName)
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                val saved = snapshot.getValue(MultiPlayerMatch::class.java)
                    if(saved!!.turn!=invitation.orientation){
                        playMatchHandler.postDelayed(play(),1000)
                    }else{
                        val mHandler = Handler()
                        mHandler.postDelayed(checkFirebase(),1000)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun checkWin():Boolean{

        val ball = field.findBall()
        val lost1 = Point(5,21)
        val lost2 = Point(6,21)
        val lost3 = Point(7,21)
        val lost4 = Point(8,21)
        val lost5 = Point(9,21)
        val win1 = Point(5,1)
        val win2 = Point(6,1)
        val win3 = Point(7,1)
        val win4 = Point(8,1)
        val win5 = Point(9,1)
        when(ball){
            lost1,lost2,lost3,lost4,lost5 -> {
                if(invitation.orientation==Static.ORIENTATION_NORMAL){
                lostAnimation()
                }else{
                    winAnimation()
                }
                return true
            }
            win1, win2, win3, win4, win5 -> {
                if(invitation.orientation==Static.ORIENTATION_NORMAL) {
                    winAnimation()
                }
                else{
                    lostAnimation()
                }
                return true
            }
        }
        return false
    }

    private fun setOpponentTurn():Int{
        return if (invitation.orientation==Static.ORIENTATION_NORMAL) Static.ORIENTATION_UP_SIDE_DOWN else Static.ORIENTATION_NORMAL
    }

    private fun displayField() {
        updateMoves()
    }

    private fun updateMoves() {
        view?.let {
            val ball = field.findBall()
            if (invitation.orientation == Static.ORIENTATION_NORMAL) {
                rootView.fragment_multi_player_match_game_field.setImageDrawable(MovesHardDrawable(requireContext(), field, screenSize.screenUnit.toDouble(), ball))
            } else {
                rootView.fragment_multi_player_match_game_field.setImageDrawable(MovesHardUpSideDownDrawable(requireContext(), field, screenSize.screenUnit.toDouble(), ball))
            }
        }
    }

    private fun goToMainMenu() {
        activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MainFragment()).commit()
    }

    private fun clearInvitation(){
        val invitationDbRef = Firebase.database.getReference("Invitations").child(loggedInStatus.userid)
        val clearInvitation = Invitation()
        clearInvitation.player = loggedInStatus.userid
        invitationDbRef.setValue(clearInvitation)

    }

}