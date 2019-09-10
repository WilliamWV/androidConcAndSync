package com.example.concurrencyeval.implementations.mm

import com.example.concurrencyeval.activities.MatMultActivity
import com.example.concurrencyeval.util.RunReport
import kotlin.system.measureTimeMillis

class MMThread (
    size: Int, tasks: Int, activity: MatMultActivity
): MMImplementation(size, tasks, activity) {

    override fun execute():RunReport{

        val workers :Array<Thread> = Array(tasks){ Thread(
            MMWorkerRunnable(m1, m2, mAns, size, tasks, 0)
        )}
        //OBS: verify here if it is better to test running the tasks soon after its
        //initialization of only when all threads were initialized

        val time = measureTimeMillis {
            for (i in 0 until tasks) {
                workers[i] = Thread(
                    MMWorkerRunnable(m1, m2, mAns, size, tasks, i)
                )
                workers[i].start()
            }
            for (i in 0 until tasks) {
                workers[i].join()
            }
        }

        return RunReport(time)
    }

}
