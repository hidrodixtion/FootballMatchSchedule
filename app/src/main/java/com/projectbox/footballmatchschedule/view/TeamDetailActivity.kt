package com.projectbox.footballmatchschedule.view

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.projectbox.footballmatchschedule.R
import com.projectbox.footballmatchschedule.pageradapter.TeamDetailPagerAdapter
import com.projectbox.footballmatchschedule.viewmodel.TeamInfoVM
import kotlinx.android.synthetic.main.activity_team_detail.*
import kotlinx.android.synthetic.main.content_main.*
import org.koin.android.architecture.ext.viewModel

class TeamDetailActivity : AppCompatActivity() {

    companion object {
        const val Ext_League_ID = "league_id"
    }

    val teamInfoVM: TeamInfoVM by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_detail)

        initUI()
        initObserver()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun initUI() {
        tab.setupWithViewPager(view_pager)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        if (intent.hasExtra(Ext_League_ID)) {
            view_pager.adapter = TeamDetailPagerAdapter(supportFragmentManager, intent.getStringExtra(Ext_League_ID))
        }
    }

    private fun initObserver() {
        teamInfoVM.team.observe(this, Observer {
            it?.let {
                txt_team.text = it.teamName
                txt_year.text = it.formedYear ?: ""

                Glide.with(this).load(it.teamBadge).into(img_badge)
            }
        })

        teamInfoVM.getTeamFromID(intent.getStringExtra(Ext_League_ID))
    }
}
