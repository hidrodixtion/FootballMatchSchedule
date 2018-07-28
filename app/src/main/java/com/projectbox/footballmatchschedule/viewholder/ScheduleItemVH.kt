package com.projectbox.footballmatchschedule.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.projectbox.footballmatchschedule.helper.DateConverter
import com.projectbox.footballmatchschedule.model.response.Schedule
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_schedule.*

/**
 * Created by adinugroho
 */
class ScheduleItemVH(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(schedule: Schedule) {
        txt_home.text = schedule.homeTeam
        txt_away.text = schedule.awayTeam

        txt_home_score.text = schedule.homeScore?.toString()
        txt_away_score.text = schedule.awayScore?.toString()

        txt_date.text = DateConverter.convertFromScheduleDate(schedule.date)
        txt_time.text = DateConverter.convertFromScheduleTime(schedule.time)
    }
}