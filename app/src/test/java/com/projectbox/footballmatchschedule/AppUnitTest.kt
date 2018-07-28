package com.projectbox.footballmatchschedule

import android.content.Context
import com.projectbox.footballmatchschedule.db.ManagedDB
import com.projectbox.footballmatchschedule.model.AppData
import com.projectbox.footballmatchschedule.model.ScheduleType
import com.projectbox.footballmatchschedule.repository.ScheduleAPIRepository
import com.projectbox.footballmatchschedule.repository.TeamAPIRepository
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Unconfined
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module.applicationContext
import org.koin.standalone.StandAloneContext
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
class AppUnitTest : KoinTest {

    private fun createManagedDB(context: Context): ManagedDB {
        return ManagedDB(context)
    }

    private fun createInterceptor(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder().addInterceptor(logging)
                .connectTimeout(60L, TimeUnit.SECONDS)
                .writeTimeout(60L, TimeUnit.SECONDS)
                .readTimeout(60L, TimeUnit.SECONDS)
                .build()
    }

    // url is defaulted in the param to ease the unit testing
    private fun createService(client: OkHttpClient, url: String = "https://www.thesportsdb.com/api/v1/json/1/"): IService {
        val retrofit = Retrofit.Builder().baseUrl(url).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit.create(IService::class.java)
    }

    private val repository: ScheduleAPIRepository by inject()
    private val teamRepository: TeamAPIRepository by inject()

    @Before
    fun before() {
        val contextModules = applicationContext {
            bean { RuntimeEnvironment.application } bind Context::class

            bean { createInterceptor() }
            bean { createService(get()) }
            bean { createManagedDB(get()) }

            factory { ScheduleAPIRepository(get(), get()) }
            factory { TeamAPIRepository(get(), get()) }
        }
        StandAloneContext.startKoin(listOf(contextModules))
    }

    @After
    fun after() {
        StandAloneContext.closeKoin()
    }

    @Test
    fun `past schedule has the right count`() {
        runBlocking(CommonPool) {
            val schedules = repository.getSchedules(ScheduleType.Past, AppData.leagues.first().id)

            assertThat(schedules, hasSize(15))
        }
    }

    @Test
    fun `next schedule has the right count`() {
        runBlocking(Unconfined) {
            val schedules = repository.getSchedules(ScheduleType.Next, AppData.leagues.first().id)

            assertThat(schedules, hasSize(15))
        }
    }

    @Test
    fun `EPL teams has the right count`() {
        runBlocking(UI) {
            val teams = teamRepository.getAllTeams(AppData.leagues.first().id)

            assertThat(teams, hasSize(20))
        }
    }

    @Test
    fun `Searching Team 'bar' has Barcelona in it`() {
        runBlocking(UI) {
            val teams = teamRepository.searchTeamFromAPI("bar").map { it.teamName }

            assertThat(teams, hasItem("Barcelona"))
        }
    }

    @Test
    fun `Searching Schedule 'chelsea' has Chelsea home schedule in it`() {
        runBlocking(UI) {
            val schedules = repository.searchSchedule("chelsea").map { it.homeTeam }

            assertThat(schedules, hasItem("Chelsea"))
        }
    }

    @Test
    fun `Barcelona has Lionel Messi as a player`() {
        runBlocking(UI) {
            val teams = teamRepository.searchTeamFromAPI("bar")
            val barcelona = teams.first { it.teamName == "Barcelona" }

            val players = teamRepository.getPlayersFromTeam(barcelona.idTeam).map { it.name }

            assertThat(players, hasItem("Lionel Messi"))
        }
    }
}
