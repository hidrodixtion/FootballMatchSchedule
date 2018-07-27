package com.projectbox.footballmatchschedule.view


import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.projectbox.footballmatchschedule.R
import com.projectbox.footballmatchschedule.adapter.TeamAdapter
import com.projectbox.footballmatchschedule.model.AppData
import com.projectbox.footballmatchschedule.viewmodel.TeamVM
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import org.koin.android.architecture.ext.viewModel

class TeamFragment : Fragment(), AnkoComponent<Context> {

    companion object {
        const val Ext_Show_Favorite = "show_favorite"

        fun getInstance(showFavorite: Boolean = false): TeamFragment {
            val bundle = Bundle()
            bundle.putBoolean(Ext_Show_Favorite, showFavorite)
            val fragment = TeamFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    lateinit var swipeToRefresh: SwipeRefreshLayout
    lateinit var spinner: Spinner
    lateinit var listTeam: RecyclerView
    lateinit var progressBar: ProgressBar

    lateinit var adapter: TeamAdapter

    private val teamVM by viewModel<TeamVM>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?) = createView(AnkoContext.create(ctx))

    override fun createView(ui: AnkoContext<Context>) = with(ui){
        linearLayout {
            lparams(width = matchParent, height = wrapContent)
            orientation = LinearLayout.VERTICAL
            topPadding = dip(16)
            leftPadding = dip(16)
            rightPadding = dip(16)

            spinner = spinner()
            swipeToRefresh = swipeRefreshLayout {
                setColorSchemeResources(
                        R.color.colorAccent,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light
                )

                relativeLayout {
                    lparams(width = matchParent, height = wrapContent)

                    listTeam = recyclerView {
                        lparams(width = matchParent, height = wrapContent)
                        layoutManager = LinearLayoutManager(ctx)
                    }

                    progressBar = progressBar {
                    }.lparams {
                        centerHorizontally()
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments ?: return

        progressBar.visibility = View.GONE
        initObserver()

        spinner.adapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, AppData.leagues.map { it.name })
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) { }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                swipeToRefresh.isRefreshing = true
                adapter.clearItems()
                teamVM.getTeams(AppData.leagues[position])
            }
        }

        adapter = TeamAdapter(teamVM.teamList.value ?: emptyList())
        listTeam.adapter = adapter

        if (args.getBoolean(Ext_Show_Favorite)) {
            teamVM.getFavoriteTeams()
            spinner.visibility = View.GONE
        } else
            teamVM.getTeams(AppData.leagues.first())
    }

    override fun onResume() {
        super.onResume()

        val args = arguments ?: return
        if (args.getBoolean(Ext_Show_Favorite)) {
            teamVM.getFavoriteTeams()
        }
    }

    private fun initObserver() {
        teamVM.teamList.observe(this, Observer {
            it?.let {
                adapter.updateTeams(it)
                swipeToRefresh.isRefreshing = false
            }
        })
    }
}
