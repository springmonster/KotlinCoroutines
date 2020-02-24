package com.kuang.launch

import com.kuang.utils.Logger
import kotlinx.coroutines.*

suspend fun main() {
//    ioStart()
    io2Start()
}

private suspend fun ioStart() {
    val job = GlobalScope.launch(Dispatchers.IO, start = CoroutineStart.DEFAULT) {
        // 2. DefaultDispatcher-worker-1
        Logger.error(1)
        delay(1000)
        // 3. DefaultDispatcher-worker-1
        Logger.debug(2)
    }
    // 1. main thread
    Logger.debug(3)
    job.join()
    // 4. DefaultDispatcher-worker-1
    Logger.debug(4)
}

private suspend fun io2Start() {
    val job = GlobalScope.launch(Dispatchers.Main) {
        Logger.debug(1)
        delay(1000)
        Logger.debug(2)
        val deferred = async(Dispatchers.IO) {
            // 只有4和5是在新线程上去做处理
            Logger.debug(4)
            delay(100)
            Logger.debug(5)
            "Hello"
        }
        Logger.debug(6)
        val await = deferred.await()
        Logger.debug(7)
    }
    Logger.debug(3)
    job.join()
    Logger.debug(4)
}