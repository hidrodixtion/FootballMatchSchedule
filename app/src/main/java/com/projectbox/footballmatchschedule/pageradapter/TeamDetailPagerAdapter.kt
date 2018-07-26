package com.projectbox.footballmatchschedule.pageradapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.projectbox.footballmatchschedule.view.TeamInfoFragment
import com.projectbox.footballmatchschedule.view.TeamPlayersFragment

/**
 * Created by adinugroho
 */
class TeamDetailPagerAdapter(val fm: FragmentManager): FragmentPagerAdapter(fm) {
    val list = listOf(
            "Overview",
            "Players"
    )

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> TeamInfoFragment.getInstance()
            else -> TeamPlayersFragment.getInstance()
        }
    }

    override fun getCount() = list.size

    override fun getPageTitle(position: Int) = list[position]
}