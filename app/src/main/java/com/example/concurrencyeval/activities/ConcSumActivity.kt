package com.example.concurrencyeval.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.R
import com.example.concurrencyeval.implementations.sum.SumCoroutines
import com.example.concurrencyeval.implementations.sum.SumThread
import com.example.concurrencyeval.implementations.sum.SumThreadPool
import com.example.concurrencyeval.util.RunReport

class ConcSumActivity : AppCompatActivity() {

    private var selectedImplementation: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conc_sum)
        selectedImplementation = intent.getIntExtra(Constants.IMPL_EXTRA, -1)
        val runButton: Button = findViewById(R.id.cs_run_button)
        runButton.setOnClickListener {
            val progress : ProgressBar = findViewById(R.id.cs_progressBar)
            progress.visibility = VISIBLE
            val numbers: Int = findViewById<EditText>(R.id.cs_et_numbers).text.toString().toInt()
            val tasks: Int = findViewById<EditText>(R.id.cs_et_tasks).text.toString().toInt()
            when (selectedImplementation){
                Constants.THREADS->SumThread(numbers, tasks, this).start()
                Constants.THREAD_POOL->SumThreadPool(numbers, tasks, this).start()
                Constants.COROUTINES->SumCoroutines(numbers, tasks, this).start()
                else->this.updateReport(RunReport(-1))
            }
        }

    }

    fun updateReport(report: RunReport) {
        val timeTV: TextView = findViewById(R.id.cs_time_report)
        val timeReport = "${report.time}ms"
        timeTV.text = timeReport
        val progress : ProgressBar = findViewById(R.id.cs_progressBar)
        progress.visibility = View.INVISIBLE
    }
}
