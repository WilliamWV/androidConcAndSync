package com.example.concurrencyeval

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ProblemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_problem)
        val reportText: TextView = findViewById(R.id.probl_report)
        val message = "Selected problem ${Constants.problemNames[intent.getIntExtra(Constants.PROBLEM_EXTRA, -1)]} implemented with ${Constants.implNames[intent.getIntExtra(Constants.IMPL_EXTRA, -1)]}"
        reportText.text = message
    }
}
