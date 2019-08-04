package com.example.concurrencyeval.implementations.philosophers

import android.util.Log
import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.util.RunReport
import java.io.File
import kotlin.math.sqrt


object PhUtil {

    val debug = false

    fun buildReport(philosophers: Int, file: File) : RunReport {
        val timesEaten = IntArray(philosophers){0}
        file.forEachLine {
            timesEaten[it.toInt()] += 1
        }
        if (debug)
            this.printAll(timesEaten)

        return RunReport(timesEaten)
    }

    fun printAll(arr: IntArray){
        Log.d("PH_DEBUG", "Number of times each philosopher have eaten:")
        arr.forEach {
           Log.d("PH_DEBUG", it.toString())
        }
    }
}