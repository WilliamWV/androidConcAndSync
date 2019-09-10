package com.example.concurrencyeval.implementations.download

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.activities.ImgDownloadActivity
import com.example.concurrencyeval.util.RunReport
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL
import kotlin.system.measureTimeMillis

class DwCoroutines(private val imageId: Int, val activity: ImgDownloadActivity) {

    fun execute(){
        GlobalScope.launch {
            var img : Bitmap? = null
            val time = measureTimeMillis {

                val imgSrc = Constants.imgURL[imageId]
                val inp = URL(imgSrc).openStream()
                img = BitmapFactory.decodeStream(inp)

            }

            activity.runOnUiThread {
                activity.updateReport(RunReport(time, img))
            }
        }
    }
}