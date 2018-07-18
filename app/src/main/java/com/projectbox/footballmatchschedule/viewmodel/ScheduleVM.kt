package com.projectbox.footballmatchschedule.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.projectbox.footballmatchschedule.IService
import com.projectbox.footballmatchschedule.model.*
import com.projectbox.footballmatchschedule.view.ScheduleFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by adinugroho
 */
class ScheduleVM(private val service: IService) : ViewModel() {
    var scheduleList = MutableLiveData<List<Schedule>>()

    fun getSchedule(scheduleType: ScheduleType) {
        val request = when (scheduleType) {
            ScheduleType.Past -> service.getPastLeague()
            ScheduleType.Next -> service.getNextLeague()
        }

        // also retrieved TeamData when team list is still empty
        if (AppData.teamData.isEmpty()) {
            Observables.zip(request, service.getAllTeams()).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                            onNext = {
                                scheduleList.value = it.first.scheduleList
                                AppData.teamData = it.second.teams
                            },
                            onError = { Timber.e(it.localizedMessage) }
                    )
        } else {
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                            onNext = {
                                scheduleList.value = it.scheduleList
                            },
                            onError = {
                                Timber.e(it.localizedMessage)
                            }
                    )
        }
    }
}