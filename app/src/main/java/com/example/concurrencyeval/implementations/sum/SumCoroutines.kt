package com.example.concurrencyeval.implementations.sum

import android.util.Log
import com.example.concurrencyeval.activities.ConcSumActivity
import com.example.concurrencyeval.util.RunReport
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.*
import kotlin.system.measureTimeMillis

class SumCoroutines(
    numbers: Int, tasks: Int, activity: ConcSumActivity
): SumImplementation (numbers, tasks, activity){

    private val debug = false

    override fun execute(): RunReport {
        val arr = SumUtil.randArray(numbers)
        if (debug){
            Log.d("SUM_DEBUG", "arr")
            SumUtil.printArr(arr)
        }
        val time = measureTimeMillis {
            runBlocking {
                val ans = LongArray(numbers)
                val logN = (log(numbers.toDouble(), 2.0)).roundToInt() // number of levels
                for (level in 0 until logN) {
                    val threadsToRun = min(tasks, numbers / 2.0.pow((1 + level).toDouble()).toInt())
                    val jobs:MutableList<Job> = mutableListOf()
                    for (i in 0 until threadsToRun) {
                        jobs += GlobalScope.launch {
                            val sums = numbers / 2.0.pow(1 + level).toInt()
                            val sumsToRun = max(sums / tasks, 1)
                            for (t in 0 until sumsToRun) {
                                val index = i * sumsToRun + t
                                ans[index] = arr[2 * index] + arr[2 * index + 1]
                            }
                        }
                    }
                    for (i in 0 until threadsToRun) {
                        jobs[i].join()
                    }
                    for (i in 0 until numbers) {
                        arr[i] = ans[i]
                    }
                }
            }
        }
        if (debug){
            Log.d("SUM_DEBUG", "ans")
            Log.d("SUM_DEBUG", arr[0].toString())
        }

        return RunReport(time)

    }
}