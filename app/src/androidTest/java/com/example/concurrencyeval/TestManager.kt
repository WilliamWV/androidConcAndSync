package com.example.concurrencyeval

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestManager {

    private val mmTests = FinalMMTest()
    private val csTests = FinalSumTest()
    private val phTests = FinalPhTest()
    private val idTests = FinalImgDownTest()
    private val pcTests = FinalProdConsTest()

    @Test
    fun allTests(){
        mmTests.runTest()
        csTests.runTest()
        phTests.runTest()
        idTests.runTest()
        pcTests.runTest()
    }
}