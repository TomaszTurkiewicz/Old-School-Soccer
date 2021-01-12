package com.tt.oldschoolsoccer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import android.content.Intent
import android.graphics.Shader
import android.util.Log
import android.util.TypedValue
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tt.oldschoolsoccer.R.drawable
import com.tt.oldschoolsoccer.classes.*
import com.tt.oldschoolsoccer.database.PointOnField
import com.tt.oldschoolsoccer.database.PointOnFieldEasyDatabase
import com.tt.oldschoolsoccer.drawable.ButtonDrawable
import com.tt.oldschoolsoccer.drawable.TileDrawable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivityCoroutine() {

    private var screenHeight=0
    private var screenWidth=0
    private var screenUnit=0
    private var buttonsHeight=0
    private var buttonsWidth=0
    private var marginLeft=0
    private var marginTop=0
    private lateinit var auth:FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen()
        setContentView(R.layout.activity_main)
        auth=Firebase.auth
        makeUI()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)

    }

    private fun makeUI() {
        getScreenSizeAndSaveToSharedPreferences()
        setBackgroundGrid()
        setButtonsUI()
        makeConstraintLayout()
        setButtonsOnClickListeners()




        // todo make ui for single player activity
        // todo samouczek
        // todo different types of games (single, liga, tournament)
        // todo make logic for single player game normal (choosing best moves and checking if phone can score)!!!
        // todo make logic for single player game (checking if phone can score if not blocking user moves)!!!
    }

    private fun setButtonsOnClickListeners() {
        login_logout_Image_view.setOnClickListener {
            if(auth.currentUser!=null){
                signOut()
            }else{
                signIn()
            }
        }

    }

    private fun signOut() {
        auth.signOut()
        Functions.saveLoggedStateToSharedPreferences(this,LoggedInStatus())
        updateLoginUI()
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode:Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            }catch (e:ApiException){
                Log.w("TAG","Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this){ task ->
                    if(task.isSuccessful){
                        val user = Firebase.auth.currentUser
                        if(user!=null){
                            Functions.saveLoggedStateToSharedPreferences(this,LoggedInStatus(true,user.uid))
                            checkUserInDataBase(user)
                            val databaseCreated = Functions.isRoomDatabaseCreatedFromSharedPreferences(this,user.uid)
                            if(!databaseCreated){
                                val field = GameField()
                                field.generate(Static.EASY)
                                launch {
                                    for(i in 0..8){
                                        for(j in 0..12){
                                            val item = field.getPoint(i,j)
                                            applicationContext?.let{
                                                PointOnFieldEasyDatabase(it).getPointOnFiledDao().addPointOnField(item)
                                            }
                                        }
                                    }
                                    Toast.makeText(this@MainActivity,"DATABASE CREATED",Toast.LENGTH_LONG).show()
                                    Functions.databaseCreatedToSharedPreferences(this@MainActivity,true,user.uid)
                                }
                            }else{
                                Toast.makeText(this@MainActivity,"DATABASE ALREADY EXISTS",Toast.LENGTH_LONG).show()
                            }
                        }
                    }else{
                        updateLoginUI()
                        Functions.saveLoggedStateToSharedPreferences(this,LoggedInStatus())
                    }
                }

    }

    private fun checkUserInDataBase(user: FirebaseUser) {
        val dbRef = Firebase.database.getReference("User").child(user.uid)
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(!snapshot.exists()){
                    // doesn't exist - create record in database
                    val userDB = User(id = user.uid)
                    dbRef.setValue(userDB)
                    checkUserInDataBase(user)
                }
                else{
                    // exists so check if sharedpreferences are the same as firebase
                    val fUser = snapshot.getValue(User::class.java)
                    val shpUser = Functions.readUserFromSharedPreferences(this@MainActivity,user.uid)
                    if(fUser!=null){
                        if(!fUser.userName.equals(shpUser.userName)){
                            shpUser.userName = fUser.userName
                        }
                        if(shpUser.easyGame.numberOfGames>fUser.easyGame.numberOfGames){
                            fUser.easyGame = shpUser.easyGame
                        }else{
                            shpUser.easyGame = fUser.easyGame
                        }
                        if(shpUser.normalGame.numberOfGames>fUser.normalGame.numberOfGames){
                            fUser.normalGame = shpUser.normalGame
                        }else{
                            shpUser.normalGame = fUser.normalGame
                        }
                        if(shpUser.hardGame.numberOfGames>fUser.hardGame.numberOfGames){
                            fUser.hardGame = shpUser.hardGame
                        }else{
                            shpUser.hardGame = fUser.hardGame
                        }
                        if(shpUser.multiGame.numberOfGames>fUser.multiGame.numberOfGames){
                            fUser.multiGame = shpUser.multiGame
                        }else{
                            shpUser.multiGame = fUser.multiGame
                        }
                        dbRef.setValue(fUser)
                        Functions.saveUserToSharedPreferences(this@MainActivity,shpUser)
                        Functions.saveLoggedStateToSharedPreferences(this@MainActivity, loggedInStatus = LoggedInStatus(true,user.uid))
                        updateLoginUI()
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                // do nothing
            }
        })
    }


    private fun setButtonsUI() {
        buttonsWidth=16*screenUnit
        buttonsHeight=4*screenUnit
        marginTop=buttonsHeight/2
        marginLeft=2*screenUnit

        choose_game_type_button_main_activity.layoutParams = ConstraintLayout.LayoutParams(buttonsWidth,buttonsHeight)
        choose_game_type_button_main_activity.background=ButtonDrawable(this, (buttonsWidth).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
        choose_game_type_button_main_activity.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        login_logout_Image_view.layoutParams = ConstraintLayout.LayoutParams(3 * screenUnit,3 * screenUnit)
        user_name.layoutParams = ConstraintLayout.LayoutParams(12*screenUnit,3*screenUnit)
        user_name.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenUnit.toFloat())

        updateLoginUI()


    }

    private fun updateLoginUI() {
        val loggedInStatus = Functions.readLoggedStateFromSharedPreferences(this)

        if(loggedInStatus.loggedIn){
            login_logout_Image_view.background = ContextCompat.getDrawable(this, drawable.account_green)
            user_name.visibility = View.VISIBLE
            val tUser = Functions.readUserFromSharedPreferences(this,loggedInStatus.userid)
            user_name.text = tUser.userName

        }else {
            login_logout_Image_view.background = ContextCompat.getDrawable(this, drawable.account_grey)
            user_name.visibility = View.GONE
        }

    }

    private fun makeConstraintLayout() {
        val set:ConstraintSet = ConstraintSet()
        set.clone(main_layout)

        set.connect(login_logout_Image_view.id,ConstraintSet.TOP,main_layout.id,ConstraintSet.TOP,screenUnit)
        set.connect(login_logout_Image_view.id,ConstraintSet.RIGHT,main_layout.id,ConstraintSet.RIGHT,marginLeft)

        set.connect(user_name.id,ConstraintSet.TOP,main_layout.id,ConstraintSet.TOP,screenUnit)
        set.connect(user_name.id,ConstraintSet.LEFT,main_layout.id,ConstraintSet.LEFT,marginLeft)

        set.connect(choose_game_type_button_main_activity.id,ConstraintSet.TOP,login_logout_Image_view.id,ConstraintSet.BOTTOM,marginTop)
        set.connect(choose_game_type_button_main_activity.id,ConstraintSet.LEFT,main_layout.id,ConstraintSet.LEFT,marginLeft)

        set.applyTo(main_layout)
    }

    private fun setBackgroundGrid() {
        main_layout.background = TileDrawable((ContextCompat.getDrawable(this,drawable.background)!!),Shader.TileMode.REPEAT,screenUnit)

    }

    private fun getScreenSizeAndSaveToSharedPreferences() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenHeight = displayMetrics.heightPixels
        screenWidth = displayMetrics.widthPixels
        val unitWidth = screenWidth/20
        val unitHeight = screenHeight/30
        screenUnit=if(unitWidth>unitHeight)unitHeight else unitWidth
        Functions.saveScreenUnit(this,screenUnit)

    }

    private fun fullScreen() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        val decorView:View = window.decorView
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

    fun goToChooseGameTypeActivityOnClick(view: View) {
        val intent = Intent(this,ChooseGameType::class.java)
        startActivity(intent)
        finish()
    }

    companion object{
        private const val RC_SIGN_IN = 9001
    }
}

/*
* todo connect to firebase (picture, user, login)
* todo add admob
* todo activity multiplayer
* todo samouczek koniecznie !!!
* todo puchary (rozgrywki)
* todo nagrody za strzelone branki i wygrane mecze
* todo ikona aplikacji
* todo update helper listener
* todo add sounds
*
*/