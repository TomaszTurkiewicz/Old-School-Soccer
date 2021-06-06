package com.tt.oldschoolsoccer.fragments.multiPlayer

import android.graphics.Shader
import android.os.Bundle
import android.text.Editable
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tt.oldschoolsoccer.R
import com.tt.oldschoolsoccer.classes.*
import com.tt.oldschoolsoccer.drawable.ButtonDrawable
import com.tt.oldschoolsoccer.drawable.ButtonPressedDrawable
import com.tt.oldschoolsoccer.drawable.TileDrawable
import com.tt.oldschoolsoccer.fragments.ChooseGameTypeFragment
import com.tt.oldschoolsoccer.fragments.MainFragment
import com.tt.oldschoolsoccer.recyclerViews.MultiPlayerAllUserRecyclerViewAdapter
import com.tt.oldschoolsoccer.recyclerViews.MultiPlayerHistoryRecyclerView
import kotlinx.android.synthetic.main.fragment_multi_player_list.view.*
import kotlinx.android.synthetic.main.fragment_ranking.view.*


class MultiPlayerListFragment : Fragment() {

    private lateinit var rootView: View
    private var screenSize = ScreenSize()
    private var iconSize = 0
    private var buttonHeight = 0
    private var buttonWidth = 0
    private lateinit var allUserList: MutableList<UserRanking>
    private lateinit var tmpUserList: MutableList<UserRanking>
    private lateinit var invitationList: MutableList<Invitation>
    private lateinit var finalUserList: MutableList<UserRanking>
    private lateinit var history: MultiPlayerHistory
    private lateinit var recyclerView: RecyclerView
    private var loggedInStatus = LoggedInStatus()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screenSize = Functions.readScreenSize(requireContext())
        allUserList = mutableListOf()
        tmpUserList = mutableListOf()
        invitationList = mutableListOf()
        finalUserList = mutableListOf()
        loggedInStatus = Functions.readLoggedStateFromSharedPreferences(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        rootView = inflater.inflate(R.layout.fragment_multi_player_list, container, false)

        makeUI()

        recyclerView = rootView.fragment_multi_player_list_recycler_view

        rootView.fragment_multi_player_list_search_input_text.addTextChangedListener {
            checkListWithCriteria(it)
        }

        return rootView
    }

    private fun checkListWithCriteria(it: Editable?) {
        tmpUserList.clear()
        it?.let {
            val user = it.toString()
            for(i in finalUserList.indices){
                if(finalUserList[i].userName.contains(user)){
                    tmpUserList.add(finalUserList[i])
                }
            }
            initRecyclerViewForAllUsers(tmpUserList)
        }


    }

    private fun makeUI() {

        setBackgroundGrid()
        setSizes()
        setButtonsUI()
        makeConstraintLayout()
        prepareAllList()
        prepareHistoryList()
        setOnClickListeners()
    }

    private fun prepareHistoryList() {
        val historyRef = Firebase.database.getReference("History").child(loggedInStatus.userid)
        historyRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    history = snapshot.getValue(MultiPlayerHistory::class.java)!!
                    for(i in history.history.indices){
                        for(j in allUserList.indices){
                            if(history.history[i].userId==allUserList[j].id){
                                history.history[i].icon=allUserList[j].icon
                                history.history[i].name=allUserList[j].userName
                            }
                        }
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    private fun setOnClickListeners() {
       rootView.fragment_multi_player_list_back_button.setOnClickListener {
           activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ChooseGameTypeFragment()).commit()
       }

        rootView.fragment_multi_player_list_all_button.setOnClickListener{
            displayPressedButton(rootView.fragment_multi_player_list_all_button)
            rootView.fragment_multi_player_list_search_input_text.text.clear()
            initRecyclerViewForAllUsers(finalUserList)
        }

        rootView.fragment_multi_player_list_history_button.setOnClickListener{
            displayPressedButton(rootView.fragment_multi_player_list_history_button)
            initRecyclerViewForHistory(history)
        }
    }

    private fun initRecyclerViewForHistory(history: MultiPlayerHistory) {
        val a = 100
        //todo change userHistory to not null icon and name
        val adapter = MultiPlayerHistoryRecyclerView(requireContext(),history,screenSize.screenUnit){ view, singleUserHistory -> inviteFromHistory(view,singleUserHistory)}
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun inviteFromHistory(view: View, singleUserHistory : SingleUserHistory){
        //todo !!!!
    }


    private fun prepareAllList() {
        val invRef = Firebase.database.getReference("Invitations")
        invRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (invitation in snapshot.children) {
                        val tInv = invitation.getValue(Invitation::class.java)
                        if(!tInv!!.opponentAccept && !tInv.myAccept && !tInv.player.equals(loggedInStatus.userid))
                        invitationList.add(tInv)
                    }
                }
                val userRef = Firebase.database.getReference("Ranking")
                userRef.addListenerForSingleValueEvent(object:ValueEventListener{
                    override fun onDataChange(snapshot1: DataSnapshot) {
                        if(snapshot1.exists()){
                            for(user in snapshot1.children){
                                val tUser = user.getValue(UserRanking::class.java)
                                if(tUser!!.playWithPeople){
                                    allUserList.add(tUser)
                                }
                            }
                        }
                        for(i in allUserList.indices){
                            val id = allUserList[i].id
                            for(j in invitationList.indices){
                                val tmpId = invitationList[j].player
                                if(tmpId == id){
                                    finalUserList.add(allUserList[i])
                                }
                            }
                        }
                        var sorted = false
                        while (!sorted) {
                            sorted = true
                            for (i in 0 until finalUserList.size - 1) {
                                if (hasToBeSorted(i)) {
                                    val userTmp = finalUserList[i]
                                    finalUserList[i] = finalUserList[i + 1]
                                    finalUserList[i + 1] = userTmp
                                    sorted = false
                                }
                            }
                        }
                        initRecyclerViewForAllUsers(finalUserList)
                    }
                    override fun onCancelled(error1: DatabaseError) {
                    }
                })
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun initRecyclerViewForAllUsers(userList:MutableList<UserRanking>) {
        val adapter = MultiPlayerAllUserRecyclerViewAdapter(requireContext(),userList,screenSize.screenUnit) { view, userRanking -> invite(view, userRanking) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun invite(view: View, userRanking: UserRanking) {
        val myRef = Firebase.database.getReference("Invitations").child(loggedInStatus.userid)
        myRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val meInvitations = snapshot.getValue(Invitation::class.java)
                    // checking if I have been invited already
                    if(meInvitations!!.opponentAccept){
                        //todo alert dialog about someone invite me

                        Toast.makeText(requireContext(),"Someone invite me",Toast.LENGTH_LONG).show()
                    }else{

                        //check if opponent still play with people
                        val opponentUserPlayWithPeopleRef = Firebase.database.getReference("Ranking").child(userRanking.id)
                        opponentUserPlayWithPeopleRef.addListenerForSingleValueEvent(object: ValueEventListener{
                            override fun onDataChange(snapshot1: DataSnapshot) {
                                if(snapshot1.exists()){
                                    val opponentUserPlayWithPeople = snapshot1.getValue(UserRanking::class.java)
                                    if(opponentUserPlayWithPeople!!.playWithPeople){

                                        // check if opponent isn't involved in other games
                                        val opponentInvitationsRef = Firebase.database.getReference("Invitations").child(userRanking.id)
                                        opponentInvitationsRef.addListenerForSingleValueEvent(object : ValueEventListener{
                                            override fun onDataChange(snapshot2: DataSnapshot) {
                                                if(snapshot2.exists()){
                                                    val opponentInvitations = snapshot2.getValue(Invitation::class.java)
                                                    if(!opponentInvitations!!.opponentAccept && !opponentInvitations.myAccept){
                                                    meInvitations.myAccept = true
                                                    meInvitations.opponent = opponentInvitations.player
                                                    meInvitations.orientation = Static.ORIENTATION_NORMAL
                                                    opponentInvitations.opponentAccept = true
                                                    opponentInvitations.opponent = meInvitations.player
                                                    opponentInvitations.orientation = Static.ORIENTATION_UP_SIDE_DOWN

                                                    opponentInvitationsRef.setValue(opponentInvitations)
                                                        myRef.setValue(meInvitations)

                                                        backToChooseGameType()


                                                        val message = "Play with "+ userRanking.userName
                                                        Toast.makeText(requireContext(),message,Toast.LENGTH_LONG).show()
                                                    }
                                                    else{
                                                        // todo alert dialog opponent ask someone else to play
                                                        Toast.makeText(requireContext(),"Someone else want to play with him",Toast.LENGTH_LONG).show()
                                                    }
                                                }
                                            }

                                            override fun onCancelled(error2: DatabaseError) {

                                            }

                                        })
                                    }
                                    else{
                                        Toast.makeText(requireContext(),"Doesn't want to play with people",Toast.LENGTH_LONG).show()
                                        // todo alert dialog opponent doesn't want to play with people anymore
                                    }
                                }
                            }

                            override fun onCancelled(error1: DatabaseError) {
                            }

                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError){
            }

        })



    }

    private fun backToChooseGameType() {
        activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ChooseGameTypeFragment()).commit()

    }

    private fun hasToBeSorted(i: Int): Boolean {
        return when {
            finalUserList[i].multiGame<finalUserList[i+1].multiGame -> {
                true
            }
            finalUserList[i].multiGame==finalUserList[i+1].multiGame -> {
                finalUserList[i].multiNoOfGames<finalUserList[i+1].multiNoOfGames
            }
            else -> {
                false
            }
        }

    }

    private fun makeConstraintLayout() {
        val set = ConstraintSet()
        set.clone(rootView.fragment_multi_player_list_layout)

        set.connect(rootView.fragment_multi_player_list_back_button.id, ConstraintSet.TOP,rootView.fragment_multi_player_list_layout.id, ConstraintSet.TOP,screenSize.screenUnit)
        set.connect(rootView.fragment_multi_player_list_back_button.id, ConstraintSet.LEFT,rootView.fragment_multi_player_list_layout.id, ConstraintSet.LEFT,16*screenSize.screenUnit)

        set.connect(rootView.fragment_multi_player_list_all_button.id, ConstraintSet.TOP,rootView.fragment_multi_player_list_layout.id, ConstraintSet.TOP,3*screenSize.screenUnit)
        set.connect(rootView.fragment_multi_player_list_all_button.id, ConstraintSet.LEFT,rootView.fragment_multi_player_list_layout.id, ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_multi_player_list_history_button.id, ConstraintSet.TOP,rootView.fragment_multi_player_list_all_button.id, ConstraintSet.TOP,0)
        set.connect(rootView.fragment_multi_player_list_history_button.id, ConstraintSet.LEFT,rootView.fragment_multi_player_list_all_button.id, ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_multi_player_list_search_icon.id,ConstraintSet.BOTTOM,rootView.fragment_multi_player_list_layout.id,ConstraintSet.BOTTOM,screenSize.verticalOffset+screenSize.screenUnit)
        set.connect(rootView.fragment_multi_player_list_search_icon.id,ConstraintSet.LEFT,rootView.fragment_multi_player_list_layout.id,ConstraintSet.LEFT,screenSize.screenUnit)

        set.connect(rootView.fragment_multi_player_list_search_input_text.id,ConstraintSet.TOP,rootView.fragment_multi_player_list_search_icon.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_multi_player_list_search_input_text.id,ConstraintSet.LEFT,rootView.fragment_multi_player_list_search_icon.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_multi_player_list_recycler_view.id, ConstraintSet.TOP,rootView.fragment_multi_player_list_all_button.id, ConstraintSet.BOTTOM,0)
        set.connect(rootView.fragment_multi_player_list_recycler_view.id, ConstraintSet.LEFT,rootView.fragment_multi_player_list_layout.id, ConstraintSet.LEFT,0)
        set.connect(rootView.fragment_multi_player_list_recycler_view.id, ConstraintSet.RIGHT,rootView.fragment_multi_player_list_layout.id, ConstraintSet.RIGHT,0)
        set.connect(rootView.fragment_multi_player_list_recycler_view.id, ConstraintSet.BOTTOM,rootView.fragment_multi_player_list_search_icon.id, ConstraintSet.TOP,screenSize.screenUnit)

        set.connect(rootView.fragment_multi_player_list_lower_frame.id,ConstraintSet.LEFT,rootView.fragment_multi_player_list_layout.id,ConstraintSet.LEFT,0)
        set.connect(rootView.fragment_multi_player_list_lower_frame.id,ConstraintSet.TOP,rootView.fragment_multi_player_list_recycler_view.id,ConstraintSet.BOTTOM,0)

        set.connect(rootView.fragment_multi_player_list_upper_frame.id,ConstraintSet.BOTTOM,rootView.fragment_multi_player_list_recycler_view.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_multi_player_list_upper_frame.id,ConstraintSet.LEFT,rootView.fragment_multi_player_list_layout.id,ConstraintSet.LEFT,2*buttonWidth)

        set.applyTo(rootView.fragment_multi_player_list_layout)
    }

    private fun setButtonsUI() {
        rootView.fragment_multi_player_list_back_button.layoutParams = ConstraintLayout.LayoutParams(iconSize,iconSize)
        rootView.fragment_multi_player_list_back_button.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.close))

        rootView.fragment_multi_player_list_all_button.layoutParams = ConstraintLayout.LayoutParams(buttonWidth,buttonHeight)
        rootView.fragment_multi_player_list_history_button.layoutParams = ConstraintLayout.LayoutParams(buttonWidth,buttonHeight)

        rootView.fragment_multi_player_list_all_button.setTextSize(TypedValue.COMPLEX_UNIT_PX,(0.9*screenSize.screenUnit).toFloat())
        rootView.fragment_multi_player_list_history_button.setTextSize(TypedValue.COMPLEX_UNIT_PX,(0.9*screenSize.screenUnit).toFloat())

        rootView.fragment_multi_player_list_search_icon.layoutParams = ConstraintLayout.LayoutParams(iconSize,iconSize)
        rootView.fragment_multi_player_list_search_icon.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.search))

        rootView.fragment_multi_player_list_search_input_text.layoutParams = ConstraintLayout.LayoutParams(screenSize.horizontalCount*screenSize.screenUnit-iconSize-2*screenSize.screenUnit,iconSize)

        rootView.fragment_multi_player_list_lower_frame.layoutParams = ConstraintLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,screenSize.screenUnit/10)
        rootView.fragment_multi_player_list_upper_frame.layoutParams = ConstraintLayout.LayoutParams(screenSize.horizontalLength-2*buttonWidth,screenSize.screenUnit/10)

        displayPressedButton(rootView.fragment_multi_player_list_all_button)
    }

    private fun displayPressedButton(view: View) {
        when(view){
            rootView.fragment_multi_player_list_all_button -> allPressed()
            rootView.fragment_multi_player_list_history_button -> historyPressed()
        }

    }

    private fun historyPressed() {
        rootView.fragment_multi_player_list_history_button.background = ButtonPressedDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenSize.screenUnit.toDouble())
        rootView.fragment_multi_player_list_all_button.background = ButtonDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenSize.screenUnit.toDouble())
    }

    private fun allPressed() {
        rootView.fragment_multi_player_list_history_button.background = ButtonDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenSize.screenUnit.toDouble())
        rootView.fragment_multi_player_list_all_button.background = ButtonPressedDrawable(requireContext(), buttonWidth.toDouble(), buttonHeight.toDouble(), screenSize.screenUnit.toDouble())
    }

    private fun setSizes() {
        iconSize = 2*screenSize.screenUnit
        buttonHeight = 2*screenSize.screenUnit
        buttonWidth = 4*screenSize.screenUnit
    }

    private fun setBackgroundGrid() {
        rootView.fragment_multi_player_list_layout.background = TileDrawable((ContextCompat.getDrawable(requireContext(),R.drawable.background)!!),
                Shader.TileMode.REPEAT,screenSize.screenUnit)
    }


}