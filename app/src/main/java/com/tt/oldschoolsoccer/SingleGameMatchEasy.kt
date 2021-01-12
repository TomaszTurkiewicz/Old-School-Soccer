package com.tt.oldschoolsoccer

import android.graphics.Point
import android.graphics.Shader
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tt.oldschoolsoccer.classes.*
import com.tt.oldschoolsoccer.database.PointOnField
import com.tt.oldschoolsoccer.database.PointOnFieldEasyDatabase
import com.tt.oldschoolsoccer.drawable.*
import kotlinx.android.synthetic.main.activity_single_game_match_easy.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class SingleGameMatchEasy : AppCompatActivityCoroutine() {
    var screenUnit:Int=0
    var field = GameField()
    var nextMovePhone:Boolean=false



    private val gameLoopHandler = Handler()
    private val startGameHandler = Handler()
    var loggedInStatus = LoggedInStatus()
    var fieldReady = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen()
        setContentView(R.layout.activity_single_game_match_easy)
        makeUI()
        loggedInStatus = Functions.readLoggedStateFromSharedPreferences(this)

        disableButtons()
        gameLogic()



    }


    private fun gameLogic() {
        field.generate(Static.EASY)

        // if logged in
        if(loggedInStatus.loggedIn) {
            //check if game is saved for user
            val savedGame = Functions.readEasyGameFromSharedPreferences(this, loggedInStatus.userid)
            // if is saved - read from database
            if (savedGame) {
                launch {
                    applicationContext?.let {
                        val list = PointOnFieldEasyDatabase(it).getPointOnFiledDao().getAllPointsOnField()

                        for (item in list) {
                            val i = item.position % 9
                            val j = item.position / 9

                            field.field[i][j] = item
                        }
                    }

                    fieldReady = true

                }

            }
            //if is not saved update counter, shared preferences and database
            else {


                Functions.saveEasyGameToSharedPreferences(this, true, loggedInStatus.userid)

                updateUserCounters(1,0,0,0)

                launch {
                    for (i in 0..8) {
                        for (j in 0..12) {
                            val item = field.getPoint(i, j)
                            applicationContext?.let {
                                PointOnFieldEasyDatabase(it).getPointOnFiledDao().updatePointOnField(item)
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
        displayBall()
    }

    override fun onResume() {
        super.onResume()

        startGame().run()
    }

    override fun onPause() {
        super.onPause()
        gameLoopHandler.removeCallbacksAndMessages(null)
    }

    private fun startGame():Runnable = Runnable {
        if(fieldReady){

            startGameHandler.removeCallbacksAndMessages(null)
            updateMoves()
            updateButtons()
            displayBall()
            gameLoop().run()
        }else{
            startGameHandler.postDelayed(startGame(),1000)
        }
    }

    private fun gameLoop():Runnable = Runnable {
        if(field.myMove){
            gameLoopHandler.removeCallbacksAndMessages(null)
            updateButtons()
        }
        else{

            makeMovePhone()
            displayBall()
            updateMoves()
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

    private fun checkWin():Boolean {
        val ball = field.findBall()
        val lost1 = Point(3,12)
        val lost2 = Point(4,12)
        val lost3 = Point(5,12)
        val win1 = Point(3,0)
        val win2 = Point(4,0)
        val win3 = Point(5,0)
        when(ball){
            lost1,lost2,lost3 -> {
                if(loggedInStatus.loggedIn) {

                    updateUserCounters(0,0,0,1)

                    clearDatabaseAndSharedPreferences()
                }
                lostAnimation()
                return true
            }
            win1, win2, win3 -> {
                if(loggedInStatus.loggedIn) {

                    updateUserCounters(0,1,0,0)

                    clearDatabaseAndSharedPreferences()
                }
                winAnimation()
                return true
            }

        }
        return false


    }

    private fun winAnimation() {

        win.text = "WIN"
        win.setTextColor(ContextCompat.getColor(this,R.color.win))
    }

    private fun lostAnimation() {

        win.text = "LOST"
        win.setTextColor(ContextCompat.getColor(this,R.color.lost))
    }

    private fun makeMovePhone() {
        val ball = field.findBall()
            when (ball.x) {
                -1 -> finish()
                0, 1, 2 -> moveDownRightPhone(ball)
                3, 4, 5 -> moveDownPhone(ball)
                6, 7, 8 -> moveDownLeftPhone(ball)
            }


    }




    private fun moveDownLeftPhone(ball:Point) {
        val availableMoves = field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y])
        if(availableMoves.downLeft){
            phoneMove(Static.DOWN_LEFT,field.moveDownLeft(false))
        }else{
            if(availableMoves.down){
                phoneMove(Static.DOWN,field.moveDown(false))
            }else{
                if(availableMoves.downRight){
                    phoneMove(Static.DOWN_RIGHT,field.moveDownRight(false))
                }else{
                    if(availableMoves.left){
                        phoneMove(Static.LEFT,field.moveLeft(false))
                    }else{
                        if(availableMoves.right){
                            phoneMove(Static.RIGHT,field.moveRight(false))
                        }else{
                            if(availableMoves.upLeft){
                                phoneMove(Static.UP_LEFT,field.moveUpLeft(false))
                            }else{
                                if(availableMoves.upRight){
                                    phoneMove(Static.UP_RIGHT,field.moveUpRight(false))
                                }else{
                                    if(availableMoves.up){
                                        phoneMove(Static.UP,field.moveUp(false))
                                    }else{
                                        if(loggedInStatus.loggedIn) {

                                            updateUserCounters(0,0,1,0)

                                            clearDatabaseAndSharedPreferences()
                                        }
                                        tieAnimation()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun tieAnimation() {

        win.text = "TIE"
        win.setTextColor(ContextCompat.getColor(this,R.color.test))

    }

    private fun moveDownPhone(ball:Point) {
        val availableMoves = field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y])
        if(availableMoves.down){
            phoneMove(Static.DOWN,field.moveDown(false))
        }else{
            if(availableMoves.downLeft){
                phoneMove(Static.DOWN_LEFT,field.moveDownLeft(false))
            }else{
                if(availableMoves.downRight){
                    phoneMove(Static.DOWN_RIGHT,field.moveDownRight(false))
                }else{
                    if(availableMoves.left){
                        phoneMove(Static.LEFT,field.moveLeft(false))
                    }else{
                        if(availableMoves.right){
                            phoneMove(Static.RIGHT,field.moveRight(false))
                        }else{
                            if(availableMoves.upLeft){
                                phoneMove(Static.UP_LEFT,field.moveUpLeft(false))
                            }else{
                                if(availableMoves.upRight){
                                    phoneMove(Static.UP_RIGHT,field.moveUpRight(false))
                                }else{
                                    if(availableMoves.up){
                                        phoneMove(Static.UP,field.moveUp(false))
                                    }else{
                                        if(loggedInStatus.loggedIn) {

                                            updateUserCounters(0,0,1,0)

                                            clearDatabaseAndSharedPreferences()
                                        }
                                        tieAnimation()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private fun moveDownRightPhone(ball: Point) {
        val availableMoves = field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y])
        if(availableMoves.downRight){
            phoneMove(Static.DOWN_RIGHT,field.moveDownRight(false))
        }else{
            if(availableMoves.down){
                phoneMove(Static.DOWN,field.moveDown(false))
            }else{
                if(availableMoves.downLeft){
                    phoneMove(Static.DOWN_LEFT,field.moveDownLeft(false))
                }else{
                    if(availableMoves.right){
                        phoneMove(Static.RIGHT,field.moveRight(false))
                    }else{
                        if(availableMoves.left){
                            phoneMove(Static.LEFT,field.moveLeft(false))
                        }else{
                            if(availableMoves.upRight){
                                phoneMove(Static.UP_RIGHT,field.moveUpRight(false))
                            }else{
                                if(availableMoves.upLeft){
                                    phoneMove(Static.UP_LEFT,field.moveUpLeft(false))
                                }else{
                                    if(availableMoves.up){
                                        phoneMove(Static.UP,field.moveUp(false))
                                    }else{
                                        if(loggedInStatus.loggedIn) {

                                            updateUserCounters(0,0,1,0)

                                            clearDatabaseAndSharedPreferences()
                                        }
                                        tieAnimation()

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun phoneMove(direction: Int, move: PointsAfterMove){
        val pointsAfterMove = move

        val stuckAndMovePhone = field.checkIfStuckAndNextMove(direction)


        if(loggedInStatus.loggedIn) {
            launch {
                applicationContext?.let {
                    PointOnFieldEasyDatabase(it).getPointOnFiledDao().updatePointOnField(pointsAfterMove.beforeMovePoint)
                    PointOnFieldEasyDatabase(it).getPointOnFiledDao().updatePointOnField(pointsAfterMove.afterMovePoint)


                }
            }
        }


        nextMovePhone=stuckAndMovePhone.nextMove

    }

    private fun displayBall() {
        val ball = field.findBall()
        ball_easy.setImageDrawable(BallDrawable(this,field.field[ball.x][ball.y], screenUnit.toDouble()))
    }



    private fun makeUI() {
        screenUnit= Functions.readScreenUnit(this)
        makeBackgroundGrid()
        setViewSizes()
        setConstraintLayout()
        setDrawable()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        easyMoveUpButton.setOnClickListener {
            afterPress(Static.UP,field.moveUp(true))
        }

        easyMoveUpRightButton.setOnClickListener {
            afterPress(Static.UP_RIGHT,field.moveUpRight(true))
        }

        easyMoveRightButton.setOnClickListener {
            afterPress(Static.RIGHT,field.moveRight(true))
        }

        easyMoveDownRightButton.setOnClickListener {
            afterPress(Static.DOWN_RIGHT,field.moveDownRight(true))
        }

        easyMoveDownButton.setOnClickListener {
              afterPress(Static.DOWN,field.moveDown(true))
        }

        easyMoveDownLeftButton.setOnClickListener {
            afterPress(Static.DOWN_LEFT,field.moveDownLeft(true))
        }

        easyMoveLeftButton.setOnClickListener {
            afterPress(Static.LEFT,field.moveLeft(true))
        }

        easyMoveUpLeftButton.setOnClickListener {
            afterPress(Static.UP_LEFT,field.moveUpLeft(true))
        }
    }

    private fun afterPress(direction: Int, move: PointsAfterMove){
        val pointsAfterMove = move

        if(loggedInStatus.loggedIn){
        launch {
            applicationContext?.let {
                PointOnFieldEasyDatabase(it).getPointOnFiledDao().updatePointOnField(pointsAfterMove.beforeMovePoint)
                PointOnFieldEasyDatabase(it).getPointOnFiledDao().updatePointOnField(pointsAfterMove.afterMovePoint)

            }
        }
        }
        displayBall()
        updateMoves()
        updateButtons()
        val endGame = checkWin()
        if (endGame) {
            if(loggedInStatus.loggedIn) {

                clearDatabaseAndSharedPreferences()
            }
            gameLoopHandler.removeCallbacksAndMessages(null)
            disableButtons()
        } else {
            checkNextMove(direction)
        }
    }

    private fun clearDatabaseAndSharedPreferences() {
        Functions.saveEasyGameToSharedPreferences(this,false,loggedInStatus.userid)
    }

    private fun checkNextMove(direction:Int) {
        val nextMove = field.checkIfStuckAndNextMove(direction)
        if(nextMove.stuck){

            gameLoopHandler.removeCallbacksAndMessages(this)
            if(loggedInStatus.loggedIn) {

                updateUserCounters(0, 0, 1, 0)
                clearDatabaseAndSharedPreferences()

            }
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

    private fun disableButtons() {
        easyMoveUpButton.visibility=View.GONE
        easyMoveUpRightButton.visibility=View.GONE
        easyMoveRightButton.visibility=View.GONE
        easyMoveDownRightButton.visibility=View.GONE
        easyMoveDownButton.visibility=View.GONE
        easyMoveDownLeftButton.visibility=View.GONE
        easyMoveLeftButton.visibility=View.GONE
        easyMoveUpLeftButton.visibility=View.GONE

    }

    private fun updateMoves() {
        field_easy.setImageDrawable(MovesEasyDrawable(this,field, screenUnit.toDouble()))
    }

    private fun updateButtons() {
        disableButtons()
        val ball = field.findBall()
        val availableMoves = field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y])

        if (availableMoves.up){
                easyMoveUpButton.visibility = View.VISIBLE
        }

        if(availableMoves.upRight){
                easyMoveUpRightButton.visibility=View.VISIBLE
        }

        if(availableMoves.right){
            easyMoveRightButton.visibility=View.VISIBLE
        }

        if(availableMoves.downRight){
            easyMoveDownRightButton.visibility=View.VISIBLE
        }

        if(availableMoves.down){
            easyMoveDownButton.visibility=View.VISIBLE
        }

        if(availableMoves.downLeft){
            easyMoveDownLeftButton.visibility=View.VISIBLE
        }

        if(availableMoves.left){
            easyMoveLeftButton.visibility=View.VISIBLE
        }

        if(availableMoves.upLeft){
            easyMoveUpLeftButton.visibility=View.VISIBLE
        }
    }

    private fun setDrawable() {
        field_easy.background = FieldEasyDrawable(this, screenUnit.toDouble())
        easyMoveDownButton.background = ContextCompat.getDrawable(this,R.drawable.down)
        easyMoveDownRightButton.background = ContextCompat.getDrawable(this,R.drawable.down_right)
        easyMoveRightButton.background = ContextCompat.getDrawable(this,R.drawable.right)
        easyMoveUpRightButton.background = ContextCompat.getDrawable(this,R.drawable.up_right)
        easyMoveUpButton.background = ContextCompat.getDrawable(this,R.drawable.up)
        easyMoveUpLeftButton.background = ContextCompat.getDrawable(this,R.drawable.up_left)
        easyMoveLeftButton.background = ContextCompat.getDrawable(this,R.drawable.left)
        easyMoveDownLeftButton.background = ContextCompat.getDrawable(this,R.drawable.down_left)


    }

    private fun setConstraintLayout() {
        val set = ConstraintSet()
        set.clone(single_game_match_easy)

        set.connect(field_easy.id,ConstraintSet.TOP,single_game_match_easy.id,ConstraintSet.TOP,screenUnit)
        set.connect(field_easy.id,ConstraintSet.LEFT,single_game_match_easy.id,ConstraintSet.LEFT,screenUnit)

        set.connect(ball_easy.id,ConstraintSet.TOP,single_game_match_easy.id,ConstraintSet.TOP,screenUnit)
        set.connect(ball_easy.id,ConstraintSet.LEFT,single_game_match_easy.id,ConstraintSet.LEFT,screenUnit)

        set.connect(easyMiddle.id,ConstraintSet.TOP,single_game_match_easy.id,ConstraintSet.TOP,26*screenUnit)
        set.connect(easyMiddle.id,ConstraintSet.LEFT,single_game_match_easy.id,ConstraintSet.LEFT,9*screenUnit)

        set.connect(easyMoveUpButton.id,ConstraintSet.BOTTOM,easyMiddle.id,ConstraintSet.TOP,4*screenUnit)
        set.connect(easyMoveUpButton.id,ConstraintSet.LEFT,easyMiddle.id,ConstraintSet.LEFT,0)

        set.connect(easyMoveRightButton.id,ConstraintSet.BOTTOM,easyMiddle.id,ConstraintSet.BOTTOM,0)
        set.connect(easyMoveRightButton.id,ConstraintSet.LEFT,easyMiddle.id,ConstraintSet.RIGHT,4*screenUnit)

        set.connect(easyMoveDownButton.id,ConstraintSet.TOP,easyMiddle.id,ConstraintSet.BOTTOM,4*screenUnit)
        set.connect(easyMoveDownButton.id,ConstraintSet.LEFT,easyMiddle.id,ConstraintSet.LEFT,0)

        set.connect(easyMoveLeftButton.id,ConstraintSet.BOTTOM,easyMiddle.id,ConstraintSet.BOTTOM,0)
        set.connect(easyMoveLeftButton.id,ConstraintSet.RIGHT,easyMiddle.id,ConstraintSet.LEFT,4*screenUnit)

        set.connect(easyMoveUpRightButton.id,ConstraintSet.BOTTOM,easyMiddle.id,ConstraintSet.TOP,3*screenUnit)
        set.connect(easyMoveUpRightButton.id,ConstraintSet.LEFT,easyMiddle.id,ConstraintSet.RIGHT,3*screenUnit)

        set.connect(easyMoveDownRightButton.id,ConstraintSet.TOP,easyMiddle.id,ConstraintSet.BOTTOM,3*screenUnit)
        set.connect(easyMoveDownRightButton.id,ConstraintSet.LEFT,easyMiddle.id,ConstraintSet.RIGHT,3*screenUnit)

        set.connect(easyMoveDownLeftButton.id,ConstraintSet.TOP,easyMiddle.id,ConstraintSet.BOTTOM,3*screenUnit)
        set.connect(easyMoveDownLeftButton.id,ConstraintSet.RIGHT,easyMiddle.id,ConstraintSet.LEFT,3*screenUnit)

        set.connect(easyMoveUpLeftButton.id,ConstraintSet.BOTTOM,easyMiddle.id,ConstraintSet.TOP,3*screenUnit)
        set.connect(easyMoveUpLeftButton.id,ConstraintSet.RIGHT,easyMiddle.id,ConstraintSet.LEFT,3*screenUnit)

        set.applyTo(single_game_match_easy)

    }

    private fun setViewSizes() {
        field_easy.layoutParams = ConstraintLayout.LayoutParams(10*screenUnit,14*screenUnit)
        ball_easy.layoutParams = ConstraintLayout.LayoutParams(10*screenUnit,14*screenUnit)
        easyMoveUpButton.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        easyMoveUpRightButton.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        easyMoveRightButton.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        easyMoveDownRightButton.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        easyMoveDownButton.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        easyMoveDownLeftButton.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        easyMoveLeftButton.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        easyMoveUpLeftButton.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        easyMiddle.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)

    }

    private fun makeBackgroundGrid() {
        single_game_match_easy.background = TileDrawable((ContextCompat.getDrawable(this, R.drawable.background)!!),
                Shader.TileMode.REPEAT,screenUnit)
    }

    private fun fullScreen() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        val decorView: View = window.decorView
        decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            if(visibility and View.SYSTEM_UI_FLAG_FULLSCREEN==0){
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            }
        }
    }


    /**
     *  1) read user statistics from shared preferences
     *  2) add one of counters
     *  3) save back to shared preferences
     *  4) update user statistics in Firebase
     */
    private fun updateUserCounters(numbersOfGames:Int,win:Int,tie:Int,lose:Int){
        val user = Functions.readUserFromSharedPreferences(this,loggedInStatus.userid)
        user.easyGame.numberOfGames+=numbersOfGames
        user.easyGame.win+=win
        user.easyGame.tie+=tie
        user.easyGame.lose+=lose
        Functions.saveUserToSharedPreferences(this,user)
        val dbRef = Firebase.database.getReference("User").child(loggedInStatus.userid)
        dbRef.setValue(user)
    }


}



/*
todo lost animation
todo win animation
todo stuck animation
todo back button
 */