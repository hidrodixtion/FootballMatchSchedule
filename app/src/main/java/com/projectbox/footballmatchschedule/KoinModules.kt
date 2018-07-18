package com.projectbox.footballmatchschedule

import com.projectbox.footballmatchschedule.viewmodel.ScheduleVM
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.Module
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

    fun getModules(): Module {
        return applicationContext {
            bean { createInterceptor() }
            bean { createService(get(), getProperty(URL)) }

            viewModel { ScheduleVM(get()) }
        }
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

    private fun createService(client: OkHttpClient, url: String): IService {
        val retrofit = Retrofit.Builder().baseUrl(url).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        return retrofit.create(IService::class.java)
    }
}