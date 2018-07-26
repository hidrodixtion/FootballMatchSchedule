package com.projectbox.footballmatchschedule.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.projectbox.footballmatchschedule.R
import com.projectbox.footballmatchschedule.model.Team
import com.projectbox.footballmatchschedule.viewholder.TeamItemVH

/**
 * Created by adinugroho
 */
class TeamAdapter(private var teams: List<Team>) : RecyclerView.Adapter<TeamItemVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamItemVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_team, parent, false)
        return TeamItemVH(view)
    }

    override fun getItemCount() = teams.size

    override fun onBindViewHolder(holder: TeamItemVH, position: Int) {
        holder.bind(teams[position])
    }

    fun updateTeams(teams: List<Team>) {
        this.teams = teams
        notifyDataSetChanged()
    }

    fun clearItems() {
        this.teams = emptyList()
        notifyDataSetChanged()
    }
}