package com.example.concurrencyeval.implementations.philosophers

import android.util.Log
import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.util.RunReport
import java.io.File
import kotlin.math.sqrt


object PhUtil {

    val debug = true

    fun buildReport(philosophers: Int) : RunReport {
        val timesEaten = IntArray(philosophers){0}
        File(Constants.PHILOSOPHERS_FILE).forEachLine {
            timesEaten[it.toInt() - 1] += 1
        }
        if (debug)
            this.printAll(timesEaten)

        return RunReport(timesEaten)

    }

    fun printAll(arr: IntArray){
        Log.d("PH_DEBUG", "Number of times each philosopher have eaten:")

        arr.forEach {
           println(
               Log.d("PD_DEBUG", it.toString())
           )
        }
    }
}