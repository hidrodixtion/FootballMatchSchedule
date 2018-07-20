package com.projectbox.footballmatchschedule.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.database.sqlite.SQLiteConstraintException
import com.projectbox.footballmatchschedule.model.Schedule
import com.projectbox.footballmatchschedule.repository.FavoriteManagedDB
import com.projectbox.footballmatchschedule.repository.FavoriteScheduleColumns
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

/**
 * Created by adinugroho
 */
class ScheduleDetailVM(private val db: FavoriteManagedDB) : ViewModel() {
    var isFavoriteSchedule = MutableLiveData<Boolean>()
    var message = MutableLiveData<String>()

    fun checkIfFavoriteSchedule(id: String) {
        db.use {
            val result = select(FavoriteScheduleColumns.TABLE_NAME).whereArgs("${FavoriteScheduleColumns.C_ID} = {id}", "id" to id)
            val favorites = result.parseList(classParser<Schedule>())
            isFavoriteSchedule.value = (favorites.isNotEmpty())
        }
    }

    fun addToFavorite(schedule: Schedule) {
        try {
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
                        FavoriteScheduleColumns.C_DATE to schedule.date
                )

                isFavoriteSchedule.value = true
                message.value = "Schedule has been added to Favorites"
            }
        } catch (err: SQLiteConstraintException) {
            message.value = err.localizedMessage
        }
    }

    fun removeFromFavorite(schedule: Schedule) {
        try {
            db.use {
                delete(FavoriteScheduleColumns.TABLE_NAME, "${FavoriteScheduleColumns.C_ID} = {id}", "id" to schedule.scheduleID)
            }

            isFavoriteSchedule.value = false
            message.value = "Schedule has been removed from Favorites"
        } catch (err: SQLiteConstraintException) {
            message.value = err.localizedMessage
        }
    }
}