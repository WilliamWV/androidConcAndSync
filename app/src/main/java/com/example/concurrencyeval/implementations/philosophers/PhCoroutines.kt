package com.example.concurrencyeval.implementations.philosophers

import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.activities.PhilosophersActivity
import com.example.concurrencyeval.util.RunReport
import kotlinx.coroutines.*
import java.io.File
import java.util.concurrent.Executors

class PhCoroutines(
    philosophers: Int, time: Int, activity: PhilosophersActivity
): PhImplementation(philosophers, time, activity){
    override fun execute(): RunReport {
        runBlocking {
            val jobs: MutableList<Job> = mutableListOf()
            val forks: Array<Any> = Array(philosophers){Any()}
            val file = File(phDir, Constants.PHILOSOPHERS_FILE)
            val pool = Executors.newFixedThreadPool(philosophers).asCoroutineDispatcher()

            for (i in 0 until philosophers) {
                jobs += GlobalScope.launch(pool) {
                    think(i, forks, file)
                }
            }
            for (job in jobs) {
                job.join()
            }
        }


        return PhUtil.buildReport(philosophers, File(phDir, Constants.PHILOSOPHERS_FILE))
    }

    private fun think(id: Int, forks: Array<Any>, file: File){
        val beg = System.currentTimeMillis()
        var end = beg
        while ((end - beg) / 1000 <= time) {
            if (id == 0) {
                synchronized(forks[id]) {
                    synchronized(forks[(id+1) % forks.size]) {
                        file.appendText("$id\n")
                    }
                }
            } else {
                synchronized(forks[(id+1)%forks.size]) {
                    synchronized(forks[id]) {
                        file.appendText("$id\n")
                    }
                }
            }
            end = System.currentTimeMillis()
        }
    }

}