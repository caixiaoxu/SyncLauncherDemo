package com.lsy.synclauncherdemo

import android.app.Application
import com.lsy.synclauncher.dispatcher.AppStartTaskDispatcher

/**
 * @author Xuwl
 * @date 2021/5/13
 *
 */
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        AppStartTaskDispatcher.setContext(this).setShowLog(true).addAppStartTask(AppStartTaskOne())
            .addAppStartTask(AppStartTaskTwo()).addAppStartTask(AppStartTaskThree())
            .addAppStartTask(AppStartTaskFour()).addAppStartTask(AppStartTaskFive())
            .start().await()
    }
}