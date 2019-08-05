package com.example.concurrencyeval.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.R
import com.example.concurrencyeval.implementations.download.DwThread
import com.example.concurrencyeval.implementations.download.DwThreadPoll
import com.example.concurrencyeval.util.RunReport

class FileDownloadActivity : AppCompatActivity() {

    private var selectedImplementation: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_download)
        selectedImplementation = intent.getIntExtra(Constants.IMPL_EXTRA, -1)
        val runButton: Button = findViewById(R.id.fd_run_button)
        runButton.setOnClickListener {
            val progress : ProgressBar = findViewById(R.id.fd_progressBar)
            progress.visibility = VISIBLE
            when (selectedImplementation){
                Constants.THREADS -> DwThread(this).start()
                Constants.THREAD_POOL->DwThreadPoll(this).start()
                else-> this.updateReport(RunReport(-1))
            }

        }


    }
    fun updateReport(report: RunReport){
        val timeTV: TextView = findViewById(R.id.fd_time_report)
        val timeReport = "${report.time}ms"
        timeTV.text = timeReport
        val progress : ProgressBar = findViewById(R.id.fd_progressBar)
        progress.visibility = View.INVISIBLE
    }
}
