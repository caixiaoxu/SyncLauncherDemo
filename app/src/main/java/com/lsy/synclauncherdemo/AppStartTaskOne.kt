package com.lsy.synclauncherdemo

import com.lsy.synclauncher.task.AppStartTask
import com.lsy.synclauncher.utils.AppStartTaskLogUtil

/**
 * @author Xuwl
 * @date 2021/5/13
 *
 */
class AppStartTaskOne : AppStartTask() {
    override fun isRunOnMainThread(): Boolean = false

    override fun run() {
        AppStartTaskLogUtil.showLog("任务一")
    }

    override fun getDependsTaskList(): List<Class<out AppStartTask>>? {
        return arrayListOf(AppStartTaskTwo::class.java)
    }
}