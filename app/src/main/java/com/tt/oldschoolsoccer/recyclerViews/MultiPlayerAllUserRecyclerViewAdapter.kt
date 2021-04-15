package com.tt.oldschoolsoccer.recyclerViews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tt.oldschoolsoccer.R
import com.tt.oldschoolsoccer.classes.UserRanking
import com.tt.oldschoolsoccer.drawable.UserIconDrawable
import kotlinx.android.synthetic.main.multi_player_invite_list_row_layout.view.*
import org.w3c.dom.Text

class MultiPlayerAllUserRecyclerViewAdapter(val context: Context, private val allUserList: List<UserRanking>, val screenUnit:Int) : RecyclerView.Adapter<MultiPlayerAllUserRecyclerViewAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.multi_player_invite_list_row_layout,parent,false)
        return ViewHolder(view,screenUnit)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.icon.setImageDrawable(UserIconDrawable(context, (3*screenUnit).toDouble(),allUserList[position].icon))
        holder.userName.text = allUserList[position].userName
        holder.score.text=allUserList[position].totalScore.toString()
        val numberOfGames = allUserList[position].easyNoOfGames+allUserList[position].normalNoOfGames+allUserList[position].hardNoOfGames+allUserList[position].multiNoOfGames
        holder.numberOfGames.text = numberOfGames.toString()
        holder.inviteButton.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.invite_icon))
    }

    override fun getItemCount(): Int {
        return allUserList.size
    }

    class ViewHolder(itemView: View, screenUnit: Int) : RecyclerView.ViewHolder(itemView){
        val layout : LinearLayout = itemView.multi_player_invite_list_row_layout
        val icon : ImageView = itemView.multi_player_invite_list_row_user_icon
        val userName: TextView = itemView.multi_player_invite_list_row_user_name_tv
        val numberOfGames: TextView = itemView.multi_player_invite_list_row_number_of_games
        val score: TextView = itemView.multi_player_invite_list_row_score
        val inviteButton: ImageView = itemView.multi_player_invite_list_row_invite_button

        init {
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,5*screenUnit)
            layout.layoutParams = layoutParams
            val iconParams = LinearLayout.LayoutParams(3*screenUnit,3*screenUnit)
            iconParams.setMargins(screenUnit,screenUnit,screenUnit,screenUnit)
            icon.layoutParams = iconParams
        }

    }
}