package com.tt.oldschoolsoccer.fragments.multiPlayer

import android.graphics.Point
import android.graphics.Shader
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.tt.oldschoolsoccer.database.PointOnField
import com.tt.oldschoolsoccer.database.UserDBDatabase
import com.tt.oldschoolsoccer.drawable.*
import com.tt.oldschoolsoccer.fragments.MainFragment
import kotlinx.android.synthetic.main.fragment_multi_player_match.view.*
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class MultiPlayerMatchFragment : FragmentCoroutine() {

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
    private var gameCounter = 0
    private var prepareCounter=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenSize = Functions.readScreenSize(requireContext())
        loggedInStatus = Functions.readLoggedStateFromSharedPreferences(requireContext())

        createField()

    }

    private fun createField() {
        field.generate(Static.MULTI)

    }

    override fun onResume() {
        super.onResume()

        prepareMatch().run()


    }

    private fun play(): Runnable = Runnable {
        gameCounter+=1
        rootView.game_counter.text=gameCounter.toString()
        playMatchHandler.removeCallbacksAndMessages(null)
        val dbRef = Firebase.database.getReference("Match").child(invitation.battleName)
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    multiPlayerMatch = snapshot.getValue(MultiPlayerMatch::class.java)!!
                    if(multiPlayerMatch.turn==invitation.orientation){
                        // my move
                            readDatabaseAndEnableButtons()


                    }else{
                        readDatabase()


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

    private fun displayMovesAndEnableButtons(): Runnable = Runnable {

        if(counter>=tmpMoveList.size){
            playMatchHandler.postDelayed(play(),100)
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
        if(availableMoves.up){
            rootView.fragment_multi_player_match_move_up_btn.visibility=View.VISIBLE
        }
        if(availableMoves.upRight){
            rootView.fragment_multi_player_match_move_up_right_btn.visibility=View.VISIBLE
        }
        if(availableMoves.right){
            rootView.fragment_multi_player_match_move_right_btn.visibility=View.VISIBLE
        }
        if(availableMoves.downRight){
            rootView.fragment_multi_player_match_move_down_right_btn.visibility=View.VISIBLE
        }
        if(availableMoves.down){
            rootView.fragment_multi_player_match_move_down_btn.visibility=View.VISIBLE
        }
        if(availableMoves.downLeft){
            rootView.fragment_multi_player_match_move_down_left_btn.visibility=View.VISIBLE
        }
        if(availableMoves.left){
            rootView.fragment_multi_player_match_move_left_btn.visibility=View.VISIBLE
        }
        if(availableMoves.upLeft){
            rootView.fragment_multi_player_match_move_up_left_btn.visibility=View.VISIBLE
        }

    }

    private fun prepareMatch(): Runnable = Runnable {
        prepareCounter+=1
        rootView.prepare_counter.text=prepareCounter.toString()
        if(invitationReady){
            prepareMatchHandler.removeCallbacksAndMessages(null)
            val matchRef = Firebase.database.getReference("Match").child(invitation.battleName)
            matchRef.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        multiPlayerMatch = snapshot.getValue(MultiPlayerMatch::class.java)!!
                        moveList.clear()
                        tmpMoveList.clear()
                        //todo compare with field and display



                        prepareMatchHandler.removeCallbacksAndMessages(null)
                        play().run()



                    }else{
                        multiPlayerMatch.turn = 1
                        val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
                        multiPlayerMatch.time = calendar.timeInMillis
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



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_multi_player_match, container, false)

        makeUI()


        return rootView
    }

    private fun makeUI() {
        setBackgroundGrid()
        setViewSizes()
        setDrawable()
        setConstraintLayout()
        setOnClickListeners()
        disableButtons()
        makeIcons()

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

    private fun checkNextMove(direction: Int) {
        val nextMove = field.checkIfStuckAndNextMove(direction)
        if(nextMove.stuck){
            playMatchHandler.removeCallbacksAndMessages(null)
            lostAnimation()
        }
        if(nextMove.nextMove){
            enableButtons()
        }else{
            multiPlayerMatch.turn = setOpponentTurn()
            val multiRef = Firebase.database.getReference("Match").child(invitation.battleName)
            multiRef.setValue(multiPlayerMatch)

            checkFirebase().run()
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

                lostAnimation()
                return true
            }
            win1, win2, win3, win4, win5 -> {

                winAnimation()
                return true
            }
        }
        return false
    }

    private fun winAnimation() {
        // todo !!!
    }

    private fun lostAnimation() {
        //todo !!!
    }

    private fun setOpponentTurn():Int{
        return if (invitation.orientation==Static.ORIENTATION_NORMAL) Static.ORIENTATION_UP_SIDE_DOWN else Static.ORIENTATION_NORMAL
    }






    private fun setOnClickListeners() {
        //todo for orientation normal and up side down

        rootView.fragment_multi_player_match_move_up_btn.setOnClickListener {
            normalPress(field.moveUp(true),Static.UP)
        }
        rootView.fragment_multi_player_match_move_up_right_btn.setOnClickListener {
            normalPress(field.moveUpRight(true),Static.UP_RIGHT)

        }
        rootView.fragment_multi_player_match_move_right_btn.setOnClickListener {
            normalPress(field.moveRight(true),Static.RIGHT)

        }
        rootView.fragment_multi_player_match_move_down_right_btn.setOnClickListener {
            normalPress(field.moveDownRight(true),Static.DOWN_RIGHT)

        }
        rootView.fragment_multi_player_match_move_down_btn.setOnClickListener {
            normalPress(field.moveDown(true),Static.DOWN)

        }
        rootView.fragment_multi_player_match_move_down_left_btn.setOnClickListener {
            normalPress(field.moveDownLeft(true),Static.DOWN_LEFT)

        }
        rootView.fragment_multi_player_match_move_left_btn.setOnClickListener {
            normalPress(field.moveLeft(true),Static.LEFT)

        }
        rootView.fragment_multi_player_match_move_up_left_btn.setOnClickListener {
            normalPress(field.moveUpLeft(true),Static.UP_LEFT)

        }

        rootView.fragment_multi_player_match_back_button.setOnClickListener {
            goToMainMenu()
        }

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

        set.connect(rootView.prepare_counter.id,ConstraintSet.LEFT,rootView.fragment_multi_player_match_layout.id,ConstraintSet.LEFT,screenSize.screenUnit)
        set.connect(rootView.prepare_counter.id,ConstraintSet.TOP,rootView.fragment_multi_player_match_layout.id,ConstraintSet.TOP,screenSize.screenUnit)

        set.connect(rootView.game_counter.id,ConstraintSet.RIGHT,rootView.fragment_multi_player_match_layout.id,ConstraintSet.RIGHT,screenSize.screenUnit)
        set.connect(rootView.game_counter.id,ConstraintSet.TOP,rootView.fragment_multi_player_match_layout.id,ConstraintSet.TOP,screenSize.screenUnit)

        set.applyTo(rootView.fragment_multi_player_match_layout)

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

    private fun setBackgroundGrid() {
        rootView.fragment_multi_player_match_layout.background = TileDrawable((ContextCompat.getDrawable(requireContext(),R.drawable.background)!!),
                Shader.TileMode.REPEAT,screenSize.screenUnit)

    }

}