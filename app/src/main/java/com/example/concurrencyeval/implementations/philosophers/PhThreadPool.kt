package com.example.concurrencyeval.implementations.philosophers

import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.activities.PhilosophersActivity
import com.example.concurrencyeval.util.RunReport
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class PhThreadPool(
    philosophers: Int, time: Int, activity: PhilosophersActivity
) : PhImplementation(philosophers, time, activity) {

    private val threadPool : ThreadPoolExecutor = Executors.newFixedThreadPool(philosophers) as ThreadPoolExecutor


    override fun execute(): RunReport {
        val forks: Array<String> = Array(philosophers){LCS.randString(Constants.LCS_RANGE, Constants.LCS_LENGTH)}
        val frequencies = IntArray(philosophers) {0}


        for (i in 0 until philosophers){
            val leftFork = forks[i]
            val rightFork = forks[(i+1)%forks.size]

            //If all philosophers attempts to obtain the left fork first
            //except from one that attempts the right first there is no
            //possible to occur deadlock
            threadPool.execute(
                if (i == 0){
                    PhWorkerRunnable(rightFork, leftFork, time, frequencies, i)
                } else{
                    PhWorkerRunnable(leftFork, rightFork, time, frequencies, i)
                }
            )
        }

        threadPool.shutdown()
        threadPool.awaitTermination(1, TimeUnit.HOURS)

        return RunReport(frequencies)
    }
}