package com.kuang.sampleapp.service

import com.kuang.sampleapp.api.GithubApi
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory

val githubService: GithubApi by lazy {
    retrofit2.Retrofit.Builder()
        .client(OkHttpClient.Builder().build())
        .baseUrl("http://yapi.demo.qunar.com/mock/83026/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GithubApi::class.java)
}