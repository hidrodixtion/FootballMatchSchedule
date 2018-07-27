package com.projectbox.footballmatchschedule.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.projectbox.footballmatchschedule.model.response.Player
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_player.*

/**
 * Created by adinugroho
 */
class PlayerItemVH(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(player: Player) {
        Glide.with(containerView).load(player.photo).into(img_badge)
        txt_name.text = player.name
        txt_position.text = player.position
    }
}