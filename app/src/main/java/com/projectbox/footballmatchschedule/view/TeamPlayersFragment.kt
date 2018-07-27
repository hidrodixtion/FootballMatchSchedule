package com.projectbox.footballmatchschedule.view


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.projectbox.footballmatchschedule.R
import com.projectbox.footballmatchschedule.adapter.PlayerAdapter
import com.projectbox.footballmatchschedule.viewmodel.TeamPlayerVM
import kotlinx.android.synthetic.main.fragment_team_players.*
import org.jetbrains.anko.support.v4.ctx
import org.koin.android.architecture.ext.viewModel

class TeamPlayersFragment : Fragment() {

    companion object {
        fun getInstance(teamID: String): TeamPlayersFragment {
            val bundle = Bundle()
            bundle.putString(TeamDetailActivity.Ext_Team_ID, teamID)
            val fragment = TeamPlayersFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private val vm by viewModel<TeamPlayerVM>()

    lateinit var adapter: PlayerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team_players, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initObserver()
    }

    private fun initUI() {
        adapter = PlayerAdapter(emptyList())

        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(ctx, LinearLayout.VERTICAL, false)
        recycler_view.setHasFixedSize(true)
    }

    private fun initObserver() {
        val args = arguments ?: return

        vm.players.observe(this, Observer {
            it?.let {
                adapter.update(it)
            }
        })

        vm.getAllPlayers(args.getString(TeamDetailActivity.Ext_Team_ID))
    }
}
