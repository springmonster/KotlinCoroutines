package com.kuang.utils

import android.util.Log
import org.apache.commons.lang3.time.FastDateFormat
import java.util.*

object Logger {

    private val isAndroid: Boolean by lazy {
        try {
            Class.forName("android.os.Build")
            true
        } catch (e: ClassNotFoundException) {
            false
        }
    }

    private val debugLogger by lazy {
        if (isAndroid) {
            { msg: Any? ->
                Log.d("Coroutine", msg.toString())
                Unit
            }
        } else {
            ::println
        }
    }

    private val errorLogger by lazy {
        if (isAndroid) {
            { msg: Any? ->
                Log.e("Coroutine", msg.toString())
                Unit
            }
        } else {
            System.err::println
        }
    }

    private val dateFormat by lazy { FastDateFormat.getInstance("HH:mm:ss:SSS") }
    private val header = { stackTraceElement: StackTraceElement ->
        when (isAndroid) {
            true -> "[${Thread.currentThread().name}] "
            false -> "${dateFormat.format(Date(System.currentTimeMillis()))} [${Thread.currentThread().name}] "
        } + codeLine(stackTraceElement)
    }

    private fun codeLine(ste: StackTraceElement): String {
        val buf = StringBuilder()
        buf.append("${ste.className.split(".").last()}.${ste.methodName}")
        if (ste.isNativeMethod) {
            buf.append("(Native Method)")
        } else {
            val fName = ste.fileName
            if (fName == null) {
                buf.append("(Unknown Source)")
            } else {
                val lineNum = ste.lineNumber
                buf.append('(')
                buf.append(fName)
                if (lineNum >= 0) {
                    buf.append(':').append(lineNum)
                }
                buf.append("):")
            }
        }
        return buf.toString()
    }

    @JvmStatic
    fun debug(msg: Any?) {
        debugLogger(
            "${header(
                Thread.currentThread().stackTrace.dropWhile { it.fileName != "Logger.kt" || it.methodName != "debug" }[1]
            )} $msg"
        )
    }

    @JvmStatic
    fun error(msg: Any?) {
        errorLogger(
            "${header(
                Thread.currentThread().stackTrace.dropWhile { it.fileName != "Logger.kt" || it.methodName != "error" }[1]
            )} $msg"
        )
    }
}