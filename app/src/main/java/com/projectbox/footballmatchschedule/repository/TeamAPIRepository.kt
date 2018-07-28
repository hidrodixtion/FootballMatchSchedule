package com.projectbox.footballmatchschedule.repository

import android.database.sqlite.SQLiteConstraintException
import com.projectbox.footballmatchschedule.IService
import com.projectbox.footballmatchschedule.db.ManagedDB
import com.projectbox.footballmatchschedule.db.TeamColumns
import com.projectbox.footballmatchschedule.model.League
import com.projectbox.footballmatchschedule.model.response.Player
import com.projectbox.footballmatchschedule.model.response.Team
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update
import retrofit2.HttpException
import ru.gildor.coroutines.retrofit.await
import timber.log.Timber

/**
 * Created by adinugroho
 */
class TeamAPIRepository(private val service: IService) {
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

    suspend fun searchTeamFromAPI(query: String): List<Team> {
        try {
            val teamResponse = service.searchTeam(query).await()
            return teamResponse.teams
        } catch (e: HttpException) {
            Timber.e("Error ${e.code()} : ${e.localizedMessage}")
        } catch (e: Throwable) {
            Timber.e("Call unsuccessful because ${e.localizedMessage}")
        }

        return emptyList()
    }

    suspend fun getTeamFromAPI(teamID: String): List<Team> {
        try {
            val teamResponse = service.getTeam(teamID).await()
            return teamResponse.teams
        } catch (e: HttpException) {
            Timber.e("Error ${e.code()} : ${e.localizedMessage}")
        } catch (e: Throwable) {
            Timber.e("Call unsuccessful because ${e.localizedMessage}")
        }

        return emptyList()
    }

    suspend fun getPlayersFromTeam(id: String): List<Player> {
        try {
            val response = service.getAllPlayers(id).await()
            return response.player
        } catch (e: HttpException) {
            Timber.e("Error ${e.code()} : ${e.localizedMessage}")
        } catch (e: Throwable) {
            Timber.e("Call unsuccessful because ${e.localizedMessage}")
        }

        return emptyList()
    }
}