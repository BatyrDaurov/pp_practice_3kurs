package com.example.destinationdetective

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GameSummeryAdapter(private val dataList:ArrayList<PlaceModel>):
    RecyclerView.Adapter<GameSummeryAdapter.ViewHolder>()
{
    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val tvRound:TextView = view.findViewById(R.id.tv_summery_round)
        val tvDistance:TextView = view.findViewById(R.id.tv_summery_distance)
        val tvScore:TextView = view.findViewById(R.id.tv_summery_score)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.game_summery_list,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val distance = dataList[position].distance
        val score = dataList[position].score
        holder.tvRound.text = (position+1).toString()
        holder.tvDistance.text = "${Math.round(distance * 1.609)} км"
        holder.tvScore.text = "$score очков"
    }
}