package com.example.concurrencyeval.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.R

@Suppress("UNCHECKED_CAST")
class InitActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener{

    private lateinit var problemSpinner: Spinner
    private lateinit var implSpinner: Spinner

    private val problemsPositionMap = hashMapOf(
        0 to Constants.MATRIX_MULT, 1 to Constants.PHILOSOPHERS,
        2 to Constants.CONCURR_SUM, 3 to Constants.DOWNLOAD_FILE,
        4 to Constants.PROD_CONS
    )

    private var implsPositionMap = hashMapOf(
        0 to Constants.THREADS, 1 to Constants.ASYNC_TASK,
        2 to Constants.HAMER, 3 to Constants.INTENT_SERV,
        4 to Constants.THREAD_POOL, 5 to Constants.COROUTINES
    )

    override fun onNothingSelected(adapter: AdapterView<*>?) {
        // do nothing
    }

    private fun updateSpinnerToProblem(spinnerOptionsId: Int, implPosMap: HashMap<Int, Int>){
        val selectedText = this.implSpinner.selectedItem.toString()
        this.implSpinner = populateSpinners(R.id.init_spinner_impl, spinnerOptionsId)
        val spinnerAdapter = this.implSpinner.adapter as ArrayAdapter<String>
        val newItemPosition = spinnerAdapter.getPosition(selectedText)
        if (newItemPosition != -1)
            this.implSpinner.setSelection(newItemPosition)
        implsPositionMap = implPosMap
    }

    override fun onItemSelected(adapter: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        when(this.problemsPositionMap[pos]){
            Constants.DOWNLOAD_FILE -> {
                updateSpinnerToProblem(
                    R.array.id_implementations,
                    hashMapOf(
                        0 to Constants.THREADS, 1 to Constants.ASYNC_TASK,
                        2 to Constants.HAMER, 3 to Constants.INTENT_SERV,
                        4 to Constants.THREAD_POOL, 5 to Constants.COROUTINES
                    )
                )
            }
            Constants.MATRIX_MULT -> {
                updateSpinnerToProblem(
                    R.array.mm_implementations,
                    hashMapOf(
                        0 to Constants.THREADS, 1 to Constants.HAMER,
                        2 to Constants.THREAD_POOL, 3 to Constants.COROUTINES
                    )
                )
            }
            Constants.PHILOSOPHERS -> {
                updateSpinnerToProblem(
                    R.array.ph_implementations,
                    hashMapOf(
                        0 to Constants.THREADS, 1 to Constants.HAMER,
                        2 to Constants.THREAD_POOL, 3 to Constants.COROUTINES
                    )
                )
            }
            Constants.CONCURR_SUM -> {
                updateSpinnerToProblem(
                    R.array.cs_implementations,
                    hashMapOf(
                        0 to Constants.THREADS, 1 to Constants.THREADS_BARRIER,
                        2 to Constants.HAMER, 3 to Constants.THREAD_POOL,
                        4 to Constants.COROUTINES
                    )
                )
            }
            Constants.PROD_CONS -> {
                updateSpinnerToProblem(
                    R.array.pc_implementations,
                    hashMapOf(
                        0 to Constants.SEMAPHORE, 1 to Constants.SYNCHRONIZED,
                        2 to Constants.ATOMIC, 3 to Constants.LOCK
                    )

                )
            }
        }
    }

    private fun populateSpinners(spinnerViewId: Int, spinnerResourcesId: Int) : Spinner{
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
        this.problemSpinner = populateSpinners(
            R.id.init_spinner_problems,
            R.array.problems
        )
        this.implSpinner = populateSpinners(
            R.id.init_spinner_impl,
            R.array.mm_implementations
        )

        this.problemSpinner.onItemSelectedListener = this

        val doneButton: Button = findViewById(R.id.init_b_done)
        doneButton.setOnClickListener{
            val selectedProblem = this.problemsPositionMap[this.problemSpinner.selectedItemPosition]
            val selectedImplementation = this.implsPositionMap[this.implSpinner.selectedItemPosition]

            val intent: Intent
            intent = when (selectedProblem){

                Constants.MATRIX_MULT   -> Intent(this, MatMultActivity::class.java)
                Constants.CONCURR_SUM   -> Intent(this, ConcSumActivity::class.java)
                Constants.PHILOSOPHERS  -> Intent(this, PhilosophersActivity::class.java)
                Constants.DOWNLOAD_FILE -> Intent(this, ImgDownloadActivity::class.java)
                Constants.PROD_CONS     -> Intent(this, ProdConsActivity::class.java)

                else -> Intent(this, ProblemActivity::class.java).apply {
                    putExtra(Constants.PROBLEM_EXTRA, selectedProblem)
                }
            }.apply {
                putExtra(Constants.IMPL_EXTRA, selectedImplementation)
            }

            startActivity(intent)

        }
    }

}
