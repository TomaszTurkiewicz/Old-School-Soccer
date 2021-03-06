package com.tt.oldschoolsoccer.fragments

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tt.oldschoolsoccer.R
import com.tt.oldschoolsoccer.classes.*
import com.tt.oldschoolsoccer.database.UserDB
import com.tt.oldschoolsoccer.database.UserDBDatabase
import com.tt.oldschoolsoccer.drawable.ButtonDrawable
import com.tt.oldschoolsoccer.drawable.ButtonPressedDrawable
import com.tt.oldschoolsoccer.drawable.TileDrawable
import com.tt.oldschoolsoccer.recyclerViews.RankingRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_choose_game_type.view.*
import kotlinx.android.synthetic.main.fragment_ranking.view.*
import kotlinx.coroutines.launch


class RankingFragment : FragmentCoroutine() {

    private var screenSize = ScreenSize()
    private var loggedInStatus = LoggedInStatus()
    private lateinit var rootView: View
    private var iconSize = 0
    private var buttonHeight = 0
    private var buttonWidth = 0
    private lateinit var userList: MutableList<UserRanking>
    private lateinit var recyclerView:RecyclerView
    private var sortType:Int = Static.TOTAL_SORTING


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenSize = Functions.readScreenSize(requireContext())
        loggedInStatus = Functions.readLoggedStateFromSharedPreferences(requireContext())
        userList = mutableListOf()


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_ranking, container, false)

        makeUI()

        recyclerView = rootView.fragment_ranking_recycler_view

        return rootView
    }

    private fun makeUI() {

        setBackgroundGrid()
        setSizes()
        setButtonsUI()
        makeConstraintLayout()
        prepareRecyclerView()
        setOnClickListeners()
    }

    private fun prepareRecyclerView() {
        val dbRankingRef = Firebase.database.getReference("Ranking")
        dbRankingRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(user in snapshot.children){
                        val tUser = user.getValue(UserRanking::class.java)
                        userList.add(tUser!!)
                    }
                    checkLoggedInUser()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //do nothing
            }
        })

    }

    private fun checkLoggedInUser() {
        if(loggedInStatus.loggedIn){
            val userInList = findUserInList(userList)
            if(userInList){
                sortAndDisplay()
            }else{
                addUser()
            }
        }else{
            sortAndDisplay()
        }

    }

    private fun addUser() {
        launch {
            requireContext().let {
                val userDB = UserDBDatabase(it).getUserDBDao().getUser(loggedInStatus.userid)
                val userRanking = UserRanking().createUserRankingFromDB(userDB)
                userList.add(userRanking)
                sortAndDisplay()
            }
        }

    }

    private fun findUserInList(userList: MutableList<UserRanking>): Boolean {
        for(i in userList.indices){
            if(userList[i].id.equals(loggedInStatus.userid))
            return true
        }
        return false
    }

    private fun sortAndDisplay() {
        var sorted = false

        while (!sorted) {
            sorted = true
            for (i in 0 until userList.size - 1) {
                if (hasToBeSorted(i)) {
                    val userTmp = userList[i]
                    userList[i] = userList[i + 1]
                    userList[i + 1] = userTmp
                    sorted = false
                }
            }
        }

        initRecyclerView()

    }

    private fun hasToBeSorted(i: Int): Boolean {
        var x = false
        when(sortType){
            Static.TOTAL_SORTING -> x = totalSort(i)
            Static.MULTI_SORTING -> x = multiSort(i)
            Static.HARD_SORTING -> x = hardSort(i)
            Static.NORMAL_SORTING -> x = normalSort(i)
            Static.EASY_SORTING -> x = easySort(i)
        }
        return x
    }

    private fun easySort(i: Int): Boolean {
        return when {
            userList[i].easyGame<userList[i+1].easyGame -> {
                true
            }
            userList[i].easyGame==userList[i+1].easyGame -> {
                userList[i].easyNoOfGames<userList[i+1].easyNoOfGames
            }
            else -> {
                false
            }
        }
    }

    private fun normalSort(i: Int): Boolean {
        return when {
            userList[i].normalGame<userList[i+1].normalGame -> {
                true
            }
            userList[i].normalGame==userList[i+1].normalGame -> {
                userList[i].normalNoOfGames<userList[i+1].normalNoOfGames
            }
            else -> {
                false
            }
        }
    }

    private fun hardSort(i: Int): Boolean {
        return when {
            userList[i].hardGame<userList[i+1].hardGame -> {
                true
            }
            userList[i].hardGame==userList[i+1].hardGame -> {
                userList[i].hardNoOfGames<userList[i+1].hardNoOfGames
            }
            else -> {
                false
            }
        }
    }

    private fun multiSort(i: Int): Boolean {
        return when {
            userList[i].multiGame<userList[i+1].multiGame -> {
                true
            }
            userList[i].multiGame==userList[i+1].multiGame -> {
                userList[i].multiNoOfGames<userList[i+1].multiNoOfGames
            }
            else -> {
                false
            }
        }

    }

    private fun totalSort(i: Int): Boolean {
        return when {
            userList[i].totalScore<userList[i+1].totalScore -> {
                true
            }
            userList[i].totalScore==userList[i+1].totalScore -> {
                userList[i].multiNoOfGames+userList[i].hardNoOfGames+userList[i].normalNoOfGames+userList[i].easyNoOfGames<userList[i+1].multiNoOfGames+userList[i+1].hardNoOfGames+userList[i+1].normalNoOfGames+userList[i+1].easyNoOfGames
            }
            else -> {
                false
            }
        }

    }

    private fun initRecyclerView() {
        val adapter = RankingRecyclerViewAdapter(requireContext(),userList,screenSize.screenUnit,loggedInStatus.userid,sortType)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun setOnClickListeners() {
        rootView.fragment_ranking_back_button.setOnClickListener {
            goToMainMenu()
        }

        buttonsClickListeners()

    }

    private fun buttonsClickListeners() {
        rootView.fragment_ranking_total_button.setOnClickListener {
            sortType = Static.TOTAL_SORTING
            displayPressedButton(rootView.fragment_ranking_total_button)
            sortAndDisplay()
        }

        rootView.fragment_ranking_multi_button.setOnClickListener {
            sortType = Static.MULTI_SORTING
            displayPressedButton(rootView.fragment_ranking_multi_button)
            sortAndDisplay()
        }

        rootView.fragment_ranking_hard_button.setOnClickListener {
            sortType = Static.HARD_SORTING
            displayPressedButton(rootView.fragment_ranking_hard_button)
            sortAndDisplay()
        }

        rootView.fragment_ranking_normal_button.setOnClickListener {
            sortType = Static.NORMAL_SORTING
            displayPressedButton(rootView.fragment_ranking_normal_button)
            sortAndDisplay()
        }

        rootView.fragment_ranking_easy_button.setOnClickListener {
            sortType = Static.EASY_SORTING
            displayPressedButton(rootView.fragment_ranking_easy_button)
            sortAndDisplay()
        }
    }

    private fun goToMainMenu() {
        activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MainFragment()).commit()

    }

    private fun makeConstraintLayout() {
        val set = ConstraintSet()
        set.clone(rootView.fragment_ranking_layout)

        set.connect(rootView.fragment_ranking_back_button.id,ConstraintSet.TOP,rootView.fragment_ranking_layout.id,ConstraintSet.TOP,screenSize.screenUnit)
        set.connect(rootView.fragment_ranking_back_button.id,ConstraintSet.LEFT,rootView.fragment_ranking_layout.id,ConstraintSet.LEFT,16*screenSize.screenUnit)

        set.connect(rootView.fragment_ranking_total_button.id,ConstraintSet.TOP,rootView.fragment_ranking_layout.id,ConstraintSet.TOP,3*screenSize.screenUnit)
        set.connect(rootView.fragment_ranking_total_button.id,ConstraintSet.LEFT,rootView.fragment_ranking_layout.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_ranking_multi_button.id,ConstraintSet.TOP,rootView.fragment_ranking_total_button.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_ranking_multi_button.id,ConstraintSet.LEFT,rootView.fragment_ranking_total_button.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_ranking_hard_button.id,ConstraintSet.TOP,rootView.fragment_ranking_multi_button.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_ranking_hard_button.id,ConstraintSet.LEFT,rootView.fragment_ranking_multi_button.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_ranking_normal_button.id,ConstraintSet.TOP,rootView.fragment_ranking_hard_button.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_ranking_normal_button.id,ConstraintSet.LEFT,rootView.fragment_ranking_hard_button.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_ranking_easy_button.id,ConstraintSet.TOP,rootView.fragment_ranking_normal_button.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_ranking_easy_button.id,ConstraintSet.LEFT,rootView.fragment_ranking_normal_button.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_ranking_recycler_view.id,ConstraintSet.TOP,rootView.fragment_ranking_total_button.id,ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_ranking_recycler_view.id,ConstraintSet.LEFT,rootView.fragment_ranking_layout.id,ConstraintSet.LEFT,0)
        set.connect(rootView.fragment_ranking_recycler_view.id,ConstraintSet.RIGHT,rootView.fragment_ranking_layout.id,ConstraintSet.RIGHT,0)
        set.connect(rootView.fragment_ranking_recycler_view.id,ConstraintSet.BOTTOM,rootView.fragment_ranking_layout.id,ConstraintSet.BOTTOM,0)

        set.applyTo(rootView.fragment_ranking_layout)
    }



    private fun setButtonsUI() {
        rootView.fragment_ranking_back_button.layoutParams = ConstraintLayout.LayoutParams(iconSize,iconSize)
        rootView.fragment_ranking_back_button.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.close))
        rootView.fragment_ranking_total_button.layoutParams = ConstraintLayout.LayoutParams(buttonWidth,buttonHeight)
        rootView.fragment_ranking_multi_button.layoutParams = ConstraintLayout.LayoutParams(buttonWidth,buttonHeight)
        rootView.fragment_ranking_hard_button.layoutParams = ConstraintLayout.LayoutParams(buttonWidth,buttonHeight)
        rootView.fragment_ranking_normal_button.layoutParams = ConstraintLayout.LayoutParams(buttonWidth,buttonHeight)
        rootView.fragment_ranking_easy_button.layoutParams = ConstraintLayout.LayoutParams(buttonWidth,buttonHeight)
        rootView.fragment_ranking_total_button.setTextSize(TypedValue.COMPLEX_UNIT_PX,(0.9*screenSize.screenUnit).toFloat())
        rootView.fragment_ranking_multi_button.setTextSize(TypedValue.COMPLEX_UNIT_PX,(0.9*screenSize.screenUnit).toFloat())
        rootView.fragment_ranking_hard_button.setTextSize(TypedValue.COMPLEX_UNIT_PX,(0.9*screenSize.screenUnit).toFloat())
        rootView.fragment_ranking_normal_button.setTextSize(TypedValue.COMPLEX_UNIT_PX,(0.9*screenSize.screenUnit).toFloat())
        rootView.fragment_ranking_easy_button.setTextSize(TypedValue.COMPLEX_UNIT_PX,(0.9*screenSize.screenUnit).toFloat())



        displayPressedButton(rootView.fragment_ranking_total_button)


    }

    private fun setSizes() {
        iconSize = 2*screenSize.screenUnit
        buttonHeight = 2*screenSize.screenUnit
        buttonWidth = 4*screenSize.screenUnit
    }

    private fun setBackgroundGrid() {
        rootView.fragment_ranking_layout.background = TileDrawable((ContextCompat.getDrawable(requireContext(),R.drawable.background)!!),
                Shader.TileMode.REPEAT,screenSize.screenUnit)
    }

    private fun displayPressedButton(view:View){
        when(view){
            rootView.fragment_ranking_total_button -> totalPressed()
            rootView.fragment_ranking_multi_button -> multiPressed()
            rootView.fragment_ranking_hard_button -> hardPressed()
            rootView.fragment_ranking_normal_button -> normalPressed()
            rootView.fragment_ranking_easy_button -> easyPressed()
        }

    }

    private fun easyPressed() {
        rootView.fragment_ranking_total_button.background = ButtonDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenSize.screenUnit.toDouble())
        rootView.fragment_ranking_multi_button.background = ButtonDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenSize.screenUnit.toDouble())
        rootView.fragment_ranking_hard_button.background = ButtonDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenSize.screenUnit.toDouble())
        rootView.fragment_ranking_normal_button.background = ButtonDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenSize.screenUnit.toDouble())
        rootView.fragment_ranking_easy_button.background = ButtonPressedDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenSize.screenUnit.toDouble())
    }

    private fun normalPressed() {
        rootView.fragment_ranking_total_button.background = ButtonDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenSize.screenUnit.toDouble())
        rootView.fragment_ranking_multi_button.background = ButtonDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenSize.screenUnit.toDouble())
        rootView.fragment_ranking_hard_button.background = ButtonDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenSize.screenUnit.toDouble())
        rootView.fragment_ranking_normal_button.background = ButtonPressedDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenSize.screenUnit.toDouble())
        rootView.fragment_ranking_easy_button.background = ButtonDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenSize.screenUnit.toDouble())
    }

    private fun hardPressed() {
        rootView.fragment_ranking_total_button.background = ButtonDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenSize.screenUnit.toDouble())
        rootView.fragment_ranking_multi_button.background = ButtonDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenSize.screenUnit.toDouble())
        rootView.fragment_ranking_hard_button.background = ButtonPressedDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenSize.screenUnit.toDouble())
        rootView.fragment_ranking_normal_button.background = ButtonDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenSize.screenUnit.toDouble())
        rootView.fragment_ranking_easy_button.background = ButtonDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenSize.screenUnit.toDouble())
    }

    private fun multiPressed() {
        rootView.fragment_ranking_total_button.background = ButtonDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenSize.screenUnit.toDouble())
        rootView.fragment_ranking_multi_button.background = ButtonPressedDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenSize.screenUnit.toDouble())
        rootView.fragment_ranking_hard_button.background = ButtonDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenSize.screenUnit.toDouble())
        rootView.fragment_ranking_normal_button.background = ButtonDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenSize.screenUnit.toDouble())
        rootView.fragment_ranking_easy_button.background = ButtonDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenSize.screenUnit.toDouble())
    }

    private fun totalPressed() {
        rootView.fragment_ranking_total_button.background = ButtonPressedDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenSize.screenUnit.toDouble())
        rootView.fragment_ranking_multi_button.background = ButtonDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenSize.screenUnit.toDouble())
        rootView.fragment_ranking_hard_button.background = ButtonDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenSize.screenUnit.toDouble())
        rootView.fragment_ranking_normal_button.background = ButtonDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenSize.screenUnit.toDouble())
        rootView.fragment_ranking_easy_button.background = ButtonDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenSize.screenUnit.toDouble())
    }

}