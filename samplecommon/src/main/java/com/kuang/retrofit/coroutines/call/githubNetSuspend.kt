package com.kuang.retrofit.coroutines.call

import com.kuang.retrofit.dto.User
import com.kuang.utils.Logger
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory

val githubNetCall by lazy {
    retrofit2.Retrofit.Builder()
        .client(OkHttpClient.Builder().addInterceptor {
            it.proceed(it.request()).apply {
                Logger.debug("request : ${code()}")
            }
        }.build())
        .baseUrl("https://api.github.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GithubApiCall::class.java)
}

fun main() {
    githubNetCall.getUser("SpringMonster").enqueue(object : Callback<User> {
        override fun onFailure(call: Call<User>, t: Throwable) {
            Logger.error(t)
        }

        override fun onResponse(call: Call<User>, response: Response<User>) {
            if (response.isSuccessful) {
                Logger.debug(response.body().toString())
            } else {
                Logger.error("${response.code()}:${response.errorBody()}")
            }
        }
    })
}