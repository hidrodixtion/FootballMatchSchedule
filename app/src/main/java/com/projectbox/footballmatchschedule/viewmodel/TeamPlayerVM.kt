package com.projectbox.footballmatchschedule.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.projectbox.footballmatchschedule.model.response.Player
import com.projectbox.footballmatchschedule.repository.TeamAPIRepository
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

/**
 * Created by adinugroho
 */
class TeamPlayerVM(private val api: TeamAPIRepository): ViewModel() {

    val players = MutableLiveData<List<Player>>()

    fun getAllPlayers(teamID: String) {
        launch(UI) {
            players.value = api.getPlayersFromTeam(teamID)
        }
    }
}