package com.lsy.synclauncher.runable

import android.os.Process
import com.lsy.synclauncher.dispatcher.AppStartTaskDispatcher
import com.lsy.synclauncher.task.AppStartTask

/**
 * 任务任务类
 *  执行流程：
 *      1、设置优先级
 *      2、设置任务等待
 *      3、执行任务内容
 *      4、通知子任务完成
 *      5、标记任务完成
 */
class AppStartTaskRunnable(
    private val task: AppStartTask,
    private val dispatcher: AppStartTaskDispatcher? = null
) :
    Runnable {
    override fun run() {
        //设置优先级
        Process.setThreadPriority(task.priority())
        //任务等待
        task.waitToNotify()
        //执行内容
        task.run()
        //通知子任务完成
        dispatcher?.setNotifyChildren(task)
        //标记任务结束
        dispatcher?.markAppStartTaskFinish(task)
    }
}