package com.example.concurrencyeval.activities

import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.R
import com.example.concurrencyeval.implementations.sum.SumCoroutines
import com.example.concurrencyeval.implementations.sum.SumHaMeR
import com.example.concurrencyeval.implementations.sum.SumThread
import com.example.concurrencyeval.implementations.sum.SumThreadPool
import com.example.concurrencyeval.util.RunReport

class ConcSumActivity : AbstractActivity(Constants.CONCURR_SUM) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.setImplementation(intent.getIntExtra(Constants.IMPL_EXTRA, -1))
        setContentView(R.layout.activity_conc_sum)

        super.onCreate(savedInstanceState)
        //Use "super" to avoid confusion of where the variable is defined
        super.mRunButton.setOnClickListener {
            super.mProgress.visibility = VISIBLE
            val numbers: Int = findViewById<EditText>(R.id.cs_et_numbers).text.toString().toInt()
            val tasks: Int = findViewById<EditText>(R.id.cs_et_tasks).text.toString().toInt()
            when (super.mImplementation){
                Constants.THREADS->SumThread(numbers, tasks, this).start()
                Constants.THREAD_POOL->SumThreadPool(numbers, tasks, this).start()
                Constants.COROUTINES->SumCoroutines(numbers, tasks, this).start()
                Constants.HAMER->SumHaMeR(numbers, tasks, this).start()
                else->this.updateReport(RunReport(-1))
            }
        }

    }

    override fun updateReport(report: RunReport) {
        val timeTV: TextView = findViewById(R.id.cs_time_report)
        val timeReport = "${report.time}ms"
        timeTV.text = timeReport
        val progress : ProgressBar = findViewById(R.id.cs_progressBar)
        progress.visibility = View.INVISIBLE
    }
}
