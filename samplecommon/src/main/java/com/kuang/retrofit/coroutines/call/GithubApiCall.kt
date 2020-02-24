package com.kuang.retrofit.coroutines.call

import com.kuang.retrofit.dto.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubApiCall {

    @GET("/users/{login}")
    fun getUser(@Path("login") login:String):Call<User>
}