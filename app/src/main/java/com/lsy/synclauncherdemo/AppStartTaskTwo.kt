package com.lsy.synclauncherdemo

import com.lsy.synclauncher.task.AppStartTask
import com.lsy.synclauncher.utils.AppStartTaskLogUtil

/**
 * @author Xuwl
 * @date 2021/5/13
 *
 */
class AppStartTaskTwo : AppStartTask() {
    override fun isRunOnMainThread(): Boolean = false

    override fun run() {
        Thread.sleep(200)
        AppStartTaskLogUtil.showLog("任务二")
    }

    override fun getDependsTaskList(): List<Class<out AppStartTask>> {
        return arrayListOf(AppStartTaskThree::class.java, AppStartTaskFour::class.java)
    }
}