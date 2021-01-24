package com.tt.oldschoolsoccer.fragments.singlePlayer

import android.graphics.Point
import android.graphics.Shader
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tt.oldschoolsoccer.R
import com.tt.oldschoolsoccer.classes.*
import com.tt.oldschoolsoccer.database.PointOnFieldNormalDatabase
import com.tt.oldschoolsoccer.database.UserDBDatabase
import com.tt.oldschoolsoccer.drawable.BallDrawable
import com.tt.oldschoolsoccer.drawable.FieldEasyDrawable
import com.tt.oldschoolsoccer.drawable.MovesEasyDrawable
import com.tt.oldschoolsoccer.drawable.TileDrawable
import kotlinx.android.synthetic.main.fragment_single_player_normal_game.view.*
import kotlinx.coroutines.launch
import kotlin.math.abs


class SinglePlayerNormalGameFragment : FragmentCoroutine() {

    private var screenUnit:Int = 0
    private var loggedInStatus = LoggedInStatus()
    private lateinit var rootView:View
    private var field = GameField()
    private var fieldReady = false
    private val startGameHandler = Handler()
    private val gameLoopHandler = Handler()
    private var nextMovePhone:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * read screen unit, read logged in status and create field
         */

        screenUnit = Functions.readScreenUnit(requireContext())
        loggedInStatus = Functions.readLoggedStateFromSharedPreferences(requireContext())

        createField()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /**
         * create UI
         */
        rootView = inflater.inflate(R.layout.fragment_single_player_normal_game, container, false)

        makeUI()

        return rootView
    }

    override fun onResume() {
        super.onResume()
        /**
         * start game
         */
        startGame().run()
    }

    override fun onPause() {
        super.onPause()
        /**
         * stop every runnable just in case
         */
        gameLoopHandler.removeCallbacksAndMessages(null)
        startGameHandler.removeCallbacksAndMessages(null)
    }



    /**
     * check if field is ready?
     * not - wait 1 second check again
     * yes - disable this runnable, update UI and start game loop
     */

    private fun startGame(): Runnable = Runnable {
        if(fieldReady){
            startGameHandler.removeCallbacksAndMessages(null)
            updateField()
            updateButtons()
            gameLoop().run()
        }else{
            startGameHandler.postDelayed(startGame(),1000)
        }

    }


    /**
     * loop for my move and phone move
     */
    private fun gameLoop(): Runnable = Runnable {
        if(field.myMove){
            gameLoopHandler.removeCallbacksAndMessages(null)
            field.clearTestMoves()
            updateButtons()
        }
        else{
            gameLoopHandler.removeCallbacksAndMessages(null)

            launch {
                phoneTurn()
            }





        }

    }

    private fun phoneTurn() {
        val moveList =  field.checkBestMove()

        var tmpBall = field.findBall()

        for(x in moveList){
            when(x){
                Static.UP -> {
                    val newBall = GameField().bestMoveUp(field.field,tmpBall)
                    tmpBall = newBall
                }
                Static.UP_RIGHT -> {
                    val newBall = GameField().bestMoveUpRight(field.field,tmpBall)
                    tmpBall = newBall
                }
                Static.UP_LEFT -> {
                    val newBall = GameField().bestMoveUpLeft(field.field,tmpBall)
                    tmpBall = newBall
                }
                Static.LEFT -> {
                    val newBall = GameField().bestMoveLeft(field.field,tmpBall)
                    tmpBall = newBall
                }
                Static.RIGHT -> {
                    val newBall = GameField().bestMoveRight(field.field,tmpBall)
                    tmpBall = newBall
                }
                Static.DOWN_RIGHT -> {
                    val newBall = GameField().bestMoveDownRight(field.field,tmpBall)
                    tmpBall = newBall
                }
                Static.DOWN_LEFT -> {
                    val newBall = GameField().bestMoveDownLeft(field.field,tmpBall)
                    tmpBall = newBall
                }
                Static.DOWN -> {
                    val newBall = GameField().bestMoveDown(field.field,tmpBall)
                    tmpBall = newBall
                }
            }
        }



        updateMoves()






    }


    private fun updateButtons() {
        disableButtons()
        val ball = field.findBall()
        val availableMoves = field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y])

        if (availableMoves.up){
            rootView.fragment_single_player_normal_game_move_up_btn.visibility = View.VISIBLE
        }

        if(availableMoves.upRight){
            rootView.fragment_single_player_normal_game_move_up_right_btn.visibility=View.VISIBLE
        }

        if(availableMoves.right){
            rootView.fragment_single_player_normal_game_move_right_btn.visibility=View.VISIBLE
        }

        if(availableMoves.downRight){
            rootView.fragment_single_player_normal_game_move_down_right_btn.visibility=View.VISIBLE
        }

        if(availableMoves.down){
            rootView.fragment_single_player_normal_game_move_down_btn.visibility=View.VISIBLE
        }

        if(availableMoves.downLeft){
            rootView.fragment_single_player_normal_game_move_down_left_btn.visibility=View.VISIBLE
        }

        if(availableMoves.left){
            rootView.fragment_single_player_normal_game_move_left_btn.visibility=View.VISIBLE
        }

        if(availableMoves.upLeft){
            rootView.fragment_single_player_normal_game_move_up_left_btn.visibility=View.VISIBLE
        }


    }

    private fun updateField() {
        displayBall()
        updateMoves()
    }

    private fun updateMoves() {
        rootView.fragment_single_player_normal_game_field.setImageDrawable(MovesEasyDrawable(requireContext(),field, screenUnit.toDouble()))
    }

    private fun displayBall() {
        val ball = field.findBall()
        rootView.fragment_single_player_normal_game_ball.setImageDrawable(BallDrawable(requireContext(),field.field[ball.x][ball.y], screenUnit.toDouble()))
    }


    /**
     * create field and read data from database if user logged in and game saved
     * else
     * add one to number of games and create new field if user logged in and game not saved
     * else
     * just create new field if user not logged in
     */
    private fun createField() {
        field.generate(Static.NORMAL)
        if(loggedInStatus.loggedIn){
            val savedGame = Functions.readNormalGameFromSharedPreferences(requireContext(),loggedInStatus.userid)
            if(savedGame){
                launch {
                    requireContext().let {
                        val list = PointOnFieldNormalDatabase(it).getPointOnFieldDao().getAllPointsOnField()
                        for (item in list) {
                            val i = item.position % 9
                            val j = item.position / 9
                            field.field[i][j] = item
                        }
                    }
                    fieldReady = true
                }
            }
            else{
                Functions.saveNormalGameToSharedPreferences(requireContext(), true, loggedInStatus.userid)
                updateUserCounters(1,0,0,0)
                launch {
                    for (i in 0..8) {
                        for (j in 0..12) {
                            val item = field.getPoint(i, j)
                            requireContext().let {
                                PointOnFieldNormalDatabase(it).getPointOnFieldDao().updatePointOnField(item)
                            }
                        }
                    }
                    fieldReady = true
                }
            }
        }
        else{
            fieldReady = true
        }
    }

    /**
     * updating user statistics after:
     * - new game
     * - win
     * - lose
     * - tie
     */
    private fun updateUserCounters(numbersOfGames:Int,win:Int,tie:Int,lose:Int) {

        launch {
            requireContext().let {
                val user = UserDBDatabase(it).getUserDBDao().getUser(loggedInStatus.userid)
                user.normalGameNumberOfGame+=numbersOfGames
                user.normalGameWin+=win
                user.normalGameLose+=lose
                user.normalGameTie+=tie

                UserDBDatabase(it).getUserDBDao().updateUserInDB(user)

                val userFb = User().userFromDB(user)
                val dbRef = Firebase.database.getReference("User").child(userFb.id)
                dbRef.setValue(userFb)
            }
        }

    }


    private fun makeUI() {
        makeBackgroundGrid()
        setViewSizes()
        setConstraintLayout()
        setDrawable()
        setOnClickListeners()
        disableButtons()

    }

    private fun disableButtons() {
        rootView.fragment_single_player_normal_game_move_up_btn.visibility=View.GONE
        rootView.fragment_single_player_normal_game_move_up_right_btn.visibility=View.GONE
        rootView.fragment_single_player_normal_game_move_right_btn.visibility=View.GONE
        rootView.fragment_single_player_normal_game_move_down_right_btn.visibility=View.GONE
        rootView.fragment_single_player_normal_game_move_down_btn.visibility=View.GONE
        rootView.fragment_single_player_normal_game_move_down_left_btn.visibility=View.GONE
        rootView.fragment_single_player_normal_game_move_left_btn.visibility=View.GONE
        rootView.fragment_single_player_normal_game_move_up_left_btn.visibility=View.GONE
    }


    /**
     * function after press any of direction buttons (player move)
     */
    private fun afterPress(direction: Int, move: PointsAfterMove){
        val pointsAfterMove = move
        if(loggedInStatus.loggedIn){
            launch {
                requireContext().let {
                    PointOnFieldNormalDatabase(it).getPointOnFieldDao().updatePointOnField(pointsAfterMove.beforeMovePoint)
                    PointOnFieldNormalDatabase(it).getPointOnFieldDao().updatePointOnField(pointsAfterMove.afterMovePoint)
                }
            }
        }
        updateField()
        disableButtons()
        val endGame = checkWin()
        if (endGame) {
            if(loggedInStatus.loggedIn) {
                clearDatabaseAndSharedPreferences()
            }
            gameLoopHandler.removeCallbacksAndMessages(null)
        } else {
            checkNextMove(direction)
        }

    }


    /**
     * checking if next move is available
     */
    private fun checkNextMove(direction: Int) {
        val nextMove = field.checkIfStuckAndNextMove(direction)
        if(nextMove.stuck){
            gameLoopHandler.removeCallbacksAndMessages(this)
            tieAnimation()
        }
        if(nextMove.nextMove){
            updateButtons()
        }else{
            disableButtons()
            field.myMove=false
            gameLoopHandler.postDelayed(gameLoop(),1000)
        }

    }

    private fun tieAnimation() {
        if(loggedInStatus.loggedIn) {
            updateUserCounters(0,0,1,0)
            clearDatabaseAndSharedPreferences()
        }
        rootView.fragment_single_player_normal_game_win.text = "TIE"
        rootView.fragment_single_player_normal_game_win.setTextColor(ContextCompat.getColor(requireContext(),R.color.test))

    }

    private fun clearDatabaseAndSharedPreferences() {
        Functions.saveNormalGameToSharedPreferences(requireContext(),false,loggedInStatus.userid)

    }


    /**
     * checking for winning and losing
     */
    private fun checkWin(): Boolean {
        val ball = field.findBall()
        val lost1 = Point(3,12)
        val lost2 = Point(4,12)
        val lost3 = Point(5,12)
        val win1 = Point(3,0)
        val win2 = Point(4,0)
        val win3 = Point(5,0)
        when(ball){
            lost1,lost2,lost3 -> {

                lostAnimation()
                return true
            }
            win1, win2, win3 -> {

                winAnimation()
                return true
            }
        }
        return false

    }

    private fun winAnimation() {
        if(loggedInStatus.loggedIn) {
            updateUserCounters(0,1,0,0)
            clearDatabaseAndSharedPreferences()
        }
        rootView.fragment_single_player_normal_game_win.text = "WIN"
        rootView.fragment_single_player_normal_game_win.setTextColor(ContextCompat.getColor(requireContext(),R.color.win))

    }

    private fun lostAnimation() {
        if(loggedInStatus.loggedIn) {
            updateUserCounters(0,0,0,1)
            clearDatabaseAndSharedPreferences()
        }
        rootView.fragment_single_player_normal_game_win.text = "LOST"
        rootView.fragment_single_player_normal_game_win.setTextColor(ContextCompat.getColor(requireContext(),R.color.lost))

    }

    private fun setOnClickListeners(){
        rootView.fragment_single_player_normal_game_move_up_btn.setOnClickListener {
            afterPress(Static.UP,field.moveUp(true))
        }

        rootView.fragment_single_player_normal_game_move_up_right_btn.setOnClickListener {
            afterPress(Static.UP_RIGHT,field.moveUpRight(true))
        }

        rootView.fragment_single_player_normal_game_move_right_btn.setOnClickListener {
            afterPress(Static.RIGHT,field.moveRight(true))
        }

        rootView.fragment_single_player_normal_game_move_down_right_btn.setOnClickListener {
            afterPress(Static.DOWN_RIGHT,field.moveDownRight(true))
        }

        rootView.fragment_single_player_normal_game_move_down_btn.setOnClickListener {
            afterPress(Static.DOWN,field.moveDown(true))
        }

        rootView.fragment_single_player_normal_game_move_down_left_btn.setOnClickListener {
            afterPress(Static.DOWN_LEFT,field.moveDownLeft(true))
        }

        rootView.fragment_single_player_normal_game_move_left_btn.setOnClickListener {
            afterPress(Static.LEFT,field.moveLeft(true))
        }

        rootView.fragment_single_player_normal_game_move_up_left_btn.setOnClickListener {
            afterPress(Static.UP_LEFT,field.moveUpLeft(true))
        }
    }

    private fun setDrawable() {
        rootView.fragment_single_player_normal_game_field.background = FieldEasyDrawable(requireContext(), screenUnit.toDouble())
        rootView.fragment_single_player_normal_game_move_down_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.down)
        rootView.fragment_single_player_normal_game_move_down_right_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.down_right)
        rootView.fragment_single_player_normal_game_move_right_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.right)
        rootView.fragment_single_player_normal_game_move_up_right_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.up_right)
        rootView.fragment_single_player_normal_game_move_up_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.up)
        rootView.fragment_single_player_normal_game_move_up_left_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.up_left)
        rootView.fragment_single_player_normal_game_move_left_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.left)
        rootView.fragment_single_player_normal_game_move_down_left_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.down_left)

    }

    private fun setConstraintLayout() {
        val set = ConstraintSet()
        set.clone(rootView.fragment_single_player_normal_game_layout)

        set.connect(rootView.fragment_single_player_normal_game_field.id, ConstraintSet.TOP,rootView.fragment_single_player_normal_game_layout.id, ConstraintSet.TOP,screenUnit)
        set.connect(rootView.fragment_single_player_normal_game_field.id, ConstraintSet.LEFT,rootView.fragment_single_player_normal_game_layout.id, ConstraintSet.LEFT,screenUnit)

        set.connect(rootView.fragment_single_player_normal_game_ball.id, ConstraintSet.TOP,rootView.fragment_single_player_normal_game_layout.id, ConstraintSet.TOP,screenUnit)
        set.connect(rootView.fragment_single_player_normal_game_ball.id, ConstraintSet.LEFT,rootView.fragment_single_player_normal_game_layout.id, ConstraintSet.LEFT,screenUnit)

        set.connect(rootView.fragment_single_player_normal_game_middle.id, ConstraintSet.TOP,rootView.fragment_single_player_normal_game_layout.id, ConstraintSet.TOP,26*screenUnit)
        set.connect(rootView.fragment_single_player_normal_game_middle.id, ConstraintSet.LEFT,rootView.fragment_single_player_normal_game_layout.id, ConstraintSet.LEFT,9*screenUnit)

        set.connect(rootView.fragment_single_player_normal_game_move_up_btn.id, ConstraintSet.BOTTOM,rootView.fragment_single_player_normal_game_middle.id, ConstraintSet.TOP,4*screenUnit)
        set.connect(rootView.fragment_single_player_normal_game_move_up_btn.id, ConstraintSet.LEFT,rootView.fragment_single_player_normal_game_middle.id, ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_single_player_normal_game_move_right_btn.id, ConstraintSet.BOTTOM,rootView.fragment_single_player_normal_game_middle.id, ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_single_player_normal_game_move_right_btn.id, ConstraintSet.LEFT,rootView.fragment_single_player_normal_game_middle.id, ConstraintSet.RIGHT,4*screenUnit)

        set.connect(rootView.fragment_single_player_normal_game_move_down_btn.id, ConstraintSet.TOP,rootView.fragment_single_player_normal_game_middle.id, ConstraintSet.BOTTOM,4*screenUnit)
        set.connect(rootView.fragment_single_player_normal_game_move_down_btn.id, ConstraintSet.LEFT,rootView.fragment_single_player_normal_game_middle.id, ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_single_player_normal_game_move_left_btn.id, ConstraintSet.BOTTOM,rootView.fragment_single_player_normal_game_middle.id, ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_single_player_normal_game_move_left_btn.id, ConstraintSet.RIGHT,rootView.fragment_single_player_normal_game_middle.id, ConstraintSet.LEFT,4*screenUnit)

        set.connect(rootView.fragment_single_player_normal_game_move_up_right_btn.id, ConstraintSet.BOTTOM,rootView.fragment_single_player_normal_game_middle.id, ConstraintSet.TOP,3*screenUnit)
        set.connect(rootView.fragment_single_player_normal_game_move_up_right_btn.id, ConstraintSet.LEFT,rootView.fragment_single_player_normal_game_middle.id, ConstraintSet.RIGHT,3*screenUnit)

        set.connect(rootView.fragment_single_player_normal_game_move_down_right_btn.id, ConstraintSet.TOP,rootView.fragment_single_player_normal_game_middle.id, ConstraintSet.BOTTOM,3*screenUnit)
        set.connect(rootView.fragment_single_player_normal_game_move_down_right_btn.id, ConstraintSet.LEFT,rootView.fragment_single_player_normal_game_middle.id, ConstraintSet.RIGHT,3*screenUnit)

        set.connect(rootView.fragment_single_player_normal_game_move_down_left_btn.id, ConstraintSet.TOP,rootView.fragment_single_player_normal_game_middle.id, ConstraintSet.BOTTOM,3*screenUnit)
        set.connect(rootView.fragment_single_player_normal_game_move_down_left_btn.id, ConstraintSet.RIGHT,rootView.fragment_single_player_normal_game_middle.id, ConstraintSet.LEFT,3*screenUnit)

        set.connect(rootView.fragment_single_player_normal_game_move_up_left_btn.id, ConstraintSet.BOTTOM,rootView.fragment_single_player_normal_game_middle.id, ConstraintSet.TOP,3*screenUnit)
        set.connect(rootView.fragment_single_player_normal_game_move_up_left_btn.id, ConstraintSet.RIGHT,rootView.fragment_single_player_normal_game_middle.id, ConstraintSet.LEFT,3*screenUnit)

        set.applyTo(rootView.fragment_single_player_normal_game_layout)

    }

    private fun setViewSizes() {
        rootView.fragment_single_player_normal_game_field.layoutParams = ConstraintLayout.LayoutParams(10*screenUnit,14*screenUnit)
        rootView.fragment_single_player_normal_game_ball.layoutParams = ConstraintLayout.LayoutParams(10*screenUnit,14*screenUnit)
        rootView.fragment_single_player_normal_game_move_up_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_single_player_normal_game_move_up_right_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_single_player_normal_game_move_right_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_single_player_normal_game_move_down_right_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_single_player_normal_game_move_down_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_single_player_normal_game_move_down_left_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_single_player_normal_game_move_left_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_single_player_normal_game_move_up_left_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_single_player_normal_game_middle.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)

    }

    private fun makeBackgroundGrid() {
        rootView.fragment_single_player_normal_game_layout.background = TileDrawable((ContextCompat.getDrawable(requireContext(), R.drawable.background)!!),
            Shader.TileMode.REPEAT,screenUnit)
    }

}