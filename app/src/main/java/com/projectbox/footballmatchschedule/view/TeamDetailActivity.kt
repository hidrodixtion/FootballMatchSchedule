package com.projectbox.footballmatchschedule.view

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.projectbox.footballmatchschedule.R
import com.projectbox.footballmatchschedule.event.PlayerClickEvent
import com.projectbox.footballmatchschedule.pageradapter.TeamDetailPagerAdapter
import com.projectbox.footballmatchschedule.viewmodel.TeamInfoVM
import kotlinx.android.synthetic.main.activity_team_detail.*
import kotlinx.android.synthetic.main.content_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.ctx
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.startActivity
import org.koin.android.architecture.ext.viewModel

class TeamDetailActivity : AppCompatActivity() {

    companion object {
        const val Ext_Team_ID = "team_id"
    }

    private val teamInfoVM: TeamInfoVM by viewModel()

    lateinit var menuItem: MenuItem
    var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_detail)

        initUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val menu = menu ?: return false
        menuItem = menu.findItem(R.id.add_to_favorite)

        initObserver()
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
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

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    private fun initUI() {
        tab.setupWithViewPager(view_pager)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        if (intent.hasExtra(Ext_Team_ID)) {
            view_pager.adapter = TeamDetailPagerAdapter(supportFragmentManager, intent.getStringExtra(Ext_Team_ID))
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

        teamInfoVM.isFavoriteTeam.observe(this, Observer {
            it?.let {
                isFavorite = it

                if (isFavorite) {
                    menuItem.icon = ContextCompat.getDrawable(ctx, R.drawable.ic_added_to_favorites)
                } else {
                    menuItem.icon = ContextCompat.getDrawable(ctx, R.drawable.ic_add_to_favorites)
                }
            }
        })

        teamInfoVM.message.observe(this, Observer {
            it?.let {
                snackbar(toolbar, it).show()
            }
        })

        teamInfoVM.getTeamFromID(intent.getStringExtra(Ext_Team_ID))
    }

    private fun toggleFavorite() {
        teamInfoVM.toggleFavorite()
    }

    @Subscribe
    fun onPlayerClicked(e: PlayerClickEvent) {
        startActivity<PlayerDetailActivity>(
                PlayerDetailActivity.Ext_Name to e.player.name,
                PlayerDetailActivity.Ext_Description to e.player.description,
                PlayerDetailActivity.Ext_Image to e.player.fanart
        )
    }
}
