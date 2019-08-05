package com.example.concurrencyeval.implementations.download

import android.app.IntentService
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import com.example.concurrencyeval.Constants
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import kotlin.system.measureTimeMillis

class DwIntentServices : IntentService("intent") {


    fun String.saveTo(file: File) {
        URL(this).openStream().use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
    }


    override fun onHandleIntent(intent: Intent) {

        val time = measureTimeMillis {
            val dir: File = applicationContext.cacheDir
            val file: File = File.createTempFile("temp_book", "pdf", dir)
            Constants.DOWNLOAD_FILE_87_LINK.saveTo(file)
            file.delete()
        }

        val intent = Intent(Constants.TIME_INTENT)
        intent.putExtra(Constants.TIME_EXTRA, time)

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)

    }

}