package com.example.concurrencyeval.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.R
import com.example.concurrencyeval.implementations.philosophers.PhThread
import com.example.concurrencyeval.implementations.philosophers.PhThreadPool
import com.example.concurrencyeval.util.RunReport

class PhilosophersActivity : AppCompatActivity() {

    private var selectedImplementation: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_philosophers)
        selectedImplementation = intent.getIntExtra(Constants.IMPL_EXTRA, -1)
        val runButton: Button = findViewById(R.id.ph_run_button)
        runButton.setOnClickListener {
            val progress : ProgressBar = findViewById(R.id.ph_progressBar)
            progress.visibility = View.VISIBLE
            val philosophers: Int = findViewById<EditText>(R.id.ph_et_philosophers).text.toString().toInt()
            val time: Int = findViewById<EditText>(R.id.ph_et_time).text.toString().toInt()
            when(selectedImplementation){
                Constants.THREADS -> PhThread(philosophers, time, this).start()
                Constants.THREAD_POOL -> PhThreadPool(philosophers, time, this).start()
                else -> this.updateReport(RunReport(-1))
            }
        }

    }
    fun updateReport(report: RunReport){
        val avgTV: TextView = findViewById(R.id.ph_average_report)
        val stdTV: TextView = findViewById(R.id.ph_deviation_report)
        val avgReport = "${report.avg}"
        val stdReport = "${report.std}"
        avgTV.text = avgReport
        stdTV.text = stdReport
        val progress : ProgressBar = findViewById(R.id.ph_progressBar)
        progress.visibility = View.INVISIBLE


    }
}
