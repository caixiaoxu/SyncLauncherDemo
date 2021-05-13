package com.lsy.synclauncherdemo

import com.lsy.synclauncher.task.AppStartTask
import com.lsy.synclauncher.utils.AppStartTaskLogUtil

/**
 * @author Xuwl
 * @date 2021/5/13
 *
 */
class AppStartTaskThree : AppStartTask() {
    override fun isRunOnMainThread(): Boolean = true

    override fun run() {
        Thread.sleep(100)
        AppStartTaskLogUtil.showLog("任务三")
    }
}