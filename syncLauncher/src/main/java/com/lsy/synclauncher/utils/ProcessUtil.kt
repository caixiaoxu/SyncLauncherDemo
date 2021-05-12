package com.lsy.synclauncher.utils

import android.app.ActivityManager
import android.content.Context
import android.os.Process

object ProcessUtil {
    /**
     * 是否在主进程
     */
    fun isMainProcess(context: Context): Boolean {
        return context.packageName == getProcessName(context)
    }

    /**
     * 获取当前线程名
     */
    fun getProcessName(context: Context): String? {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses
        val myPid = Process.myPid()
        if (appProcesses.isNullOrEmpty()) return null
        for (appProcess in appProcesses) {
            if (appProcess.processName == context.packageName) {
                if (appProcess.pid == myPid) {
                    return appProcess.processName
                }
            }
        }
        return null
    }
}