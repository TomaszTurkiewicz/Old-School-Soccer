package com.tt.oldschoolsoccer.fragments.singlePlayer

import android.graphics.Shader
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tt.oldschoolsoccer.R
import com.tt.oldschoolsoccer.classes.*
import com.tt.oldschoolsoccer.drawable.*
import com.tt.oldschoolsoccer.fragments.MainFragment
import kotlinx.android.synthetic.main.fragment_single_player_hard_game.view.*
import kotlinx.android.synthetic.main.fragment_single_player_normal_game.view.*
import kotlinx.android.synthetic.main.fragment_single_player_normal_game.view.fragment_single_player_normal_game_layout


class SinglePlayerHardGameFragment : FragmentCoroutine() {
    private var screenUnit:Int = 0
    private var loggedInStatus = LoggedInStatus()
    private lateinit var rootView:View
    private var field = GameField()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenUnit = Functions.readScreenUnit(requireContext())
        loggedInStatus = Functions.readLoggedStateFromSharedPreferences(requireContext())

        createField()

    }

    private fun createField() {
        field.generate(Static.HARD)




    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_single_player_hard_game, container, false)

        makeUI()

        return rootView
    }

    override fun onResume() {
        super.onResume()
        updateField()
        //todo !!!
    }

    override fun onPause() {
        super.onPause()

        //todo
    }

    private fun startGame():Runnable = Runnable {
        //todo!!!
    }

    private fun gameLoop():Runnable = Runnable {
        //todo!!!
    }

    private fun phoneTurn(){
        //todo!!!
    }

    private fun phoneMoveRunnable(size:Int,counter:Int,bestMove:ArrayList<Int>):Runnable = Runnable {
        //todo!!!
    }

    private fun makeMovePhone(move: Int) {
        //todo!!!
    }

    private fun phoneMove(direction: Int, move:PointsAfterMove){
        //todo!!!
    }

    private fun checkBestMove(field: GameField):ArrayList<Int>? {
        //todo!!!
        return null
    }

    private fun updateButtons() {
        //todo!!!
    }

    private fun updateField() {
        updateMoves()
        displayBall()
    }

    private fun updateMoves() {
        view?.let {
            rootView.fragment_single_player_hard_game_field.setImageDrawable(MovesHardDrawable(requireContext(),field, screenUnit.toDouble()))
        }
    }

    private fun displayBall() {
        view?.let {
            val ball = field.findBall()
            rootView.fragment_single_player_hard_game_ball.setImageDrawable(BallDrawable(requireContext(),field.field[ball.x][ball.y], screenUnit.toDouble()))
        }
    }

    private fun updateUserCounters(numbersOfGames:Int,win:Int,tie:Int,lose:Int) {
        //todo!!!
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
        rootView.fragment_single_player_hard_game_move_up_btn.visibility=View.GONE
        rootView.fragment_single_player_hard_game_move_up_right_btn.visibility=View.GONE
        rootView.fragment_single_player_hard_game_move_right_btn.visibility=View.GONE
        rootView.fragment_single_player_hard_game_move_down_right_btn.visibility=View.GONE
        rootView.fragment_single_player_hard_game_move_down_btn.visibility=View.GONE
        rootView.fragment_single_player_hard_game_move_down_left_btn.visibility=View.GONE
        rootView.fragment_single_player_hard_game_move_left_btn.visibility=View.GONE
        rootView.fragment_single_player_hard_game_move_up_left_btn.visibility=View.GONE
    }

    private fun setOnClickListeners() {
        rootView.fragment_single_player_hard_game_move_up_btn.setOnClickListener {
            afterPress(Static.UP,field.moveUp(true))
        }

        rootView.fragment_single_player_hard_game_move_up_right_btn.setOnClickListener {
            afterPress(Static.UP_RIGHT,field.moveUpRight(true))
        }

        rootView.fragment_single_player_hard_game_move_right_btn.setOnClickListener {
            afterPress(Static.RIGHT,field.moveRight(true))
        }

        rootView.fragment_single_player_hard_game_move_down_right_btn.setOnClickListener {
            afterPress(Static.DOWN_RIGHT,field.moveDownRight(true))
        }

        rootView.fragment_single_player_hard_game_move_down_btn.setOnClickListener {
            afterPress(Static.DOWN,field.moveDown(true))
        }

        rootView.fragment_single_player_hard_game_move_down_left_btn.setOnClickListener {
            afterPress(Static.DOWN_LEFT,field.moveDownLeft(true))
        }

        rootView.fragment_single_player_hard_game_move_left_btn.setOnClickListener {
            afterPress(Static.LEFT,field.moveLeft(true))
        }

        rootView.fragment_single_player_hard_game_move_up_left_btn.setOnClickListener {
            afterPress(Static.UP_LEFT,field.moveUpLeft(true))
        }

        rootView.fragment_single_player_hard_back_button.setOnClickListener {
            goToMainMenu()
        }

    }

    private fun goToMainMenu() {
        activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MainFragment()).commit()
    }

    private fun afterPress(direction: Int, move: PointsAfterMove){
        //TODO
    }

    private fun setDrawable() {
        rootView.fragment_single_player_hard_game_field.background = FieldHardDrawable(requireContext(), screenUnit.toDouble())
        rootView.fragment_single_player_hard_game_move_down_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.down)
        rootView.fragment_single_player_hard_game_move_down_right_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.down_right)
        rootView.fragment_single_player_hard_game_move_right_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.right)
        rootView.fragment_single_player_hard_game_move_up_right_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.up_right)
        rootView.fragment_single_player_hard_game_move_up_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.up)
        rootView.fragment_single_player_hard_game_move_up_left_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.up_left)
        rootView.fragment_single_player_hard_game_move_left_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.left)
        rootView.fragment_single_player_hard_game_move_down_left_btn.background = ContextCompat.getDrawable(requireContext(),R.drawable.down_left)

        rootView.fragment_single_player_hard_back_button.layoutParams = ConstraintLayout.LayoutParams(4*screenUnit,2*screenUnit)
        rootView.fragment_single_player_hard_back_button.background = ButtonDrawable(requireContext(), (4*screenUnit).toDouble(), (2*screenUnit).toDouble(), screenUnit.toDouble())
        rootView.fragment_single_player_hard_back_button.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
    }

    private fun setConstraintLayout() {
        val set = ConstraintSet()
        set.clone(rootView.fragment_single_player_hard_game_layout)

        set.connect(rootView.fragment_single_player_hard_game_field.id, ConstraintSet.TOP,rootView.fragment_single_player_hard_game_layout.id, ConstraintSet.TOP,screenUnit)
        set.connect(rootView.fragment_single_player_hard_game_field.id, ConstraintSet.LEFT,rootView.fragment_single_player_hard_game_layout.id, ConstraintSet.LEFT,screenUnit)

        set.connect(rootView.fragment_single_player_hard_game_ball.id, ConstraintSet.TOP,rootView.fragment_single_player_hard_game_layout.id, ConstraintSet.TOP,screenUnit)
        set.connect(rootView.fragment_single_player_hard_game_ball.id, ConstraintSet.LEFT,rootView.fragment_single_player_hard_game_layout.id, ConstraintSet.LEFT,screenUnit)

        set.connect(rootView.fragment_single_player_hard_game_middle.id, ConstraintSet.TOP,rootView.fragment_single_player_hard_game_layout.id, ConstraintSet.TOP,26*screenUnit)
        set.connect(rootView.fragment_single_player_hard_game_middle.id, ConstraintSet.LEFT,rootView.fragment_single_player_hard_game_layout.id, ConstraintSet.LEFT,9*screenUnit)

        set.connect(rootView.fragment_single_player_hard_game_move_up_btn.id, ConstraintSet.BOTTOM,rootView.fragment_single_player_hard_game_middle.id, ConstraintSet.TOP,1*screenUnit)
        set.connect(rootView.fragment_single_player_hard_game_move_up_btn.id, ConstraintSet.LEFT,rootView.fragment_single_player_hard_game_middle.id, ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_single_player_hard_game_move_right_btn.id, ConstraintSet.BOTTOM,rootView.fragment_single_player_hard_game_middle.id, ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_single_player_hard_game_move_right_btn.id, ConstraintSet.LEFT,rootView.fragment_single_player_hard_game_middle.id, ConstraintSet.RIGHT,1*screenUnit)

        set.connect(rootView.fragment_single_player_hard_game_move_down_btn.id, ConstraintSet.TOP,rootView.fragment_single_player_hard_game_middle.id, ConstraintSet.BOTTOM,1*screenUnit)
        set.connect(rootView.fragment_single_player_hard_game_move_down_btn.id, ConstraintSet.LEFT,rootView.fragment_single_player_hard_game_middle.id, ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_single_player_hard_game_move_left_btn.id, ConstraintSet.BOTTOM,rootView.fragment_single_player_hard_game_middle.id, ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_single_player_hard_game_move_left_btn.id, ConstraintSet.RIGHT,rootView.fragment_single_player_hard_game_middle.id, ConstraintSet.LEFT,1*screenUnit)

        set.connect(rootView.fragment_single_player_hard_game_move_up_right_btn.id, ConstraintSet.BOTTOM,rootView.fragment_single_player_hard_game_middle.id, ConstraintSet.TOP,1*screenUnit)
        set.connect(rootView.fragment_single_player_hard_game_move_up_right_btn.id, ConstraintSet.LEFT,rootView.fragment_single_player_hard_game_middle.id, ConstraintSet.RIGHT,1*screenUnit)

        set.connect(rootView.fragment_single_player_hard_game_move_down_right_btn.id, ConstraintSet.TOP,rootView.fragment_single_player_hard_game_middle.id, ConstraintSet.BOTTOM,1*screenUnit)
        set.connect(rootView.fragment_single_player_hard_game_move_down_right_btn.id, ConstraintSet.LEFT,rootView.fragment_single_player_hard_game_middle.id, ConstraintSet.RIGHT,1*screenUnit)

        set.connect(rootView.fragment_single_player_hard_game_move_down_left_btn.id, ConstraintSet.TOP,rootView.fragment_single_player_hard_game_middle.id, ConstraintSet.BOTTOM,1*screenUnit)
        set.connect(rootView.fragment_single_player_hard_game_move_down_left_btn.id, ConstraintSet.RIGHT,rootView.fragment_single_player_hard_game_middle.id, ConstraintSet.LEFT,1*screenUnit)

        set.connect(rootView.fragment_single_player_hard_game_move_up_left_btn.id, ConstraintSet.BOTTOM,rootView.fragment_single_player_hard_game_middle.id, ConstraintSet.TOP,1*screenUnit)
        set.connect(rootView.fragment_single_player_hard_game_move_up_left_btn.id, ConstraintSet.RIGHT,rootView.fragment_single_player_hard_game_middle.id, ConstraintSet.LEFT,1*screenUnit)

        set.connect(rootView.fragment_single_player_hard_back_button.id, ConstraintSet.TOP,rootView.fragment_single_player_hard_game_layout.id, ConstraintSet.TOP,2*screenUnit)
        set.connect(rootView.fragment_single_player_hard_back_button.id, ConstraintSet.LEFT,rootView.fragment_single_player_hard_game_layout.id, ConstraintSet.LEFT,15*screenUnit)

        set.applyTo(rootView.fragment_single_player_hard_game_layout)

    }

    private fun setViewSizes() {
        rootView.fragment_single_player_hard_game_field.layoutParams = ConstraintLayout.LayoutParams(14*screenUnit,22*screenUnit)
        rootView.fragment_single_player_hard_game_ball.layoutParams = ConstraintLayout.LayoutParams(14*screenUnit,22*screenUnit)
        rootView.fragment_single_player_hard_game_move_up_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_single_player_hard_game_move_up_right_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_single_player_hard_game_move_right_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_single_player_hard_game_move_down_right_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_single_player_hard_game_move_down_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_single_player_hard_game_move_down_left_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_single_player_hard_game_move_left_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_single_player_hard_game_move_up_left_btn.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_single_player_hard_game_middle.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_single_player_hard_back_button.layoutParams = ConstraintLayout.LayoutParams(4*screenUnit,2*screenUnit)

    }

    private fun makeBackgroundGrid() {
        rootView.fragment_single_player_hard_game_layout.background = TileDrawable((ContextCompat.getDrawable(requireContext(), R.drawable.background)!!),
            Shader.TileMode.REPEAT,screenUnit)

    }


}