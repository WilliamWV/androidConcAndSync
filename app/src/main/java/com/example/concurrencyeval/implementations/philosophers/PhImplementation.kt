package com.example.concurrencyeval.implementations.philosophers

import android.util.Log
import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.activities.PhilosophersActivity
import com.example.concurrencyeval.util.RunReport
import java.io.File


abstract class PhImplementation (
    val philosophers: Int, val time: Int,
    val activity: PhilosophersActivity
) : Thread(){


    override fun run() {
        val report = this.execute()
        activity.runOnUiThread {
            activity.updateReport(report)
        }
    }

    abstract fun execute(): RunReport
}