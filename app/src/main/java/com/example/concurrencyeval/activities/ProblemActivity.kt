package com.example.concurrencyeval.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.R

class ProblemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_problem)
        val reportText: TextView = findViewById(R.id.probl_report)
        val message = "Selected problem ${Constants.problemNames[intent.getIntExtra(Constants.PROBLEM_EXTRA, -1)]} implemented with ${Constants.implNames[intent.getIntExtra(
            Constants.IMPL_EXTRA, -1)]}"
        reportText.text = message
    }
}
