package com.projectbox.footballmatchschedule.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.projectbox.footballmatchschedule.R
import com.projectbox.footballmatchschedule.helper.DateConverter
import com.projectbox.footballmatchschedule.model.AppData
import com.projectbox.footballmatchschedule.model.Schedule
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXT_SCHEDULE = "schedule"
    }

    lateinit var schedule: Schedule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initUI()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun initUI() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Event Detail"

        if (!intent.hasExtra(EXT_SCHEDULE)) {
            finish()
            return
        }

        schedule = intent.getParcelableExtra(EXT_SCHEDULE)

        val homeBadge = AppData.teamData.firstOrNull { it.idTeam == schedule.homeID }
        if (homeBadge != null) {
            Glide.with(this).load(homeBadge.teamBadge).into(img_home)
        }

        val awayBadge = AppData.teamData.firstOrNull { it.idTeam == schedule.awayID }
        if (awayBadge != null) {
            Glide.with(this).load(awayBadge.teamBadge).into(img_away)
        }

        txt_date.text = DateConverter.convertFromScheduleDate(schedule.date)
        txt_home.text = schedule.homeTeam
        txt_away.text = schedule.awayTeam

        txt_home_score.text = schedule.homeScore?.toString()
        txt_away_score.text = schedule.awayScore?.toString()

        val re = Regex("""[;]\s*""")
        
        txt_home_goal.text = schedule.homeGoalDetails?.replace(re, "\n")
        txt_away_goal.text = schedule.awayGoalDetails?.replace(re, "\n")

        txt_home_shot.text = schedule.homeShots?.toString()
        txt_away_shot.text = schedule.awayShots?.toString()

        txt_home_gk.text = schedule.homeGK?.replace(re, "\n")
        txt_away_gk.text = schedule.awayGK?.replace(re, "\n")

        txt_home_def.text = schedule.homeDef?.replace(re, "\n")
        txt_away_def.text = schedule.awayDef?.replace(re, "\n")

        txt_home_mid.text = schedule.homeMid?.replace(re, "\n")
        txt_away_mid.text = schedule.awayMid?.replace(re, "\n")

        txt_home_fw.text = schedule.homeFW?.replace(re, "\n")
        txt_away_fw.text = schedule.awayFW?.replace(re, "\n")
    }
}
