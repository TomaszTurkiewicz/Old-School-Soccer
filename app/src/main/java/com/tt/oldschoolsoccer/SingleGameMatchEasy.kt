package com.tt.oldschoolsoccer

import android.graphics.Point
import android.graphics.Shader
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tt.oldschoolsoccer.classes.EasyGameField
import com.tt.oldschoolsoccer.classes.Functions
import com.tt.oldschoolsoccer.classes.Static
import com.tt.oldschoolsoccer.drawable.*
import kotlinx.android.synthetic.main.activity_single_game_match_easy.*

class SingleGameMatchEasy : AppCompatActivity() {
    var screenUnit:Int=0
    var field = EasyGameField()
//    var ballPosition = Point()

    var test = 0
    var nextMovePhone:Boolean=false

    private val gameLoopHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen()
        setContentView(R.layout.activity_single_game_match_easy)
        makeUI()

        gameLogic()

  //      test()
    }

    private fun test() {
        val y=test/9
        val x = test%9

        field_easy.setImageDrawable(TestDrawable(this,field.field[x][y], screenUnit.toDouble()))

    }

    private fun gameLogic() {
        generateField()
        displayBall()
        gameLoop().run()


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
                0, 1, 2, 3 -> moveDownRightPhone(ball)
                4 -> moveDownPhone(ball)
                5, 6, 7, 8 -> moveDownLeftPhone(ball)
            }


    }

    private fun moveDownLeftPhone(ball:Point) {
        if(field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y],Static.DOWN_LEFT)){
            field.moveDownLeft()
            val stuckAndMovePhone = field.checkIfStuckAndNextMove(Static.DOWN_LEFT)
            nextMovePhone=stuckAndMovePhone.nextMove

        }else{
            if(field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y],Static.DOWN)){
                field.moveDown()
                val stuckAndMovePhone = field.checkIfStuckAndNextMove(Static.DOWN)
                nextMovePhone=stuckAndMovePhone.nextMove
            }else{
                if(field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y],Static.DOWN_RIGHT)){
                    field.moveDownRight()
                    val stuckAndMovePhone = field.checkIfStuckAndNextMove(Static.DOWN_RIGHT)
                    nextMovePhone=stuckAndMovePhone.nextMove
                }else{
                    if(field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y],Static.LEFT)){
                        field.moveLeft()
                        val stuckAndMovePhone = field.checkIfStuckAndNextMove(Static.LEFT)
                        nextMovePhone=stuckAndMovePhone.nextMove
                    }else{
                        if(field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y],Static.RIGHT)){
                            field.moveRight()
                            val stuckAndMovePhone = field.checkIfStuckAndNextMove(Static.RIGHT)
                            nextMovePhone=stuckAndMovePhone.nextMove
                        }else{
                            if(field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y],Static.UP_LEFT)){
                                field.moveUpLeft()
                                val stuckAndMovePhone = field.checkIfStuckAndNextMove(Static.UP_LEFT)
                                nextMovePhone=stuckAndMovePhone.nextMove
                            }else{
                                if(field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y],Static.UP_RIGHT)){
                                    field.moveUpRight()
                                    val stuckAndMovePhone = field.checkIfStuckAndNextMove(Static.UP_RIGHT)
                                    nextMovePhone=stuckAndMovePhone.nextMove
                                }else{
                                    if(field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y],Static.UP)){
                                        field.moveUp()
                                        val stuckAndMovePhone = field.checkIfStuckAndNextMove(Static.UP)
                                        nextMovePhone=stuckAndMovePhone.nextMove
                                    }else{
                                        finish()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun moveDownPhone(ball:Point) {
        if(field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y],Static.DOWN)){
            field.moveDown()
            val stuckAndMovePhone = field.checkIfStuckAndNextMove(Static.DOWN)
            nextMovePhone=stuckAndMovePhone.nextMove
        }else{
            if(field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y],Static.DOWN_LEFT)){
                field.moveDownLeft()
                val stuckAndMovePhone = field.checkIfStuckAndNextMove(Static.DOWN_LEFT)
                nextMovePhone=stuckAndMovePhone.nextMove
            }else{
                if(field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y],Static.DOWN_RIGHT)){
                    field.moveDownRight()
                    val stuckAndMovePhone = field.checkIfStuckAndNextMove(Static.DOWN_RIGHT)
                    nextMovePhone=stuckAndMovePhone.nextMove
                }else{
                    if(field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y],Static.LEFT)){
                        field.moveLeft()
                        val stuckAndMovePhone = field.checkIfStuckAndNextMove(Static.LEFT)
                        nextMovePhone=stuckAndMovePhone.nextMove
                    }else{
                        if(field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y],Static.RIGHT)){
                            field.moveRight()
                            val stuckAndMovePhone = field.checkIfStuckAndNextMove(Static.RIGHT)
                            nextMovePhone=stuckAndMovePhone.nextMove
                        }else{
                            if(field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y],Static.UP_LEFT)){
                                field.moveUpLeft()
                                val stuckAndMovePhone = field.checkIfStuckAndNextMove(Static.UP_LEFT)
                                nextMovePhone=stuckAndMovePhone.nextMove
                            }else{
                                if(field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y],Static.UP_RIGHT)){
                                    field.moveUpRight()
                                    val stuckAndMovePhone = field.checkIfStuckAndNextMove(Static.UP_RIGHT)
                                    nextMovePhone=stuckAndMovePhone.nextMove
                                }else{
                                    if(field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y],Static.UP)){
                                        field.moveUp()
                                        val stuckAndMovePhone = field.checkIfStuckAndNextMove(Static.UP)
                                        nextMovePhone=stuckAndMovePhone.nextMove
                                    }else{
                                        finish()
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
        if(field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y],Static.DOWN_RIGHT)){
            field.moveDownRight()
            val stuckAndMovePhone = field.checkIfStuckAndNextMove(Static.DOWN_RIGHT)
            nextMovePhone=stuckAndMovePhone.nextMove
        }else{
            if(field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y],Static.DOWN)){
                field.moveDown()
                val stuckAndMovePhone = field.checkIfStuckAndNextMove(Static.DOWN)
                nextMovePhone=stuckAndMovePhone.nextMove
            }else{
                if(field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y],Static.DOWN_LEFT)){
                    field.moveDownLeft()
                    val stuckAndMovePhone = field.checkIfStuckAndNextMove(Static.DOWN_LEFT)
                    nextMovePhone=stuckAndMovePhone.nextMove
                }else{
                    if(field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y],Static.RIGHT)){
                        field.moveRight()
                        val stuckAndMovePhone = field.checkIfStuckAndNextMove(Static.RIGHT)
                        nextMovePhone=stuckAndMovePhone.nextMove
                    }else{
                        if(field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y],Static.LEFT)){
                            field.moveLeft()
                            val stuckAndMovePhone = field.checkIfStuckAndNextMove(Static.LEFT)
                            nextMovePhone=stuckAndMovePhone.nextMove
                        }else{
                            if(field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y],Static.UP_RIGHT)){
                                field.moveUpRight()
                                val stuckAndMovePhone = field.checkIfStuckAndNextMove(Static.UP_RIGHT)
                                nextMovePhone=stuckAndMovePhone.nextMove
                            }else{
                                if(field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y],Static.UP_LEFT)){
                                    field.moveUpLeft()
                                    val stuckAndMovePhone = field.checkIfStuckAndNextMove(Static.UP_LEFT)
                                    nextMovePhone=stuckAndMovePhone.nextMove
                                }else{
                                    if(field.checkIfMoveInDirectionIsAvailable(field.field[ball.x][ball.y],Static.UP)){
                                        field.moveUp()
                                        val stuckAndMovePhone = field.checkIfStuckAndNextMove(Static.UP)
                                        nextMovePhone=stuckAndMovePhone.nextMove
                                    }else{
                                        finish()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private fun displayBall() {

        for(i in 0..8){
            for(j in 0..12){
                if (field.field[i][j].ball){
                    ball_easy.setImageDrawable(BallDrawable(this,field.field[i][j], screenUnit.toDouble()))
                }
            }
        }


    }

    private fun generateField() {

        field.generate()

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
            field.moveUp()
            displayBall()
            updateMoves()
            updateButtons()
            val endGame = checkWin()
            if (endGame) {
                gameLoopHandler.removeCallbacksAndMessages(null)
                disableButtons()
            } else {
                checkNextMove(Static.UP)
            }
        }
        easyMoveUpRightButton.setOnClickListener {
            field.moveUpRight()
            displayBall()
            updateMoves()
            updateButtons()
                        val endGame = checkWin()
            if(endGame){
                gameLoopHandler.removeCallbacksAndMessages(null)
                disableButtons()
            }else {
            checkNextMove(Static.UP_RIGHT)
            }
        }

        easyMoveRightButton.setOnClickListener {
            field.moveRight()
            displayBall()
            updateMoves()
            updateButtons()
            val endGame = checkWin()
            if (endGame) {
                gameLoopHandler.removeCallbacksAndMessages(null)
                disableButtons()
            } else {
                checkNextMove(Static.RIGHT)
            }
        }
        easyMoveDownRightButton.setOnClickListener {
            field.moveDownRight()
            displayBall()
            updateMoves()
            updateButtons()
            val endGame = checkWin()
            if (endGame) {
                gameLoopHandler.removeCallbacksAndMessages(null)
                disableButtons()
            } else {
                checkNextMove(Static.DOWN_RIGHT)
            }
        }
        easyMoveDownButton.setOnClickListener {
            field.moveDown()
            displayBall()
            updateMoves()
            updateButtons()
            val endGame = checkWin()
            if (endGame) {
                gameLoopHandler.removeCallbacksAndMessages(null)
                disableButtons()
            } else {
                checkNextMove(Static.DOWN)
            }
        }
        easyMoveDownLeftButton.setOnClickListener {
            field.moveDownLeft()
            displayBall()
            updateMoves()
            updateButtons()
            val endGame = checkWin()
            if (endGame) {
                gameLoopHandler.removeCallbacksAndMessages(null)
                disableButtons()
            } else {
                checkNextMove(Static.DOWN_LEFT)
            }
        }
        easyMoveLeftButton.setOnClickListener {
            field.moveLeft()
            displayBall()
            updateMoves()
            updateButtons()
            val endGame = checkWin()
            if (endGame) {
                gameLoopHandler.removeCallbacksAndMessages(null)
                disableButtons()
            } else {
                checkNextMove(Static.LEFT)
            }
        }
        easyMoveUpLeftButton.setOnClickListener {
            field.moveUpLeft()
            displayBall()
            updateMoves()
            updateButtons()
            val endGame = checkWin()
            if (endGame) {
                gameLoopHandler.removeCallbacksAndMessages(null)
                disableButtons()
            } else {
                checkNextMove(Static.UP_LEFT)
            }
        }
    }

    private fun checkNextMove(direction:Int) {
        val nextMove = field.checkIfStuckAndNextMove(direction)
        if(nextMove.stuck){
            gameLoopHandler.removeCallbacksAndMessages(this)
            finish()
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
        if(field.field[ball.x][ball.y].moveUp!=null) {
            if (!field.field[ball.x][ball.y].moveUp!!) {
                easyMoveUpButton.visibility = View.VISIBLE
            }
        }
        if(field.field[ball.x][ball.y].moveUpRight!=null){
            if(!field.field[ball.x][ball.y].moveUpRight!!){
                easyMoveUpRightButton.visibility=View.VISIBLE
            }
        }
        if(field.field[ball.x][ball.y].moveRight!=null){
            if(!field.field[ball.x][ball.y].moveRight!!){
            easyMoveRightButton.visibility=View.VISIBLE
            }
        }
        if(field.field[ball.x][ball.y].moveDownRight!=null){
        if(!field.field[ball.x][ball.y].moveDownRight!!){
            easyMoveDownRightButton.visibility=View.VISIBLE
        }
        }
        if(field.field[ball.x][ball.y].moveDown!=null){
        if(!field.field[ball.x][ball.y].moveDown!!){
            easyMoveDownButton.visibility=View.VISIBLE
        }
        }
        if(field.field[ball.x][ball.y].moveDownLeft!=null){
        if(!field.field[ball.x][ball.y].moveDownLeft!!){
            easyMoveDownLeftButton.visibility=View.VISIBLE
        }
        }
        if(field.field[ball.x][ball.y].moveLeft!=null){
        if(!field.field[ball.x][ball.y].moveLeft!!){
            easyMoveLeftButton.visibility=View.VISIBLE
        }
        }
        if(field.field[ball.x][ball.y].moveUpLeft!=null){
        if(!field.field[ball.x][ball.y].moveUpLeft!!){
            easyMoveUpLeftButton.visibility=View.VISIBLE
        }
        }
    }

    private fun setDrawable() {
        field_easy.background = FieldEasyDrawable(this, screenUnit.toDouble())

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

    private fun updateUI(){
        displayBall()
        updateMoves()
        updateButtons()
    }
}

/*
todo different line colors
todo lost animation
todo win animation
todo user points
todo stuck animation
todo arrows drawable
todo back button
 */