package com.example.concurrencyeval.implementations.sum

import kotlin.math.max
import kotlin.math.pow


class SumWorkerRunnable(
    val arr: LongArray, val ans: LongArray, private val numbers: Int,
    private val level: Int, val tasks: Int, private val id: Int
) :Runnable {
    override fun run() {
        val sums = numbers / 2.0.pow(1 + level).toInt()
        val sumsToRun = max(sums / tasks, 1)
        for (i in 0 until sumsToRun) {
            val index = id * sumsToRun + i
            ans[index] = arr[2 * index] + arr[2 * index + 1]
        }
    }
}