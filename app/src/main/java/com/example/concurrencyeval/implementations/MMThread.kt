package com.example.concurrencyeval.implementations

import android.util.Log
import com.example.concurrencyeval.activities.MatMultActivity
import com.example.concurrencyeval.util.MMUtil
import com.example.concurrencyeval.util.RunReport
import kotlin.system.measureTimeMillis

class MMThread (val size: Int, val tasks: Int, val activity: MatMultActivity):Thread(){
    private val debug: Boolean = false

    override fun run() {
        val report = this.execute()
        activity.runOnUiThread {
            activity.updateReport(report)
        }
    }

    fun execute():RunReport{
        val m1 = MMUtil.randMatrix(size)
        val m2 = MMUtil.randMatrix(size)
        val ans = Array(size){LongArray(size)}

        if (debug){
            Log.d("MM_DEBUG", "m1")
            MMUtil.printMatrix(size, m1)
            Log.d("MM_DEBUG", "m2")
            MMUtil.printMatrix(size, m2)

        }
        val workers :Array<MMWorkerThread> = Array(tasks){ MMWorkerThread(m1, m2, ans, size, tasks, 0) }
        //OBS: verify here if it is better to test running the tasks soon after its
        //initialization of only when all threads were initialized

        val time = measureTimeMillis {
            for (i in 0 until tasks) {
                workers[i] = MMWorkerThread(m1, m2, ans, size, tasks, i)
                workers[i].start()
            }

            for (i in 0 until tasks) {
                workers[i].join()
            }
        }

        if (debug){
            Log.d("MM_DEBUG", "ans")

            MMUtil.printMatrix(size, ans)
        }

        return RunReport(time)
    }

    class MMWorkerThread(
        val m1: Array<LongArray>, val m2: Array<LongArray>,
        val ans: Array<LongArray>, val size: Int, val tasks: Int,
        val id: Int
    ) : Thread() {

        override fun run() {
            val lines = size / tasks
            val firstLine = id * lines

            for (i in firstLine until firstLine+lines){
                for (j in 0 until size){
                    var sum: Long = 0
                    for (k in 0 until size){
                        sum += m1[i][k] * m2[k][j]
                    }
                    ans[i][j] = sum
                }
            }
        }
    }

}


