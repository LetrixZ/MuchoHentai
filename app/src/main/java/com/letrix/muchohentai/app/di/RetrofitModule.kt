package com.letrix.muchohentai.app.di

import com.google.gson.GsonBuilder
import com.letrix.muchohentai.app.network.ApiService
import com.letrix.muchohentai.app.network.JikanApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object RetrofitModule {

    private val prodUrl: String = "https://muchohentai-api.herokuapp.com/"
    private val devUrl: String = "http://192.168.1.40:5000/"

    @Singleton
    @Provides
    fun provideApiService(): ApiService {
        val client = OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS).build()
        return Retrofit.Builder()
            .baseUrl(devUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideJikanService(): JikanApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.jikan.moe/v3/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(JikanApiService::class.java)
    }

}