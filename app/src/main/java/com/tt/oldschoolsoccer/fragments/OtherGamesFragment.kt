package com.tt.oldschoolsoccer.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Shader
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.marginLeft
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.tt.oldschoolsoccer.R
import com.tt.oldschoolsoccer.classes.FragmentCoroutine
import com.tt.oldschoolsoccer.classes.Functions
import com.tt.oldschoolsoccer.classes.OtherGamesObject
import com.tt.oldschoolsoccer.drawable.ButtonDrawable
import com.tt.oldschoolsoccer.drawable.TileDrawable
import kotlinx.android.synthetic.main.fragment_choose_game_type.view.*
import kotlinx.android.synthetic.main.fragment_other_games.view.*
import kotlinx.android.synthetic.main.fragment_single_player_easy_game.view.*
import kotlinx.coroutines.launch


class OtherGamesFragment : FragmentCoroutine() {

    private var screenUnit:Int=0
    private var buttonSendHeight=0
    private var buttonSendWidth=0
    private var tvSendHeight=0
    private var tvSendWidth=0
    private var pageCounter = 1
    private var totalCounter = 0
    private var gameListReady = false
    private val mHandlerDisplayFirstItems = Handler()
    private lateinit var rootView:View
    private val storage = Firebase.storage
    private var pictureSize = 0
    private var gameNameTVWidth = 0
    private var gameNameTVHeight = 0
    private var arrowSize = 0
    private var pageCounterWidth = 0

    private lateinit var gameList: MutableList<OtherGamesObject>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screenUnit = Functions.readScreenUnit(requireContext())

        gameList = mutableListOf()

        val dbRef = Firebase.database.getReference("OtherGames")
        dbRef.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(game in snapshot.children){
                        val tGame = game.getValue(OtherGamesObject::class.java)
                        gameList.add(tGame!!)
                    }

                    gameListReady = true
                }



         }

            override fun onCancelled(error: DatabaseError) {
                //todo make popup info about error
            }

        })

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_other_games, container, false)

        makeUI()

        rootView.fragment_other_games_page_counter.visibility = View.GONE
        rootView.fragment_other_games_right_arrow.visibility = View.GONE
        rootView.fragment_other_games_left_arrow.visibility = View.GONE

        setOnClickListeners()

        return rootView
    }

    override fun onResume() {
        super.onResume()
        displayFirstItemsLoop().run()
    }

    private fun displayFirstItemsLoop():Runnable = Runnable {
        if(gameListReady){
            mHandlerDisplayFirstItems.removeCallbacksAndMessages(null)
            totalCounter = gameList.size/2+gameList.size%2

            if(totalCounter>1){
                rootView.fragment_other_games_page_counter.visibility = View.VISIBLE
                rootView.fragment_other_games_right_arrow.visibility = View.VISIBLE
                rootView.fragment_other_games_left_arrow.visibility = View.VISIBLE

                rootView.fragment_other_games_right_arrow.setOnClickListener {
                    pageCounter+=1
                    rootView.fragment_other_games_image_view_first.setImageDrawable(null)
                    rootView.fragment_other_games_image_view_second.setImageDrawable(null)
                    displayGames(pageCounter)
                }

                rootView.fragment_other_games_left_arrow.setOnClickListener {
                    pageCounter-=1
                    rootView.fragment_other_games_image_view_first.setImageDrawable(null)
                    rootView.fragment_other_games_image_view_second.setImageDrawable(null)
                    displayGames(pageCounter)
                }

            }

            displayGames(pageCounter)
        }else{
            mHandlerDisplayFirstItems.postDelayed(displayFirstItemsLoop(),100)
        }
    }

    private fun displayGames(pageCounter: Int) {

        if(pageCounter==1){
            rootView.fragment_other_games_left_arrow.visibility = View.INVISIBLE
        }else{
            rootView.fragment_other_games_left_arrow.visibility = View.VISIBLE
        }

        if(pageCounter==totalCounter){
            rootView.fragment_other_games_right_arrow.visibility = View.GONE
        }else{
            rootView.fragment_other_games_right_arrow.visibility = View.VISIBLE
        }

        rootView.fragment_other_games_page_counter.text = "$pageCounter/$totalCounter"

        rootView.fragment_other_games_text_view_first.text = gameList[(pageCounter-1)*2].gameName

        rootView.fragment_other_games_button_first.setOnClickListener {
            val link = gameList[(pageCounter-1)*2].uri
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        if(pageCounter==totalCounter&&gameList.size%2==1) {
            rootView.fragment_other_games_text_view_second.visibility = View.GONE
            rootView.fragment_other_games_image_view_second.visibility = View.GONE
            rootView.fragment_other_games_button_second.visibility = View.GONE


        }
        else{
            rootView.fragment_other_games_text_view_second.visibility = View.VISIBLE
            rootView.fragment_other_games_image_view_second.visibility = View.VISIBLE
            rootView.fragment_other_games_button_second.visibility = View.VISIBLE
            rootView.fragment_other_games_text_view_second.text = gameList[(pageCounter - 1) * 2 + 1].gameName
            rootView.fragment_other_games_button_second.setOnClickListener {
                val link = gameList[(pageCounter-1)*2+1].uri
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        launch {
            requireContext().let {
                val storageRefFirst =
                    storage.reference.child("OtherGames").child(gameList[(pageCounter-1)*2].picture)
                val size: Long = 1024 * 1024

                storageRefFirst.getBytes(size).addOnSuccessListener {
                    val bm = BitmapFactory.decodeByteArray(it, 0, it.size)
                    rootView.fragment_other_games_image_view_first.setImageBitmap(bm)
                }

                if(pageCounter==totalCounter&&gameList.size%2==1) {}
                else{
                    val storageRefSecond =
                            storage.reference.child("OtherGames").child(gameList[(pageCounter - 1) * 2 + 1].picture)

                    storageRefSecond.getBytes(size).addOnSuccessListener {
                        val bm = BitmapFactory.decodeByteArray(it, 0, it.size)
                        rootView.fragment_other_games_image_view_second.setImageBitmap(bm)
                    }
                }
            }
        }
    }

    private fun setOnClickListeners() {
        rootView.fragment_other_games_send_game_linear_layout.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT,"Old School Soccer Game")
                //todo REPLACE THIS LINK WITH LINK TO OLD SCHOOL SOCCER IN GOOGLE PLAY STORE!!!
                val message = "https://play.google.com/store/apps/details?id=com.tt.eggs"
                intent.putExtra(Intent.EXTRA_TEXT,message)
                startActivity(Intent.createChooser(intent,"CHOOSE"))
            } catch (e:Exception){

            }
        }

    }

    private fun makeUI() {
        setBackgroundGrid()
        setSizes()
        setButtonUI()
        makeConstraintLayout()

    }

    private fun makeConstraintLayout() {
        val set = ConstraintSet()
        set.clone(rootView.fragment_other_games_layout)

        set.connect(rootView.fragment_other_games_send_game_linear_layout.id, ConstraintSet.TOP,rootView.fragment_other_games_layout.id, ConstraintSet.TOP,screenUnit)
        set.connect(rootView.fragment_other_games_send_game_linear_layout.id, ConstraintSet.LEFT,rootView.fragment_other_games_layout.id, ConstraintSet.LEFT,screenUnit)

        set.connect(rootView.fragment_other_games_image_view_first.id,ConstraintSet.TOP,rootView.fragment_other_games_layout.id,ConstraintSet.TOP,6*screenUnit)
        set.connect(rootView.fragment_other_games_image_view_first.id,ConstraintSet.LEFT,rootView.fragment_other_games_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView.fragment_other_games_text_view_first.id,ConstraintSet.TOP,rootView.fragment_other_games_image_view_first.id,ConstraintSet.TOP,screenUnit)
        set.connect(rootView.fragment_other_games_text_view_first.id,ConstraintSet.LEFT,rootView.fragment_other_games_image_view_first.id,ConstraintSet.RIGHT,screenUnit)

        set.connect(rootView.fragment_other_games_button_first.id,ConstraintSet.TOP,rootView.fragment_other_games_image_view_first.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView.fragment_other_games_button_first.id,ConstraintSet.LEFT,rootView.fragment_other_games_image_view_first.id,ConstraintSet.LEFT,0)


        set.connect(rootView.fragment_other_games_image_view_second.id,ConstraintSet.TOP,rootView.fragment_other_games_button_first.id,ConstraintSet.BOTTOM,4*screenUnit)
        set.connect(rootView.fragment_other_games_image_view_second.id,ConstraintSet.LEFT,rootView.fragment_other_games_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView.fragment_other_games_text_view_second.id,ConstraintSet.TOP,rootView.fragment_other_games_image_view_second.id,ConstraintSet.TOP,screenUnit)
        set.connect(rootView.fragment_other_games_text_view_second.id,ConstraintSet.LEFT,rootView.fragment_other_games_image_view_second.id,ConstraintSet.RIGHT,screenUnit)

        set.connect(rootView.fragment_other_games_button_second.id,ConstraintSet.TOP,rootView.fragment_other_games_image_view_second.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView.fragment_other_games_button_second.id,ConstraintSet.LEFT,rootView.fragment_other_games_image_view_second.id,ConstraintSet.LEFT,0)

        set.connect(rootView.fragment_other_games_left_arrow.id,ConstraintSet.LEFT,rootView.fragment_other_games_layout.id,ConstraintSet.LEFT,screenUnit)
        set.connect(rootView.fragment_other_games_left_arrow.id,ConstraintSet.BOTTOM,rootView.fragment_other_games_layout.id,ConstraintSet.BOTTOM,screenUnit)

        set.connect(rootView.fragment_other_games_page_counter.id,ConstraintSet.LEFT,rootView.fragment_other_games_left_arrow.id,ConstraintSet.RIGHT,screenUnit)
        set.connect(rootView.fragment_other_games_page_counter.id,ConstraintSet.BOTTOM,rootView.fragment_other_games_left_arrow.id,ConstraintSet.BOTTOM,0)

        set.connect(rootView.fragment_other_games_right_arrow.id,ConstraintSet.LEFT,rootView.fragment_other_games_page_counter.id,ConstraintSet.RIGHT,screenUnit)
        set.connect(rootView.fragment_other_games_right_arrow.id,ConstraintSet.BOTTOM,rootView.fragment_other_games_page_counter.id,ConstraintSet.BOTTOM,0)

        set.applyTo(rootView.fragment_other_games_layout)
    }

    private fun setButtonUI() {
        rootView.fragment_other_games_send_game_to_a_friend_tv.layoutParams = LinearLayout.LayoutParams(tvSendWidth,tvSendHeight)
        rootView.fragment_other_games_send_game_to_a_friend_tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenUnit.toFloat())

        rootView.fragment_other_games_image_view_first.layoutParams = ConstraintLayout.LayoutParams(pictureSize,pictureSize)
        rootView.fragment_other_games_image_view_second.layoutParams = ConstraintLayout.LayoutParams(pictureSize,pictureSize)

        rootView.fragment_other_games_text_view_first.layoutParams = ConstraintLayout.LayoutParams(gameNameTVWidth,gameNameTVHeight)
        rootView.fragment_other_games_text_view_second.layoutParams = ConstraintLayout.LayoutParams(gameNameTVWidth,gameNameTVHeight)

        rootView.fragment_other_games_text_view_first.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_other_games_text_view_second.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        rootView.fragment_other_games_button_first.layoutParams = ConstraintLayout.LayoutParams(tvSendWidth,tvSendHeight)
        rootView.fragment_other_games_button_second.layoutParams = ConstraintLayout.LayoutParams(tvSendWidth,tvSendHeight)

        rootView.fragment_other_games_button_first.background = ButtonDrawable(requireContext(),tvSendWidth.toDouble(),tvSendHeight.toDouble(),screenUnit.toDouble())
        rootView.fragment_other_games_button_second.background = ButtonDrawable(requireContext(),tvSendWidth.toDouble(),tvSendHeight.toDouble(),screenUnit.toDouble())

        rootView.fragment_other_games_button_first.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView.fragment_other_games_button_second.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        rootView.fragment_other_games_left_arrow.layoutParams = ConstraintLayout.LayoutParams(arrowSize,arrowSize)
        rootView.fragment_other_games_right_arrow.layoutParams = ConstraintLayout.LayoutParams(arrowSize,arrowSize)

        rootView.fragment_other_games_left_arrow.background = ContextCompat.getDrawable(requireContext(),R.drawable.left)
        rootView.fragment_other_games_right_arrow.background = ContextCompat.getDrawable(requireContext(),R.drawable.right)

        rootView.fragment_other_games_page_counter.layoutParams = ConstraintLayout.LayoutParams(pageCounterWidth,arrowSize)
        rootView.fragment_other_games_page_counter.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        val lp:LinearLayout.LayoutParams = LinearLayout.LayoutParams(buttonSendWidth,buttonSendHeight)
        lp.setMargins(screenUnit,0,0,0)
        rootView.fragment_other_games_send_game_button.layoutParams = lp
        rootView.fragment_other_games_send_game_linear_layout.background = ButtonDrawable(requireContext(),(buttonSendWidth+tvSendWidth+screenUnit).toDouble(), (buttonSendHeight).toDouble(), screenUnit.toDouble())
        rootView.fragment_other_games_send_game_button.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.send))
    }

    private fun setSizes() {
        buttonSendHeight=3*screenUnit
        buttonSendWidth=3*screenUnit
        tvSendHeight = 3*screenUnit
        tvSendWidth = 14*screenUnit
        pictureSize = 5*screenUnit
        gameNameTVHeight = 3*screenUnit
        gameNameTVWidth = 12*screenUnit
        arrowSize = 3*screenUnit
        pageCounterWidth = 10*screenUnit

    }

    private fun setBackgroundGrid() {
        rootView.fragment_other_games_layout.background = TileDrawable((ContextCompat.getDrawable(requireContext(),R.drawable.background)!!),
                Shader.TileMode.REPEAT,screenUnit)
    }


}