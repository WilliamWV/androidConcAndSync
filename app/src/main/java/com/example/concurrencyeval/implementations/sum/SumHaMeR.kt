package com.example.concurrencyeval.implementations.sum

import android.os.HandlerThread
import android.os.Handler
import com.example.concurrencyeval.activities.ConcSumActivity
import com.example.concurrencyeval.util.RunReport
import kotlin.math.*
import kotlin.system.measureTimeMillis

class SumHaMeR(
    numbers: Int, tasks: Int, activity: ConcSumActivity
): SumImplementation(numbers, tasks, activity) {

    private val cores = Runtime.getRuntime().availableProcessors()

    override fun execute(): RunReport {
        val time = measureTimeMillis {
            //Prepare the handlerThreads
            val ans = LongArray(numbers)
            val levels = ceil(log(numbers.toDouble(), 2.0)).roundToInt() // number of levels
            for (level in 0 until levels) {

                val handlerThreads: MutableList<HandlerThread> = mutableListOf()
                val handlers: MutableList<Handler> = mutableListOf()
                for (i in 0 until cores){
                    val handlerThread = HandlerThread(i.toString())
                    handlerThread.start()
                    handlerThreads += handlerThread
                    val handler = Handler(handlerThread.looper)
                    handlers+= handler
                }

                val tasksToRun = min(tasks, ceil(numbers / 2.0.pow((1 + level))).toInt())
                for (i in 0 until tasksToRun) {
                    handlers[i%cores].post(SumWorkerRunnable(mArr, ans, numbers, level, tasks, i))
                }
                for (i in 0 until cores) {
                    handlerThreads[i].quitSafely()
                    handlerThreads[i].join()
                }
                for (i in 0 until numbers) {
                    mArr[i] = ans[i]
                }
            }


        }
        return RunReport(time)

    }
}