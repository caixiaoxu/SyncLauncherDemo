package com.lsy.synclauncher.task

import android.os.Process
import androidx.annotation.IntRange
import com.lsy.synclauncher.executor.AppStartTaskExecutor
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executor

/**
 * 任务基类
 *      定义定时器(用于等待父任务执行完成，数量=父任务的数量)
 *      函数：父任务的列表
 *      函数：让当前任务进入等待状态
 *      函数：通知父任务完成，减少定时数
 *      函数：执行线程
 *      函数：线程的优先级
 *      函数：是否需要等待
 *      函数：是否执行在主线程
 *      函数：任务执行内容
 */
abstract class AppStartTask {
    //定义定时器(用于等待父任务执行完成，数量=父任务的数量)
    private val countDownLatch = CountDownLatch(getDependsTaskList()?.size ?: 0);

    /**
     * 父任务的列表
     */
    open fun getDependsTaskList(): List<Class<out AppStartTask>>? = null

    /**
     * 让当前任务进入等待状态
     */
    fun waitToNotify() {
        try {
            countDownLatch.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    /**
     * 通知父任务完成，减少定时数
     */
    fun notifyDependsTaskFinish() {
        countDownLatch.countDown()
    }

    /**
     * 执行线程
     */
    open fun runOnExecutor(): Executor = AppStartTaskExecutor.sIOThreadPoolExecutor

    /**
     * 优先级的范围，可根据Task重要程度及工作量指定；之后根据实际情况决定是否有必要放更大
     */
    @IntRange(
        from = Process.THREAD_PRIORITY_FOREGROUND.toLong(),
        to = Process.THREAD_PRIORITY_LOWEST.toLong()
    )
    open fun priority(): Int = Process.THREAD_PRIORITY_BACKGROUND

    /**
     * 是否需要等待
     */
    open fun needWait(): Boolean = true

    /**
     * 是否执行在主线程
     */
    abstract fun isRunOnMainThread(): Boolean

    /**
     * 执行的任务内容
     */
    abstract fun run()
}