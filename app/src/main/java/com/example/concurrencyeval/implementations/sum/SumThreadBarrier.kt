package com.example.concurrencyeval.implementations.sum

import com.example.concurrencyeval.activities.ConcSumActivity
import com.example.concurrencyeval.util.RunReport
import java.util.concurrent.CyclicBarrier
import kotlin.system.measureTimeMillis

class SumThreadBarrier(
    numbers: Int, tasks: Int, activity: ConcSumActivity
) : SumImplementation(numbers, tasks, activity){


    private val mAns = LongArray(numbers)
    private var mBarrier = CyclicBarrier(
        tasks, Runnable {
            for (i in 0 until numbers) {
                mArr[i] = mAns[i]
            }
        }
    )

    override fun execute(): RunReport {

        val time = measureTimeMillis {
            val threads = mutableListOf<Thread>()
            for (i in 0 until tasks){
                threads+= Thread(SumWorkerRunnableWithBarrier(mArr, mAns, numbers, tasks, i, mBarrier))
                threads[i].start()
            }
            threads.forEach { thread ->
                thread.join()
            }
        }

        return RunReport(time)
    }
}