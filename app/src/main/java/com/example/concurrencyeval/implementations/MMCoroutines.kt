package com.example.concurrencyeval.implementations

import com.example.concurrencyeval.activities.MatMultActivity
import com.example.concurrencyeval.util.MMUtil
import com.example.concurrencyeval.util.RunReport
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis


class MMCoroutines(
    private val size: Int, private val tasks: Int,
    private val activity: MatMultActivity
): Thread() {

    override fun run() {

        val report = this.execute()
        activity.runOnUiThread {
            activity.updateReport(report)
        }
    }
    private fun execute(): RunReport {

        val m1 = MMUtil.randMatrix(size)
        val m2 = MMUtil.randMatrix(size)
        val ans = Array(size){LongArray(size)}
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

        return RunReport(time)
    }
}