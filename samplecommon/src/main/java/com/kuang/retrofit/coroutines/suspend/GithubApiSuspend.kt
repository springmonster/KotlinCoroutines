package com.kuang.retrofit.coroutines.suspend

import com.kuang.retrofit.dto.User
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubApiSuspend {

    @GET("/users/{login}")
    suspend fun getUser(@Path("login") login: String): User
}