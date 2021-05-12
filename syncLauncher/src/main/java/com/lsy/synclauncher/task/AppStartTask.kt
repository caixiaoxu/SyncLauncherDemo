package com.lsy.synclauncher.task

import android.os.Process
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
    fun getDependsTaskList(): List<Class<out AppStartTask>>? = null

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
    fun runOnExecutor(): Executor = AppStartTaskExecutor.sIOThreadPoolExecutor

    /**
     * 线程的优先级
     */
    fun priority(): Int = Process.THREAD_PRIORITY_BACKGROUND


    /**
     * 是否需要等待
     */
    fun needAwait(): Boolean = true

    /**
     * 是否执行在主线程
     */
    abstract fun isRunOnMainThread(): Boolean

    /**
     * 执行的任务内容
     */
    abstract fun run()

}