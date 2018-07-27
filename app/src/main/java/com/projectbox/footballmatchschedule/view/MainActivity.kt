package com.projectbox.footballmatchschedule.view

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.projectbox.footballmatchschedule.R
import com.projectbox.footballmatchschedule.event.ScheduleClickEvent
import com.projectbox.footballmatchschedule.event.TeamClickEvent
import com.projectbox.footballmatchschedule.pageradapter.FavoritePagerAdapter
import com.projectbox.footballmatchschedule.pageradapter.SchedulePagerAdapter
import com.projectbox.footballmatchschedule.pageradapter.TeamPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

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
            R.id.action_search -> startActivity<SearchScheduleActivity>()
        }
        return super.onOptionsItemSelected(item)
    }

    private val onNavigationItemSelected = BottomNavigationView.OnNavigationItemSelectedListener {
        return@OnNavigationItemSelectedListener when(it.itemId) {
            R.id.menu_schedule -> {
                menuSearch.isVisible = true
                openViewPager(SchedulePagerAdapter(supportFragmentManager))
                true
            }
            R.id.menu_team -> {
                menuSearch.isVisible = true
                openViewPager(TeamPagerAdapter(supportFragmentManager))
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
        startActivity<ScheduleDetailActivity>(ScheduleDetailActivity.EXT_SCHEDULE to e.schedule)
    }

    @Subscribe
    fun onTeamClickEvent(e: TeamClickEvent) {
        startActivity<TeamDetailActivity>(TeamDetailActivity.Ext_Team_ID to e.team.idTeam)
    }
}
