package com.example.concurrencyeval.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.os.Bundle
import android.os.HandlerThread
import android.os.Message
import android.support.v4.content.LocalBroadcastManager
import android.util.DisplayMetrics
import android.view.View
import android.view.View.VISIBLE
import android.widget.*
import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.R
import com.example.concurrencyeval.implementations.download.*
import com.example.concurrencyeval.util.RunReport
import java.io.Serializable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.roundToInt


class ImgDownloadActivity : AbstractActivity(Constants.DOWNLOAD_FILE), Serializable {

    private var threadPool: ExecutorService? = null
    private var handlerThread: HandlerThread? = null
    private var handler: DwHandler? = null

    private val messageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Extract data included in the Intent
            val time = intent.getLongExtra(Constants.TIME_EXTRA, -1/*default value*/)
            val img: Bitmap? = intent.getParcelableExtra(Constants.IMG_EXTRA)
            updateReport(RunReport(time, img))
        }
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            messageReceiver, IntentFilter(Constants.TIME_INTENT)
        )
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
            messageReceiver
        )
    }

    private fun populateSpinner() :Spinner{
        val spinner: Spinner = findViewById(R.id.id_spinner_choose_img)
        ArrayAdapter.createFromResource(
            this,
            R.array.images,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        return spinner
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.setImplementation(intent.getIntExtra(Constants.IMPL_EXTRA, -1))
        setContentView(R.layout.activity_file_download)

        super.onCreate(savedInstanceState)

        val imageSpinner = this.populateSpinner()

        super.mRunButton.setOnClickListener {
            super.mProgress.visibility = VISIBLE
            when (super.mImplementation){
                Constants.THREADS -> DwThread(imageSpinner.selectedItemPosition, this).start()
                Constants.THREAD_POOL->{
                    if (this.threadPool == null)
                        this.threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
                    this.threadPool!!.execute (DwTaskRunnable(imageSpinner.selectedItemPosition, this))
                }
                Constants.COROUTINES->DwCoroutines(imageSpinner.selectedItemPosition, this).execute()
                Constants.HAMER->{
                    if (handlerThread == null) {
                        handlerThread = HandlerThread("IMG_DOWN")
                        handlerThread!!.start()
                        handler = DwHandler(this, handlerThread!!.looper)
                    }
                    handler?.sendMessage(Message.obtain(handler, imageSpinner.selectedItemPosition))
                }
                Constants.ASYNC_TASK->DwAsyncTask(this).execute(imageSpinner.selectedItemPosition)
                Constants.INTENT_SERV->{
                    val serv = Intent(this, DwIntentServices::class.java)
                    serv.putExtra(Constants.IMG_EXTRA, imageSpinner.selectedItemPosition)
                    startService(serv)
                }
                else-> this.updateReport(RunReport( -1, null))
            }

        }


    }
    override fun updateReport(report: RunReport){
        val timeTV: TextView = findViewById(R.id.fd_time_report)
        val timeReport = "${report.time}ms"
        timeTV.text = timeReport
        val progress : ProgressBar = findViewById(R.id.fd_progressBar)
        progress.visibility = View.INVISIBLE
        val img =  report.ans as Bitmap?
        if(img != null) {
            val imgView: ImageView = findViewById(R.id.id_iv_image_report)
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val width = displayMetrics.widthPixels
            val scale = (width.toDouble()) / img.width.toDouble()
            val margin = 32
            imgView.setImageBitmap(Bitmap.createScaledBitmap(img, width - margin, (img.height * scale).roundToInt(), false))
        }
    }


}
