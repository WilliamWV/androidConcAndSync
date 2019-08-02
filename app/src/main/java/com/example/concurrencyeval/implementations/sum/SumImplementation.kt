package com.example.concurrencyeval.implementations.sum

import com.example.concurrencyeval.activities.ConcSumActivity
import com.example.concurrencyeval.util.RunReport

abstract class SumImplementation (
    val numbers: Int, val tasks: Int,
    val activity: ConcSumActivity
): Thread(){
    override fun run() {
        val report = this.execute()
        activity.runOnUiThread {
            activity.updateReport(report)
        }
    }
    abstract fun execute(): RunReport
}