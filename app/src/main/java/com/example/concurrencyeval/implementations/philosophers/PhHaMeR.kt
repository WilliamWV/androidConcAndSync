package com.example.concurrencyeval.implementations.philosophers

import android.os.Handler
import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.activities.PhilosophersActivity
import com.example.concurrencyeval.implementations.mm.MMHaMeR
import com.example.concurrencyeval.util.RunReport

class PhHaMeR(
    philosophers: Int, time: Int, activity: PhilosophersActivity
) : PhImplementation(philosophers, time, activity){
    override fun execute(): RunReport {
        val handlerThreads = mutableListOf<MMHaMeR.HaMeRThread>()
        val handlers = mutableListOf<Handler>()
        for (i in 0 until philosophers){
            val handlerThread = MMHaMeR.HaMeRThread(i.toString())
            handlerThread.start()
            handlerThreads += handlerThread
            val handler = Handler(handlerThread.looper)
            handlers+= handler
        }

        val forks: Array<String> = Array(philosophers){LCS.randString(Constants.LCS_RANGE, Constants.LCS_LENGTH)}
        val frequencies = IntArray(philosophers) {0}

        for (i in 0 until philosophers){
            val leftFork = forks[i]
            val rightFork = forks[(i+1)%forks.size]

            //If all philosophers attempts to obtain the left fork first
            //except from one that attempts the right first there is no
            //possible to occur deadlock
            if (i == 0){
                handlers[i].post(PhWorkerRunnable(rightFork, leftFork, time, frequencies, i))
            } else{
                handlers[i].post(PhWorkerRunnable(leftFork, rightFork, time, frequencies, i))
            }

        }
        for (i in 0 until philosophers){

            handlerThreads[i].quitSafely()
            handlerThreads[i].join()
        }

        return RunReport(frequencies)
    }

}