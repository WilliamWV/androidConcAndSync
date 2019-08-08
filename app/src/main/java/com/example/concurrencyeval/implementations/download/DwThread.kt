package com.example.concurrencyeval.implementations.download

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.activities.ImgDownloadActivity
import com.example.concurrencyeval.util.RunReport
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import kotlin.system.measureTimeMillis

class DwThread(val imageId: Int, val activity: ImgDownloadActivity) : Thread() {


    override fun run() {

        var img : Bitmap? = null
        val time = measureTimeMillis {

            val imgSrc = Constants.imgURL[this.imageId]
            val inp = URL(imgSrc).openStream()
            img = BitmapFactory.decodeStream(inp)

        }

        activity.runOnUiThread{
            activity.updateReport(img, RunReport(time))
        }

    }
}