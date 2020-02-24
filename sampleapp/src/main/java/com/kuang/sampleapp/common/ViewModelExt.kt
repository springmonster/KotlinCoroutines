package com.kuang.sampleapp.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils

fun <T> ViewModel.request(requestBlock: suspend () -> BaseResponse<T>): LiveData<BaseResult> {
    return liveData {
        if (NetworkUtils.isConnected()) {
            emit(ResultLoading)
        }
        try {
            val response = requestBlock()
            LogUtils.d(response)
            emit(ResultSuccess(response))
        } catch (e: Exception) {
            LogUtils.e(e.message)
            emit(ResultError(e))
        }
    }
}