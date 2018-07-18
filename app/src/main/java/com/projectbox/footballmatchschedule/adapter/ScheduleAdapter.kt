package com.projectbox.footballmatchschedule.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.projectbox.footballmatchschedule.R
import com.projectbox.footballmatchschedule.event.ScheduleClickEvent
import com.projectbox.footballmatchschedule.model.Schedule
import com.projectbox.footballmatchschedule.viewholder.ScheduleItemVH
import org.greenrobot.eventbus.EventBus
import timber.log.Timber

/**
 * Created by adinugroho
 */
class ScheduleAdapter(private var mList: List<Schedule>): RecyclerView.Adapter<ScheduleItemVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ScheduleItemVH(LayoutInflater.from(parent.context).inflate(R.layout.item_schedule, parent, false))

    override fun getItemCount() = mList.size

    override fun onBindViewHolder(holder: ScheduleItemVH, position: Int) {
        holder.bind(mList[position])

        holder.containerView.setOnClickListener {
            EventBus.getDefault().post(ScheduleClickEvent(mList[position]))
        }
    }

    fun update(list: List<Schedule>) {
        mList = list
        Timber.v(mList.size.toString())
        notifyDataSetChanged()
    }
}