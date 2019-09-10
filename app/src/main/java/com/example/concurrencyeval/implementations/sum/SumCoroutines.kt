package com.example.concurrencyeval.implementations.sum

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


    override fun execute(): RunReport {
        val time = measureTimeMillis {
            runBlocking {
                val ans = LongArray(numbers)
                val levels = ceil(log(numbers.toDouble(), 2.0)).roundToInt() // number of levels
                for (level in 0 until levels) {
                    val tasksToRun = min(tasks, ceil(numbers / 2.0.pow((1 + level))).toInt())
                    val jobs:MutableList<Job> = mutableListOf()
                    for (i in 0 until tasksToRun) {
                        jobs += GlobalScope.launch {
                            val sums = ceil(numbers / 2.0.pow(1 + level)).roundToInt()
                            val extraSum = if (sums % tasks > i) 1 else 0
                            val taskSums = sums / tasks + extraSum
                            val firstSumOffset = min(i, sums%tasks)
                            val firstSum = i * (sums/tasks) + firstSumOffset

                            for (index in firstSum until firstSum + taskSums) {
                                if(ceil(numbers / 2.0.pow(level)).roundToInt() > 2 * index + 1)
                                    ans[index] = mArr[2 * index] + mArr[2 * index + 1]
                                else
                                    ans[index] = mArr[2 * index]
                            }
                        }
                    }
                    for (i in 0 until tasksToRun) {
                        jobs[i].join()
                    }
                    for (i in 0 until numbers) {
                        mArr[i] = ans[i]
                    }
                }
            }
        }
        return RunReport(time)

    }
}