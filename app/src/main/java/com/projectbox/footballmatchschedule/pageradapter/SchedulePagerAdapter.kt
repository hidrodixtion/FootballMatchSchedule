package com.projectbox.footballmatchschedule.pageradapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.projectbox.footballmatchschedule.model.ScheduleType
import com.projectbox.footballmatchschedule.view.ScheduleFragment

/**
 * Created by adinugroho
 */
class SchedulePagerAdapter(val fm: FragmentManager): FragmentPagerAdapter(fm) {
    val schedules = listOf(
            ScheduleType.Next,
            ScheduleType.Past
    )

    override fun getItem(position: Int): Fragment {
        return ScheduleFragment.getInstance(schedules[position])
    }

    override fun getCount(): Int = schedules.size

    override fun getPageTitle(position: Int): CharSequence? = schedules[position].name
}