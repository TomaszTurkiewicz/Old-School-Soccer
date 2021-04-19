package com.tt.oldschoolsoccer.fragments.multiPlayer

import android.graphics.Shader
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tt.oldschoolsoccer.R
import com.tt.oldschoolsoccer.classes.Functions
import com.tt.oldschoolsoccer.classes.LoggedInStatus
import com.tt.oldschoolsoccer.classes.UserRanking
import com.tt.oldschoolsoccer.drawable.ButtonDrawable
import com.tt.oldschoolsoccer.drawable.ButtonPressedDrawable
import com.tt.oldschoolsoccer.drawable.TileDrawable
import com.tt.oldschoolsoccer.fragments.ChooseGameTypeFragment
import com.tt.oldschoolsoccer.fragments.MainFragment
import com.tt.oldschoolsoccer.recyclerViews.MultiPlayerAllUserRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_multi_player_list.view.*
import kotlinx.android.synthetic.main.fragment_ranking.view.*


class MultiPlayerListFragment : Fragment() {

    private lateinit var rootView: View
    private var screenUnit = 0
    private var iconSize = 0
    private var buttonHeight = 0
    private var buttonWidth = 0
    private lateinit var allUserList: MutableList<UserRanking>
    private lateinit var recyclerView: RecyclerView
    private var loggedInStatus = LoggedInStatus()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screenUnit = Functions.readScreenUnit(requireContext())
        allUserList = mutableListOf()
        loggedInStatus = Functions.readLoggedStateFromSharedPreferences(requireContext())

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        rootView = inflater.inflate(R.layout.fragment_multi_player_list, container, false)

        makeUI()

        recyclerView = rootView.fragment_multi_player_list_recycler_view

        return rootView
    }

    private fun makeUI() {

        setBackgroundGrid()
        setSizes()
        setButtonsUI()
        makeConstraintLayout()
        prepareLists()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
       rootView.fragment_multi_player_list_back_button.setOnClickListener {
           activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ChooseGameTypeFragment()).commit()
       }
    }

    private fun prepareLists() {
        val dbRankingRef = Firebase.database.getReference("Ranking")
        dbRankingRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(user in snapshot.children){
                        val tUser = user.getValue(UserRanking::class.java)
                        allUserList.add(tUser!!)
                    }

                    allUserList = allUserList.filter { element ->
                        element.playWithPeople && element.id != loggedInStatus.userid
                    } as MutableList<UserRanking>

                    var sorted = false

                    while (!sorted) {
                        sorted = true
                        for (i in 0 until allUserList.size - 1) {
                            if (hasToBeSorted(i)) {
                                val userTmp = allUserList[i]
                                allUserList[i] = allUserList[i + 1]
                                allUserList[i + 1] = userTmp
                                sorted = false
                            }
                        }
                    }

                    initRecyclerViewForAllUsers()



                }
            }

            override fun onCancelled(error: DatabaseError) {
                //do nothing
            }
        })

        //todo prepare history list


    }

    private fun initRecyclerViewForAllUsers() {
        val adapter = MultiPlayerAllUserRecyclerViewAdapter(requireContext(),allUserList,screenUnit)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun hasToBeSorted(i: Int): Boolean {
        return when {
            allUserList[i].multiGame<allUserList[i+1].multiGame -> {
                true
            }
            allUserList[i].multiGame==allUserList[i+1].multiGame -> {
                allUserList[i].multiNoOfGames<allUserList[i+1].multiNoOfGames
            }
            else -> {
                false
            }
        }

    }

    private fun makeConstraintLayout() {
        val set = ConstraintSet()
        set.clone(rootView.fragment_multi_player_list_layout)

        set.connect(rootView.fragment_multi_player_list_back_button.id, ConstraintSet.TOP,rootView.fragment_multi_player_list_layout.id, ConstraintSet.TOP,screenUnit)
        set.connect(rootView.fragment_multi_player_list_back_button.id, ConstraintSet.LEFT,rootView.fragment_multi_player_list_layout.id, ConstraintSet.LEFT,16*screenUnit)

        set.connect(rootView.fragment_multi_player_list_all_button.id, ConstraintSet.TOP,rootView.fragment_multi_player_list_layout.id, ConstraintSet.TOP,3*screenUnit)
        set.connect(rootView.fragment_multi_player_list_all_button.id, ConstraintSet.LEFT,rootView.fragment_multi_player_list_layout.id, ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_multi_player_list_history_button.id, ConstraintSet.TOP,rootView.fragment_multi_player_list_all_button.id, ConstraintSet.TOP,0)
        set.connect(rootView.fragment_multi_player_list_history_button.id, ConstraintSet.LEFT,rootView.fragment_multi_player_list_all_button.id, ConstraintSet.RIGHT,0)


        set.connect(rootView.fragment_multi_player_list_recycler_view.id, ConstraintSet.TOP,rootView.fragment_multi_player_list_all_button.id, ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_multi_player_list_recycler_view.id, ConstraintSet.LEFT,rootView.fragment_multi_player_list_layout.id, ConstraintSet.LEFT,0)
        set.connect(rootView.fragment_multi_player_list_recycler_view.id, ConstraintSet.RIGHT,rootView.fragment_multi_player_list_layout.id, ConstraintSet.RIGHT,0)
        set.connect(rootView.fragment_multi_player_list_recycler_view.id, ConstraintSet.BOTTOM,rootView.fragment_multi_player_list_layout.id, ConstraintSet.BOTTOM,0)

        set.applyTo(rootView.fragment_multi_player_list_layout)
    }

    private fun setButtonsUI() {
        rootView.fragment_multi_player_list_back_button.layoutParams = ConstraintLayout.LayoutParams(iconSize,iconSize)
        rootView.fragment_multi_player_list_back_button.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.close))

        rootView.fragment_multi_player_list_all_button.layoutParams = ConstraintLayout.LayoutParams(buttonWidth,buttonHeight)
        rootView.fragment_multi_player_list_history_button.layoutParams = ConstraintLayout.LayoutParams(buttonWidth,buttonHeight)

        rootView.fragment_multi_player_list_all_button.setTextSize(TypedValue.COMPLEX_UNIT_PX,(0.9*screenUnit).toFloat())
        rootView.fragment_multi_player_list_history_button.setTextSize(TypedValue.COMPLEX_UNIT_PX,(0.9*screenUnit).toFloat())

        displayPressedButton(rootView.fragment_multi_player_list_all_button)
    }

    private fun displayPressedButton(view: View) {
        when(view){
            rootView.fragment_multi_player_list_all_button -> allPressed()
            rootView.fragment_multi_player_list_history_button -> historyPressed()
        }

    }

    private fun historyPressed() {
        rootView.fragment_multi_player_list_history_button.background = ButtonPressedDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenUnit.toDouble())
        rootView.fragment_multi_player_list_all_button.background = ButtonDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenUnit.toDouble())
    }

    private fun allPressed() {
        rootView.fragment_multi_player_list_history_button.background = ButtonDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenUnit.toDouble())
        rootView.fragment_multi_player_list_all_button.background = ButtonPressedDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenUnit.toDouble())
    }

    private fun setSizes() {
        iconSize = 2*screenUnit
        buttonHeight = 2*screenUnit
        buttonWidth = 4*screenUnit
    }

    private fun setBackgroundGrid() {
        rootView.fragment_multi_player_list_layout.background = TileDrawable((ContextCompat.getDrawable(requireContext(),R.drawable.background)!!),
                Shader.TileMode.REPEAT,screenUnit)
    }


}