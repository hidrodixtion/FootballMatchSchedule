package com.projectbox.footballmatchschedule.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.projectbox.footballmatchschedule.model.response.Team
import com.projectbox.footballmatchschedule.repository.TeamAPIRepository
import com.projectbox.footballmatchschedule.repository.TeamDBRepository
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

/**
 * Created by adinugroho
 */
class TeamInfoVM(private val api: TeamAPIRepository, private val db: TeamDBRepository): ViewModel() {
    val team = MutableLiveData<Team>()
    val isFavoriteTeam = MutableLiveData<Boolean>()
    val message = MutableLiveData<String>()

    private lateinit var teamID: String

    fun getTeamFromID(teamID: String) {
        this.teamID = teamID

        val teamResult = db.getTeamFromDB(teamID)

        if (teamResult != null) {
            team.value = teamResult
            isFavoriteTeam.value = (teamResult.isFavorited == 1)
        } else {
            launch(UI) {
                val result = api.getTeamFromAPI(teamID)
                db.insertTeamsToDB(result)
                team.value = result.first()
            }
        }
    }

    fun toggleFavorite() {
        val teamResult = db.getTeamFromDB(teamID) ?: return

        if (teamResult.isFavorited == 1) {
            isFavoriteTeam.value = db.toggleFavorite(teamID, false)
            message.value = "Team has been removed from favorite"
        } else {
            isFavoriteTeam.value = db.toggleFavorite(teamID, true)
            message.value = "Team has been added to favorite"
        }
    }
}