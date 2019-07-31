package com.example.concurrencyeval.implementations

import android.util.Log
import com.example.concurrencyeval.activities.MatMultActivity
import com.example.concurrencyeval.util.MMUtil
import com.example.concurrencyeval.util.RunReport
import java.util.concurrent.*
import kotlin.system.measureTimeMillis

class MMThreadPool(val size: Int, val tasks: Int, val activity: MatMultActivity) : Thread(){

    private val debug = false

    private val numOfCores = Runtime.getRuntime().availableProcessors()
    val threadPool : ThreadPoolExecutor = Executors.newFixedThreadPool(numOfCores) as ThreadPoolExecutor

    override fun run() {
        val report = this.execute()
        activity.runOnUiThread {
            activity.updateReport(report)
        }
    }

    fun execute(): RunReport {
        val m1 = MMUtil.randMatrix(size)
        val m2 = MMUtil.randMatrix(size)
        val ans = Array(size){LongArray(size)}
        //OBS: verify if creating more tasks than threads is not better to evaluate
        if (debug){
            Log.d("MM_DEBUG", "m1")
            MMUtil.printMatrix(size, m1)
            Log.d("MM_DEBUG", "m2")
            MMUtil.printMatrix(size, m2)

        }

        val time = measureTimeMillis {
            for (i in 0 until tasks) {
                threadPool.execute(MMWorkerRunnable(m1, m2, ans, size, tasks, i))
            }
            //run all tasks that are not ready yet
            threadPool.shutdown()
            threadPool.awaitTermination(1, TimeUnit.HOURS)
        }

        if (debug){
            Log.d("MM_DEBUG", "ans")

            MMUtil.printMatrix(size, ans)
        }

        return RunReport(time)
    }
}