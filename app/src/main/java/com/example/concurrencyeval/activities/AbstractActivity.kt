package com.example.concurrencyeval.activities

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.util.RunReport

abstract class AbstractActivity(val problemId: Int) : AppCompatActivity() {

    lateinit var mDescriptionTV : TextView
    lateinit var mDescriptionText : String
    lateinit var mRunButton : Button
    lateinit var mProgress : ProgressBar
    var mImplementation : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val descriptionTVId = Constants.problemsUIIds[problemId]!![Constants.DESCRIPTION_TV]!!
        val runButtonId = Constants.problemsUIIds[problemId]!![Constants.RUN_BUTTON]!!
        val progressBarId = Constants.problemsUIIds[problemId]!![Constants.PROGRESS_BAR]!!
        //Add explicit "this" to avoid memory leak of the reference to this object
        mRunButton = this.findViewById(runButtonId)

        mProgress = this.findViewById(progressBarId)
        mDescriptionTV = this.findViewById(descriptionTVId)
        mDescriptionText = mDescriptionTV.text.toString() + ". Implemented with ${Constants.implNames[mImplementation]}"
        mDescriptionTV.text = mDescriptionText
    }

    abstract fun updateReport(report: RunReport)

    fun setImplementation(impl: Int){
        mImplementation = impl
    }
}