package com.projectbox.footballmatchschedule.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.projectbox.footballmatchschedule.R
import com.projectbox.footballmatchschedule.model.response.Player
import com.projectbox.footballmatchschedule.viewholder.TeamItemVH

/**
 * Created by adinugroho
 */
class PlayerAdapter(private var players: List<Player>): RecyclerView.Adapter<TeamItemVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamItemVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_team, parent, false)
        return TeamItemVH(view)
    }

    override fun getItemCount() = players.size

    override fun onBindViewHolder(holder: TeamItemVH, position: Int) {
        holder.bind(players[position])
    }

    fun update(players: List<Player>) {
        this.players = players
        notifyDataSetChanged()
    }
}