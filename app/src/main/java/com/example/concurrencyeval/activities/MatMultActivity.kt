package com.example.concurrencyeval.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
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

class MatMultActivity : AppCompatActivity() {

    private var selectedImplementation: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mat_mult)
        selectedImplementation = intent.getIntExtra(Constants.IMPL_EXTRA, -1)
        val description: TextView = findViewById(R.id.mm_tv_description)
        val newDescription = description.text.toString() + ". Implemented with ${Constants.implNames[selectedImplementation]}"
        description.text = newDescription

        val runButton: Button = findViewById(R.id.mm_run_button)
        runButton.setOnClickListener {
            val progress : ProgressBar = findViewById(R.id.mm_progressBar)
            progress.visibility = VISIBLE
            val size: Int = findViewById<EditText>(R.id.mm_et_size).text.toString().toInt()
            val tasks: Int = findViewById<EditText>(R.id.mm_et_tasks).text.toString().toInt()
            when (selectedImplementation){
                Constants.THREADS -> MMThread(size, tasks, this).start() // OBS on MMThread
                Constants.THREAD_POOL -> MMThreadPool(size, tasks, this).start() // OBS on MMThreadPool
                Constants.COROUTINES -> MMCoroutines(size, tasks, this).start()
                Constants.HAMER -> MMHaMeR(size, tasks, this).start()
                else-> this.updateReport(RunReport(-1))
            }

        }

    }
    fun updateReport(report: RunReport){
        val timeTV:TextView = findViewById(R.id.mm_time_report)
        val timeReport = "${report.time}ms"
        timeTV.text = timeReport
        val progress : ProgressBar = findViewById(R.id.mm_progressBar)
        progress.visibility = INVISIBLE


    }

}
