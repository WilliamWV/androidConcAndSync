package com.example.concurrencyeval

import com.example.concurrencyeval.util.RunReport

interface GeneralInstrTest {

    var reports: MutableList<RunReport>
    fun runTest()

}