package com.kuang.launch

import com.kuang.utils.Logger
import kotlinx.coroutines.*
import java.util.concurrent.Executors
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

suspend fun main() {
    ioStart()
//    lazyStart()
//    atomicStart()
//    unDispatchedStart()
//    openDebugStart()
}

private suspend fun ioStart() {
    val job = GlobalScope.launch(start = CoroutineStart.DEFAULT) {
        Logger.error(1)
        delay(1000)
        Logger.debug(2)
    }
    Logger.debug(3)
    job.join()
    Logger.debug(4)
}

private suspend fun lazyStart() {
    val deferred = GlobalScope.async(start = CoroutineStart.LAZY) {
        Logger.debug(1)
        "Hello"
    }
    Logger.debug(2)
    val await = deferred.await()
    Logger.debug(await)
}

private suspend fun atomicStart() {
    val job = GlobalScope.launch(start = CoroutineStart.ATOMIC) {
        Logger.error(1)
        delay(1000)
        Logger.debug(2)
    }
    Logger.debug(3)
    job.cancel()
    job.join()
//    delay(5000)
    Logger.debug(4)
}

/**
 * 注意查看log输出的线程
 */
private suspend fun unDispatchedStart() {
    val job = GlobalScope.launch(start = CoroutineStart.UNDISPATCHED) {
        Logger.error(1)
        delay(1000)
        Logger.debug(2)
    }
    Logger.debug(3)
//    job.cancel()
    job.join()
//    delay(5000)
    Logger.debug(4)
}

private suspend fun openDebugStart() {
    val job =
        GlobalScope.launch(MyContinuationInterceptor() + CoroutineName("HelloWorld")) {
            Logger.error(1)
            delay(1000)
            Logger.debug(2)
        }
    Logger.debug(3)
    job.join()
//    delay(5000)
    Logger.debug(4)
}

class MyContinuationInterceptor : ContinuationInterceptor {
    override val key: CoroutineContext.Key<*>
        get() = ContinuationInterceptor

    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> {
        return MyContinuation(continuation)
    }

}

class MyContinuation<T>(private val continuation: Continuation<T>) : Continuation<T> {

    private val executor = Executors.newSingleThreadExecutor {
        Thread(it, "MyThreadExecutor").also {
            it.isDaemon = true
        }
    }

    override fun resumeWith(result: Result<T>) {
        Logger.debug(result)
        executor.submit {
            continuation.resumeWith(result)
        }
        Logger.debug("After resume with")
    }

    override val context: CoroutineContext
        get() = continuation.context
}