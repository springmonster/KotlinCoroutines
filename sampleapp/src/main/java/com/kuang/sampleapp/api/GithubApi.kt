package com.kuang.sampleapp.api

import com.kuang.sampleapp.common.BaseResponse
import com.kuang.sampleapp.dto.User
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubApi {

    @GET("users/{user}")
    suspend fun getUser(@Path("user") user: String): BaseResponse<User>
}