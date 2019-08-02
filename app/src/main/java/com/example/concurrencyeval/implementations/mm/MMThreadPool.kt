package com.example.concurrencyeval.implementations.mm

import com.example.concurrencyeval.activities.MatMultActivity
import com.example.concurrencyeval.util.MMUtil
import com.example.concurrencyeval.util.RunReport
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

class MMThreadPool(
    size: Int, tasks: Int, activity: MatMultActivity
) : MMImplementation(size, tasks, activity){

    private val numOfCores = Runtime.getRuntime().availableProcessors()
    private val threadPool : ThreadPoolExecutor = Executors.newFixedThreadPool(numOfCores) as ThreadPoolExecutor

    override fun execute(): RunReport {
        val m1 = MMUtil.randMatrix(size)
        val m2 = MMUtil.randMatrix(size)
        val ans = Array(size){LongArray(size)}
        //OBS: verify if creating more tasks than threads is not better to evaluate

        val time = measureTimeMillis {
            for (i in 0 until tasks) {
                threadPool.execute(
                    MMWorkerRunnable(m1, m2, ans, size, tasks, i)
                )
            }
            //run all tasks that are not ready yet
            threadPool.shutdown()
            threadPool.awaitTermination(1, TimeUnit.HOURS)
        }
        return RunReport(time)
    }
}