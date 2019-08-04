package com.example.concurrencyeval.implementations.sum

import android.util.Log
import com.example.concurrencyeval.activities.ConcSumActivity
import com.example.concurrencyeval.util.RunReport
import kotlin.system.measureTimeMillis
import kotlin.math.*


class SumThread(
    numbers: Int, tasks: Int, activity: ConcSumActivity
) : SumImplementation(numbers, tasks, activity){

    private val debug = false

    override fun execute(): RunReport {
        val arr = SumUtil.randArray(numbers)
        val time = measureTimeMillis {
            val ans = LongArray(numbers)
            val logN = (log(numbers.toDouble(), 2.0)).roundToInt() // number of levels
            for (level in 0 until logN) {
                val threadsToRun = min(tasks, numbers / 2.0.pow((1 + level).toDouble()).toInt())
                val threads = mutableListOf<Thread>()
                for (i in 0 until threadsToRun) {
                    threads+= Thread(SumWorkerRunnable(arr, ans, numbers, level, tasks, i))
                }
                for (i in 0 until threadsToRun) {
                    threads[i].start()
                }
                for (i in 0 until threadsToRun) {
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