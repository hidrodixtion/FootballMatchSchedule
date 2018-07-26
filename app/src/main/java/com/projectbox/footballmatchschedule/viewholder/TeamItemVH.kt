package com.projectbox.footballmatchschedule.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.projectbox.footballmatchschedule.model.Team
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_team.*

/**
 * Created by adinugroho
 */
class TeamItemVH(override val containerView: View): RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(item: Team) {
        Glide.with(containerView).load(item.teamBadge).into(img_badge)
        txt_name.text = item.teamName
    }
}