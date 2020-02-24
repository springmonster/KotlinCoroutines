package com.kuang.sampleapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.kuang.sampleapp.dto.User
import com.kuang.sampleapp.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelProvider.NewInstanceFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_activity_btn.setOnClickListener {
            mainViewModel.getUser("SpringMonster").observe(this, responseProcessor<User>(
                {
                    main_activity_tv.text = it.login
                }
            ))
        }
    }
}
