package com.example.concurrencyeval.implementations.download

import android.os.AsyncTask
import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.activities.ImgDownloadActivity
import com.example.concurrencyeval.util.RunReport
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import kotlin.system.measureTimeMillis

class DwAsyncTask: AsyncTask<ImgDownloadActivity, Void, Void>() {


    private fun String.saveTo(file: File) {
        URL(this).openStream().use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
    }
    override fun doInBackground(vararg activities: ImgDownloadActivity?): Void? {
        val time = measureTimeMillis {
            val dir: File = activities[0]!!.baseContext.cacheDir
            val file: File = File.createTempFile("temp_book", "pdf", dir)
            Constants.DOWNLOAD_FILE_87_LINK.saveTo(file)
            file.delete()
        }

        activities[0]?.runOnUiThread{
            activities[0]?.updateReport(RunReport(time))
        }
        return null
    }

}