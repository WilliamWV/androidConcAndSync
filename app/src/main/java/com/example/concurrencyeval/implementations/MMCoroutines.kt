package com.example.concurrencyeval.implementations

import android.support.annotation.RestrictTo
import android.util.Log
import com.example.concurrencyeval.activities.MatMultActivity
import com.example.concurrencyeval.util.MMUtil
import com.example.concurrencyeval.util.RunReport
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext




class MMCoroutines(val size: Int, val tasks: Int, val activity: MatMultActivity): Thread() {

    val debug = false
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
        val jobs: MutableList<Job> = mutableListOf()

        val time = measureTimeMillis {
            runBlocking {
                for (t in 0 until tasks) {
                    jobs += GlobalScope.launch {
                        val lines = size / tasks
                        val firstLine = t * lines

                        for (i in firstLine until firstLine + lines) {
                            for (j in 0 until size) {
                                var sum: Long = 0
                                for (k in 0 until size) {
                                    sum += m1[i][k] * m2[k][j]
                                }
                                ans[i][j] = sum
                            }
                        }
                    }
                }
                for (job in jobs){
                    job.join()
                }
            }


        }

        if (debug){
            Log.d("MM_DEBUG", "ans")

            MMUtil.printMatrix(size, ans)
        }

        return RunReport(time)
    }
}