package com.projectbox.footballmatchschedule.view

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.projectbox.footballmatchschedule.R
import com.projectbox.footballmatchschedule.event.ScheduleClickEvent
import com.projectbox.footballmatchschedule.event.TeamClickEvent
import com.projectbox.footballmatchschedule.helper.DateConverter
import com.projectbox.footballmatchschedule.pageradapter.FavoritePagerAdapter
import com.projectbox.footballmatchschedule.pageradapter.SchedulePagerAdapter
import com.projectbox.footballmatchschedule.pageradapter.TeamPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.startActivity
import timber.log.Timber
import java.util.*

class MainActivity : AppCompatActivity() {

    private var currentMenu = SearchActivity.SearchType.Schedule

    lateinit var menuSearch: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        menuSearch = menu!!.findItem(R.id.action_search)

        bottom_navbar.setOnNavigationItemSelectedListener(onNavigationItemSelected)
        bottom_navbar.selectedItemId = R.id.menu_schedule

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_search -> {
                startActivity<SearchActivity>(SearchActivity.Ext_Type to currentMenu.name)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val onNavigationItemSelected = BottomNavigationView.OnNavigationItemSelectedListener {
        return@OnNavigationItemSelectedListener when(it.itemId) {
            R.id.menu_schedule -> {
                menuSearch.isVisible = true
                openViewPager(SchedulePagerAdapter(supportFragmentManager))
                currentMenu = SearchActivity.SearchType.Schedule
                true
            }
            R.id.menu_team -> {
                menuSearch.isVisible = true
                openViewPager(TeamPagerAdapter(supportFragmentManager))
                currentMenu = SearchActivity.SearchType.Team
                true
            }
            R.id.menu_favorite -> {
                menuSearch.isVisible = false
                openViewPager(FavoritePagerAdapter(supportFragmentManager))
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
        if (adapter.count == 1) {
            tab.visibility = View.GONE
        } else {
            tab.visibility = View.VISIBLE
        }

        val transaction = supportFragmentManager.beginTransaction()
        supportFragmentManager.fragments.forEach {
            transaction.remove(it)
        }
        transaction.commitNow()

        view_pager.removeAllViews()
        view_pager.adapter = null
        view_pager.adapter = adapter
    }

    @Subscribe
    fun onScheduleClickEvent(e: ScheduleClickEvent) {
        if (e.isAlarm) {
            val calendar = DateConverter.convertToCalendar(e.schedule)
            val title = "${e.schedule.homeTeam} vs ${e.schedule.awayTeam}"

            val intent = Intent(Intent.ACTION_EDIT)
            intent.type = "vnd.android.cursor.item/event"
            intent.putExtra(CalendarContract.Events.TITLE, title)
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendar.timeInMillis)
            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calendar.timeInMillis)
            intent.putExtra(CalendarContract.Events.ALL_DAY, false)
            intent.putExtra(CalendarContract.Events.DESCRIPTION, title)

            startActivity(intent)
        } else {
            startActivity<ScheduleDetailActivity>(ScheduleDetailActivity.EXT_SCHEDULE to e.schedule)
        }
    }

    @Subscribe
    fun onTeamClickEvent(e: TeamClickEvent) {
        startActivity<TeamDetailActivity>(TeamDetailActivity.Ext_Team_ID to e.team.idTeam)
    }
}
