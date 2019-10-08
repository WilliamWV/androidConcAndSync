package com.example.concurrencyeval.implementations.philosophers

import com.example.concurrencyeval.Constants
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock


class PhWorkerRunnableLock(
    val forks: Array<String>, val locks: Array<ReentrantLock>, val time: Int, val freq: IntArray, val id: Int
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
            if (!locks[firstFork].tryLock(remainingTime(), TimeUnit.MILLISECONDS)) return
            val fork1 = forks[firstFork]
            if (!locks[secondFork].tryLock(remainingTime(), TimeUnit.MILLISECONDS)){
                locks[firstFork].unlock()
                return
            }
            val fork2 = forks[secondFork]

            LCS.lcsLength(fork1, fork2)
            forks[id%locks.size] = LCS.randString(Constants.LCS_RANGE, Constants.LCS_LENGTH)
            forks[(id+1)%locks.size] = LCS.randString(Constants.LCS_RANGE, Constants.LCS_LENGTH)

            freq[id] += 1

            locks[secondFork].unlock()
            locks[firstFork].unlock()

        }
    }
}