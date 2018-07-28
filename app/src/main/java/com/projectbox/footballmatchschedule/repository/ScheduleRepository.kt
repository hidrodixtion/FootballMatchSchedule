package com.projectbox.footballmatchschedule.repository

import android.database.sqlite.SQLiteConstraintException
import com.projectbox.footballmatchschedule.IService
import com.projectbox.footballmatchschedule.db.FavoriteScheduleColumns
import com.projectbox.footballmatchschedule.db.ManagedDB
import com.projectbox.footballmatchschedule.model.response.Schedule
import com.projectbox.footballmatchschedule.model.ScheduleType
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
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

    fun isFavoriteSchedule(id: String): Boolean {
        var isFavorite = false

        db.use {
            val result = select(FavoriteScheduleColumns.TABLE_NAME).whereArgs("${FavoriteScheduleColumns.C_ID} = {id}", "id" to id)
            val favorites = result.parseList(classParser<Schedule>())
            isFavorite = (favorites.isNotEmpty())
        }

        return isFavorite
    }

    fun removeFromFavorite(id: String): String? {
        return try {
            db.use {
                delete(FavoriteScheduleColumns.TABLE_NAME, "${FavoriteScheduleColumns.C_ID} = {id}", "id" to id)
            }
            null
        } catch (err: SQLiteConstraintException) {
            err.localizedMessage
        }
    }

    fun addToFavorite(schedule: Schedule): String? {
        return try {
            db.use {
                insert(
                        FavoriteScheduleColumns.TABLE_NAME,
                        FavoriteScheduleColumns.C_ID to schedule.scheduleID,
                        FavoriteScheduleColumns.C_HOME_ID to schedule.homeID,
                        FavoriteScheduleColumns.C_AWAY_ID to schedule.awayID,
                        FavoriteScheduleColumns.C_HOME_TEAM to schedule.homeTeam,
                        FavoriteScheduleColumns.C_AWAY_TEAM to schedule.awayTeam,
                        FavoriteScheduleColumns.C_HOME_SCORE to schedule.homeScore,
                        FavoriteScheduleColumns.C_AWAY_SCORE to schedule.awayScore,
                        FavoriteScheduleColumns.C_HOME_GOAL_DETAILS to schedule.homeGoalDetails,
                        FavoriteScheduleColumns.C_AWAY_GOAL_DETAILS to schedule.awayGoalDetails,
                        FavoriteScheduleColumns.C_HOME_SHOTS to schedule.homeShots,
                        FavoriteScheduleColumns.C_AWAY_SHOTS to schedule.awayShots,
                        FavoriteScheduleColumns.C_HOME_GK to schedule.homeGK,
                        FavoriteScheduleColumns.C_HOME_DEF to schedule.homeDef,
                        FavoriteScheduleColumns.C_HOME_MID to schedule.homeMid,
                        FavoriteScheduleColumns.C_HOME_FW to schedule.homeFW,
                        FavoriteScheduleColumns.C_AWAY_GK to schedule.awayGK,
                        FavoriteScheduleColumns.C_AWAY_DEF to schedule.awayDef,
                        FavoriteScheduleColumns.C_AWAY_MID to schedule.awayMid,
                        FavoriteScheduleColumns.C_AWAY_FW to schedule.awayFW,
                        FavoriteScheduleColumns.C_DATE to schedule.date,
                        FavoriteScheduleColumns.C_TIME to schedule.time,
                        FavoriteScheduleColumns.C_SPORT to "soccer"
                )

                null
            }
        } catch (err: SQLiteConstraintException) {
            err.localizedMessage
        }
    }
}