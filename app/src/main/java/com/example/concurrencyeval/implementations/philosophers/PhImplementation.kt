package com.example.concurrencyeval.implementations.philosophers

import com.example.concurrencyeval.activities.PhilosophersActivity
import com.example.concurrencyeval.util.RunReport


abstract class PhImplementation (
    val philosophers: Int, val time: Int,
    val sync: Int, val activity: PhilosophersActivity
) : Thread(){


    override fun run() {
        val report = this.execute()
        activity.runOnUiThread {
            activity.updateReport(report)
        }
    }

    abstract fun execute(): RunReport
}