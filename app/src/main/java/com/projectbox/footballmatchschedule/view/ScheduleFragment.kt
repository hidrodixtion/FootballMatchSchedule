package com.projectbox.footballmatchschedule.view


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter

import com.projectbox.footballmatchschedule.R
import com.projectbox.footballmatchschedule.adapter.ScheduleAdapter
import com.projectbox.footballmatchschedule.model.AppData
import com.projectbox.footballmatchschedule.model.ScheduleType
import com.projectbox.footballmatchschedule.model.response.Schedule
import com.projectbox.footballmatchschedule.viewmodel.ScheduleVM
import kotlinx.android.synthetic.main.fragment_schedule.*
import org.jetbrains.anko.support.v4.ctx
import org.koin.android.architecture.ext.viewModel
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 *
 */
class ScheduleFragment : Fragment() {
    companion object {
        const val EXT_SCHEDULE = "ext_schedule"

        fun getInstance(type: ScheduleType): ScheduleFragment {
            val bundle = Bundle()
            bundle.putString(EXT_SCHEDULE, type.name)
            val fragment = ScheduleFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private val vmSchedule by viewModel<ScheduleVM>()

    private lateinit var adapter: ScheduleAdapter
    private lateinit var schedule: ScheduleType

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initUI()
        initVMObserver()
    }

    override fun onResume() {
        super.onResume()
        if (schedule == ScheduleType.Favorite) {
            vmSchedule.getSchedule(schedule)
        }
    }

    private fun initUI() {
        val args = arguments ?: return
        schedule = ScheduleType.valueOf(args.getString(EXT_SCHEDULE))

        swipe_refresh.setColorSchemeResources(
                R.color.colorAccent,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        )

        if (schedule == ScheduleType.Favorite) {
            spinner.visibility = View.GONE
            vmSchedule.getSchedule(schedule)
        } else {
            spinner.adapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, AppData.leagues.map { it.name })
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {}

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    swipe_refresh.isRefreshing = true
                    vmSchedule.getSchedule(schedule, AppData.leagues[position])
                }
            }
        }
        recycler_view.layoutManager = LinearLayoutManager(ctx)
        recycler_view.setHasFixedSize(true)

        adapter = ScheduleAdapter(emptyList(), schedule == ScheduleType.Next)
        recycler_view.adapter = adapter
    }

    private fun initVMObserver() {
        vmSchedule.scheduleList.observe(this, Observer {
            val list = it ?: emptyList()

            adapter.update(list)

            swipe_refresh.isRefreshing = false
        })
    }

}
