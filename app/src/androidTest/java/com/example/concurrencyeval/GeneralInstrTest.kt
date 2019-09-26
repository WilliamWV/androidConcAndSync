package com.example.concurrencyeval

import com.example.concurrencyeval.util.TestReport

interface GeneralInstrTest {

    var reports: MutableList<TestReport>
    fun runTest()
    fun analizeReports()

}