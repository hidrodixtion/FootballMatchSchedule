package com.projectbox.footballmatchschedule.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.internal.BottomNavigationMenu
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import com.projectbox.footballmatchschedule.R
import com.projectbox.footballmatchschedule.event.ScheduleClickEvent
import com.projectbox.footballmatchschedule.model.ScheduleType
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.startActivity
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private var currentSchedule: ScheduleType = ScheduleType.Past
        set(value) {
            val fragment = ScheduleFragment.getInstance(value)
            openFragment(fragment)

            when(value) {
                ScheduleType.Past -> supportActionBar?.title = "Past Schedules"
                ScheduleType.Next -> supportActionBar?.title = "Next Schedules"
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navbar.setOnNavigationItemSelectedListener(onNavigationItemSelected)

        currentSchedule = ScheduleType.Past
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
            R.id.menu_prev -> {
                currentSchedule = ScheduleType.Past
                true
            }
            R.id.menu_next -> {
                currentSchedule = ScheduleType.Next
                true
            }
            else -> false
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
    }

    @Subscribe
    fun onScheduleClickEvent(e: ScheduleClickEvent) {
        startActivity<DetailActivity>(DetailActivity.EXT_SCHEDULE to e.schedule)
    }
}
