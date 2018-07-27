package com.projectbox.footballmatchschedule.view

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.widget.LinearLayout
import com.projectbox.footballmatchschedule.R
import com.projectbox.footballmatchschedule.adapter.ScheduleAdapter
import com.projectbox.footballmatchschedule.adapter.TeamAdapter
import com.projectbox.footballmatchschedule.event.ScheduleClickEvent
import com.projectbox.footballmatchschedule.event.TeamClickEvent
import com.projectbox.footballmatchschedule.model.ScheduleType
import com.projectbox.footballmatchschedule.viewmodel.ScheduleVM
import com.projectbox.footballmatchschedule.viewmodel.TeamVM
import kotlinx.android.synthetic.main.activity_search_schedule.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.ctx
import org.jetbrains.anko.startActivity
import org.koin.android.architecture.ext.viewModel
import timber.log.Timber

class SearchActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    companion object {
        const val Ext_Type = "search_type"
    }

    private val scheduleVM by viewModel<ScheduleVM>()
    private val teamVM by viewModel<TeamVM>()

    private lateinit var searchType: SearchType
    private lateinit var teamAdapter: TeamAdapter
    private lateinit var scheduleAdapter: ScheduleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_schedule)

        initUI()
        initObservers()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean = false

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let {
            if (it.length >= 3) {
                when (searchType) {
                    SearchType.Schedule -> scheduleVM.searchSchedule(it)
                    SearchType.Team -> teamVM.searchTeam(it)
                }
            }

            return true
        }

        return false
    }

    private fun initUI() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recycler_view.layoutManager = LinearLayoutManager(ctx, LinearLayout.VERTICAL, false)
        recycler_view.setHasFixedSize(true)

        searchType = SearchType.valueOf(intent.getStringExtra(Ext_Type))

        when (searchType) {
            SearchType.Schedule -> {
                scheduleAdapter = ScheduleAdapter(emptyList())
                recycler_view.adapter = scheduleAdapter
            }
            SearchType.Team -> {
                teamAdapter = TeamAdapter(emptyList())
                recycler_view.adapter = teamAdapter
            }
        }

        search_view.requestFocus()
        search_view.setOnQueryTextListener(this)
    }

    private fun initObservers() {
        scheduleVM.scheduleList.observe(this, Observer {
            Timber.v("HEHE %s", it?.toString())
            it?.let { list ->
                val soccerList = list.filter { it.sport?.toLowerCase() == "soccer" }
                scheduleAdapter.update(list)
            }
        })

        teamVM.teamList.observe(this, Observer {
            it?.let {
                teamAdapter.updateTeams(it)
            }
        })
    }

    @Subscribe
    fun onScheduleClicked(e: ScheduleClickEvent) {
        startActivity<ScheduleDetailActivity>(ScheduleDetailActivity.EXT_SCHEDULE to e.schedule)
    }

    @Subscribe
    fun onTeamClickEvent(e: TeamClickEvent) {
        startActivity<TeamDetailActivity>(TeamDetailActivity.Ext_Team_ID to e.team.idTeam)
    }

    enum class SearchType {
        Team, Schedule
    }
}

