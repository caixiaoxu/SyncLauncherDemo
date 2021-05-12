package com.lsy.synclauncher.dispatcher

import android.content.Context
import android.os.Looper
import com.lsy.synclauncher.task.AppStartTask
import com.lsy.synclauncher.utils.AppStartTaskLogUtil
import com.lsy.synclauncher.utils.ProcessUtil
import java.util.concurrent.atomic.AtomicInteger

/**
 * 启动任务调度类(单例模式)
 *      1、添加任务到列表
 *      2、开启任务
 *      3、任务等待
 */
object AppStartTaskDispatcher {
    var mContext: Context? = null

    //判断当前是否在主线程
    private var isInMainProgress = false

    //是否显示日志
    var mShowLog: Boolean = false

    //需要等待的数量
    private val mNeedWaitCount = AtomicInteger()

    //所有启动任务任务
    private val mStartTaskList: ArrayList<AppStartTask> = ArrayList()

    //存放每个Task  （key= Class < ? extends AppStartTask>）
    private val mTaskMap: HashMap<Class<out AppStartTask>, AppStartTask> = HashMap()

    //每个Task的孩子 （key= Class < ? extends AppStartTask>）
    private val mTaskChildMap: HashMap<Class<out AppStartTask?>, List<Class<out AppStartTask>>> =
        HashMap()

    //记录启动总时间
    private var mStartTime: Long = 0L

    fun setContext(context: Context): AppStartTaskDispatcher {
        this.mContext = context
        isInMainProgress = ProcessUtil.isMainProcess(context)
        return this
    }

    fun setShowLog(showLog: Boolean): AppStartTaskDispatcher {
        mShowLog = showLog;
        return this
    }

    /**
     * 添加启动任务类
     */
    fun addAppStartTask(task: AppStartTask?): AppStartTaskDispatcher {
        task?.let {
            //加入列表
            mStartTaskList.add(task)
            //如果需要等待执行，等待数量加1
            if (task.needAwait()) {
                mNeedWaitCount.getAndIncrement()
            }
        } ?: throw RuntimeException("addAppStartTask() 传入的appStartTask为null")
        return this
    }

    /**
     * 开启任务
     */
    fun start(): AppStartTaskDispatcher {
        if (null == mContext) throw RuntimeException("context为null，调用start()方法前必须调用setContext()方法")
        if (Looper.getMainLooper() != Looper.myLooper()) throw RuntimeException("start方法必须在主线程调用")
        if (!isInMainProgress) {
            AppStartTaskLogUtil.showLog("当前进程非主进程")
            return this
        }
        mStartTime = System.currentTimeMillis()
        return this
    }
}

