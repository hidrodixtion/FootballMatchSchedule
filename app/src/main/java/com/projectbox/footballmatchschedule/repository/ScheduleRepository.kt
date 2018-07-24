package com.projectbox.footballmatchschedule.repository

import com.projectbox.footballmatchschedule.IService
import com.projectbox.footballmatchschedule.model.Schedule
import com.projectbox.footballmatchschedule.model.ScheduleType
import retrofit2.HttpException
import ru.gildor.coroutines.retrofit.await
import timber.log.Timber

/**
 * Created by adinugroho
 */
class ScheduleRepository(private val service: IService) {
    suspend fun getSchedules(scheduleType: ScheduleType): List<Schedule> {
        val request = when (scheduleType) {
            ScheduleType.Past -> service.getPastLeague()
            ScheduleType.Next -> service.getNextLeague()
            ScheduleType.Favorite -> return emptyList()
        }

        try {
            val scheduleResponse = request.await()
            return scheduleResponse.scheduleList
        } catch (e: HttpException) {
            Timber.e("Error ${e.code()} : ${e.localizedMessage}")
        } catch (e: Throwable) {
            Timber.e("Call unsuccessful because ${e.localizedMessage}")
        }

        return emptyList()
    }
}