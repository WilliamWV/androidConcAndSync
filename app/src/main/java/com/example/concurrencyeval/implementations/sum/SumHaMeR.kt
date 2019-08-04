package com.example.concurrencyeval.implementations.sum

import android.os.HandlerThread
import android.os.Handler
import android.util.Log
import com.example.concurrencyeval.activities.ConcSumActivity
import com.example.concurrencyeval.util.RunReport
import kotlin.math.log
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.system.measureTimeMillis

class SumHaMeR(
    numbers: Int, tasks: Int, activity: ConcSumActivity
): SumImplementation(numbers, tasks, activity) {

    private val debug = false
    private val cores = Runtime.getRuntime().availableProcessors()

    override fun execute(): RunReport {
        val arr = SumUtil.randArray(numbers)
        if (debug){
            Log.d("SUM_DEBUG", "arr")
            SumUtil.printArr(arr)
        }
        val time = measureTimeMillis {
            //Prepare the handlerThreads
            val ans = LongArray(numbers)
            val logN = (log(numbers.toDouble(), 2.0)).roundToInt() // number of levels
            for (level in 0 until logN) {

                val handlerThreads: MutableList<HandlerThread> = mutableListOf()
                val handlers: MutableList<Handler> = mutableListOf()
                for (i in 0 until cores){
                    val handlerThread = HandlerThread(i.toString())
                    handlerThread.start()
                    handlerThreads += handlerThread
                    val handler = Handler(handlerThread.looper)
                    handlers+= handler
                }

                val threadsToRun = min(tasks, numbers / 2.0.pow((1 + level).toDouble()).toInt())
                for (i in 0 until threadsToRun) {
                    handlers[i%cores].post(SumWorkerRunnable(arr, ans, numbers, level, tasks, i))
                }
                for (i in 0 until cores) {
                    handlerThreads[i].quitSafely()
                    handlerThreads[i].join()
                }
                for (i in 0 until numbers) {
                    arr[i] = ans[i]
                }
            }


        }
        if (debug){
            Log.d("SUM_DEBUG", "ans")
            Log.d("SUM_DEBUG", arr[0].toString())
        }

        return RunReport(time)

    }
}