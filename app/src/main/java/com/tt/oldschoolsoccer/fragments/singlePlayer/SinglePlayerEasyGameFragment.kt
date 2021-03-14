package com.tt.oldschoolsoccer.fragments.singlePlayer

import android.app.AlertDialog
import android.app.Dialog
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
import com.tt.oldschoolsoccer.R
import com.tt.oldschoolsoccer.classes.*
import com.tt.oldschoolsoccer.database.PointOnFieldEasyDatabase
import com.tt.oldschoolsoccer.database.UserDB
import com.tt.oldschoolsoccer.database.UserDBDatabase
import com.tt.oldschoolsoccer.drawable.*
import com.tt.oldschoolsoccer.fragments.MainFragment
import kotlinx.android.synthetic.main.alert_dialog_end_game.view.*
import kotlinx.android.synthetic.main.alert_dialog_user_name.view.*
import kotlinx.android.synthetic.main.fragment_single_player_easy_game.view.*
import kotlinx.coroutines.launch
import kotlin.math.abs

class SinglePlayerEasyGameFragment : FragmentCoroutine() {

    private var screenUnit:Int=0
    private var field = GameField()
    private var loggedInStatus = LoggedInStatus()
    private var fieldReady = false
    private lateinit var rootView:View
    private val gameLoopHandler = Handler()
    private var nextMovePhone:Boolean=false
    private val startGameHandler = Handler()
    private val score = Point(4,12)
    private val endGameWinHandler = Handler()
    private val endGameLoseHandler = Handler()
    private val endGameTieHandler = Handler()
    private var endGameLoopCounter=0
    private lateinit var userName:String
    private lateinit var dialog:AlertDialog
    private var phoneIcon = UserIconColors()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * read screen unit, read logged in status and create field
         */

        screenUnit= Functions.readScreenUnit(requireContext())
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
        rootView = inflater.inflate(R.layout.fragment_single_player_easy_game, container, false)
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
        endGameWinHandler.removeCallbacksAndMessages(null)
        endGameTieHandler.removeCallbacksAndMessages(null)
        endGameLoseHandler.removeCallbacksAndMessages(null)
        if(loggedInStatus.loggedIn){
            Functions.saveMyMoveEasyToSharedPreferences(requireContext(),field.myMove,loggedInStatus.userid)
        }
    }

    /**
     * check if field is ready?
     * not - wait 1 second check again
     * yes - disable this runnable, update UI and start game loop
     */
    private fun startGame():Runnable = Runnable {
        if(fieldReady){
            startGameHandler.removeCallbacksAndMessages(null)
            updateField()
            if(field.myMove){
            updateButtons()
            }
            gameLoop().run()
        }else{
            startGameHandler.postDelayed(startGame(),1000)
        }
    }



    /**
     * create field and read data from database if user logged in and game saved
     * else
     * add one to number of games and create new field if user logged in and game not saved
     * else
     * just create new field if user not logged in
     */
    private fun createField() {
        field.generate(Static.EASY)
        // if logged in
        if(loggedInStatus.loggedIn) {

            //check if game is saved for user
            val savedGame = Functions.readEasyGameFromSharedPreferences(requireContext(), loggedInStatus.userid)
            // if is saved - read from database
            if (savedGame) {
                phoneIcon = Functions.readRandomIconFromSharedPreferences(requireContext(),"EASY")

                launch {
                    requireContext().let {
                        val list = PointOnFieldEasyDatabase(it).getPointOnFieldDao().getAllPointsOnField()
                        for (item in list) {
                            val i = item.position % 9
                            val j = item.position / 9
                            field.field[i][j] = item
                        }
                        field.myMove = Functions.readMyMoveEasyGameFromSharedPreferences(requireContext(),loggedInStatus.userid)
                    }
                    fieldReady = true
                }
            }
            //if is not saved update counter, shared preferences and database
            else {
                phoneIcon = UserIconColors().randomColors()
                Functions.saveRandomIconToSharedPreferences(requireContext(),"EASY",phoneIcon)

                Functions.saveEasyGameToSharedPreferences(requireContext(), true, loggedInStatus.userid)
                updateUserCounters(1,0,0,0)
                launch {
                    for (i in 0..8) {
                        for (j in 0..12) {
                            val item = field.getPoint(i, j)
                            requireContext().let {
                                PointOnFieldEasyDatabase(it).getPointOnFieldDao().updatePointOnField(item)
                            }
                        }
                    }
                    fieldReady = true
                }
            }
        }
        //if not logged in
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
    private fun updateUserCounters(numbersOfGames:Int,win:Int,tie:Int,lose:Int){
        launch {
            requireContext().let {
                val user = UserDBDatabase(it).getUserDBDao().getUser(loggedInStatus.userid)
                user.easyGameNumberOfGame+=numbersOfGames
                user.easyGameWin+=win
                user.easyGameLose+=lose
                user.easyGameTie+=tie

                UserDBDatabase(it).getUserDBDao().updateUserInDB(user)
            }
        }
    }



    private fun makeUI() {
        makeBackgroundGrid()
        setViewSizes()
        setDrawable()
        setConstraintLayout()
        setOnClickListeners()
        disableButtons()
        makeIcons()
    }

    private fun makeIcons() {
        if(loggedInStatus.loggedIn){

            launch {
                    requireContext().let {
                        val user = User().userFromDB(UserDBDatabase(it).getUserDBDao().getUser(loggedInStatus.userid))
                        rootView.fragment_single_player_easy_user_icon.setImageDrawable(UserIconDrawable(requireContext(), (3*screenUnit).toDouble(),user.icon))
                    }
                }
            rootView.fragment_single_player_easy_phone_icon.setImageDrawable(UserIconDrawable(requireContext(),(3*screenUnit).toDouble(),phoneIcon))
        }
        else{
            rootView.fragment_single_player_easy_phone_icon.setImageDrawable(UserIconDrawable(requireContext(),(3*screenUnit).toDouble(),UserIconColors().randomColors()))
            rootView.fragment_single_player_easy_user_icon.setImageDrawable(UserIconDrawable(requireContext(), (3*screenUnit).toDouble(),UserIconColors().notLoggedIn()))
        }
    }

    private fun setOnClickListeners() {

        rootView.fragment_single_player_easy_game_move_up_btn.setOnClickListener {
            afterPress(Static.UP,field.moveUp(true))
        }

        rootView.fragment_single_player_easy_game_move_up_right_btn.setOnClickListener {
            afterPress(Static.UP_RIGHT,field.moveUpRight(true))
        }

        rootView.fragment_single_player_easy_game_move_right_btn.setOnClickListener {
            afterPress(Static.RIGHT,field.moveRight(true))
        }

        rootView.fragment_single_player_easy_game_move_down_right_btn.setOnClickListener {
            afterPress(Static.DOWN_RIGHT,field.moveDownRight(true))
        }

        rootView.fragment_single_player_easy_game_move_down_btn.setOnClickListener {
            afterPress(Static.DOWN,field.moveDown(true))
        }

        rootView.fragment_single_player_easy_game_move_down_left_btn.setOnClickListener {
            afterPress(Static.DOWN_LEFT,field.moveDownLeft(true))
        }

        rootView.fragment_single_player_easy_game_move_left_btn.setOnClickListener {
            afterPress(Static.LEFT,field.moveLeft(true))
        }

        rootView.fragment_single_player_easy_game_move_up_left_btn.setOnClickListener {
            afterPress(Static.UP_LEFT,field.moveUpLeft(true))
        }

        rootView.fragment_single_player_easy_back_button.setOnClickListener {
            goToMainMenu()
        }

    }

    private fun goToMainMenu() {
        activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container,MainFragment()).commit()

    }

    /**
     * function after press any of direction buttons (player move)
     */
    private fun afterPress(direction: Int, move: PointsAfterMove) {
        val pointsAfterMove = move
        if(loggedInStatus.loggedIn){
            launch {
                requireContext().let {
                    PointOnFieldEasyDatabase(it).getPointOnFieldDao().updatePointOnField(pointsAfterMove.beforeMovePoint)
                    PointOnFieldEasyDatabase(it).getPointOnFieldDao().updatePointOnField(pointsAfterMove.afterMovePoint)
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

    /**
     * loop for my move and phone move
     */
    private fun gameLoop():Runnable = Runnable {
        if(field.myMove){
            gameLoopHandler.removeCallbacksAndMessages(null)
            updateButtons()
        }
        else{
            makeMovePhone()
            updateField()
            val endGame = checkWin()
            if(endGame){
                gameLoopHandler.removeCallbacksAndMessages(null)
            }
            else{
                if(!nextMovePhone){
                    field.myMove = true
                }
                gameLoopHandler.postDelayed(gameLoop(),1000)
            }

        }
    }

    /**
     * phone move (different moves according to where ball is at this time)
     */
    private fun makeMovePhone() {
        val ball = field.findBall()
        movePhone(ball)
    }

    private fun compareDistance(oldDistance:Point?, newDistance:Point):Boolean{
        var replace = false
        if(oldDistance==null){
            replace = true
        }else{
            if(newDistance.y<oldDistance.y){
                replace = true
            }
            else if ((newDistance.y==oldDistance.y)&&(newDistance.x<oldDistance.x)){
                replace = true
            }
        }
        return replace
    }

    /**
     * move next move phone (shortest distance to score)
     */
    private fun movePhone(ball: Point){
        val availableMoves = field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y])

        val nextMove = NextMove()

        if(availableMoves.down){
            val newBall = Point(ball.x,ball.y+1)
            val distanceToScorePoint = Point(abs(score.x-newBall.x),(score.y-newBall.y))
                nextMove.setNextMoveWithDistancePoint(Static.DOWN,distanceToScorePoint)

        }

        if(availableMoves.downRight){
            val newBall = Point(ball.x+1,ball.y+1)
            val distanceToScorePoint = Point(abs(score.x-newBall.x),(score.y-newBall.y))
            val oldDistanceToScorePoint = nextMove.getDistancePoint()
            val replace = compareDistance(oldDistanceToScorePoint,distanceToScorePoint)
            if(replace){
                nextMove.setNextMoveWithDistancePoint(Static.DOWN_RIGHT,distanceToScorePoint)
            }
        }

        if(availableMoves.downLeft){
            val newBall = Point(ball.x-1,ball.y+1)
            val distanceToScorePoint = Point(abs(score.x-newBall.x),(score.y-newBall.y))
            val oldDistanceToScorePoint = nextMove.getDistancePoint()
            val replace = compareDistance(oldDistanceToScorePoint,distanceToScorePoint)
            if(replace){
                nextMove.setNextMoveWithDistancePoint(Static.DOWN_LEFT,distanceToScorePoint)
            }
        }

        if(availableMoves.right){
            val newBall = Point(ball.x+1,ball.y)
            val distanceToScorePoint = Point(abs(score.x-newBall.x),(score.y-newBall.y))
            val oldDistanceToScorePoint = nextMove.getDistancePoint()
            val replace = compareDistance(oldDistanceToScorePoint,distanceToScorePoint)
            if(replace){
                nextMove.setNextMoveWithDistancePoint(Static.RIGHT,distanceToScorePoint)
            }
        }

        if(availableMoves.left){
            val newBall = Point(ball.x-1,ball.y)
            val distanceToScorePoint = Point(abs(score.x-newBall.x),(score.y-newBall.y))
            val oldDistanceToScorePoint = nextMove.getDistancePoint()
            val replace = compareDistance(oldDistanceToScorePoint,distanceToScorePoint)
            if(replace){
                nextMove.setNextMoveWithDistancePoint(Static.LEFT,distanceToScorePoint)
            }
        }

        if(availableMoves.upRight){
            val newBall = Point(ball.x+1,ball.y-1)
            val distanceToScorePoint = Point(abs(score.x-newBall.x),(score.y-newBall.y))
            val oldDistanceToScorePoint = nextMove.getDistancePoint()
            val replace = compareDistance(oldDistanceToScorePoint,distanceToScorePoint)
            if(replace){
                nextMove.setNextMoveWithDistancePoint(Static.UP_RIGHT,distanceToScorePoint)
            }
        }

        if(availableMoves.upLeft){
            val newBall = Point(ball.x-1,ball.y-1)
            val distanceToScorePoint = Point(abs(score.x-newBall.x),(score.y-newBall.y))
            val oldDistanceToScorePoint = nextMove.getDistancePoint()
            val replace = compareDistance(oldDistanceToScorePoint,distanceToScorePoint)
            if(replace){
                nextMove.setNextMoveWithDistancePoint(Static.UP_LEFT,distanceToScorePoint)
            }
        }

        if(availableMoves.up){
            val newBall = Point(ball.x,ball.y-1)
            val distanceToScorePoint = Point(abs(score.x-newBall.x),(score.y-newBall.y))
            val oldDistanceToScorePoint = nextMove.getDistancePoint()
            val replace = compareDistance(oldDistanceToScorePoint,distanceToScorePoint)
            if(replace){
                nextMove.setNextMoveWithDistancePoint(Static.UP,distanceToScorePoint)
            }
        }

        if(nextMove.isNextMoveSet()){
            makeMove(nextMove.getDirection())
        }
        else{
            tieAnimation()
        }

    }

    /**
     * making move phone in given direction
     */
    private fun makeMove(direction: Int?) {
        when(direction){
            Static.UP -> phoneMove(Static.UP,field.moveUp(false))
            Static.UP_RIGHT -> phoneMove(Static.UP_RIGHT,field.moveUpRight(false))
            Static.RIGHT -> phoneMove(Static.RIGHT,field.moveRight(false))
            Static.DOWN_RIGHT -> phoneMove(Static.DOWN_RIGHT,field.moveDownRight(false))
            Static.DOWN -> phoneMove(Static.DOWN,field.moveDown(false))
            Static.DOWN_LEFT -> phoneMove(Static.DOWN_LEFT,field.moveDownLeft(false))
            Static.LEFT -> phoneMove(Static.LEFT,field.moveLeft(false))
            Static.UP_LEFT -> phoneMove(Static.UP_LEFT,field.moveUpLeft(false))
        }

    }


    /**
     * making phone move
     * update two points in database if logged in (before and after move)
     * checking if phone has next move
     */
    private fun phoneMove(direction: Int, move: PointsAfterMove){
        val pointsAfterMove = move
        val stuckAndMovePhone = field.checkIfStuckAndNextMove(direction)
        if(loggedInStatus.loggedIn) {
            launch {
                requireContext().let {
                    PointOnFieldEasyDatabase(it).getPointOnFieldDao().updatePointOnField(pointsAfterMove.beforeMovePoint)
                    PointOnFieldEasyDatabase(it).getPointOnFieldDao().updatePointOnField(pointsAfterMove.afterMovePoint)
                }
            }
        }
        nextMovePhone=stuckAndMovePhone.nextMove
    }



    private fun clearDatabaseAndSharedPreferences() {
        Functions.saveEasyGameToSharedPreferences(requireContext(),false,loggedInStatus.userid)
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
        endGameWinHandler.postDelayed(endGameWinRunnable(),100)
    }

    private fun tieAnimation() {
        if(loggedInStatus.loggedIn) {
            updateUserCounters(0,0,1,0)
            clearDatabaseAndSharedPreferences()
        }
        endGameTieHandler.postDelayed(endGameTieRunnable(),100)
    }

    private fun lostAnimation() {
        if(loggedInStatus.loggedIn) {
            updateUserCounters(0,0,0,1)
            clearDatabaseAndSharedPreferences()
        }
        endGameLoseHandler.postDelayed(endGameLoseRunnable(),100)
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

    private fun removeDialogLose() {
        dialog.dismiss()
        endGameLoopCounter+=1
        endGameLoseHandler.postDelayed(endGameLoseRunnable(),100)

    }

    private fun doNothingLose() {
        endGameLoopCounter+=1
        endGameLoseHandler.postDelayed(endGameLoseRunnable(),1000)

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
                Shader.TileMode.REPEAT,screenUnit)

        mView.alert_dialog_end_game_title.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,3*screenUnit)
        mView.alert_dialog_end_game_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,(1.5*screenUnit).toFloat())
        mView.alert_dialog_end_game_title.text = "SORRY"
        mView.alert_dialog_end_game_title.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_red_dark))

        mView.alert_dialog_end_game_message.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,3*screenUnit)
        mView.alert_dialog_end_game_message.setTextSize(TypedValue.COMPLEX_UNIT_PX,(1.5*screenUnit).toFloat())
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

    private fun prepareUserNameLose() {
        userName = "PHONE"
        endGameLoopCounter += 1
        endGameLoseHandler.postDelayed(endGameLoseRunnable(),1000)

    }

    private fun endGameTieRunnable() = Runnable {
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

    private fun prepareUserNameTie() {
        userName = "NOBODY"
        endGameLoopCounter += 1
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
                Shader.TileMode.REPEAT,screenUnit)

        mView.alert_dialog_end_game_title.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,3*screenUnit)
        mView.alert_dialog_end_game_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,(1.5*screenUnit).toFloat())
        mView.alert_dialog_end_game_title.text = "UNFORTUNATELY"
        mView.alert_dialog_end_game_title.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_yellow_dark))

        mView.alert_dialog_end_game_message.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,3*screenUnit)
        mView.alert_dialog_end_game_message.setTextSize(TypedValue.COMPLEX_UNIT_PX,(1.5*screenUnit).toFloat())
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

    private fun endGameWinRunnable()= Runnable {
        when(endGameLoopCounter){
            0 -> prepareUserNameWin()
            1 -> displayWinAlertDialog()
            2 -> doNothingWin()
            3 -> removeDialogWin()
            4 -> goToMainMenu()
        }
    }

    private fun removeDialogWin() {
        dialog.dismiss()
        endGameLoopCounter+=1
        endGameWinHandler.postDelayed(endGameWinRunnable(),100)

    }

    private fun doNothingWin() {
        endGameLoopCounter+=1
        endGameWinHandler.postDelayed(endGameWinRunnable(),1000)

    }

    private fun prepareUserNameWin() {
        if(loggedInStatus.loggedIn){
            launch {
                requireContext().let {
                    val user = UserDBDatabase(it).getUserDBDao().getUser(loggedInStatus.userid)
                    userName = user.name
                }
            }
        }
        else{
            userName = "YOU"
        }
        endGameLoopCounter += 1
        endGameWinHandler.postDelayed(endGameWinRunnable(),1000)
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
                Shader.TileMode.REPEAT,screenUnit)

        mView.alert_dialog_end_game_title.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,3*screenUnit)
        mView.alert_dialog_end_game_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,(1.5*screenUnit).toFloat())
        mView.alert_dialog_end_game_title.text = "CONGRATULATION"
        mView.alert_dialog_end_game_title.setTextColor(ContextCompat.getColor(requireContext(),R.color.icon_green_dark))

        mView.alert_dialog_end_game_message.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,3*screenUnit)
        mView.alert_dialog_end_game_message.setTextSize(TypedValue.COMPLEX_UNIT_PX,(1.5*screenUnit).toFloat())
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



    private fun updateButtons() {
        disableButtons()
        val ball = field.findBall()
        val availableMoves = field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y])

        if (availableMoves.up){
            rootView.fragment_single_player_easy_game_move_up_btn.visibility = View.VISIBLE
        }

        if(availableMoves.upRight){
            rootView.fragment_single_player_easy_game_move_up_right_btn.visibility=View.VISIBLE
        }

        if(availableMoves.right){
            rootView.fragment_single_player_easy_game_move_right_btn.visibility=View.VISIBLE
        }

        if(availableMoves.downRight){
            rootView.fragment_single_player_easy_game_move_down_right_btn.visibility=View.VISIBLE
        }

        if(availableMoves.down){
            rootView.fragment_single_player_easy_game_move_down_btn.visibility=View.VISIBLE
        }

        if(availableMoves.downLeft){
            rootView.fragment_single_player_easy_game_move_down_left_btn.visibility=View.VISIBLE
        }

        if(availableMoves.left){
            rootView.fragment_single_player_easy_game_move_left_btn.visibility=View.VISIBLE
        }

        if(availableMoves.upLeft){
            rootView.fragment_single_player_easy_game_move_up_left_btn.visibility=View.VISIBLE
        }

    }

    private fun disableButtons() {
        rootView.fragment_single_player_easy_game_move_up_btn.visibility=View.GONE
        rootView.fragment_single_player_easy_game_move_up_right_btn.visibility=View.GONE
        rootView.fragment_single_player_easy_game_move_right_btn.visibility=View.GONE
        rootView.fragment_single_player_easy_game_move_down_right_btn.visibility=View.GONE
        rootView.fragment_single_player_easy_game_move_down_btn.visibility=View.GONE
        rootView.fragment_single_player_easy_game_move_down_left_btn.visibility=View.GONE
        rootView.fragment_single_player_easy_game_move_left_btn.visibility=View.GONE
        rootView.fragment_single_player_easy_game_move_up_left_btn.visibility=View.GONE
    }

    private fun updateField() {
        displayBall()
        updateMoves()
    }

    private fun updateMoves() {
        rootView.fragment_single_player_easy_game_field.setImageDrawable(MovesEasyDrawable(requireContext(),field, screenUnit.toDouble()))

    }

    private fun displayBall() {
        val ball = field.findBall()
        rootView.fragment_single_player_easy_game_ball.setImageDrawable(BallDrawable(requireContext(),field.field[ball.x][ball.y], screenUnit.toDouble()))

    }

    private fun setDrawable() {
        rootView.fragment_single_player_easy_game_field.background = FieldEasyDrawable(requireContext(), screenUnit.toDouble())
        rootView.fragment_single_player_easy_game_move_down_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.down)
        rootView.fragment_single_player_easy_game_move_down_right_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.down_right)
        rootView.fragment_single_player_easy_game_move_right_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.right)
        rootView.fragment_single_player_easy_game_move_up_right_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.up_right)
        rootView.fragment_single_player_easy_game_move_up_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.up)
        rootView.fragment_single_player_easy_game_move_up_left_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.up_left)
        rootView.fragment_single_player_easy_game_move_left_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.left)
        rootView.fragment_single_player_easy_game_move_down_left_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.down_left)


        rootView.fragment_single_player_easy_back_button.background = ButtonDrawable(requireContext(), (4*screenUnit).toDouble(), (2*screenUnit).toDouble(), screenUnit.toDouble())
        rootView.fragment_single_player_easy_back_button.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
    }

    private fun setConstraintLayout() {
        val set = ConstraintSet()
        set.clone(rootView.fragment_single_player_easy_game_layout)

        set.connect(rootView.fragment_single_player_easy_game_field.id, ConstraintSet.TOP,rootView.fragment_single_player_easy_game_layout.id, ConstraintSet.TOP,screenUnit)
        set.connect(rootView.fragment_single_player_easy_game_field.id, ConstraintSet.LEFT,rootView.fragment_single_player_easy_game_layout.id, ConstraintSet.LEFT,screenUnit)

        set.connect(rootView.fragment_single_player_easy_game_ball.id, ConstraintSet.TOP,rootView.fragment_single_player_easy_game_layout.id, ConstraintSet.TOP,screenUnit)
        set.connect(rootView.fragment_single_player_easy_game_ball.id, ConstraintSet.LEFT,rootView.fragment_single_player_easy_game_layout.id, ConstraintSet.LEFT,screenUnit)

        set.connect(rootView.fragment_single_player_easy_game_middle.id, ConstraintSet.TOP,rootView.fragment_single_player_easy_game_layout.id, ConstraintSet.TOP,26*screenUnit)
        set.connect(rootView.fragment_single_player_easy_game_middle.id, ConstraintSet.LEFT,rootView.fragment_single_player_easy_game_layout.id, ConstraintSet.LEFT,9*screenUnit)

        set.connect(rootView.fragment_single_player_easy_game_move_up_btn.id, ConstraintSet.BOTTOM,rootView.fragment_single_player_easy_game_middle.id, ConstraintSet.TOP,4*screenUnit)
        set.connect(rootView.fragment_single_player_easy_game_move_up_btn.id, ConstraintSet.LEFT,rootView.fragment_single_player_easy_game_middle.id, ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_single_player_easy_game_move_right_btn.id, ConstraintSet.BOTTOM,rootView.fragment_single_player_easy_game_middle.id, ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_single_player_easy_game_move_right_btn.id, ConstraintSet.LEFT,rootView.fragment_single_player_easy_game_middle.id, ConstraintSet.RIGHT,4*screenUnit)

        set.connect(rootView.fragment_single_player_easy_game_move_down_btn.id, ConstraintSet.TOP,rootView.fragment_single_player_easy_game_middle.id, ConstraintSet.BOTTOM,4*screenUnit)
        set.connect(rootView.fragment_single_player_easy_game_move_down_btn.id, ConstraintSet.LEFT,rootView.fragment_single_player_easy_game_middle.id, ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_single_player_easy_game_move_left_btn.id, ConstraintSet.BOTTOM,rootView.fragment_single_player_easy_game_middle.id, ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_single_player_easy_game_move_left_btn.id, ConstraintSet.RIGHT,rootView.fragment_single_player_easy_game_middle.id, ConstraintSet.LEFT,4*screenUnit)

        set.connect(rootView.fragment_single_player_easy_game_move_up_right_btn.id, ConstraintSet.BOTTOM,rootView.fragment_single_player_easy_game_middle.id, ConstraintSet.TOP,3*screenUnit)
        set.connect(rootView.fragment_single_player_easy_game_move_up_right_btn.id, ConstraintSet.LEFT,rootView.fragment_single_player_easy_game_middle.id, ConstraintSet.RIGHT,3*screenUnit)

        set.connect(rootView.fragment_single_player_easy_game_move_down_right_btn.id, ConstraintSet.TOP,rootView.fragment_single_player_easy_game_middle.id, ConstraintSet.BOTTOM,3*screenUnit)
        set.connect(rootView.fragment_single_player_easy_game_move_down_right_btn.id, ConstraintSet.LEFT,rootView.fragment_single_player_easy_game_middle.id, ConstraintSet.RIGHT,3*screenUnit)

        set.connect(rootView.fragment_single_player_easy_game_move_down_left_btn.id, ConstraintSet.TOP,rootView.fragment_single_player_easy_game_middle.id, ConstraintSet.BOTTOM,3*screenUnit)
        set.connect(rootView.fragment_single_player_easy_game_move_down_left_btn.id, ConstraintSet.RIGHT,rootView.fragment_single_player_easy_game_middle.id, ConstraintSet.LEFT,3*screenUnit)

        set.connect(rootView.fragment_single_player_easy_game_move_up_left_btn.id, ConstraintSet.BOTTOM,rootView.fragment_single_player_easy_game_middle.id, ConstraintSet.TOP,3*screenUnit)
        set.connect(rootView.fragment_single_player_easy_game_move_up_left_btn.id, ConstraintSet.RIGHT,rootView.fragment_single_player_easy_game_middle.id, ConstraintSet.LEFT,3*screenUnit)

        set.connect(rootView.fragment_single_player_easy_back_button.id,ConstraintSet.TOP,rootView.fragment_single_player_easy_game_layout.id,ConstraintSet.TOP,2*screenUnit)
        set.connect(rootView.fragment_single_player_easy_back_button.id,ConstraintSet.LEFT,rootView.fragment_single_player_easy_game_layout.id,ConstraintSet.LEFT,14*screenUnit)

        set.connect(rootView.fragment_single_player_easy_phone_icon.id,ConstraintSet.TOP,rootView.fragment_single_player_easy_game_field.id,ConstraintSet.TOP,screenUnit)
        set.connect(rootView.fragment_single_player_easy_phone_icon.id,ConstraintSet.LEFT,rootView.fragment_single_player_easy_game_field.id,ConstraintSet.RIGHT,2*screenUnit)

        set.connect(rootView.fragment_single_player_easy_vs_tv.id,ConstraintSet.TOP,rootView.fragment_single_player_easy_phone_icon.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView.fragment_single_player_easy_vs_tv.id,ConstraintSet.LEFT,rootView.fragment_single_player_easy_phone_icon.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_single_player_easy_user_icon.id,ConstraintSet.TOP,rootView.fragment_single_player_easy_vs_tv.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView.fragment_single_player_easy_user_icon.id,ConstraintSet.LEFT,rootView.fragment_single_player_easy_vs_tv.id,ConstraintSet.LEFT,0)

        set.applyTo(rootView.fragment_single_player_easy_game_layout)
    }

    private fun setViewSizes() {
        rootView.fragment_single_player_easy_game_field.layoutParams = ConstraintLayout.LayoutParams(10*screenUnit,14*screenUnit)
        rootView.fragment_single_player_easy_game_ball.layoutParams = ConstraintLayout.LayoutParams(10*screenUnit,14*screenUnit)
        rootView.fragment_single_player_easy_game_move_up_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_single_player_easy_game_move_up_right_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_single_player_easy_game_move_right_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_single_player_easy_game_move_down_right_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_single_player_easy_game_move_down_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_single_player_easy_game_move_down_left_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_single_player_easy_game_move_left_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_single_player_easy_game_move_up_left_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_single_player_easy_game_middle.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_single_player_easy_back_button.layoutParams = ConstraintLayout.LayoutParams(4*screenUnit,2*screenUnit)

        rootView.fragment_single_player_easy_user_icon.layoutParams = ConstraintLayout.LayoutParams(3*screenUnit,3*screenUnit)
        rootView.fragment_single_player_easy_phone_icon.layoutParams = ConstraintLayout.LayoutParams(3*screenUnit,3*screenUnit)
        rootView.fragment_single_player_easy_vs_tv.layoutParams = ConstraintLayout.LayoutParams(3*screenUnit,3*screenUnit)
        rootView.fragment_single_player_easy_vs_tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenUnit.toFloat())

    }

    private fun makeBackgroundGrid() {
        rootView.fragment_single_player_easy_game_layout.background = TileDrawable((ContextCompat.getDrawable(requireContext(), R.drawable.background)!!),
                Shader.TileMode.REPEAT,screenUnit)

    }

}
