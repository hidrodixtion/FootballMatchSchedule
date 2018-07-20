package com.projectbox.footballmatchschedule.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.projectbox.footballmatchschedule.IService
import com.projectbox.footballmatchschedule.model.AppData
import com.projectbox.footballmatchschedule.model.Schedule
import com.projectbox.footballmatchschedule.model.ScheduleResponse
import com.projectbox.footballmatchschedule.model.ScheduleType
import com.projectbox.footballmatchschedule.repository.FavoriteManagedDB
import com.projectbox.footballmatchschedule.repository.FavoriteScheduleColumns
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import timber.log.Timber

/**
 * Created by adinugroho
 */
class ScheduleVM(private val service: IService, private val db: FavoriteManagedDB) : ViewModel() {
    var scheduleList = MutableLiveData<List<Schedule>>()

    fun getSchedule(scheduleType: ScheduleType) {
        when (scheduleType) {
            ScheduleType.Past -> requestSchedule(service.getPastLeague())
            ScheduleType.Next -> requestSchedule(service.getNextLeague())
            ScheduleType.Favorite -> getFavoriteSchedules()
        }
    }

    private fun requestSchedule(request: Observable<ScheduleResponse>) {
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

    private fun getFavoriteSchedules() {
        db.use {
            val result = select(FavoriteScheduleColumns.TABLE_NAME)
            scheduleList.value = result.parseList(classParser())
        }
    }
}