/**
 * This file contains tests to Matrix multiplication problem implemented with the HaMeR framework,
 * these tests are here because they needed some APIs functions to run properly, so these calls
 * could not be mocked on unit tests
 *
 * **/

package com.example.concurrencyeval

import android.content.Intent
import android.util.Log
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.concurrencyeval.activities.ProdConsActivity
import com.example.concurrencyeval.util.TestReport
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeoutException


@RunWith(AndroidJUnit4::class)
class FinalProdConsTest : GeneralInstrTest{
    private val logTag = "PC_TEST"
    @After
    override fun analizeReports() {
        Log.d(logTag, "=".repeat(100))
        Log.d(logTag, "=".repeat(37) + " PRODUCERS AND CONSUMERS " + "=".repeat(38))
        Log.d(logTag, "=".repeat(100))
        reports.forEach { report ->
            Log.d(logTag,
                "Repetition "+ report.testParams["rep"] + ": " + "Report execution of " + Constants.implNames[report.testParams["impl"]] +
                        " implementation, with " + report.testParams["producers"] + " producers and " +
                        report.testParams["consumers"] + " consumers, and " +
                        report.testParams["buffSize"] + " buffer positions."
            )
            Log.d(logTag, "Produced items: " + report.runReport.prod.toString())
            Log.d(logTag, "Consumed items: " + report.runReport.cons.toString())
        }
        Log.d(logTag, "=".repeat(100))
    }

    override var reports: MutableList<TestReport> = mutableListOf()

    @get:Rule
    var pcActivity: ActivityTestRule<ProdConsActivity> = ActivityTestRule(ProdConsActivity::class.java, false, false)

    private fun performInteractions(producers: Int, consumers: Int, buffSize: Int, impl: Int){
        onView(withId(R.id.pc_et_producers)).perform(
            clearText(),
            typeText(producers.toString()),
            click()
        )
        onView(withId(R.id.pc_et_consumers)).perform(
            clearText(),
            typeText(consumers.toString()),
            click()
        )
        onView(withId(R.id.pc_et_buffer)).perform(
            clearText(),
            typeText(buffSize.toString()),
            click()
        )
        onView(isRoot()).perform(closeSoftKeyboard())
        for (i in 0 .. Constants.REPETITIONS) {
            onView(withId(R.id.pc_run_button)).perform(click())
            pcActivity.activity.waitTask()
            if (!(i == 0 && Constants.IGNORE_FIRST)) {

                assertTrue(pcActivity.activity.report.prod >= 0)
                assertTrue(pcActivity.activity.report.cons >= 0)
                reports.add(
                    TestReport(
                        Constants.PROD_CONS,
                        pcActivity.activity.report,
                        mapOf(
                            "producers" to producers, "consumers" to consumers,
                            "buffSize" to buffSize, "impl" to impl, "rep" to i
                        )
                    )

                )
            }
        }
    }

    private fun runPcTest(producers: Int, consumers: Int, buffSize: Int, impl: Int, fails: Int = 0){
        try {
            performInteractions(producers, consumers, buffSize, impl)
        }catch(te: TimeoutException){
            // try again if number of fails does not have passed maximum
            if (fails >= Constants.MAX_TIMEOUT_FAILS) throw te
            else runPcTest(producers, consumers, buffSize, fails + 1)
        }

    }

    @Test
    override fun runTest(){

        val producersToTest = listOf(5, 10, 20)
        val consumersToTest = listOf(5, 10, 20)
        val bufferToUse = listOf(2, 8)

        val implementations = listOf(Constants.LOCK, Constants.ATOMIC, Constants.SEMAPHORE, Constants.SYNCHRONIZED)

        implementations.forEach { impl ->
            val intent = Intent()
            intent.putExtra(Constants.IMPL_EXTRA, impl)
            pcActivity.launchActivity(intent)

            bufferToUse.forEach{ buffSize ->
                producersToTest.forEach { producers ->
                    consumersToTest.forEach { consumers ->
                        runPcTest(producers, consumers, buffSize, impl)
                    }
                }
            }
            pcActivity.finishActivity()
        }
    }

}