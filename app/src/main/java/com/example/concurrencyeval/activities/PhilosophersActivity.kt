package com.example.concurrencyeval.activities

import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.R
import com.example.concurrencyeval.implementations.philosophers.PhCoroutines
import com.example.concurrencyeval.implementations.philosophers.PhHaMeR
import com.example.concurrencyeval.implementations.philosophers.PhThread
import com.example.concurrencyeval.implementations.philosophers.PhThreadPool
import com.example.concurrencyeval.util.RunReport

class PhilosophersActivity : AbstractActivity(Constants.PHILOSOPHERS) {

    private val positionOfSyncs = hashMapOf(
        0 to Constants.SEMAPHORE,
        1 to Constants.SYNCHRONIZED,
        2 to Constants.LOCK
    )

    private fun populateSpinner() : Spinner {
        val spinner: Spinner = findViewById(R.id.ph_spinner_choose_sync)
        ArrayAdapter.createFromResource(
            this,
            R.array.ph_sync,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        return spinner
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.setImplementation(intent.getIntExtra(Constants.IMPL_EXTRA, -1))
        setContentView(R.layout.activity_philosophers)

        super.onCreate(savedInstanceState)

        val syncSpinner = this.populateSpinner()

        super.mRunButton.setOnClickListener {
            super.runManager.taskStarted()
            super.mProgress.visibility = View.VISIBLE
            val philosophers: Int = findViewById<EditText>(R.id.ph_et_philosophers).text.toString().toInt()
            val time: Int = findViewById<EditText>(R.id.ph_et_time).text.toString().toInt()
            val syncPosition = syncSpinner.selectedItemPosition

            when(super.mImplementation){
                Constants.THREADS -> PhThread(philosophers, time, positionOfSyncs[syncPosition]!!,this).start()
                Constants.THREAD_POOL -> PhThreadPool(philosophers, time, positionOfSyncs[syncPosition]!!, this).start()
                Constants.COROUTINES -> PhCoroutines(philosophers, time, positionOfSyncs[syncPosition]!!, this).start()
                Constants.HAMER -> PhHaMeR(philosophers, time, positionOfSyncs[syncPosition]!!, this).start()
                else -> this.updateReport(RunReport(intArrayOf(0)))
            }
        }
    }
    override fun updateReport(report: RunReport){
        val avgTV: TextView = findViewById(R.id.ph_average_report)
        val stdTV: TextView = findViewById(R.id.ph_deviation_report)
        val avgReport = "%.4f".format(report.avg)
        val stdReport = "%.4f".format(report.std)
        avgTV.text = avgReport
        stdTV.text = stdReport
        val progress : ProgressBar = findViewById(R.id.ph_progressBar)
        progress.visibility = View.INVISIBLE
        super.report = report
        super.runManager.taskCompleted()

    }
}
