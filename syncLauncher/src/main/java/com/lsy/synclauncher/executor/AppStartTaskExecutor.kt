package com.lsy.synclauncher.executor

import java.util.concurrent.*
import kotlin.math.max
import kotlin.math.min

/**
 * 线程提供类
 */
object AppStartTaskExecutor {

    //获取CPU的核处理器数
    private val CPU_COUNT = Runtime.getRuntime().availableProcessors()

    //计算创建对应的线程池数量
    private val CORE_POOL_SIZE = max(2, min(CPU_COUNT - 1, 4))

    //线程池线程数的最大值
    private val MAXIMUM_POOL_SIZE = CORE_POOL_SIZE

    //线程空置回收时间
    private const val KEEP_ALIVE_SECONDS = 5L

    //线程池队列
    private val mPoolWorkQueue: BlockingQueue<Runnable> by lazy { LinkedBlockingQueue() }

    private val mHandler =
        RejectedExecutionHandler { r, _ -> Executors.newCachedThreadPool().execute(r); }

    //获取对应参数，创建CPU线程池
    val sCPUThreadPoolExecutor = ThreadPoolExecutor(
        CORE_POOL_SIZE,
        MAXIMUM_POOL_SIZE,
        KEEP_ALIVE_SECONDS,
        TimeUnit.SECONDS,
        mPoolWorkQueue,
        Executors.defaultThreadFactory(),
        mHandler
    ).apply {
        allowCoreThreadTimeOut(true)
    }

    //创建IO线程池
    val sIOThreadPoolExecutor: ExecutorService =
        Executors.newCachedThreadPool(Executors.defaultThreadFactory())
}