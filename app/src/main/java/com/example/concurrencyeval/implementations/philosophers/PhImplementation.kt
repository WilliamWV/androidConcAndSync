package com.example.concurrencyeval.implementations.philosophers

import android.content.Context
import android.util.Log
import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.activities.PhilosophersActivity
import com.example.concurrencyeval.util.RunReport
import java.io.File
import java.lang.Exception


abstract class PhImplementation (
    val philosophers: Int, val time: Int,
    val activity: PhilosophersActivity
) : Thread(){

    val phDir = File(activity.applicationContext.filesDir, "PH")

    override fun run() {
        this.cleanFile()
        val report = this.execute()
        activity.runOnUiThread {
            activity.updateReport(report)
        }
    }
    private fun cleanFile(){
        try {
            File(phDir, Constants.PHILOSOPHERS_FILE).writeText("")
        } catch (ex : Exception){
            Log.d("ERROR", ex.toString())
            phDir.mkdirs()
            File(phDir, Constants.PHILOSOPHERS_FILE).createNewFile()
        }
    }

    abstract fun execute(): RunReport
}