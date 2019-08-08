package com.example.concurrencyeval.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.View
import android.view.View.VISIBLE
import android.widget.*
import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.R
import com.example.concurrencyeval.implementations.download.*
import com.example.concurrencyeval.util.RunReport
import java.io.Serializable
import kotlin.math.roundToInt


class ImgDownloadActivity : AppCompatActivity(), Serializable {

    private var selectedImplementation: Int = 0

    private val messageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Extract data included in the Intent
            val time = intent.getLongExtra(Constants.TIME_EXTRA, -1/*default value*/)
            updateReport(null, RunReport(time))
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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_download)
        val imageSpinner = this.populateSpinner()

        selectedImplementation = intent.getIntExtra(Constants.IMPL_EXTRA, -1)
        val description: TextView = findViewById(R.id.fd_tv_description)
        val newDescription = description.text.toString() + ". Implemented with ${Constants.implNames[selectedImplementation]}"
        description.text = newDescription
        val runButton: Button = findViewById(R.id.fd_run_button)
        runButton.setOnClickListener {
            val progress : ProgressBar = findViewById(R.id.fd_progressBar)
            progress.visibility = VISIBLE
            when (selectedImplementation){
                Constants.THREADS -> DwThread(imageSpinner.selectedItemPosition, this).start()
                Constants.THREAD_POOL->DwThreadPoll(this).start()
                Constants.COROUTINES->DwCoroutines(this).execute()
                Constants.HAMER->DwHaMeR(this).start()
                Constants.ASYNC_TASK->DwAsyncTask().execute(this)
                Constants.INTENT_SERV->{
                    val serv = Intent(this, DwIntentServices::class.java)
                    startService(serv)
                }
                else-> this.updateReport(null, RunReport( -1))
            }

        }


    }
    fun updateReport(img: Bitmap?, report: RunReport){
        val timeTV: TextView = findViewById(R.id.fd_time_report)
        val timeReport = "${report.time}ms"
        timeTV.text = timeReport
        val progress : ProgressBar = findViewById(R.id.fd_progressBar)
        progress.visibility = View.INVISIBLE
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
