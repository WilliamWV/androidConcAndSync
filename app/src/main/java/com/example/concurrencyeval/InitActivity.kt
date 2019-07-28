package com.example.concurrencyeval

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner

class InitActivity : AppCompatActivity(){

    private lateinit var problemSpinner: Spinner
    private lateinit var implSpinner: Spinner

    private val problemsPositionMap = hashMapOf(
        0 to Constants.MATRIX_MULT, 1 to Constants.PHILOSOPHERS,
        2 to Constants.CONCURR_SUM, 3 to Constants.LARGE_DOWN,
        4 to Constants.SMALL_DOWN, 5 to Constants.UI_BACKGROUND
    )

    private val implsPositionMap = hashMapOf(
        0 to Constants.THREADS, 1 to Constants.ASYNC_TASK,
        2 to Constants.HAMER, 3 to Constants.INTENT_SERV,
        4 to Constants.THREAD_POOL, 5 to Constants.COROUTINES
    )

    fun populateSpinners(spinnerViewId: Int, spinnerResourcesId: Int) : Spinner{
        val spinner: Spinner = findViewById(spinnerViewId)
        ArrayAdapter.createFromResource(
            this,
            spinnerResourcesId,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        return spinner
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)

        //Spinner population
        this.problemSpinner = populateSpinners(R.id.init_spinner_problems, R.array.problems)
        this.implSpinner = populateSpinners(R.id.init_spinner_impl, R.array.implementations)

        val doneButton: Button = findViewById(R.id.init_b_done)
        doneButton.setOnClickListener{
          val selectedProblem = this.problemsPositionMap[this.problemSpinner.selectedItemPosition]
          val selectedImplementation = this.implsPositionMap[this.implSpinner.selectedItemPosition]
          val intent = Intent(this, ProblemActivity::class.java).apply{
              putExtra(Constants.PROBLEM_EXTRA, selectedProblem)
              putExtra(Constants.IMPL_EXTRA, selectedImplementation)
          }
          startActivity(intent)

        }
    }


}
