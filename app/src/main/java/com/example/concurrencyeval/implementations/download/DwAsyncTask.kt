package com.example.concurrencyeval.implementations.download

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.activities.ImgDownloadActivity
import com.example.concurrencyeval.util.RunReport
import java.net.URL
import kotlin.system.measureTimeMillis

class DwAsyncTask(val activity: ImgDownloadActivity) : AsyncTask<Int, Void, Pair<Bitmap?, Long>>() {
    override fun doInBackground(vararg imgId: Int?): Pair<Bitmap?, Long> {
        var img : Bitmap? = null
        val time = measureTimeMillis {

            val imgSrc = Constants.imgURL[imgId[0]]
            val inp = URL(imgSrc).openStream()
            img = BitmapFactory.decodeStream(inp)
        }
        return Pair(img, time)
    }

    override fun onPostExecute(result: Pair<Bitmap?, Long>?) {

        activity.updateReport(RunReport(result?.second as Long, result.first))

    }
}