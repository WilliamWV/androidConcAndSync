package com.example.concurrencyeval.implementations.mm

import com.example.concurrencyeval.activities.MatMultActivity
import com.example.concurrencyeval.util.RunReport

abstract class MMImplementation (
    val size: Int, val tasks: Int,
    val activity: MatMultActivity
):Thread(){
    override fun run() {
        val report = this.execute()
        activity.runOnUiThread {
            activity.updateReport(report)
        }
    }
    abstract fun execute(): RunReport
}