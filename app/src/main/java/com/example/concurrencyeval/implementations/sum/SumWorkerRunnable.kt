package com.example.concurrencyeval.implementations.sum

import kotlin.math.*


class SumWorkerRunnable(
    val arr: LongArray, val ans: LongArray, private val numbers: Int,
    private val level: Int, val tasks: Int, private val id: Int
) :Runnable {
    override fun run() {
        val sums = ceil(numbers / 2.0.pow(1 + level)).roundToInt()
        val extraSum = if (sums % tasks > id) 1 else 0
        val taskSums = sums / tasks + extraSum
        val firstSumOffset = min(id, sums%tasks)
        val firstSum = id * (sums/tasks) + firstSumOffset

        for (index in firstSum until firstSum + taskSums) {
            if(ceil(numbers / 2.0.pow(level)).roundToInt() > 2 * index + 1)
                ans[index] = arr[2 * index] + arr[2 * index + 1]
            else
                ans[index] = arr[2 * index]
        }
    }
}