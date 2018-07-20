package com.projectbox.footballmatchschedule.view

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.projectbox.footballmatchschedule.R
import com.projectbox.footballmatchschedule.helper.DateConverter
import com.projectbox.footballmatchschedule.model.AppData
import com.projectbox.footballmatchschedule.model.Schedule
import com.projectbox.footballmatchschedule.viewmodel.ScheduleDetailVM
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.design.snackbar
import org.koin.android.architecture.ext.viewModel

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXT_SCHEDULE = "schedule"
    }

    private val vmDetail by viewModel<ScheduleDetailVM>()

    private lateinit var schedule: Schedule
    private var menuItem: MenuItem? = null

    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menuItem = menu?.findItem(R.id.add_to_favorite)

        initListener()
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.add_to_favorite -> {
                toggleFavorite()
                true
            }
            else -> false
        }
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

    private fun initListener() {
        vmDetail.isFavoriteSchedule.observe(this, Observer {
            val fav = it ?: return@Observer

            if (fav)
                menuItem?.icon = ContextCompat.getDrawable(ctx, R.drawable.ic_added_to_favorites)
            else
                menuItem?.icon = ContextCompat.getDrawable(ctx, R.drawable.ic_add_to_favorites)

            isFavorite = fav
        })

        vmDetail.message.observe(this, Observer {
            val msg = it ?: return@Observer

            snackbar(scroll_view, msg).show()
        })

        vmDetail.checkIfFavoriteSchedule(schedule.scheduleID)
    }

    private fun toggleFavorite() {
        // if now isFav => remove from fav
        // else => add to fav
        if (isFavorite)
            vmDetail.removeFromFavorite(schedule)
        else
            vmDetail.addToFavorite(schedule)
    }
}
