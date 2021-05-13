package com.lsy.synclauncherdemo

import com.lsy.synclauncher.task.AppStartTask
import com.lsy.synclauncher.utils.AppStartTaskLogUtil

/**
 * @author Xuwl
 * @date 2021/5/13
 *
 */
class AppStartTaskFive : AppStartTask() {
    override fun isRunOnMainThread(): Boolean = false

    override fun run() {
        Thread.sleep(500)
        AppStartTaskLogUtil.showLog("任务五")
    }
}