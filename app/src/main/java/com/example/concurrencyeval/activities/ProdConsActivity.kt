package com.example.concurrencyeval.activities

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.R
import com.example.concurrencyeval.implementations.prodcons.ProdConsManager
import com.example.concurrencyeval.util.RunReport

class ProdConsActivity : AbstractActivity(Constants.PROD_CONS) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.setImplementation(intent.getIntExtra(Constants.IMPL_EXTRA, -1))
        setContentView(R.layout.activity_prodcons)

        super.onCreate(savedInstanceState)

        super.mRunButton.setOnClickListener {
            super.mProgress.visibility = View.VISIBLE
            val producers: Int = findViewById<EditText>(R.id.pc_et_producers).text.toString().toInt()
            val consumers: Int = findViewById<EditText>(R.id.pc_et_consumers).text.toString().toInt()
            val bufferSize: Int = findViewById<EditText>(R.id.pc_et_buffer).text.toString().toInt()
            ProdConsManager(super.mImplementation, producers, consumers, bufferSize, this).start()
        }

    }

    override fun updateReport(report: RunReport) {
        val producedTV: TextView = findViewById(R.id.pc_prod_items_report)
        val consumedTV: TextView = findViewById(R.id.pc_cons_items_report)
        val producedReport = report.prod.toString()
        val consumedReport = report.cons.toString()

        producedTV.text = producedReport
        consumedTV.text = consumedReport

        super.mProgress.visibility = View.INVISIBLE
    }
}