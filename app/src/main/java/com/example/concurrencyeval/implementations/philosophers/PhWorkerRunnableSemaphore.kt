package com.example.concurrencyeval.implementations.philosophers

import com.example.concurrencyeval.Constants
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit


class PhWorkerRunnableSemaphore(
   val forks: Array<String>, val sems: Array<Semaphore>, val time: Int, val freq: IntArray, val id: Int
): Runnable{


    private val begin = System.currentTimeMillis()

    private fun remainingTime(): Long{
        return time * 1000 - (System.currentTimeMillis() - begin)
    }


    override fun run() {

        var firstFork = id%forks.size
        var secondFork = (id+1)%forks.size
        if (id == 0){
            firstFork = (id+1)%forks.size
            secondFork = id%forks.size
        }

        while (remainingTime() > 0){
            if (!sems[firstFork].tryAcquire(remainingTime(), TimeUnit.MILLISECONDS)) return
            val fork1 = forks[firstFork]
            if (!sems[secondFork].tryAcquire(remainingTime(), TimeUnit.MILLISECONDS)){
                sems[firstFork].release()
                return
            }
            val fork2 = forks[secondFork]

            LCS.lcsLength(fork1, fork2)
            forks[id%sems.size] = LCS.randString(Constants.LCS_RANGE, Constants.LCS_LENGTH)
            forks[(id+1)%sems.size] = LCS.randString(Constants.LCS_RANGE, Constants.LCS_LENGTH)

            freq[id] += 1

            sems[secondFork].release()
            sems[firstFork].release()

        }
    }
}