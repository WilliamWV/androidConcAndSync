package com.example.concurrencyeval.implementations.download

import android.app.IntentService
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.content.LocalBroadcastManager
import com.example.concurrencyeval.Constants
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import kotlin.system.measureTimeMillis

class DwIntentServices : IntentService("intent") {


    private fun String.saveTo(file: File) {
        URL(this).openStream().use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
    }


    override fun onHandleIntent(intent: Intent) {

        var img : Bitmap? = null
        val time = measureTimeMillis {

            val imageId = intent.getIntExtra(Constants.IMG_EXTRA, -1)
            if (imageId != -1) {
                val imgSrc = Constants.imgURL[imageId]
                val inp = URL(imgSrc).openStream()
                img = BitmapFactory.decodeStream(inp)
            }

        }

        val reportIntent = Intent(Constants.TIME_INTENT)
        reportIntent.putExtra(Constants.TIME_EXTRA, time)
        reportIntent.putExtra(Constants.IMG_EXTRA, img)

        LocalBroadcastManager.getInstance(this).sendBroadcast(reportIntent)

    }

}