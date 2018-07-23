package com.projectbox.footballmatchschedule

import android.content.Context
import com.projectbox.footballmatchschedule.repository.FavoriteManagedDB
import com.projectbox.footballmatchschedule.repository.ScheduleRepository
import com.projectbox.footballmatchschedule.repository.TeamRepository
import com.projectbox.footballmatchschedule.viewmodel.ScheduleDetailVM
import com.projectbox.footballmatchschedule.viewmodel.ScheduleVM
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by adinugroho
 */
class KoinModules {
    companion object {
        const val URL = "SERVER_URL"
    }


    fun getModules() = applicationContext {
        bean { createInterceptor() }
//            bean { createService(get(), getProperty(URL)) }
        bean { createService(get()) }
        bean { favScheduleDB(get()) }

        factory { ScheduleRepository(get()) }
        factory { TeamRepository(get()) }

        viewModel { ScheduleVM(get(), get(), get()) }
        viewModel { ScheduleDetailVM(get()) }
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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        return retrofit.create(IService::class.java)
    }

    private fun favScheduleDB(context: Context): FavoriteManagedDB {
        return FavoriteManagedDB(context)
    }
}