package com.example.concurrencyeval.implementations.philosophers

import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.activities.PhilosophersActivity
import com.example.concurrencyeval.util.RunReport


class PhThread(
    philosophers: Int, time: Int, sync: Int, activity:PhilosophersActivity
): PhImplementation(philosophers, time, sync, activity){

    override fun execute(): RunReport {
        val phThreads: MutableList<Thread> = mutableListOf()
        val forks: Array<String> = Array(philosophers){LCS.randString(Constants.LCS_RANGE, Constants.LCS_LENGTH)}
        val frequencies = IntArray(philosophers) {0}

        for (i in 0 until philosophers){
            val leftFork = forks[i]
            val rightFork = forks[(i+1)%forks.size]

            //If all philosophers attempts to obtain the left fork first
            //except from one that attempts the right first there is no
            //possible to occur deadlock
            phThreads += if (i == 0){
                Thread(PhWorkerRunnableSync(rightFork, leftFork, time, frequencies, i))
            } else{
                Thread(PhWorkerRunnableSync(leftFork, rightFork, time, frequencies, i))
            }
            phThreads[i].start()
        }

        for (i in 0 until philosophers){
            phThreads[i].join()
        }
        return RunReport(frequencies)
    }
}