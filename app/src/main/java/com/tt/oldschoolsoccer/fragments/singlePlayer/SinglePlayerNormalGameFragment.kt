package com.tt.oldschoolsoccer.fragments.singlePlayer

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
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tt.oldschoolsoccer.R
import com.tt.oldschoolsoccer.classes.*
import com.tt.oldschoolsoccer.database.PointOnFieldNormalDatabase
import com.tt.oldschoolsoccer.database.UserDBDatabase
import com.tt.oldschoolsoccer.drawable.*
import com.tt.oldschoolsoccer.fragments.MainFragment
import kotlinx.android.synthetic.main.fragment_single_player_easy_game.view.*
import kotlinx.android.synthetic.main.fragment_single_player_normal_game.view.*
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList
import kotlin.math.abs


class SinglePlayerNormalGameFragment : FragmentCoroutine() {

    private var screenUnit:Int = 0
    private var loggedInStatus = LoggedInStatus()
    private lateinit var rootView:View
    private var field = GameField()
    private var fieldReady = false
    private val startGameHandler = Handler()
    private val gameLoopHandler = Handler()
    private val phoneMoveHandler = Handler()
    private var nextMovePhone:Boolean=false
    private val score = Point(4,12)

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

        if(loggedInStatus.loggedIn){
            Functions.saveMyMoveNormalToSharedPreferences(requireContext(),field.myMove,loggedInStatus.userid)
        }
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
            if(field.myMove) {
                updateButtons()
            }
            gameLoop().run()
        }else{
            startGameHandler.postDelayed(startGame(),1000)
        }

    }


    /**
     * loop for my move and phone move
     */
    private fun gameLoop(): Runnable = Runnable {

        updateField()
        
        if(field.myMove){

            gameLoopHandler.removeCallbacksAndMessages(null)
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

        /**
         * find best route - which is ending closer to the score
         */
        val bestMoves = checkBestMove(field)

        val listSize = bestMoves.size
        val counter = 0

        phoneMoveRunnable(listSize,counter,bestMoves).run()

    }

    private fun phoneMoveRunnable(size:Int,counter:Int,bestMove:ArrayList<Int>):Runnable = Runnable {
        when (counter) {
            0 -> {
                field.clearTestMoves()
                updateField()
                phoneMoveHandler.postDelayed(phoneMoveRunnable(size,counter+1,bestMove),500)
            }
            (size+1) -> {
                phoneMoveHandler.removeCallbacksAndMessages(null)

                updateField()
                val end = checkWin()
                field.myMove = true
                if (!end) {
                    gameLoopHandler.postDelayed(gameLoop(), 100)
                }
            }
            else -> {
                val move = bestMove[counter-1]
                makeMovePhone(move)
                updateField()
                phoneMoveHandler.postDelayed(phoneMoveRunnable(size,counter+1,bestMove),1000)
            }
        }
    }

    private fun makeMovePhone(move: Int) {
        when(move){
            Static.UP -> phoneMove(Static.UP,field.moveUp(false))
            Static.UP_LEFT -> phoneMove(Static.UP,field.moveUpLeft(false))
            Static.UP_RIGHT -> phoneMove(Static.UP,field.moveUpRight(false))
            Static.LEFT -> phoneMove(Static.UP,field.moveLeft(false))
            Static.RIGHT -> phoneMove(Static.UP,field.moveRight(false))
            Static.DOWN_LEFT -> phoneMove(Static.UP,field.moveDownLeft(false))
            Static.DOWN_RIGHT -> phoneMove(Static.UP,field.moveDownRight(false))
            Static.DOWN -> phoneMove(Static.UP,field.moveDown(false))
        }

    }

    private fun phoneMove(direction: Int, move:PointsAfterMove){
        val pointsAfterMove = move
        if(loggedInStatus.loggedIn){
            launch {
                requireContext().let {
                    PointOnFieldNormalDatabase(it).getPointOnFieldDao().updatePointOnField(pointsAfterMove.beforeMovePoint)
                    PointOnFieldNormalDatabase(it).getPointOnFieldDao().updatePointOnField(pointsAfterMove.afterMovePoint)
                }
            }
        }
    }

    /**
     * return list of moves for best way to score or closer to score
     */
    private fun checkBestMove(field: GameField):ArrayList<Int> {
        val bestMoves = ArrayList<Int>()
        val ball = field.findBall()
        val pointOnFieldToCheck = PointOnFieldToCheck(null,ball,null)
        pointOnFieldToCheck.setAvailableMoves(field)
        /**
         * prepare list with first point - starting point
         */
        val list = ArrayList<PointOnFieldToCheck>()
        list.add(pointOnFieldToCheck)
        /**
         * temporary list for storing additional moves
         */
        var listCompleted = false
        val tmpList = ArrayList<PointOnFieldToCheck>()
        /**
         * preparing main list of all moves
         */
        while(!listCompleted){
            /**
             * checking if each element has been already checked
             * if not checking if additional move can be done from new point
             * if yes create new points (if move in given direction is possible) and add it to the temporary list
             */
            for(x in list){
                if(!x.isChecked()){
                    if(x.isNextMove()) {
                        if (x.availableMoves.down) {
                            val newBall = field.testMoveDown(field.field, x.currentBall)
                            val pointOnFieldToCheckTmp = PointOnFieldToCheck(Static.DOWN, newBall,x.currentBall)
                            pointOnFieldToCheckTmp.setAvailableMoves(field)
                            tmpList.add(pointOnFieldToCheckTmp)
                        }
                        if (x.availableMoves.downLeft) {
                            val newBall = field.testMoveDownLeft(field.field, x.currentBall)
                            val pointOnFieldToCheckTmp = PointOnFieldToCheck(Static.DOWN_LEFT, newBall,x.currentBall)
                            pointOnFieldToCheckTmp.setAvailableMoves(field)
                            tmpList.add(pointOnFieldToCheckTmp)
                        }
                        if (x.availableMoves.downRight) {
                            val newBall = field.testMoveDownRight(field.field, x.currentBall)
                            val pointOnFieldToCheckTmp = PointOnFieldToCheck(Static.DOWN_RIGHT, newBall,x.currentBall)
                            pointOnFieldToCheckTmp.setAvailableMoves(field)
                            tmpList.add(pointOnFieldToCheckTmp)
                        }
                        if (x.availableMoves.right) {
                            val newBall = field.testMoveRight(field.field, x.currentBall)
                            val pointOnFieldToCheckTmp = PointOnFieldToCheck(Static.RIGHT, newBall,x.currentBall)
                            pointOnFieldToCheckTmp.setAvailableMoves(field)
                            tmpList.add(pointOnFieldToCheckTmp)
                        }
                        if (x.availableMoves.left) {
                            val newBall = field.testMoveLeft(field.field, x.currentBall)
                            val pointOnFieldToCheckTmp = PointOnFieldToCheck(Static.LEFT, newBall,x.currentBall)
                            pointOnFieldToCheckTmp.setAvailableMoves(field)
                            tmpList.add(pointOnFieldToCheckTmp)
                        }
                        if (x.availableMoves.upRight) {
                            val newBall = field.testMoveUpRight(field.field, x.currentBall)
                            val pointOnFieldToCheckTmp = PointOnFieldToCheck(Static.UP_RIGHT, newBall,x.currentBall)
                            pointOnFieldToCheckTmp.setAvailableMoves(field)
                            tmpList.add(pointOnFieldToCheckTmp)
                        }
                        if (x.availableMoves.upLeft) {
                            val newBall = field.testMoveUpLeft(field.field, x.currentBall)
                            val pointOnFieldToCheckTmp = PointOnFieldToCheck(Static.UP_LEFT, newBall,x.currentBall)
                            pointOnFieldToCheckTmp.setAvailableMoves(field)
                            tmpList.add(pointOnFieldToCheckTmp)
                        }
                        if (x.availableMoves.up) {
                            val newBall = field.testMoveUp(field.field, x.currentBall)
                            val pointOnFieldToCheckTmp = PointOnFieldToCheck(Static.UP, newBall,x.currentBall)
                            pointOnFieldToCheckTmp.setAvailableMoves(field)
                            tmpList.add(pointOnFieldToCheckTmp)
                        }
                    }
                    x.setChecked()
                }
            }
            val tmpListSize = tmpList.size
            /**
             * check if temporary list has any elements
             * if no - terminate while loop
             * if yes check if any element should be added to main list
             * clear tmp list
             */
            if(tmpListSize==0){
                listCompleted=true
            }else{
                for(tmpx in tmpList){
                    var shouldBeAdded = true
                    for(x in list){
                        if((tmpx.currentBall.x==x.currentBall.x)and (tmpx.currentBall.y==x.currentBall.y)){
                            shouldBeAdded = false
                        }
                    }
                    if(shouldBeAdded){
                        list.add(tmpx)
                    }
                }
                tmpList.clear()
            }
        }
        /**
         * when main list is ready check distance
         * check if at any point distance to score equals 0 - goal
         * if not check point (end point) with smallest distance to score
         */

        var point = Point()
        val distancePoint = Point(20,20)
        for(x in list){

            val distancex = Point(abs(score.x-x.currentBall.x),score.y-x.currentBall.y)
            if(distancex.x==0&&distancex.y==0){
                distancePoint.x=0
                distancePoint.y=0
                point = x.currentBall
            }
        }
        if(distancePoint.x!=0&&distancePoint.y!=0){
            for(x in list){
                if(!x.isNextMove()){
                    val distancex = Point(abs(score.x-x.currentBall.x),score.y-x.currentBall.y)
                    val replace = compareDistance(distancePoint,distancex)
                    if(replace){
                        distancePoint.x=distancex.x
                        distancePoint.y=distancex.y
                        point = x.currentBall
                    }
                }
            }
        }

        /**
         * create best moves list going from the last point to first one
         */
        var loopChecker = true
        while (loopChecker){
            for(x in list){
                if(x.currentBall.x == point.x && x.currentBall.y == point.y){
                    if(x.incomingDirection!=null){
                     bestMoves.add(0, x.incomingDirection)
                        point = x.previousBall!!
                    }
                    else{
                        loopChecker = false
                    }
                }
            }
        }

        return bestMoves
    }

    private fun compareDistance(oldDistance: Point, newDistance: Point): Boolean {
        var replace = false
        if(newDistance.y<oldDistance.y){
            replace = true
        }
        else if ((newDistance.y==oldDistance.y)&&(newDistance.x<oldDistance.x)){
            replace = true
        }
        return replace
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
        view?.let {
        rootView.fragment_single_player_normal_game_field.setImageDrawable(MovesEasyDrawable(requireContext(),field, screenUnit.toDouble()))
        }
    }

    private fun displayBall() {
        view?.let {
        val ball = field.findBall()
        rootView.fragment_single_player_normal_game_ball.setImageDrawable(BallDrawable(requireContext(),field.field[ball.x][ball.y], screenUnit.toDouble()))
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
                        field.myMove = Functions.readMyMoveNormalGameFromSharedPreferences(requireContext(),loggedInStatus.userid)
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
        setDrawable()
        setConstraintLayout()
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

        rootView.fragment_single_player_normal_back_button.setOnClickListener {
            goToMainMenu()
        }
    }

    private fun goToMainMenu() {

        activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MainFragment()).commit()
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

        rootView.fragment_single_player_normal_back_button.layoutParams = ConstraintLayout.LayoutParams(4*screenUnit,2*screenUnit)
        rootView.fragment_single_player_normal_back_button.background = ButtonDrawable(requireContext(), (4*screenUnit).toDouble(), (2*screenUnit).toDouble(), screenUnit.toDouble())
        rootView.fragment_single_player_normal_back_button.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
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

        set.connect(rootView.fragment_single_player_normal_back_button.id,ConstraintSet.TOP,rootView.fragment_single_player_normal_game_layout.id,ConstraintSet.TOP,2*screenUnit)
        set.connect(rootView.fragment_single_player_normal_back_button.id,ConstraintSet.LEFT,rootView.fragment_single_player_normal_game_layout.id,ConstraintSet.LEFT,14*screenUnit)

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
        rootView.fragment_single_player_normal_back_button.layoutParams = ConstraintLayout.LayoutParams(4*screenUnit,2*screenUnit)
    }

    private fun makeBackgroundGrid() {
        rootView.fragment_single_player_normal_game_layout.background = TileDrawable((ContextCompat.getDrawable(requireContext(), R.drawable.background)!!),
            Shader.TileMode.REPEAT,screenUnit)
    }

}

/*
todo back button
 */