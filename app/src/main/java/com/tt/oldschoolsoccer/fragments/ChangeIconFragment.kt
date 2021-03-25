package com.tt.oldschoolsoccer.fragments

import android.graphics.Shader
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tt.oldschoolsoccer.R
import com.tt.oldschoolsoccer.classes.*
import com.tt.oldschoolsoccer.database.UserDB
import com.tt.oldschoolsoccer.database.UserDBDatabase
import com.tt.oldschoolsoccer.drawable.ButtonDrawable
import com.tt.oldschoolsoccer.drawable.TileDrawable
import com.tt.oldschoolsoccer.drawable.UserIconDrawable
import kotlinx.android.synthetic.main.fragment_change_icon.view.*
import kotlinx.coroutines.launch


class ChangeIconFragment : FragmentCoroutine() {

    private lateinit var rootView:View
    private var screenUnit = 0
    private var loggedInStatus = LoggedInStatus()
    private var imageSize = 0
    private lateinit var user:User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenUnit = Functions.readScreenUnit(requireContext())
        loggedInStatus = Functions.readLoggedStateFromSharedPreferences(requireContext())


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        rootView = inflater.inflate(R.layout.fragment_change_icon, container, false)

        makeUI()

        rootView.fragment_change_icon_back_button.setOnClickListener {
            goBackToSettingsFragment()
        }

        rootView.fragment_change_icon_left_arrow_background.setOnClickListener {
            user.icon.minusOneFromBackgroundColor()
            updateUserDB()
            updateUI()
        }

        rootView.fragment_change_icon_right_arrow_background.setOnClickListener {
            user.icon.addOneToBackgroundColor()
            updateUserDB()
            updateUI()
        }

        rootView.fragment_change_icon_right_arrow_over_arms.setOnClickListener {
            user.icon.addOneToOverArmsColor()
            updateUserDB()
            updateUI()
        }

        rootView.fragment_change_icon_left_arrow_over_arms.setOnClickListener {
            user.icon.minusOneFromArmsColor()
            updateUserDB()
            updateUI()
        }

        rootView.fragment_change_icon_right_arrow_left_sleeve.setOnClickListener {
            user.icon.addOneToLeftSleeveColor()
            updateUserDB()
            updateUI()
        }

        rootView.fragment_change_icon_left_arrow_left_sleeve.setOnClickListener {
            user.icon.minusOneFromLeftSleeveColor()
            updateUserDB()
            updateUI()
        }

        rootView.fragment_change_icon_right_arrow_right_sleeve.setOnClickListener {
            user.icon.addOneToRightSleeveColor()
            updateUserDB()
            updateUI()
        }

        rootView.fragment_change_icon_left_arrow_right_sleeve.setOnClickListener {
            user.icon.minusOneFromRightSleeveColor()
            updateUserDB()
            updateUI()
        }

        rootView.fragment_change_icon_right_arrow_left_shirt.setOnClickListener {
            user.icon.addOneToLeftShirtColor()
            updateUserDB()
            updateUI()
        }

        rootView.fragment_change_icon_left_arrow_left_shirt.setOnClickListener {
            user.icon.minusOneFromLeftShirtColor()
            updateUserDB()
            updateUI()
        }

        rootView.fragment_change_icon_right_arrow_right_shirt.setOnClickListener {
            user.icon.addOneToRightShirtColor()
            updateUserDB()
            updateUI()
        }

        rootView.fragment_change_icon_left_arrow_right_shirt.setOnClickListener {
            user.icon.minusOneFromRightShirtColor()
            updateUserDB()
            updateUI()
        }

        rootView.fragment_change_icon_right_arrow_trousers.setOnClickListener {
            user.icon.addOneToTrouserColor()
            updateUserDB()
            updateUI()
        }

        rootView.fragment_change_icon_left_arrow_trousers.setOnClickListener {
            user.icon.minusOneFromTrouserColor()
            updateUserDB()
            updateUI()
        }

        rootView.fragment_change_icon_right_arrow_trousers_external.setOnClickListener {
            user.icon.addOneToTrouserExternalColor()
            updateUserDB()
            updateUI()
        }

        rootView.fragment_change_icon_left_arrow_trousers_external.setOnClickListener {
            user.icon.minusOneFromTrouserExternalColor()
            updateUserDB()
            updateUI()
        }

        rootView.fragment_change_icon_right_arrow_trousers_internal.setOnClickListener {
            user.icon.addOneToTrouserInternalColor()
            updateUserDB()
            updateUI()
        }

        rootView.fragment_change_icon_left_arrow_trousers_internal.setOnClickListener {
            user.icon.minusOneFromTrouserInternalColor()
            updateUserDB()
            updateUI()
        }

        return rootView
    }

    private fun goBackToSettingsFragment() {
        activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container,SettingsFragment()).commit()

    }

    private fun updateUserDB() {
        launch {
            requireContext().let {
                val userDB = UserDB().dbUser(user)
                UserDBDatabase(it).getUserDBDao().updateUserInDB(userDB)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        launch {
            requireContext().let {
                val userDB = UserDBDatabase(it).getUserDBDao().getUser(loggedInStatus.userid)
                user = User().userFromDB(userDB)
                updateUI()


            }
        }
    }

    private fun updateUI() {
        rootView.fragment_change_icon_image_view.setImageDrawable(UserIconDrawable(requireContext(), (imageSize).toDouble(), user.icon))


    }


    private fun makeUI() {
        setBackgroundGrid()
        setSizes()
        setButtonsUI()
        makeConstraintLayout()



    }





    private fun makeConstraintLayout() {
        val set = ConstraintSet()
        set.clone(rootView.fragment_change_icon_layout)

        set.connect(rootView.fragment_change_icon_image_view.id,ConstraintSet.TOP,rootView.fragment_change_icon_layout.id,ConstraintSet.TOP,screenUnit)
        set.connect(rootView.fragment_change_icon_image_view.id,ConstraintSet.LEFT,rootView.fragment_change_icon_layout.id,ConstraintSet.LEFT,0)
        set.connect(rootView.fragment_change_icon_image_view.id,ConstraintSet.RIGHT,rootView.fragment_change_icon_layout.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_change_icon_left_arrow_background.id,ConstraintSet.TOP,rootView.fragment_change_icon_image_view.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView.fragment_change_icon_left_arrow_background.id,ConstraintSet.LEFT,rootView.fragment_change_icon_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView.fragment_change_icon_background_id.id,ConstraintSet.TOP,rootView.fragment_change_icon_left_arrow_background.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_change_icon_background_id.id,ConstraintSet.LEFT,rootView.fragment_change_icon_left_arrow_background.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_change_icon_right_arrow_background.id,ConstraintSet.TOP,rootView.fragment_change_icon_background_id.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_change_icon_right_arrow_background.id,ConstraintSet.LEFT,rootView.fragment_change_icon_background_id.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_change_icon_left_arrow_over_arms.id,ConstraintSet.TOP,rootView.fragment_change_icon_left_arrow_background.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView.fragment_change_icon_left_arrow_over_arms.id,ConstraintSet.LEFT,rootView.fragment_change_icon_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView.fragment_change_icon_over_arms_id.id,ConstraintSet.TOP,rootView.fragment_change_icon_left_arrow_over_arms.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_change_icon_over_arms_id.id,ConstraintSet.LEFT,rootView.fragment_change_icon_left_arrow_over_arms.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_change_icon_right_arrow_over_arms.id,ConstraintSet.TOP,rootView.fragment_change_icon_over_arms_id.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_change_icon_right_arrow_over_arms.id,ConstraintSet.LEFT,rootView.fragment_change_icon_over_arms_id.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_change_icon_left_arrow_left_sleeve.id,ConstraintSet.TOP,rootView.fragment_change_icon_left_arrow_over_arms.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView.fragment_change_icon_left_arrow_left_sleeve.id,ConstraintSet.LEFT,rootView.fragment_change_icon_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView.fragment_change_icon_left_sleeve_id.id,ConstraintSet.TOP,rootView.fragment_change_icon_left_arrow_left_sleeve.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_change_icon_left_sleeve_id.id,ConstraintSet.LEFT,rootView.fragment_change_icon_left_arrow_left_sleeve.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_change_icon_right_arrow_left_sleeve.id,ConstraintSet.TOP,rootView.fragment_change_icon_left_sleeve_id.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_change_icon_right_arrow_left_sleeve.id,ConstraintSet.LEFT,rootView.fragment_change_icon_left_sleeve_id.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_change_icon_left_arrow_right_sleeve.id,ConstraintSet.TOP,rootView.fragment_change_icon_left_arrow_left_sleeve.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView.fragment_change_icon_left_arrow_right_sleeve.id,ConstraintSet.LEFT,rootView.fragment_change_icon_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView.fragment_change_icon_right_sleeve_id.id,ConstraintSet.TOP,rootView.fragment_change_icon_left_arrow_right_sleeve.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_change_icon_right_sleeve_id.id,ConstraintSet.LEFT,rootView.fragment_change_icon_left_arrow_right_sleeve.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_change_icon_right_arrow_right_sleeve.id,ConstraintSet.TOP,rootView.fragment_change_icon_right_sleeve_id.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_change_icon_right_arrow_right_sleeve.id,ConstraintSet.LEFT,rootView.fragment_change_icon_right_sleeve_id.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_change_icon_left_arrow_left_shirt.id,ConstraintSet.TOP,rootView.fragment_change_icon_left_arrow_right_sleeve.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView.fragment_change_icon_left_arrow_left_shirt.id,ConstraintSet.LEFT,rootView.fragment_change_icon_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView.fragment_change_icon_left_shirt_id.id,ConstraintSet.TOP,rootView.fragment_change_icon_left_arrow_left_shirt.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_change_icon_left_shirt_id.id,ConstraintSet.LEFT,rootView.fragment_change_icon_left_arrow_left_shirt.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_change_icon_right_arrow_left_shirt.id,ConstraintSet.TOP,rootView.fragment_change_icon_left_shirt_id.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_change_icon_right_arrow_left_shirt.id,ConstraintSet.LEFT,rootView.fragment_change_icon_left_shirt_id.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_change_icon_left_arrow_right_shirt.id,ConstraintSet.TOP,rootView.fragment_change_icon_left_arrow_left_shirt.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView.fragment_change_icon_left_arrow_right_shirt.id,ConstraintSet.LEFT,rootView.fragment_change_icon_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView.fragment_change_icon_right_shirt_id.id,ConstraintSet.TOP,rootView.fragment_change_icon_left_arrow_right_shirt.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_change_icon_right_shirt_id.id,ConstraintSet.LEFT,rootView.fragment_change_icon_left_arrow_right_shirt.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_change_icon_right_arrow_right_shirt.id,ConstraintSet.TOP,rootView.fragment_change_icon_right_shirt_id.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_change_icon_right_arrow_right_shirt.id,ConstraintSet.LEFT,rootView.fragment_change_icon_right_shirt_id.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_change_icon_left_arrow_trousers.id,ConstraintSet.TOP,rootView.fragment_change_icon_left_arrow_right_shirt.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView.fragment_change_icon_left_arrow_trousers.id,ConstraintSet.LEFT,rootView.fragment_change_icon_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView.fragment_change_icon_trousers_id.id,ConstraintSet.TOP,rootView.fragment_change_icon_left_arrow_trousers.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_change_icon_trousers_id.id,ConstraintSet.LEFT,rootView.fragment_change_icon_left_arrow_trousers.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_change_icon_right_arrow_trousers.id,ConstraintSet.TOP,rootView.fragment_change_icon_trousers_id.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_change_icon_right_arrow_trousers.id,ConstraintSet.LEFT,rootView.fragment_change_icon_trousers_id.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_change_icon_left_arrow_trousers_external.id,ConstraintSet.TOP,rootView.fragment_change_icon_left_arrow_trousers.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView.fragment_change_icon_left_arrow_trousers_external.id,ConstraintSet.LEFT,rootView.fragment_change_icon_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView.fragment_change_icon_trousers_external_id.id,ConstraintSet.TOP,rootView.fragment_change_icon_left_arrow_trousers_external.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_change_icon_trousers_external_id.id,ConstraintSet.LEFT,rootView.fragment_change_icon_left_arrow_trousers_external.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_change_icon_right_arrow_trousers_external.id,ConstraintSet.TOP,rootView.fragment_change_icon_trousers_external_id.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_change_icon_right_arrow_trousers_external.id,ConstraintSet.LEFT,rootView.fragment_change_icon_trousers_external_id.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_change_icon_left_arrow_trousers_internal.id,ConstraintSet.TOP,rootView.fragment_change_icon_left_arrow_trousers_external.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(rootView.fragment_change_icon_left_arrow_trousers_internal.id,ConstraintSet.LEFT,rootView.fragment_change_icon_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(rootView.fragment_change_icon_trousers_internal_id.id,ConstraintSet.TOP,rootView.fragment_change_icon_left_arrow_trousers_internal.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_change_icon_trousers_internal_id.id,ConstraintSet.LEFT,rootView.fragment_change_icon_left_arrow_trousers_internal.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_change_icon_right_arrow_trousers_internal.id,ConstraintSet.TOP,rootView.fragment_change_icon_trousers_internal_id.id,ConstraintSet.TOP,0)
        set.connect(rootView.fragment_change_icon_right_arrow_trousers_internal.id,ConstraintSet.LEFT,rootView.fragment_change_icon_trousers_internal_id.id,ConstraintSet.RIGHT,0)

        set.connect(rootView.fragment_change_icon_back_button.id,ConstraintSet.TOP,rootView.fragment_change_icon_layout.id,ConstraintSet.TOP,2*screenUnit)
        set.connect(rootView.fragment_change_icon_back_button.id,ConstraintSet.LEFT,rootView.fragment_change_icon_layout.id,ConstraintSet.LEFT,14*screenUnit)













        set.applyTo(rootView.fragment_change_icon_layout)
    }

    private fun setButtonsUI() {

        rootView.fragment_change_icon_left_arrow_background.background = ContextCompat.getDrawable(requireContext(),R.drawable.left)
        rootView.fragment_change_icon_right_arrow_background.background = ContextCompat.getDrawable(requireContext(),R.drawable.right)

        rootView.fragment_change_icon_left_arrow_over_arms.background = ContextCompat.getDrawable(requireContext(),R.drawable.left)
        rootView.fragment_change_icon_right_arrow_over_arms.background = ContextCompat.getDrawable(requireContext(),R.drawable.right)

        rootView.fragment_change_icon_left_arrow_left_sleeve.background = ContextCompat.getDrawable(requireContext(),R.drawable.left)
        rootView.fragment_change_icon_right_arrow_left_sleeve.background = ContextCompat.getDrawable(requireContext(),R.drawable.right)

        rootView.fragment_change_icon_left_arrow_right_sleeve.background = ContextCompat.getDrawable(requireContext(),R.drawable.left)
        rootView.fragment_change_icon_right_arrow_right_sleeve.background = ContextCompat.getDrawable(requireContext(),R.drawable.right)

        rootView.fragment_change_icon_left_arrow_left_shirt.background = ContextCompat.getDrawable(requireContext(),R.drawable.left)
        rootView.fragment_change_icon_right_arrow_left_shirt.background = ContextCompat.getDrawable(requireContext(),R.drawable.right)

        rootView.fragment_change_icon_left_arrow_right_shirt.background = ContextCompat.getDrawable(requireContext(),R.drawable.left)
        rootView.fragment_change_icon_right_arrow_right_shirt.background = ContextCompat.getDrawable(requireContext(),R.drawable.right)

        rootView.fragment_change_icon_left_arrow_trousers.background = ContextCompat.getDrawable(requireContext(),R.drawable.left)
        rootView.fragment_change_icon_right_arrow_trousers.background = ContextCompat.getDrawable(requireContext(),R.drawable.right)

        rootView.fragment_change_icon_left_arrow_trousers_external.background = ContextCompat.getDrawable(requireContext(),R.drawable.left)
        rootView.fragment_change_icon_right_arrow_trousers_external.background = ContextCompat.getDrawable(requireContext(),R.drawable.right)

        rootView.fragment_change_icon_left_arrow_trousers_internal.background = ContextCompat.getDrawable(requireContext(),R.drawable.left)
        rootView.fragment_change_icon_right_arrow_trousers_internal.background = ContextCompat.getDrawable(requireContext(),R.drawable.right)


    }

    private fun setSizes() {
        imageSize = 6*screenUnit

        rootView.fragment_change_icon_back_button.layoutParams = ConstraintLayout.LayoutParams(4*screenUnit,2*screenUnit)
        rootView.fragment_change_icon_back_button.background = ButtonDrawable(requireContext(), (4*screenUnit).toDouble(), (2*screenUnit).toDouble(), screenUnit.toDouble())
        rootView.fragment_change_icon_back_button.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenUnit.toFloat())

        rootView.fragment_change_icon_image_view.layoutParams = ConstraintLayout.LayoutParams(imageSize,imageSize)

        rootView.fragment_change_icon_left_arrow_background.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_change_icon_background_id.layoutParams = ConstraintLayout.LayoutParams(14*screenUnit,2*screenUnit)
        rootView.fragment_change_icon_right_arrow_background.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)

        rootView.fragment_change_icon_left_arrow_over_arms.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_change_icon_over_arms_id.layoutParams = ConstraintLayout.LayoutParams(14*screenUnit,2*screenUnit)
        rootView.fragment_change_icon_right_arrow_over_arms.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)

        rootView.fragment_change_icon_left_arrow_left_sleeve.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_change_icon_left_sleeve_id.layoutParams = ConstraintLayout.LayoutParams(14*screenUnit,2*screenUnit)
        rootView.fragment_change_icon_right_arrow_left_sleeve.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)

        rootView.fragment_change_icon_left_arrow_right_sleeve.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_change_icon_right_sleeve_id.layoutParams = ConstraintLayout.LayoutParams(14*screenUnit,2*screenUnit)
        rootView.fragment_change_icon_right_arrow_right_sleeve.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)

        rootView.fragment_change_icon_left_arrow_left_shirt.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_change_icon_left_shirt_id.layoutParams = ConstraintLayout.LayoutParams(14*screenUnit,2*screenUnit)
        rootView.fragment_change_icon_right_arrow_left_shirt.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)

        rootView.fragment_change_icon_left_arrow_right_shirt.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_change_icon_right_shirt_id.layoutParams = ConstraintLayout.LayoutParams(14*screenUnit,2*screenUnit)
        rootView.fragment_change_icon_right_arrow_right_shirt.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)

        rootView.fragment_change_icon_left_arrow_trousers.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_change_icon_trousers_id.layoutParams = ConstraintLayout.LayoutParams(14*screenUnit,2*screenUnit)
        rootView.fragment_change_icon_right_arrow_trousers.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)

        rootView.fragment_change_icon_left_arrow_trousers_external.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_change_icon_trousers_external_id.layoutParams = ConstraintLayout.LayoutParams(14*screenUnit,2*screenUnit)
        rootView.fragment_change_icon_right_arrow_trousers_external.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)

        rootView.fragment_change_icon_left_arrow_trousers_internal.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
        rootView.fragment_change_icon_trousers_internal_id.layoutParams = ConstraintLayout.LayoutParams(14*screenUnit,2*screenUnit)
        rootView.fragment_change_icon_right_arrow_trousers_internal.layoutParams = ConstraintLayout.LayoutParams(2*screenUnit,2*screenUnit)
    }

    private fun setBackgroundGrid() {
        rootView.fragment_change_icon_layout.background = TileDrawable((ContextCompat.getDrawable(requireContext(),R.drawable.background)!!),
                Shader.TileMode.REPEAT,screenUnit)
    }


}