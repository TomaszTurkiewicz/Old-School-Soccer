package com.tt.oldschoolsoccer.recyclerViews

import android.content.Context
import android.util.TypedValue
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
import kotlinx.android.synthetic.main.ranking_row_layout.view.*

class RankingRecyclerViewAdapter(val context: Context, private val userList: List<UserRanking>, val screenUnit:Int, val userId:String?) : RecyclerView.Adapter<RankingRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ranking_row_layout,parent,false)
        return ViewHolder(view,screenUnit,context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.position.text = "999999"
        holder.userName.text = userList[position].userName
        holder.totalScore.text = "88.88%"
        holder.games.text = "999999"
        holder.userIcon.setImageDrawable(UserIconDrawable(context, (2*screenUnit).toDouble(),userList[position].icon))
    }

    override fun getItemCount(): Int {
       return userList.size
    }



    class ViewHolder(itemView: View, val screenUnit: Int, val context: Context) : RecyclerView.ViewHolder(itemView) {

        val position: TextView = itemView.ranking_row_position_text_view
        val totalScore: TextView = itemView.ranking_row_score_text_view
        val userIcon: ImageView = itemView.ranking_row_icon_image_view
        val userName: TextView = itemView.ranking_row_user_name_text_view
        val games: TextView = itemView.ranking_row_games_text_view


        init {
            position.layoutParams = LinearLayout.LayoutParams(3*screenUnit,2*screenUnit)
            totalScore.layoutParams = LinearLayout.LayoutParams(3*screenUnit,2*screenUnit)
            userIcon.layoutParams = LinearLayout.LayoutParams(2*screenUnit,2*screenUnit)
            userName.layoutParams = LinearLayout.LayoutParams(9*screenUnit,2*screenUnit)
            games.layoutParams = LinearLayout.LayoutParams(3*screenUnit,2*screenUnit)
            position.setTextSize(TypedValue.COMPLEX_UNIT_PX,(0.8*screenUnit).toFloat())
            totalScore.setTextSize(TypedValue.COMPLEX_UNIT_PX,(0.8*screenUnit).toFloat())
            userName.setTextSize(TypedValue.COMPLEX_UNIT_PX,(0.8*screenUnit).toFloat())
            games.setTextSize(TypedValue.COMPLEX_UNIT_PX,(0.8*screenUnit).toFloat())
            position.setTextColor(ContextCompat.getColor(context,R.color.black))
            totalScore.setTextColor(ContextCompat.getColor(context,R.color.black))
            userName.setTextColor(ContextCompat.getColor(context,R.color.black))
            games.setTextColor(ContextCompat.getColor(context,R.color.black))

        }




    }
}