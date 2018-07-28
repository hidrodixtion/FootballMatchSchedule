package com.projectbox.footballmatchschedule.repository

import android.database.sqlite.SQLiteConstraintException
import com.projectbox.footballmatchschedule.db.ManagedDB
import com.projectbox.footballmatchschedule.db.TeamColumns
import com.projectbox.footballmatchschedule.model.League
import com.projectbox.footballmatchschedule.model.response.Team
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update
import timber.log.Timber

/**
 * Created by adinugroho
 */
class TeamDBRepository(private val db: ManagedDB) {
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

    fun toggleFavorite(id: String, isFavorite: Boolean): Boolean {
        var newVal = 0
        if (isFavorite)
            newVal = 1

        try {
            db.use {
                update(TeamColumns.TABLE_NAME, TeamColumns.C_FAV to newVal).whereArgs("${TeamColumns.C_ID} = {team_id}", "team_id" to id).exec()
//                Timber.v(isFavorite.toString())
            }
        } catch (e: SQLiteConstraintException) {
            Timber.e(e.localizedMessage)
        }

        return isFavorite
    }

    fun getFavoriteTeams(): List<Team> {
        var list = emptyList<Team>()

        db.use {
            val result = select(TeamColumns.TABLE_NAME).whereArgs("${TeamColumns.C_FAV} = 1")
            list = result.parseList(classParser())
        }

        return list
    }

    fun getTeamFromDB(teamID: String): Team? {
        var teams = emptyList<Team>()

        db.use {
            val result = select(TeamColumns.TABLE_NAME).whereArgs("${TeamColumns.C_ID} = {team_id}", "team_id" to teamID)
            teams = result.parseList(classParser())
        }

        return teams.firstOrNull()
    }
}