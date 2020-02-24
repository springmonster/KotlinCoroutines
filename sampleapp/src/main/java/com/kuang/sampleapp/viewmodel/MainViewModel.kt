package com.kuang.sampleapp.viewmodel

import androidx.lifecycle.ViewModel
import com.kuang.sampleapp.common.request
import com.kuang.sampleapp.service.githubService

class MainViewModel : ViewModel() {

    fun getUser(user: String) = request {
        githubService.getUser(user)
    }
}