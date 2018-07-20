package com.projectbox.footballmatchschedule.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.projectbox.footballmatchschedule.IService
import com.projectbox.footballmatchschedule.model.*
import com.projectbox.footballmatchschedule.repository.FavoriteManagedDB
import com.projectbox.footballmatchschedule.repository.FavoriteScheduleColumns
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import retrofit2.Call
import retrofit2.HttpException
import ru.gildor.coroutines.retrofit.await
import ru.gildor.coroutines.retrofit.awaitResponse
import ru.gildor.coroutines.retrofit.awaitResult
import timber.log.Timber

/**
 * Created by adinugroho
 */
class ScheduleVM(private val service: IService, private val db: FavoriteManagedDB) : ViewModel() {
    var scheduleList = MutableLiveData<List<Schedule>>()

    fun getSchedule(scheduleType: ScheduleType) {
        when (scheduleType) {
            ScheduleType.Past -> requestSchedule(service.getPastLeague())
            ScheduleType.Next -> requestSchedule(service.getPastLeague())
            ScheduleType.Favorite -> getFavoriteSchedules()
        }
    }

    private fun requestSchedule(request: Call<ScheduleResponse>) {
        // also retrieved TeamData when team list is still empty
        if (AppData.teamData.isEmpty()) {
            launch(UI) {
                try {
                    val listOfData = listOf(
                            async(CommonPool) { request.await() },
                            async(CommonPool) { service.getAllTeams().await() }
                    ).map {
                        it.await()
                    }

                    val scheduleResponse = listOfData[0] as ScheduleResponse
                    scheduleList.value = scheduleResponse.scheduleList

                    val teamResponse = listOfData[1] as TeamResponse
                    AppData.teamData = teamResponse.teams
                } catch (e: HttpException) {
                    Timber.e("Error ${e.code()} : ${e.localizedMessage}")
                } catch (e: Throwable) {
                    Timber.e("Call unsuccessful because ${e.localizedMessage}")
                }
            }
        } else {
            launch(UI) {
                try {
                    val scheduleResponse = request.await()
                    scheduleList.value = scheduleResponse.scheduleList
                } catch (e: HttpException) {
                    Timber.e("Error ${e.code()} : ${e.localizedMessage}")
                } catch (e: Throwable) {
                    Timber.e("Call unsuccessful because ${e.localizedMessage}")
                }
            }
        }
    }

    private fun getFavoriteSchedules() {
        db.use {
            val result = select(FavoriteScheduleColumns.TABLE_NAME)
            scheduleList.value = result.parseList(classParser())
        }
    }
}