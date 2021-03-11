package com.example.github.network

import com.example.github.BuildConfig
import com.example.github.utils.Constants.SERVER_URL
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object Api {
    private lateinit var serviceGithub: GithubService
    var gson = Gson()

    private fun init() {
        val logging = HttpLoggingInterceptor()
        logging.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
        serviceGithub = retrofit.create(GithubService::class.java)
    }

    fun getService(): GithubService {
        if (!this::serviceGithub.isInitialized) {
            init()
        }
        return serviceGithub
    }
}