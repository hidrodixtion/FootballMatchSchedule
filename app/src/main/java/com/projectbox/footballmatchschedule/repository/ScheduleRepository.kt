package com.projectbox.footballmatchschedule.repository

import com.projectbox.footballmatchschedule.IService
import com.projectbox.footballmatchschedule.db.FavoriteScheduleColumns
import com.projectbox.footballmatchschedule.db.ManagedDB
import com.projectbox.footballmatchschedule.model.response.Schedule
import com.projectbox.footballmatchschedule.model.ScheduleType
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import retrofit2.HttpException
import ru.gildor.coroutines.retrofit.await
import timber.log.Timber

/**
 * Created by adinugroho
 */
class ScheduleRepository(private val service: IService, private val db: ManagedDB) {
    suspend fun getSchedules(scheduleType: ScheduleType, leagueID: String): List<Schedule> {
        val request = when (scheduleType) {
            ScheduleType.Past -> service.getPastLeague(leagueID)
            ScheduleType.Next -> service.getNextLeague(leagueID)
            ScheduleType.Favorite -> return emptyList()
        }

        try {
            val scheduleResponse = request.await()
            return scheduleResponse.scheduleList
        } catch (e: HttpException) {
            Timber.e("Error ${e.code()} : ${e.localizedMessage}")
        } catch (e: Throwable) {
            Timber.e("Call unsuccessful because ${e.localizedMessage}")
        }

        return emptyList()
    }

    fun getFavoriteSchedules(): List<Schedule>? {
        var list: List<Schedule> = emptyList()

        db.use {
            val result = select(FavoriteScheduleColumns.TABLE_NAME)
            list = result.parseList(classParser())
        }

        return list
    }

    suspend fun searchSchedule(query: String): List<Schedule> {
        try {
            val scheduleResponse = service.searchSchedule(query).await()
            return scheduleResponse.scheduleListFromSearch
        } catch (e: HttpException) {
            Timber.e("Error ${e.code()} : ${e.localizedMessage}")
        } catch (e: Throwable) {
            Timber.e("Call unsuccessful because ${e.localizedMessage}")
        }

        return emptyList()
    }
}