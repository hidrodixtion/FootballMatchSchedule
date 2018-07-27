package com.projectbox.footballmatchschedule.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.projectbox.footballmatchschedule.R
import com.projectbox.footballmatchschedule.event.PlayerClickEvent
import com.projectbox.footballmatchschedule.model.response.Player
import com.projectbox.footballmatchschedule.viewholder.PlayerItemVH
import org.greenrobot.eventbus.EventBus

/**
 * Created by adinugroho
 */
class PlayerAdapter(private var players: List<Player>): RecyclerView.Adapter<PlayerItemVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerItemVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_player, parent, false)
        return PlayerItemVH(view)
    }

    override fun getItemCount() = players.size

    override fun onBindViewHolder(holder: PlayerItemVH, position: Int) {
        holder.bind(players[position])

        holder.containerView.setOnClickListener {
            EventBus.getDefault().post(PlayerClickEvent(players[position]))
        }
    }

    fun update(players: List<Player>) {
        this.players = players
        notifyDataSetChanged()
    }
}