package com.example.concurrencyeval.implementations.mm

import com.example.concurrencyeval.activities.MatMultActivity
import com.example.concurrencyeval.util.RunReport
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.min
import kotlin.system.measureTimeMillis


class MMCoroutines(
    size: Int, tasks: Int, activity: MatMultActivity
): MMImplementation(size, tasks, activity) {

    override fun execute(): RunReport {

        val jobs: MutableList<Job> = mutableListOf()

        val time = measureTimeMillis {
            runBlocking {
                for (t in 0 until tasks) {
                    jobs += GlobalScope.launch {
                        val extraLine = if (size % tasks > t) 1 else 0
                        val lines = size / tasks + extraLine
                        val firstLineOffset = min(t, size%tasks)
                        val firstLine = t * (size/tasks) + firstLineOffset

                        for (i in firstLine until firstLine + lines) {
                            for (j in 0 until size) {
                                var sum: Long = 0
                                for (k in 0 until size) {
                                    sum += m1[i][k] * m2[k][j]
                                }
                                mAns[i][j] = sum
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