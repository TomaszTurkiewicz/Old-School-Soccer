package com.tt.oldschoolsoccer.fragments

import android.content.Intent
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
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
import com.tt.oldschoolsoccer.R
import com.tt.oldschoolsoccer.classes.*
import com.tt.oldschoolsoccer.database.PointOnFieldEasyDatabase
import com.tt.oldschoolsoccer.drawable.ButtonDrawable
import com.tt.oldschoolsoccer.drawable.TileDrawable
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.coroutines.launch

class MainFragment : FragmentCoroutine() {

    private var screenUnit:Int=0
    private var marginLeft=0
    private var buttonsHeight=0
    private var buttonsWidth=0
    private var marginTop=0
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private var rootView: View? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * read screenUnit for making any UI in this fragment
         */
        screenUnit = Functions.readScreenUnit(requireContext())

        auth = Firebase.auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(),gso)

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_main,container,false)

        makeUI(rootView!!)

        setOnClickListeners(rootView)

        updateLoginUI()

        return rootView
    }

    private fun setOnClickListeners(rootView: View?) {

        rootView!!.fragment_main_choose_game_type_button.setOnClickListener {
            goToChooseGameTypeFragment()
        }

        rootView!!.fragment_main_login_logout_Image_view.setOnClickListener {
            if(auth.currentUser!=null){
                signOut()
            }
            else{
                signIn()
            }
        }


    }

    private fun signIn(){
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    /**
     * for sing in user
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            }catch (e: ApiException){
                Log.w("TAG","Google sign in failed", e)
            }
        }
    }

    /**
     * sign in to firebase auth
     */
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity()){ task ->
                    if(task.isSuccessful){
                        val user = Firebase.auth.currentUser
                        if(user!=null){
                            Functions.saveLoggedStateToSharedPreferences(requireContext(),LoggedInStatus(true,user.uid))
                            checkUserInDataBase(user)
                            val databaseCreated = Functions.isRoomDatabaseCreatedFromSharedPreferences(requireContext(),user.uid)
                            if(!databaseCreated){
                                val field = GameField()
                                field.generate(Static.EASY)
                                launch {
                                    for(i in 0..8){
                                        for(j in 0..12){
                                            val item = field.getPoint(i,j)
                                            requireContext()?.let{
                                                PointOnFieldEasyDatabase(it).getPointOnFiledDao().addPointOnField(item)
                                            }
                                        }
                                    }
                                    Toast.makeText(requireContext(),"DATABASE CREATED", Toast.LENGTH_LONG).show()
                                    Functions.databaseCreatedToSharedPreferences(requireContext(),true,user.uid)
                                }
                            }else{
                                Toast.makeText(requireContext(),"DATABASE ALREADY EXISTS", Toast.LENGTH_LONG).show()
                            }
                        }
                    }else{
                        updateLoginUI()
                        Functions.saveLoggedStateToSharedPreferences(requireContext(),LoggedInStatus())
                    }
                }

    }

    /**
     * update UI if user is logged in or not
     */
    private fun updateLoginUI() {
        val loggedInStatus = Functions.readLoggedStateFromSharedPreferences(requireContext())
        if(loggedInStatus.loggedIn){
            rootView!!.fragment_main_login_logout_Image_view.background = ContextCompat.getDrawable(requireContext(), R.drawable.account_green)
            rootView!!.fragment_main_user_name.visibility = View.VISIBLE
            val tUser = Functions.readUserFromSharedPreferences(requireContext(),loggedInStatus.userid)
            rootView!!.fragment_main_user_name.text = tUser.userName
        }else {
            rootView!!.fragment_main_login_logout_Image_view.background = ContextCompat.getDrawable(requireContext(), R.drawable.account_grey)
            rootView!!.fragment_main_user_name.visibility = View.GONE
        }
    }

    /**
     * check user in database and synchronize with shared preferences
     */
    private fun checkUserInDataBase(user: FirebaseUser) {
        val dbRef = Firebase.database.getReference("User").child(user.uid)
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
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
                    val shpUser = Functions.readUserFromSharedPreferences(requireContext(),user.uid)
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
                        Functions.saveUserToSharedPreferences(requireContext(),shpUser)
                        Functions.saveLoggedStateToSharedPreferences(requireContext(), loggedInStatus = LoggedInStatus(true,user.uid))
                        updateLoginUI()
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                // do nothing
            }
        })
    }

    private fun signOut(){
        auth.signOut()
        Functions.saveLoggedStateToSharedPreferences(requireContext(), LoggedInStatus())
        updateLoginUI()
    }

    private fun makeUI(fragmentView:View){
        setBackgroundGrid(fragmentView)
        setSizes()
        setButtonsUI(fragmentView)
        makeConstraintLayout(fragmentView)

    }


    /**
     * initialize sizes variables
     */
    private fun setSizes() {
        marginLeft = 2*screenUnit
        buttonsWidth=16*screenUnit
        buttonsHeight=4*screenUnit
        marginTop=buttonsHeight/2

    }

    /**
     * set all views in proper positions on the screen
     */
    private fun makeConstraintLayout(view: View){
        val set = ConstraintSet()
        set.clone(view.fragment_main_layout)

        set.connect(view.fragment_main_login_logout_Image_view.id, ConstraintSet.TOP, view.fragment_main_layout.id, ConstraintSet.TOP, screenUnit)
        set.connect(view.fragment_main_login_logout_Image_view.id, ConstraintSet.RIGHT, view.fragment_main_layout.id, ConstraintSet.RIGHT, screenUnit)

        set.connect(view.fragment_main_user_name.id, ConstraintSet.TOP,view.fragment_main_layout.id, ConstraintSet.TOP,screenUnit)
        set.connect(view.fragment_main_user_name.id, ConstraintSet.LEFT,view.fragment_main_layout.id, ConstraintSet.LEFT,marginLeft)

        set.connect(view.fragment_main_choose_game_type_button.id, ConstraintSet.TOP,view.fragment_main_login_logout_Image_view.id, ConstraintSet.BOTTOM,marginTop)
        set.connect(view.fragment_main_choose_game_type_button.id, ConstraintSet.LEFT,view.fragment_main_layout.id, ConstraintSet.LEFT,marginLeft)

        set.applyTo(view.fragment_main_layout)
    }

    /**
     * buttons sizes, text sizes
     */
    private fun setButtonsUI(view: View){
        view.fragment_main_login_logout_Image_view.layoutParams = ConstraintLayout.LayoutParams(3*screenUnit,3*screenUnit)
        view.fragment_main_user_name.layoutParams = ConstraintLayout.LayoutParams(12*screenUnit,3*screenUnit)
        view.fragment_main_user_name.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        view.fragment_main_choose_game_type_button.layoutParams = ConstraintLayout.LayoutParams(buttonsWidth,buttonsHeight)
        view.fragment_main_choose_game_type_button.background = ButtonDrawable(requireContext(),(buttonsWidth).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
        view.fragment_main_choose_game_type_button.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenUnit.toFloat())
    }

    /**
     * making background grid
     */
    private fun setBackgroundGrid(view: View){
        view.fragment_main_layout.background = TileDrawable((ContextCompat.getDrawable(requireContext(),R.drawable.background)!!),
            Shader.TileMode.REPEAT,screenUnit)
    }


    private fun goToChooseGameTypeFragment() {
        activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container,ChooseGameTypeFragment()).commit()
    }

    companion object{
        private const val RC_SIGN_IN = 9001
    }


    }