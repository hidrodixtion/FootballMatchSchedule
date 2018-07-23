package com.projectbox.footballmatchschedule

import android.arch.lifecycle.Observer
import android.content.Context
import com.projectbox.footballmatchschedule.model.AppData
import com.projectbox.footballmatchschedule.model.Schedule
import com.projectbox.footballmatchschedule.model.ScheduleType
import com.projectbox.footballmatchschedule.repository.FavoriteManagedDB
import com.projectbox.footballmatchschedule.repository.ScheduleRepository
import com.projectbox.footballmatchschedule.repository.TeamRepository
import com.projectbox.footballmatchschedule.viewmodel.ScheduleVM
import kotlinx.coroutines.experimental.Unconfined
import kotlinx.coroutines.experimental.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.standalone.StandAloneContext.closeKoin
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.mockito.Mock
import org.mockito.Mockito.mock

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class AppUnitTest : KoinTest {

    private val repository: ScheduleRepository by inject()
    private val teamRepository: TeamRepository by inject()

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
            val schedules = repository.getSchedules(ScheduleType.Past)

            assertEquals(15, schedules.size)
        }
    }

    @Test
    fun `next schedule has the right count`() {
        runBlocking(Unconfined) {
            val schedules = repository.getSchedules(ScheduleType.Next)

            assertEquals(15, schedules.size)
        }
    }

    @Test
    fun `teams has the right count`() {
        runBlocking(Unconfined) {
            val teams = teamRepository.getAllTeams()

            assertEquals(20, teams.size)
        }
    }
}
