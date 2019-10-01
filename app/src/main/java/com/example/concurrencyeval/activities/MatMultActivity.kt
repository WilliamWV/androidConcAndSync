package com.example.concurrencyeval.activities

import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.R
import com.example.concurrencyeval.implementations.mm.MMCoroutines
import com.example.concurrencyeval.implementations.mm.MMHaMeR
import com.example.concurrencyeval.implementations.mm.MMThread
import com.example.concurrencyeval.implementations.mm.MMThreadPool
import com.example.concurrencyeval.util.RunReport

class MatMultActivity : AbstractActivity(Constants.MATRIX_MULT) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.setImplementation(intent.getIntExtra(Constants.IMPL_EXTRA, -1))
        setContentView(R.layout.activity_mat_mult)

        super.onCreate(savedInstanceState)

        super.mRunButton.setOnClickListener {
            super.runManager.taskStarted()
            super.mProgress.visibility = VISIBLE
            val size: Int = findViewById<EditText>(R.id.mm_et_size).text.toString().toInt()
            val tasks: Int = findViewById<EditText>(R.id.mm_et_tasks).text.toString().toInt()
            when (super.mImplementation){
                Constants.THREADS -> MMThread(size, tasks, this).start() // OBS on MMThread
                Constants.THREAD_POOL -> MMThreadPool(size, tasks, this).start() // OBS on MMThreadPool
                Constants.COROUTINES -> MMCoroutines(size, tasks, this).start()
                Constants.HAMER -> MMHaMeR(size, tasks, this).start()
                else-> this.updateReport(RunReport(-1))
            }

        }

    }
    override fun updateReport(report: RunReport){
        val timeTV:TextView = findViewById(R.id.mm_time_report)
        val timeReport = "${report.time}ms"
        timeTV.text = timeReport
        val progress : ProgressBar = findViewById(R.id.mm_progressBar)
        progress.visibility = INVISIBLE
        super.report = report
        super.runManager.taskCompleted()
    }
}
