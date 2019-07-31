package com.example.concurrencyeval.implementations

class MMWorkerRunnable(
    val m1: Array<LongArray>, val m2: Array<LongArray>,
    val ans: Array<LongArray>, val size: Int, val tasks: Int,
    val id: Int
) : Runnable {

    override fun run() {
        val lines = size / tasks
        val firstLine = id * lines

        for (i in firstLine until firstLine+lines){
            for (j in 0 until size){
                var sum: Long = 0
                for (k in 0 until size){
                    sum += m1[i][k] * m2[k][j]
                }
                ans[i][j] = sum
            }
        }
    }
}