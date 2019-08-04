package com.example.concurrencyeval.implementations.philosophers

import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.activities.PhilosophersActivity
import com.example.concurrencyeval.util.RunReport
import java.io.File


class PhThread(
    philosophers: Int, time: Int, activity:PhilosophersActivity
): PhImplementation(philosophers, time, activity){

    override fun execute(): RunReport {
        val phThreads: MutableList<Thread> = mutableListOf()
        val forks: Array<Any> = Array(philosophers){Any()}
        val file = File(phDir, Constants.PHILOSOPHERS_FILE)

        for (i in 0 until philosophers){
            val leftFork = forks[i]
            val rightFork = forks[(i+1)%forks.size]

            //If all philosophers attempts to obtain the left fork first
            //except from one that attempts the right first there is no
            //possible to occur deadlock
            phThreads += if (i == 0){
                Thread(PhWorkerRunnable(rightFork, leftFork, time, file, i))
            } else{
                Thread(PhWorkerRunnable(leftFork, rightFork, time, file, i))
            }
            phThreads[i].start()
        }

        for (i in 0 until philosophers){
            phThreads[i].join()
        }
        return PhUtil.buildReport(philosophers, File(phDir, Constants.PHILOSOPHERS_FILE))
    }
}