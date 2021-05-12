package com.lsy.synclauncher.utils

import android.util.Log
import com.lsy.synclauncher.dispatcher.AppStartTaskDispatcher

object AppStartTaskLogUtil {
    private const val TAG = "AppStartTask: "
    fun showLog(log: String) {
        if (AppStartTaskDispatcher.mShowLog) {
            Log.e(TAG, log)
        }
    }
}