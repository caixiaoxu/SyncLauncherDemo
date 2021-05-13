package com.lsy.synclauncher.utils

import com.lsy.synclauncher.task.AppStartTask
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

/**
 * 任务排序算法(有向无坏图)
 * 具体细节请看https://blog.csdn.net/u010629285/article/details/116491535
 */
object AppStartTaskSortUtil {

    fun sortAppStartTask(
        taskList: ArrayList<out AppStartTask>,//原始任务列表
        mTaskMap: HashMap<Class<out AppStartTask>, AppStartTask>,//按class存储每个任务，方便查找
        mTaskChildMap: HashMap<Class<out AppStartTask>, HashSet<Class<out AppStartTask>>>//保存任务以及子任务
    ): ArrayList<AppStartTask> {
        //最终排序列表
        val sortTaskList: ArrayList<AppStartTask> = ArrayList()
        //存储任务包含的父任务数
        val taskIntMap: HashMap<Class<out AppStartTask>, Int> = HashMap()
        val deque: Deque<Class<out AppStartTask>> = ArrayDeque()
        //首先分别取出数据，并入栈入度为0的数据
        //把孩子都加进去
        taskList.forEach { task ->
            if (!taskIntMap.containsKey(task::class.java)) {
                //按class存储任务
                mTaskMap[task::class.java] = task
                //父任务数
                taskIntMap[task::class.java] = task.getDependsTaskList()?.size ?: 0
                //入度为0的队列
                if (taskIntMap[task::class.java] == 0) {
                    deque.offer(task::class.java)
                }
            } else {
                throw RuntimeException("任务重复了: " + task::class.java)
            }

            //把任务添加到每个父任务中
            task.getDependsTaskList()?.forEach { depend ->
                mTaskChildMap[depend] = mTaskChildMap.getOrDefault(depend, HashSet()).apply { add(task::class.java) }
            }
        }

        //循环去除入度0的，再把孩子入度变成0的加进去
        while (!deque.isEmpty()) {
            //弹出最后一个
            val aclass = deque.poll()
            //加入列表
            mTaskMap[aclass]?.let { task ->
                sortTaskList.add(task)
            }
            //取出子任务
            mTaskChildMap[aclass]?.forEach { child ->
                //子任务中的父任务数量-1
                taskIntMap[child] = taskIntMap.getOrDefault(child, 0) - 1
                //如果子任务中的父任务数量为0，加入栈
                if (taskIntMap[child] == 0) {
                    deque.offer(child)
                }
            }
        }
        if (sortTaskList.size != taskList.size) throw RuntimeException("出现环了")
        return sortTaskList
    }
}