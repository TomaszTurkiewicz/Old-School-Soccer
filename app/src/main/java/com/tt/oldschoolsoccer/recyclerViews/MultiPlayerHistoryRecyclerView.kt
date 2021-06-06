package com.tt.oldschoolsoccer.recyclerViews

import android.content.Context
import android.graphics.Shader
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tt.oldschoolsoccer.R
import com.tt.oldschoolsoccer.classes.MultiPlayerHistory
import com.tt.oldschoolsoccer.classes.SingleUserHistory
import com.tt.oldschoolsoccer.classes.UserIconColors
import com.tt.oldschoolsoccer.drawable.TileDrawable
import com.tt.oldschoolsoccer.drawable.UserIconDrawable
import kotlinx.android.synthetic.main.multi_player_invite_list_row_layout.view.*

class MultiPlayerHistoryRecyclerView(val context: Context, private val historyUsers:MultiPlayerHistory, val screenUnit: Int, private val onClickListener:(View, SingleUserHistory)->Unit) : RecyclerView.Adapter<MultiPlayerHistoryRecyclerView.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.multi_player_invite_list_row_layout,parent,false)
        return ViewHolder(context,view,screenUnit)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userHistory = historyUsers.history[position]
        holder.icon.setImageDrawable(UserIconDrawable(context,(3*screenUnit).toDouble(),userHistory.icon!!))
        holder.userName.text = userHistory.name!!
        holder.score.text = userHistory.score().toString()+"% / "
        holder.numberOfGames.text = userHistory.noOfGames.toString()
        holder.inviteButton.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.play))
        holder.inviteButton.setOnClickListener { v ->
            onClickListener.invoke(v,userHistory)
        }
    }

    override fun getItemCount(): Int {
        return historyUsers.history.size
    }



    class ViewHolder(context:Context, itemView:View, screenUnit:Int):RecyclerView.ViewHolder(itemView){
        val layout : ConstraintLayout = itemView.multi_player_invite_list_row_layout
        val icon : ImageView = itemView.multi_player_invite_list_row_user_icon
        val userName: TextView = itemView.multi_player_invite_list_row_user_name_tv
        val numberOfGames: TextView = itemView.multi_player_invite_list_row_number_of_games
        val score: TextView = itemView.multi_player_invite_list_row_score
        val inviteButton: ImageView = itemView.multi_player_invite_list_row_invite_button
        private val statisticsLinearLayout: LinearLayout = itemView.multi_player_invite_list_row_statistics_linear_layout

        init {

            layout.layoutParams = ConstraintLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,5*screenUnit)
            icon.layoutParams = ConstraintLayout.LayoutParams(3*screenUnit,3*screenUnit)
            inviteButton.layoutParams = ConstraintLayout.LayoutParams(3*screenUnit,3*screenUnit)
            userName.layoutParams = ConstraintLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            userName.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenUnit.toFloat())

            score.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,screenUnit)
            score.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenUnit.toFloat())

            numberOfGames.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,screenUnit)
            numberOfGames.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenUnit.toFloat())

            statisticsLinearLayout.layoutParams = ConstraintLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)

            layout.background = TileDrawable((ContextCompat.getDrawable(context, R.drawable.background)!!),
                Shader.TileMode.REPEAT,screenUnit)

            val set = ConstraintSet()
            set.clone(itemView.multi_player_invite_list_row_layout)

            set.connect(itemView.multi_player_invite_list_row_invite_button.id,
                ConstraintSet.TOP,itemView.multi_player_invite_list_row_layout.id,
                ConstraintSet.TOP,screenUnit)
            set.connect(itemView.multi_player_invite_list_row_invite_button.id,
                ConstraintSet.LEFT,itemView.multi_player_invite_list_row_layout.id,
                ConstraintSet.LEFT,screenUnit)

            set.connect(itemView.multi_player_invite_list_row_user_icon.id,
                ConstraintSet.TOP,itemView.multi_player_invite_list_row_layout.id,
                ConstraintSet.TOP,screenUnit)
            set.connect(itemView.multi_player_invite_list_row_user_icon.id,
                ConstraintSet.LEFT,itemView.multi_player_invite_list_row_layout.id,
                ConstraintSet.LEFT,5*screenUnit)

            set.connect(itemView.multi_player_invite_list_row_user_name_tv.id,
                ConstraintSet.TOP,itemView.multi_player_invite_list_row_layout.id,
                ConstraintSet.TOP,screenUnit)
            set.connect(itemView.multi_player_invite_list_row_user_name_tv.id,
                ConstraintSet.RIGHT,itemView.multi_player_invite_list_row_layout.id,
                ConstraintSet.RIGHT,screenUnit)
            set.connect(itemView.multi_player_invite_list_row_user_name_tv.id,
                ConstraintSet.LEFT,itemView.multi_player_invite_list_row_user_icon.id,
                ConstraintSet.RIGHT,screenUnit)

            set.connect(itemView.multi_player_invite_list_row_statistics_linear_layout.id,
                ConstraintSet.TOP,itemView.multi_player_invite_list_row_layout.id,
                ConstraintSet.TOP,3*screenUnit)
            set.connect(itemView.multi_player_invite_list_row_statistics_linear_layout.id,
                ConstraintSet.RIGHT,itemView.multi_player_invite_list_row_layout.id,
                ConstraintSet.RIGHT,screenUnit)
            set.connect(itemView.multi_player_invite_list_row_statistics_linear_layout.id,
                ConstraintSet.LEFT,itemView.multi_player_invite_list_row_user_icon.id,
                ConstraintSet.RIGHT,screenUnit)

            set.applyTo(itemView.multi_player_invite_list_row_layout)
        }
    }
}