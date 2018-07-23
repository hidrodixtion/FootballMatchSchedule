package com.projectbox.footballmatchschedule.repository

import com.projectbox.footballmatchschedule.IService
import com.projectbox.footballmatchschedule.model.AppData
import com.projectbox.footballmatchschedule.model.Team
import retrofit2.HttpException
import ru.gildor.coroutines.retrofit.await
import timber.log.Timber

/**
 * Created by adinugroho
 */
class TeamRepository(private val service: IService) {
    suspend fun getAllTeams(): List<Team> {
        try {
            val teamResponse = service.getAllTeams().await()
            return teamResponse.teams
        } catch (e: HttpException) {
            Timber.e("Error ${e.code()} : ${e.localizedMessage}")
        } catch (e: Throwable) {
            Timber.e("Call unsuccessful because ${e.localizedMessage}")
        }

        return emptyList()
    }
}