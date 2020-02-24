package com.kuang.retrofit.coroutines.suspend

import com.kuang.utils.Logger
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory

val githubNetSuspend by lazy {
    retrofit2.Retrofit.Builder()
        .client(OkHttpClient.Builder().addInterceptor {
            it.proceed(it.request()).apply {
                Logger.debug("request : ${code()}")
            }
        }.build())
        .baseUrl("https://mobile-ms.uat.homecreditcfc.cn/mock/5c3daa4e11249a002776ba9a/sapp")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GithubApiSuspend::class.java)
}

suspend fun main() {
    val user = githubNetSuspend.getUser("SpringMonster")
    Logger.debug(user)
}