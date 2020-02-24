package com.kuang.sampleapp.common

sealed class BaseResult

object ResultLoading : BaseResult()

data class ResultSuccess<T>(val baseResponse: BaseResponse<T>) : BaseResult()

data class ResultError(val exception: Exception) : BaseResult()