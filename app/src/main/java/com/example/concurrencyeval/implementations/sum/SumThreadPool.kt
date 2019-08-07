package com.example.concurrencyeval.implementations.sum

import com.example.concurrencyeval.activities.ConcSumActivity
import com.example.concurrencyeval.util.RunReport
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.ThreadPoolExecutor
import kotlin.math.*
import kotlin.system.measureTimeMillis

class SumThreadPool(
    numbers: Int, tasks: Int, activity: ConcSumActivity
): SumImplementation(numbers, tasks, activity) {

    private val numOfCores = Runtime.getRuntime().availableProcessors()
    private val threadPool : ThreadPoolExecutor = Executors.newFixedThreadPool(numOfCores) as ThreadPoolExecutor


    override fun execute(): RunReport {
        val arr = SumUtil.randArray(numbers)
        val time = measureTimeMillis {
            val ans = LongArray(numbers)
            val levels = ceil(log(numbers.toDouble(), 2.0)).roundToInt() // number of levels
            for (level in 0 until levels) {
                val tasksToRun = min(tasks, ceil(numbers / 2.0.pow((1 + level))).toInt())
                //Needs to waits the tasks of this level to end before the tasks from the next level to run

                val tasksOfThisLevel = mutableListOf<Future<out Any>>()
                for (i in 0 until tasksToRun) {
                    //store reference to the execution of current level tasks
                    tasksOfThisLevel += threadPool.submit(
                        SumWorkerRunnable(arr, ans, numbers, level, tasks, i)
                    )
                }
                //waits until the execution of the current level threads to end
                tasksOfThisLevel.forEach {
                    it.get()
                }

                for (i in 0 until numbers) {
                    arr[i] = ans[i]
                }
            }
        }
        return RunReport(time)
    }
}