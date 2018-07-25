package com.projectbox.footballmatchschedule.pageradapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by adinugroho
 */
class TeamPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return Fragment()
    }

    override fun getCount(): Int = 1
}