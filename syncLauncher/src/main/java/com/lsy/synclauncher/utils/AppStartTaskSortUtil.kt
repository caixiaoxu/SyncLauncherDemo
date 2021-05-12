package com.lsy.synclauncher.utils

import com.lsy.synclauncher.task.AppStartTask
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * 任务排序算法(有向无坏图)
 */
object AppStartTaskSortUtil {

    fun sortAppStartTask(
        taskList: ArrayList<out AppStartTask>,
        mTaskMap: HashMap<Class<out AppStartTask>, AppStartTask>,
        mTaskChildMap: HashMap<Class<out AppStartTask>, ArrayList<Class<out AppStartTask>>>
    ): ArrayList<AppStartTask> {
        val sortTaskList: ArrayList<AppStartTask> = ArrayList()
        val taskIntMap: HashMap<Class<out AppStartTask>, Int> = HashMap()
        val deque: Deque<Class<out AppStartTask>> = ArrayDeque()
        //首先分别取出数据，并入栈入度为0的数据
        for (task in taskList) {
            if (!taskIntMap.containsKey(task::class.java)) {
                mTaskMap[task::class.java] = task
                taskIntMap[task::class.java] = task.getDependsTaskList()?.size ?: 0
                mTaskChildMap[task::class.java] = ArrayList()
                //入度为0的队列
                if (taskIntMap[task::class.java] == 0) {
                    deque.offer(task::class.java)
                }
            } else {
                throw RuntimeException("任务重复了: " + task::class.java)
            }
        }
        //把孩子都加进去
        taskList.forEach { task ->
            task.getDependsTaskList()?.forEach { depend ->
                mTaskChildMap.getOrDefault(depend, ArrayList()).add(task::class.java)
            }
        }

        //循环去除入度0的，再把孩子入度变成0的加进去
        while (!deque.isEmpty()) {
            val aclass = deque.poll()
            mTaskMap[aclass]?.let { task ->
                sortTaskList.add(task)
            }
            mTaskChildMap[aclass]?.forEach { child ->
                taskIntMap[child] = taskIntMap.getOrDefault(child, 0) - 1
                if (taskIntMap[child] == 0) {
                    deque.offer(child)
                }
            }
        }
        if (sortTaskList.size != taskList.size) throw RuntimeException("出现环了")
        return sortTaskList
    }
}