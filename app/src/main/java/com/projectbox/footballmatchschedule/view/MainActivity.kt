package com.projectbox.footballmatchschedule.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.internal.BottomNavigationMenu
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import com.projectbox.footballmatchschedule.R
import com.projectbox.footballmatchschedule.event.ScheduleClickEvent
import com.projectbox.footballmatchschedule.model.ScheduleType
import com.projectbox.footballmatchschedule.pageradapter.SchedulePagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.startActivity
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navbar.setOnNavigationItemSelectedListener(onNavigationItemSelected)
        bottom_navbar.selectedItemId = R.id.menu_schedule

        initUI()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    private val onNavigationItemSelected = BottomNavigationView.OnNavigationItemSelectedListener {
        return@OnNavigationItemSelectedListener when(it.itemId) {
            R.id.menu_schedule -> {
                openViewPager(SchedulePagerAdapter(supportFragmentManager))
                true
            }
            R.id.menu_team -> {
//                currentSchedule = ScheduleType.Next
                true
            }
            R.id.menu_favorite -> {
//                currentSchedule = ScheduleType.Favorite
                true
            }
            else -> false
        }
    }

    private fun initUI() {
        tab.setupWithViewPager(view_pager)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Football Match App"
    }

    private fun openViewPager(adapter: FragmentPagerAdapter) {
        view_pager.removeAllViews()
        view_pager.adapter = adapter
    }

    @Subscribe
    fun onScheduleClickEvent(e: ScheduleClickEvent) {
        startActivity<DetailActivity>(DetailActivity.EXT_SCHEDULE to e.schedule)
    }
}
