package com.example.concurrencyeval.implementations.sum

import com.example.concurrencyeval.activities.ConcSumActivity
import com.example.concurrencyeval.util.RunReport
import kotlin.math.*
import kotlin.system.measureTimeMillis


class SumThread(
    numbers: Int, tasks: Int, activity: ConcSumActivity
) : SumImplementation(numbers, tasks, activity){

    override fun execute(): RunReport {
        val arr = SumUtil.randArray(numbers)

        val time = measureTimeMillis {
            val ans = LongArray(numbers)
            val levels = ceil(log(numbers.toDouble(), 2.0)).roundToInt() // number of levels
            for (level in 0 until levels) {
                val tasksToRun = min(tasks, ceil(numbers / 2.0.pow((1 + level))).toInt())
                val threads = mutableListOf<Thread>()
                for (i in 0 until tasksToRun) {
                    threads+= Thread(SumWorkerRunnable(arr, ans, numbers, level, tasks, i))
                }
                for (i in 0 until tasksToRun) {
                    threads[i].start()
                }
                for (i in 0 until tasksToRun) {
                    threads[i].join()
                }
                for (i in 0 until numbers) {
                    arr[i] = ans[i]
                }
            }
        }

        return RunReport(time)
    }
}