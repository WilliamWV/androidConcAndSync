package com.example.concurrencyeval.implementations.download

import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.activities.FileDownloadActivity
import com.example.concurrencyeval.util.RunReport
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

class DwThreadPoll(val activity: FileDownloadActivity): Thread() {

    private fun String.saveTo(file: File) {
        URL(this).openStream().use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
    }

    override fun run(){

        val time = measureTimeMillis {
            val threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
            threadPool.execute {
                val dir: File = activity.baseContext.cacheDir
                val file: File = File.createTempFile("temp_book", "pdf", dir)
                Constants.DOWNLOAD_FILE_87_LINK.saveTo(file)
                file.delete()
            }
            threadPool.shutdown()
            threadPool.awaitTermination(1, TimeUnit.HOURS)
        }

        activity.runOnUiThread {
            activity.updateReport(RunReport(time))
        }

    }

}