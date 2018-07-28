package com.projectbox.footballmatchschedule

import com.projectbox.footballmatchschedule.model.AppData
import com.projectbox.footballmatchschedule.model.ScheduleType
import com.projectbox.footballmatchschedule.repository.ScheduleAPIRepository
import com.projectbox.footballmatchschedule.repository.TeamAPIRepository
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Unconfined
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.standalone.StandAloneContext.closeKoin
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.inject
import org.koin.test.KoinTest

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class AppUnitTest : KoinTest {

    private val scheduleAPI: ScheduleAPIRepository by inject()
    private val teamAPI: TeamAPIRepository by inject()

    @Before
    fun before() {
        startKoin(listOf(KoinModules().getModules()))
    }

    @After
    fun after() {
        closeKoin()
    }

    @Test
    fun `past schedule has the right count`() {
        runBlocking(Unconfined) {
            val schedules = scheduleAPI.getSchedules(ScheduleType.Past, AppData.leagues.first().id)

            assertThat(schedules, hasSize(15))
        }
    }

    @Test
    fun `next schedule has the right count`() {
        runBlocking(Unconfined) {
            val schedules = scheduleAPI.getSchedules(ScheduleType.Next, AppData.leagues.first().id)

            assertThat(schedules, hasSize(15))
        }
    }

    @Test
    fun `EPL teams has the right count`() {
        runBlocking(Unconfined) {
            val teams = teamAPI.getAllTeams(AppData.leagues.first().name)

            assertThat(teams, hasSize(20))
        }
    }

    @Test
    fun `Searching Team 'bar' has Barcelona in it`() {
        runBlocking(Unconfined) {
            val teams = teamAPI.searchTeamFromAPI("bar").map { it.teamName }

            assertThat(teams, hasItem("Barcelona"))
        }
    }

    @Test
    fun `Searching Schedule 'chelsea' has Chelsea home schedule in it`() {
        runBlocking(Unconfined) {
            val schedules = scheduleAPI.searchSchedule("chelsea").map { it.homeTeam }

            assertThat(schedules, hasItem("Chelsea"))
        }
    }

    @Test
    fun `Barcelona has Lionel Messi as a player`() {
        runBlocking(Unconfined) {
            val teams = teamAPI.searchTeamFromAPI("bar")
            val barcelona = teams.first { it.teamName == "Barcelona" }

            val players = teamAPI.getPlayersFromTeam(barcelona.idTeam).map { it.name }

            assertThat(players, hasItem("Lionel Messi"))
        }
    }
}
