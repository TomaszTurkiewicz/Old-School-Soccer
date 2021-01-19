package com.tt.oldschoolsoccer.fragments

import android.content.Context
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
import com.tt.oldschoolsoccer.database.UserDB
import com.tt.oldschoolsoccer.database.UserDBDatabase
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

        makeUI()

        setOnClickListeners()

        updateLoginUI()

        return rootView
    }

    private fun setOnClickListeners() {

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

        rootView!!.fragment_main_statistics_button.setOnClickListener {
            goToStatisticFragment()
        }

    }

    private fun goToStatisticFragment(){
        activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container,StatisticsFragment()).commit()
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

                    /**
                     * user has been logged in
                     */
                    if(task.isSuccessful){
                        val user = Firebase.auth.currentUser
                        if(user!=null){
                            Functions.saveLoggedStateToSharedPreferences(requireContext(),LoggedInStatus(true,user.uid))

                            // check if database is created

                            launch {
                                requireContext()?.let {
                                    val userDB = UserDBDatabase(it).getUserDBDao().getUser(user!!.uid)
                                    if(userDB==null){
                                        createDB(it,user)
                                        Toast.makeText(requireContext(), "CREATING DATABASE", Toast.LENGTH_SHORT).show()
                                    }
                                    else{
                                        synchronizeUserDB(it,userDB)
                                        Toast.makeText(requireContext(), "CHECKING USER", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                        updateLoginUI()
                    }else{

                        Functions.saveLoggedStateToSharedPreferences(requireContext(),LoggedInStatus())
                        updateLoginUI()
                    }
                }

    }

    private fun synchronizeUserDB(it: Context, userDB: UserDB) {

        val normalUser = User().userFromDB(userDB)
        val dbRef = Firebase.database.getReference("User").child(userDB.id)
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(!snapshot.exists()){
                    // doesn't exist - create record in database
                    dbRef.setValue(normalUser)
                }
                else{

                    val fUser = snapshot.getValue(User::class.java)

                    if(fUser!=null){

                        if(normalUser.easyGame.numberOfGames<fUser.easyGame.numberOfGames){
                            normalUser.easyGame = fUser.easyGame
                        }
                        if(normalUser.normalGame.numberOfGames<fUser.normalGame.numberOfGames){
                            normalUser.normalGame = fUser.normalGame
                        }
                        if(normalUser.hardGame.numberOfGames<fUser.hardGame.numberOfGames){
                            normalUser.hardGame = fUser.hardGame
                        }
                        if(normalUser.multiGame.numberOfGames<fUser.multiGame.numberOfGames){
                            normalUser.multiGame = fUser.multiGame
                        }
                        dbRef.setValue(normalUser)

                        launch {
                            requireContext()?.let {
                                UserDBDatabase(it).getUserDBDao().updateUserInDB(UserDB().dbUser(normalUser))
                            }
                        }

                        Functions.saveLoggedStateToSharedPreferences(requireContext(), loggedInStatus = LoggedInStatus(true,userDB.id))
                        updateLoginUI()
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                // do nothing
            }
        })








    }

    private suspend fun createDB(it: Context, user: FirebaseUser) {
        val userFb = createUserStatistics(it,user)
        createUserEasyField(it)


        createUserInFirebase(userFb)

    }

    private fun createUserInFirebase(userFb: User) {
        val dbRef = Firebase.database.getReference("User").child(userFb.id)
        dbRef.setValue(userFb)

    }

    private suspend fun createUserEasyField(it: Context) {
        val field = GameField()
        field.generate(Static.EASY)
        for(i in 0..8){
            for(j in 0..12){
                val item = field.getPoint(i,j)
                PointOnFieldEasyDatabase(it).getPointOnFiledDao().addPointOnField(item)
            }
        }
    }

    private suspend fun createUserStatistics(it: Context, user: FirebaseUser):User {
        val userFb = User()
        userFb.id = user.uid

        UserDBDatabase(it).getUserDBDao().addUser(UserDB().dbUser(userFb))

        return userFb

    }

    /**
     * update UI if user is logged in or not
     */
    private fun updateLoginUI() {
        val loggedInStatus = Functions.readLoggedStateFromSharedPreferences(requireContext())
        if(loggedInStatus.loggedIn){
            rootView!!.fragment_main_login_logout_Image_view.background = ContextCompat.getDrawable(requireContext(), R.drawable.account_green)
            rootView!!.fragment_main_user_name.visibility = View.VISIBLE

//            val tUser = Functions.readUserFromSharedPreferences(requireContext(),loggedInStatus.userid)

//            rootView!!.fragment_main_user_name.text = tUser.userName
            rootView!!.fragment_main_statistics_button.visibility = View.VISIBLE
        }else {
            rootView!!.fragment_main_login_logout_Image_view.background = ContextCompat.getDrawable(requireContext(), R.drawable.account_grey)
            rootView!!.fragment_main_user_name.visibility = View.GONE
            rootView!!.fragment_main_statistics_button.visibility = View.GONE
        }
    }

    /**
     * check user in database and synchronize with shared preferences
     */

    private fun signOut(){
        auth.signOut()
        Functions.saveLoggedStateToSharedPreferences(requireContext(), LoggedInStatus())
        updateLoginUI()
    }

    private fun makeUI(){
        setBackgroundGrid()
        setSizes()
        setButtonsUI()
        makeConstraintLayout()

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
    private fun makeConstraintLayout(){
        val set = ConstraintSet()
        set.clone(rootView!!.fragment_main_layout)

        set.connect(rootView!!.fragment_main_login_logout_Image_view.id, ConstraintSet.TOP, rootView!!.fragment_main_layout.id, ConstraintSet.TOP, screenUnit)
        set.connect(rootView!!.fragment_main_login_logout_Image_view.id, ConstraintSet.RIGHT, rootView!!.fragment_main_layout.id, ConstraintSet.RIGHT, screenUnit)

        set.connect(rootView!!.fragment_main_user_name.id, ConstraintSet.TOP,rootView!!.fragment_main_layout.id, ConstraintSet.TOP,screenUnit)
        set.connect(rootView!!.fragment_main_user_name.id, ConstraintSet.LEFT,rootView!!.fragment_main_layout.id, ConstraintSet.LEFT,marginLeft)

        set.connect(rootView!!.fragment_main_choose_game_type_button.id, ConstraintSet.TOP,rootView!!.fragment_main_login_logout_Image_view.id, ConstraintSet.BOTTOM,marginTop)
        set.connect(rootView!!.fragment_main_choose_game_type_button.id, ConstraintSet.LEFT,rootView!!.fragment_main_layout.id, ConstraintSet.LEFT,marginLeft)

        set.connect(rootView!!.fragment_main_statistics_button.id, ConstraintSet.TOP,rootView!!.fragment_main_choose_game_type_button.id, ConstraintSet.BOTTOM,marginTop)
        set.connect(rootView!!.fragment_main_statistics_button.id, ConstraintSet.LEFT,rootView!!.fragment_main_layout.id, ConstraintSet.LEFT,marginLeft)

        set.applyTo(rootView!!.fragment_main_layout)
    }

    /**
     * buttons sizes, text sizes
     */
    private fun setButtonsUI(){
        rootView!!.fragment_main_login_logout_Image_view.layoutParams = ConstraintLayout.LayoutParams(3*screenUnit,3*screenUnit)
        rootView!!.fragment_main_user_name.layoutParams = ConstraintLayout.LayoutParams(12*screenUnit,3*screenUnit)
        rootView!!.fragment_main_user_name.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())
        rootView!!.fragment_main_choose_game_type_button.layoutParams = ConstraintLayout.LayoutParams(buttonsWidth,buttonsHeight)
        rootView!!.fragment_main_choose_game_type_button.background = ButtonDrawable(requireContext(),(buttonsWidth).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
        rootView!!.fragment_main_choose_game_type_button.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenUnit.toFloat())
        rootView!!.fragment_main_statistics_button.layoutParams = ConstraintLayout.LayoutParams(buttonsWidth,buttonsHeight)
        rootView!!.fragment_main_statistics_button.background = ButtonDrawable(requireContext(),(buttonsWidth).toDouble(), (buttonsHeight).toDouble(), screenUnit.toDouble())
        rootView!!.fragment_main_statistics_button.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenUnit.toFloat())
    }

    /**
     * making background grid
     */
    private fun setBackgroundGrid(){
        rootView!!.fragment_main_layout.background = TileDrawable((ContextCompat.getDrawable(requireContext(),R.drawable.background)!!),
            Shader.TileMode.REPEAT,screenUnit)
    }


    private fun goToChooseGameTypeFragment() {
        activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container,ChooseGameTypeFragment()).commit()
    }

    companion object{
        private const val RC_SIGN_IN = 9001
    }


    }