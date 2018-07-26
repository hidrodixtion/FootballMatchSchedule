package com.projectbox.footballmatchschedule.repository

import android.database.sqlite.SQLiteConstraintException
import com.projectbox.footballmatchschedule.IService
import com.projectbox.footballmatchschedule.db.ManagedDB
import com.projectbox.footballmatchschedule.db.TeamColumns
import com.projectbox.footballmatchschedule.model.AppData
import com.projectbox.footballmatchschedule.model.League
import com.projectbox.footballmatchschedule.model.Team
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import retrofit2.HttpException
import ru.gildor.coroutines.retrofit.await
import timber.log.Timber

/**
 * Created by adinugroho
 */
class TeamRepository(private val service: IService, private val db: ManagedDB) {
    suspend fun getAllTeams(leagueName: String): List<Team> {
        try {
            val teamResponse = service.getAllTeams(leagueName).await()
            return teamResponse.teams
        } catch (e: HttpException) {
            Timber.e("Error ${e.code()} : ${e.localizedMessage}")
        } catch (e: Throwable) {
            Timber.e("Call unsuccessful because ${e.localizedMessage}")
        }

        return emptyList()
    }

    fun getTeamFromDB(league: League): List<Team> {
        var teams = listOf<Team>()

        db.use {
            val result = select(TeamColumns.TABLE_NAME).whereArgs("${TeamColumns.C_LEAGUE} = {league_id}", "league_id" to league.id)
            teams = result.parseList(classParser())
        }

        return teams
    }

    fun insertTeamsToDB(teams: List<Team>) {
        teams.forEach {
            try {
                db.use {
                    insert(TeamColumns.TABLE_NAME,
                            TeamColumns.C_ID to it.idTeam,
                            TeamColumns.C_NAME to it.teamName,
                            TeamColumns.C_BADGE to it.teamBadge,
                            TeamColumns.C_LEAGUE to it.idLeague,
                            TeamColumns.C_DESC to it.teamDescription,
                            TeamColumns.C_YEAR to it.formedYear,
                            TeamColumns.C_FAV to 0
                    )
                }
            } catch (e: SQLiteConstraintException) {
                Timber.e(e.localizedMessage)
            }
        }
    }
}