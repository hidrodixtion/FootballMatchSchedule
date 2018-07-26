package com.projectbox.footballmatchschedule.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.projectbox.footballmatchschedule.R
import com.projectbox.footballmatchschedule.pageradapter.TeamDetailPagerAdapter
import kotlinx.android.synthetic.main.activity_team_detail.*
import kotlinx.android.synthetic.main.content_main.*

class TeamDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_detail)

        initUI()
    }

    private fun initUI() {
        view_pager.adapter = TeamDetailPagerAdapter(supportFragmentManager)

        tab.setupWithViewPager(view_pager)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }
}
