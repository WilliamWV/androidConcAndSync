package com.example.concurrencyeval.implementations.philosophers

import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.activities.PhilosophersActivity
import com.example.concurrencyeval.util.RunReport
import kotlinx.coroutines.*
import java.util.concurrent.Executors
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit

class PhCoroutines(
    philosophers: Int, time: Int, sync: Int, activity: PhilosophersActivity
): PhImplementation(philosophers, time, sync, activity){
    val frequencies = IntArray(philosophers) {0}

    override fun execute(): RunReport {
        runBlocking {
            val jobs: MutableList<Job> = mutableListOf()
            val forks: Array<String> = Array(philosophers){LCS.randString(Constants.LCS_RANGE, Constants.LCS_LENGTH)}
            val pool = Executors.newFixedThreadPool(philosophers).asCoroutineDispatcher()

            when (sync) {
                Constants.SYNCHRONIZED -> for (i in 0 until philosophers) {
                    jobs += GlobalScope.launch(pool) {
                        thinkSynchronized(i, forks, frequencies)
                    }
                }
                Constants.SEMAPHORE -> for (i in 0 until philosophers){
                    jobs += GlobalScope.launch (pool) {
                        val sems = Array(forks.size){Semaphore(1, true)}
                        thinkSemaphore(i, forks, sems, frequencies)
                    }
                }
            }
            for (job in jobs) {
                job.join()
            }
        }


        return RunReport(frequencies)
    }

    private fun thinkSynchronized(id: Int, forks: Array<String>, freq: IntArray){
        val beg = System.currentTimeMillis()
        var end = beg
        while ((end - beg) / 1000 <= time) {
            if (id == 0) {
                synchronized(forks[id]) {
                    synchronized(forks[(id+1) % forks.size]) {
                        LCS.lcsLength(forks[id], forks[id+1])
                        forks[id] = LCS.randString(Constants.LCS_RANGE, Constants.LCS_LENGTH)
                        forks[(id+1)%forks.size] = LCS.randString(Constants.LCS_RANGE, Constants.LCS_LENGTH)

                        freq[id]+=1
                    }
                }
            } else {
                synchronized(forks[(id+1)%forks.size]) {
                    synchronized(forks[id]) {
                        forks[id] = LCS.randString(Constants.LCS_RANGE, Constants.LCS_LENGTH)
                        forks[(id+1)%forks.size] = LCS.randString(Constants.LCS_RANGE, Constants.LCS_LENGTH)

                        freq[id]+=1
                    }
                }
            }
            end = System.currentTimeMillis()
        }
    }

    private fun thinkSemaphore(id: Int, forks: Array<String>, sems: Array<Semaphore>, freq: IntArray){

        var firstFork = id%forks.size
        var secondFork = (id+1)%forks.size
        if (id == 0){
            firstFork = (id+1)%forks.size
            secondFork = id%forks.size
        }

        val beg = System.currentTimeMillis()
        var end = beg

        while ((end - beg) / 1000 <= time){
            if (!sems[firstFork].tryAcquire(1000 * time - (end - beg), TimeUnit.MILLISECONDS)) return
            val fork1 = forks[firstFork]
            if (!sems[secondFork].tryAcquire(1000 * time - (end - beg), TimeUnit.MILLISECONDS)){
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
            end = System.currentTimeMillis()
        }

    }

}