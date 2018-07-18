package com.projectbox.footballmatchschedule.view


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.projectbox.footballmatchschedule.R
import com.projectbox.footballmatchschedule.adapter.ScheduleAdapter
import com.projectbox.footballmatchschedule.model.ScheduleType
import com.projectbox.footballmatchschedule.viewmodel.ScheduleVM
import kotlinx.android.synthetic.main.fragment_schedule.*
import org.jetbrains.anko.support.v4.ctx
import org.koin.android.architecture.ext.viewModel

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

    val vmSchedule by viewModel<ScheduleVM>()

    private lateinit var adapter: ScheduleAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initVMObserver()

        vmSchedule.getSchedule(ScheduleType.valueOf(arguments!!.getString(EXT_SCHEDULE)))
    }

    private fun initUI() {
        recycler_view.layoutManager = LinearLayoutManager(ctx)
        recycler_view.setHasFixedSize(true)

        adapter = ScheduleAdapter(listOf())
        recycler_view.adapter = adapter
    }

    private fun initVMObserver() {
        vmSchedule.scheduleList.observe(this, Observer {
            val list = it ?: return@Observer

            adapter.update(list)
            progress.visibility = View.GONE
        })
    }

}
