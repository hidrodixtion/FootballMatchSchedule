package com.projectbox.footballmatchschedule.pageradapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.projectbox.footballmatchschedule.model.ScheduleType
import com.projectbox.footballmatchschedule.view.ScheduleFragment
import com.projectbox.footballmatchschedule.view.TeamFragment

/**
 * Created by adinugroho
 */
class FavoritePagerAdapter(val fm: FragmentManager): FragmentPagerAdapter(fm) {
    val tabs = listOf(
            "Matches",
            "Teams"
    )

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ScheduleFragment.getInstance(ScheduleType.Favorite)
            else -> TeamFragment.getInstance()
        }
    }

    override fun getCount() = tabs.size

    override fun getPageTitle(position: Int): CharSequence? {
        return tabs[position]
    }
}