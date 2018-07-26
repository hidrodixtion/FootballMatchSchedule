package com.projectbox.footballmatchschedule.view


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.projectbox.footballmatchschedule.R

class TeamInfoFragment : Fragment() {

    companion object {
        fun getInstance(): TeamInfoFragment {
            return TeamInfoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team_info, container, false)
    }


}
