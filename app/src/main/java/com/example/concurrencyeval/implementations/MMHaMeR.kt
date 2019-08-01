package com.example.concurrencyeval.implementations

import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import com.example.concurrencyeval.activities.MatMultActivity
import com.example.concurrencyeval.util.MMUtil
import com.example.concurrencyeval.util.RunReport
import kotlin.system.measureTimeMillis

class MMHaMeR(val size: Int, val tasks: Int, val activity: MatMultActivity): Thread() {

    private val debug = false
    override fun run() {
        val report: RunReport = this.execute()
        activity.runOnUiThread {
            activity.updateReport(report)
        }
    }

    fun execute(): RunReport{
        val m1 = MMUtil.randMatrix(size)
        val m2 = MMUtil.randMatrix(size)
        val ans = Array(size){LongArray(size)}
        //OBS: verify if creating more tasks than threads is not better to evaluate
        if (debug){
            Log.d("MM_DEBUG", "m1")
            MMUtil.printMatrix(size, m1)
            Log.d("MM_DEBUG", "m2")
            MMUtil.printMatrix(size, m2)

        }


        val time = measureTimeMillis {
            //OBS: esse aqui vai depender de quantas handler threads devem ser inicializadas no começo da
            //execução, atualmente se usa uma handlerthread para cada core do processador
            val handlerThreads = mutableListOf<HamerThread>()
            val handlers = mutableListOf<Handler>()
            val cores = Runtime.getRuntime().availableProcessors()
            for (i in 0 until cores){
                val handlerThread = HamerThread(i.toString())
                handlerThread.start()
                handlerThreads += handlerThread
                val handler = Handler(handlerThread.looper)
                handlers+= handler
            }
            for (i in 0 until tasks) {
                handlers[i%cores].post(MMWorkerRunnable(m1, m2, ans, size, tasks, i))
            }
            for (i in 0 until cores){

                handlerThreads[i].quitSafely()
                handlerThreads[i].join()
            }
        }

        if (debug){
            Log.d("MM_DEBUG", "ans")

            MMUtil.printMatrix(size, ans)
        }

        return RunReport(time)
    }

    class HamerThread(name: String) : HandlerThread(name)
}