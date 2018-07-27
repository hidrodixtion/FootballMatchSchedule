package com.projectbox.footballmatchschedule.view


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.projectbox.footballmatchschedule.R
import com.projectbox.footballmatchschedule.viewmodel.TeamInfoVM
import kotlinx.android.synthetic.main.fragment_team_info.*
import org.koin.android.architecture.ext.viewModel

class TeamInfoFragment : Fragment() {

    companion object {
        fun getInstance(id: String): TeamInfoFragment {
            val bundle = Bundle()
            bundle.putString(TeamDetailActivity.Ext_Team_ID, id)
            val fragment = TeamInfoFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    val teamInfoVM by viewModel<TeamInfoVM>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments ?: return

        initObserver()
        teamInfoVM.getTeamFromID(args.getString(TeamDetailActivity.Ext_Team_ID))
    }

    private fun initObserver() {
        teamInfoVM.team.observe(this, Observer {
            it?.let {
                txt_description.text = it.teamDescription
            }
        })
    }
}
