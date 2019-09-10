package com.example.concurrencyeval.implementations.download

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.activities.ImgDownloadActivity
import com.example.concurrencyeval.util.RunReport
import java.net.URL
import kotlin.system.measureTimeMillis

class DwThread(private val imageId: Int, val activity: ImgDownloadActivity) : Thread() {


    override fun run() {

        var img : Bitmap? = null
        val time = measureTimeMillis {

            val imgSrc = Constants.imgURL[this.imageId]
            val inp = URL(imgSrc).openStream()
            img = BitmapFactory.decodeStream(inp)

        }

        activity.runOnUiThread{
            activity.updateReport(RunReport(time, img))
        }

    }
}