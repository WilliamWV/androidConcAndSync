package com.example.concurrencyeval.implementations

import android.os.Handler
import android.os.HandlerThread
import com.example.concurrencyeval.activities.MatMultActivity
import com.example.concurrencyeval.util.MMUtil
import com.example.concurrencyeval.util.RunReport
import kotlin.system.measureTimeMillis

class MMHaMeR(
    private val size: Int, private val tasks: Int,
    private val activity: MatMultActivity
): Thread() {

    override fun run() {
        val report: RunReport = this.execute()
        activity.runOnUiThread {
            activity.updateReport(report)
        }
    }

    private fun execute(): RunReport{
        val m1 = MMUtil.randMatrix(size)
        val m2 = MMUtil.randMatrix(size)
        val ans = Array(size){LongArray(size)}


        val time = measureTimeMillis {
            //OBS: verify the number of handler threads that are created
            val handlerThreads = mutableListOf<HaMeRThread>()
            val handlers = mutableListOf<Handler>()
            val cores = Runtime.getRuntime().availableProcessors()
            for (i in 0 until cores){
                val handlerThread = HaMeRThread(i.toString())
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

        return RunReport(time)
    }

    class HaMeRThread(name: String) : HandlerThread(name)
}