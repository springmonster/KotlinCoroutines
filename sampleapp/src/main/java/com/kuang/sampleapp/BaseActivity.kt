package com.kuang.sampleapp

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kuang.sampleapp.common.BaseResult
import com.kuang.sampleapp.common.ResultError
import com.kuang.sampleapp.common.ResultLoading
import com.kuang.sampleapp.common.ResultSuccess

open class BaseActivity : AppCompatActivity() {

    fun <T> responseProcessor(
        success: (T) -> Unit = {},
        error: (Exception) -> Unit = {}
    ): (BaseResult) -> Unit {
        return { it ->
            val textView: TextView? = findViewById(R.id.activity_pb)

            when (it) {
                is ResultLoading -> {
                    textView?.let { tv ->
                        tv.text = getString(R.string.loading)
                    }
                }
                is ResultSuccess<*> -> {
                    textView?.let { tv ->
                        tv.text = getString(R.string.loaded)
                    }
                    success(it.baseResponse.data as T)
                }
                is ResultError -> {
                    textView?.let { tv ->
                        tv.text = it.exception.toString()
                    }
                    error(it.exception)
                }
            }
        }
    }
}