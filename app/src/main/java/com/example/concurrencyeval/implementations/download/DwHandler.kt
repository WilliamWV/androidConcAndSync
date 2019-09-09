package com.example.concurrencyeval.implementations.download

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.activities.ImgDownloadActivity
import com.example.concurrencyeval.util.RunReport
import java.net.URL
import kotlin.system.measureTimeMillis

class DwHandler(val activity: ImgDownloadActivity, looper: Looper): Handler(looper){
    override fun handleMessage(msg: Message?) {
        if (msg!= null) {
            var img: Bitmap? = null
            val time = measureTimeMillis {
                val imgSrc = Constants.imgURL[msg.what]
                val inp = URL(imgSrc).openStream()
                img = BitmapFactory.decodeStream(inp)

            }
            activity.runOnUiThread {
                activity.updateReport(RunReport(time, img))
            }
        }
    }
}