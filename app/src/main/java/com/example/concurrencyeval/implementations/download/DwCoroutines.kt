package com.example.concurrencyeval.implementations.download

import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.activities.ImgDownloadActivity
import com.example.concurrencyeval.util.RunReport
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import kotlin.system.measureTimeMillis

class DwCoroutines(val activity: ImgDownloadActivity) {

    private fun String.saveTo(file: File) {
        URL(this).openStream().use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
    }

    fun execute(){
        GlobalScope.launch {
            val time = measureTimeMillis {
                val dir: File = activity.baseContext.cacheDir
                val file: File = File.createTempFile("temp_book", "pdf", dir)
                Constants.DOWNLOAD_FILE_87_LINK.saveTo(file)
                file.delete()
            }

            activity.runOnUiThread {
                activity.updateReport(RunReport(time))
            }
        }
    }
}