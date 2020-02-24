package com.kuang.sampleapp.common

data class BaseResponse<T>(val code: String?, val msg: String?, val data: T)
