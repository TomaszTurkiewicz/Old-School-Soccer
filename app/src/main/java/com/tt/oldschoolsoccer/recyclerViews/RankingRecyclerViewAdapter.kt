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
import com.tt.oldschoolsoccer.classes.Static
import com.tt.oldschoolsoccer.classes.UserRanking
import com.tt.oldschoolsoccer.drawable.UserIconDrawable
import kotlinx.android.synthetic.main.ranking_row_layout.view.*

class RankingRecyclerViewAdapter(val context: Context, private val userList: List<UserRanking>, val screenUnit:Int, private val userId:String, private val sortType:Int) : RecyclerView.Adapter<RankingRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ranking_row_layout,parent,false)
        return ViewHolder(view,screenUnit,context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(userList[position].id == userId){
            holder.position.setTextColor(ContextCompat.getColor(context,R.color.icon_green_dark))
            holder.userName.setTextColor(ContextCompat.getColor(context,R.color.icon_green_dark))
            holder.totalScore.setTextColor(ContextCompat.getColor(context,R.color.icon_green_dark))
            holder.games.setTextColor(ContextCompat.getColor(context,R.color.icon_green_dark))
        }else{
            holder.position.setTextColor(ContextCompat.getColor(context,R.color.black))
            holder.userName.setTextColor(ContextCompat.getColor(context,R.color.black))
            holder.totalScore.setTextColor(ContextCompat.getColor(context,R.color.black))
            holder.games.setTextColor(ContextCompat.getColor(context,R.color.black))
        }


        holder.position.text = (position+1).toString()
        holder.userIcon.setImageDrawable(UserIconDrawable(context, (2*screenUnit).toDouble(),userList[position].icon))
        holder.userName.text = userList[position].userName

        when(sortType){
            Static.TOTAL_SORTING -> totalDisplay(holder,position)
            Static.MULTI_SORTING -> multiDisplay(holder,position)
            Static.HARD_SORTING -> hardDisplay(holder,position)
            Static.NORMAL_SORTING -> normalDisplay(holder,position)
            Static.EASY_SORTING -> easyDisplay(holder,position)
        }


    }

    private fun easyDisplay(holder: ViewHolder, position: Int) {
        holder.totalScore.text = userList[position].easyGame.toString()
        holder.games.text = userList[position].easyNoOfGames.toString()

    }

    private fun normalDisplay(holder: ViewHolder, position: Int) {
        holder.totalScore.text = userList[position].normalGame.toString()
        holder.games.text = userList[position].normalNoOfGames.toString()

    }

    private fun hardDisplay(holder: ViewHolder, position: Int) {
        holder.totalScore.text = userList[position].hardGame.toString()
        holder.games.text = userList[position].hardNoOfGames.toString()

    }

    private fun multiDisplay(holder: ViewHolder, position: Int) {
        holder.totalScore.text = userList[position].multiGame.toString()
        holder.games.text = userList[position].multiNoOfGames.toString()

    }

    private fun totalDisplay(holder: ViewHolder, position: Int) {
        holder.totalScore.text = userList[position].totalScore.toString()
        val numberOfGames = userList[position].easyNoOfGames+userList[position].normalNoOfGames+userList[position].hardNoOfGames+userList[position].multiNoOfGames
        holder.games.text = numberOfGames.toString()
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
        val layout: LinearLayout = itemView.ranking_row_l_l


        init {
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(0,screenUnit/2,0,screenUnit/2)
            layout.layoutParams = layoutParams
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