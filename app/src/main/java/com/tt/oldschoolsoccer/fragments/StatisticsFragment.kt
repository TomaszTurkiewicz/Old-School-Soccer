package com.tt.oldschoolsoccer.fragments

import android.graphics.Shader
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tt.oldschoolsoccer.R
import com.tt.oldschoolsoccer.classes.FragmentCoroutine
import com.tt.oldschoolsoccer.classes.Functions
import com.tt.oldschoolsoccer.classes.LoggedInStatus
import com.tt.oldschoolsoccer.classes.User
import com.tt.oldschoolsoccer.database.UserDBDatabase
import com.tt.oldschoolsoccer.drawable.TileDrawable
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.fragment_statistics.view.*
import kotlinx.coroutines.launch

class StatisticsFragment : FragmentCoroutine() {

    var rootView:View? = null
    private var screenUnit:Int=0
    private var loggedInStatus = LoggedInStatus()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenUnit = Functions.readScreenUnit(requireContext())
        loggedInStatus = Functions.readLoggedStateFromSharedPreferences(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_statistics, container, false)

        makeUI()







        return rootView
    }

    override fun onResume() {
        super.onResume()
        launch {
            requireContext()?.let {
                val list = UserDBDatabase(it).getUserDBDao().getAllUsers()

                rootView!!.fragment_statistics_records_in_db_real.text = list.size.toString()

                val user = User().userFromDB(UserDBDatabase(it).getUserDBDao().getUser(loggedInStatus.userid))

                rootView!!.fragment_statistics_user_id_user.text = user.id
                rootView!!.fragment_statistics_user_name_user.text = user.userName
            }
        }
    }

    private fun makeUI() {
        setBackgroundGrid()
        setSizes()
        setButtonsUI()
        makeConstraintLayout()

    }

    private fun makeConstraintLayout() {
        val set = ConstraintSet()
        set.clone(rootView!!.fragment_statistics_layout)

        set.connect(rootView!!.fragment_statistics_user_id_default.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_layout.id,ConstraintSet.TOP,screenUnit)
        set.connect(rootView!!.fragment_statistics_user_id_default.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView!!.fragment_statistics_user_id_user.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_layout.id,ConstraintSet.TOP,screenUnit)
        set.connect(rootView!!.fragment_statistics_user_id_user.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_user_id_default.id,ConstraintSet.RIGHT,screenUnit)

        set.connect(rootView!!.fragment_statistics_user_name_default.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_id_default.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView!!.fragment_statistics_user_name_default.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView!!.fragment_statistics_user_name_user.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_name_default.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_statistics_user_name_user.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_user_name_default.id,ConstraintSet.RIGHT,screenUnit)


        set.connect(rootView!!.fragment_statistics_user_easy_number_of_games_default.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_name_default.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView!!.fragment_statistics_user_easy_number_of_games_default.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView!!.fragment_statistics_user_easy_number_of_games_user.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_easy_number_of_games_default.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_statistics_user_easy_number_of_games_user.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_user_easy_number_of_games_default.id,ConstraintSet.RIGHT,screenUnit)


        set.connect(rootView!!.fragment_statistics_user_easy_win_default.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_easy_number_of_games_default.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView!!.fragment_statistics_user_easy_win_default.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView!!.fragment_statistics_user_easy_win_user.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_easy_win_default.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_statistics_user_easy_win_user.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_user_easy_win_default.id,ConstraintSet.RIGHT,screenUnit)

        set.connect(rootView!!.fragment_statistics_user_easy_lose_default.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_easy_win_default.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView!!.fragment_statistics_user_easy_lose_default.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView!!.fragment_statistics_user_easy_lose_user.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_easy_lose_default.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_statistics_user_easy_lose_user.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_user_easy_lose_default.id,ConstraintSet.RIGHT,screenUnit)


        set.connect(rootView!!.fragment_statistics_user_easy_tie_default.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_easy_lose_default.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView!!.fragment_statistics_user_easy_tie_default.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView!!.fragment_statistics_user_easy_tie_user.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_easy_tie_default.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_statistics_user_easy_tie_user.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_user_easy_tie_default.id,ConstraintSet.RIGHT,screenUnit)


        set.connect(rootView!!.fragment_statistics_user_normal_number_of_games_default.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_easy_tie_default.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView!!.fragment_statistics_user_normal_number_of_games_default.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView!!.fragment_statistics_user_normal_number_of_games_user.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_normal_number_of_games_default.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_statistics_user_normal_number_of_games_user.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_user_normal_number_of_games_default.id,ConstraintSet.RIGHT,screenUnit)


        set.connect(rootView!!.fragment_statistics_user_normal_win_default.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_normal_number_of_games_default.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView!!.fragment_statistics_user_normal_win_default.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView!!.fragment_statistics_user_normal_win_user.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_normal_win_default.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_statistics_user_normal_win_user.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_user_normal_win_default.id,ConstraintSet.RIGHT,screenUnit)


        set.connect(rootView!!.fragment_statistics_user_normal_lose_default.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_normal_win_default.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView!!.fragment_statistics_user_normal_lose_default.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView!!.fragment_statistics_user_normal_lose_user.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_normal_lose_default.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_statistics_user_normal_lose_user.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_user_normal_lose_default.id,ConstraintSet.RIGHT,screenUnit)


        set.connect(rootView!!.fragment_statistics_user_normal_tie_default.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_normal_lose_default.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView!!.fragment_statistics_user_normal_tie_default.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView!!.fragment_statistics_user_normal_tie_user.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_normal_tie_default.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_statistics_user_normal_tie_user.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_user_normal_tie_default.id,ConstraintSet.RIGHT,screenUnit)


        set.connect(rootView!!.fragment_statistics_user_hard_number_of_games_default.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_normal_tie_default.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView!!.fragment_statistics_user_hard_number_of_games_default.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView!!.fragment_statistics_user_hard_number_of_games_user.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_hard_number_of_games_default.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_statistics_user_hard_number_of_games_user.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_user_hard_number_of_games_default.id,ConstraintSet.RIGHT,screenUnit)


        set.connect(rootView!!.fragment_statistics_user_hard_win_default.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_hard_number_of_games_default.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView!!.fragment_statistics_user_hard_win_default.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView!!.fragment_statistics_user_hard_win_user.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_hard_win_default.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_statistics_user_hard_win_user.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_user_hard_win_default.id,ConstraintSet.RIGHT,screenUnit)


        set.connect(rootView!!.fragment_statistics_user_hard_lose_default.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_hard_win_default.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView!!.fragment_statistics_user_hard_lose_default.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView!!.fragment_statistics_user_hard_lose_user.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_hard_lose_default.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_statistics_user_hard_lose_user.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_user_hard_lose_default.id,ConstraintSet.RIGHT,screenUnit)


        set.connect(rootView!!.fragment_statistics_user_hard_tie_default.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_hard_lose_default.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView!!.fragment_statistics_user_hard_tie_default.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView!!.fragment_statistics_user_hard_tie_user.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_hard_tie_default.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_statistics_user_hard_tie_user.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_user_hard_tie_default.id,ConstraintSet.RIGHT,screenUnit)


        set.connect(rootView!!.fragment_statistics_user_multi_number_of_games_default.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_hard_tie_default.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView!!.fragment_statistics_user_multi_number_of_games_default.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView!!.fragment_statistics_user_multi_number_of_games_user.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_multi_number_of_games_default.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_statistics_user_multi_number_of_games_user.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_user_multi_number_of_games_default.id,ConstraintSet.RIGHT,screenUnit)


        set.connect(rootView!!.fragment_statistics_user_multi_win_default.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_multi_number_of_games_default.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView!!.fragment_statistics_user_multi_win_default.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView!!.fragment_statistics_user_multi_win_user.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_multi_win_default.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_statistics_user_multi_win_user.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_user_multi_win_default.id,ConstraintSet.RIGHT,screenUnit)


        set.connect(rootView!!.fragment_statistics_user_multi_lose_default.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_multi_win_default.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView!!.fragment_statistics_user_multi_lose_default.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView!!.fragment_statistics_user_multi_lose_user.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_multi_lose_default.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_statistics_user_multi_lose_user.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_user_multi_lose_default.id,ConstraintSet.RIGHT,screenUnit)


        set.connect(rootView!!.fragment_statistics_user_multi_tie_default.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_multi_lose_default.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView!!.fragment_statistics_user_multi_tie_default.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView!!.fragment_statistics_user_multi_tie_user.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_multi_tie_default.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_statistics_user_multi_tie_user.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_user_multi_tie_default.id,ConstraintSet.RIGHT,screenUnit)


        set.connect(rootView!!.fragment_statistics_records_in_db_default.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_user_multi_tie_default.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView!!.fragment_statistics_records_in_db_default.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView!!.fragment_statistics_records_in_db_real.id,
                ConstraintSet.TOP,rootView!!.fragment_statistics_records_in_db_default.id,ConstraintSet.TOP,0)
        set.connect(rootView!!.fragment_statistics_records_in_db_real.id,
                ConstraintSet.LEFT,rootView!!.fragment_statistics_records_in_db_default.id,ConstraintSet.RIGHT,screenUnit)

        set.applyTo(rootView!!.fragment_statistics_layout)
    }

    private fun setButtonsUI() {
        rootView!!.fragment_statistics_user_id_default.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_user_id_user.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_user_easy_number_of_games_default.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_user_easy_number_of_games_user.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_user_easy_win_default.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_user_easy_win_user.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_user_easy_lose_default.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_user_easy_lose_user.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_user_easy_tie_default.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_user_easy_tie_user.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)

        rootView!!.fragment_statistics_user_normal_number_of_games_default.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_user_normal_number_of_games_user.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_user_normal_win_default.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_user_normal_win_user.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_user_normal_lose_default.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_user_normal_lose_user.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_user_normal_tie_default.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_user_normal_tie_user.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)

        rootView!!.fragment_statistics_user_hard_number_of_games_default.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_user_hard_number_of_games_user.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_user_hard_win_default.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_user_hard_win_user.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_user_hard_lose_default.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_user_hard_lose_user.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_user_hard_tie_default.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_user_hard_tie_user.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)

        rootView!!.fragment_statistics_user_multi_number_of_games_default.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_user_multi_number_of_games_user.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_user_multi_win_default.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_user_multi_win_user.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_user_multi_lose_default.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_user_multi_lose_user.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_user_multi_tie_default.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_user_multi_tie_user.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)

        rootView!!.fragment_statistics_records_in_db_default.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)
        rootView!!.fragment_statistics_records_in_db_real.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,screenUnit)





    }

    private fun setSizes() {
        // TODO maybe later
    }

    private fun setBackgroundGrid() {
        rootView!!.fragment_statistics_layout.background = TileDrawable((ContextCompat.getDrawable(requireContext(),R.drawable.background)!!),
            Shader.TileMode.REPEAT,screenUnit)

    }
}