package com.example.concurrencyeval.implementations.download

import android.os.Handler
import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.activities.ImgDownloadActivity
import com.example.concurrencyeval.implementations.mm.MMHaMeR
import com.example.concurrencyeval.util.RunReport
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import kotlin.system.measureTimeMillis

class DwHaMeR(val activity: ImgDownloadActivity): Thread(){
    private fun String.saveTo(file: File) {
        URL(this).openStream().use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
    }

    override fun run() {

        val time = measureTimeMillis {
            val handlerThreads = mutableListOf<MMHaMeR.HaMeRThread>()
            val handlers = mutableListOf<Handler>()
            val cores = Runtime.getRuntime().availableProcessors()
            for (i in 0 until cores) {
                val handlerThread = MMHaMeR.HaMeRThread(i.toString())
                handlerThread.start()
                handlerThreads += handlerThread
                val handler = Handler(handlerThread.looper)
                handlers += handler
            }
            handlers[0].post {

                val dir: File = activity.baseContext.cacheDir
                val file: File = File.createTempFile("temp_book", "pdf", dir)
                Constants.DOWNLOAD_FILE_87_LINK.saveTo(file)
                file.delete()


            }
            for (i in 0 until cores) {

                handlerThreads[i].quitSafely()
                handlerThreads[i].join()
            }
        }
        activity.runOnUiThread{
            activity.updateReport(null, RunReport(time))
        }

    }
}