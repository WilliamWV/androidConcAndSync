/**
 * This file contains tests to Matrix multiplication problem implemented with the HaMeR framework,
 * these tests are here because they needed some APIs functions to run properly, so these calls
 * could not be mocked on unit tests
 *
 * **/

package com.example.concurrencyeval

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.matcher.ViewMatchers.isRoot
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import com.example.concurrencyeval.activities.PhilosophersActivity
import com.example.concurrencyeval.util.TestReport
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeoutException


@RunWith(AndroidJUnit4::class)
class FinalPhTest : GeneralInstrTest{
    private val logTag = "PH_TEST"
    override fun analizeReports() {
        Log.d(logTag, "=".repeat(30))
        Log.d(logTag, "=".repeat(8) + " PHILOSOPHERS " + "=".repeat(8))
        Log.d(logTag, "=".repeat(30))
        reports.forEach { report ->
            Log.d(logTag,
                "Report execution of " + Constants.implNames[report.testParams["impl"]] +
                        " implementation, with " + report.testParams["philosophers"] +
                        "philosophers, running by " + report.testParams["time"] + " seconds"
            )
            Log.d(logTag, "Average executions: " + report.runReport.avg.toString())
            Log.d(logTag, "Standard deviation: " + report.runReport.std.toString())
        }
        Log.d(logTag, "=".repeat(30))
    }

    override var reports: MutableList<TestReport> = mutableListOf()

    @get:Rule
    var phActivity: ActivityTestRule<PhilosophersActivity> = ActivityTestRule(PhilosophersActivity::class.java, false, false)

    private fun performInteractions(philosophers: Int, time: Int, impl: Int){
        onView(withId(R.id.ph_et_time)).perform(
            clearText(),
            typeText(time.toString()),
            click()
        )
        onView(withId(R.id.ph_et_philosophers)).perform(
            clearText(),
            typeText(philosophers.toString()),
            click()
        )
        onView(isRoot()).perform(closeSoftKeyboard())
        onView(withId(R.id.ph_run_button)).perform(click())
        phActivity.activity.waitTask()
        assertTrue(phActivity.activity.report.avg > 0)
        assertTrue(phActivity.activity.report.std > 0)
        reports.add(
            TestReport(
                Constants.PHILOSOPHERS,
                phActivity.activity.report,
                mapOf("philosophers" to philosophers, "time" to time, "impl" to impl)
            ))
    }

    private fun runPhTest(philosophers: Int, time: Int, impl: Int, fails: Int = 0){
        try {
            performInteractions(philosophers, time, impl)
        }catch(te: TimeoutException){
            // try again if number of fails does not have passed maximum
            if (fails >= Constants.MAX_TIMEOUT_FAILS) throw te
            else runPhTest(philosophers, time, fails + 1)
        }
    }

    @Test
    override fun runTest(){

        val philosophersToUse = listOf(5, 11, 101)
        val timeToTest = listOf(2)
        val implementations = listOf(Constants.THREADS, Constants.THREAD_POOL, Constants.HAMER, Constants.COROUTINES)

        implementations.forEach { impl ->
            val intent = Intent()
            intent.putExtra(Constants.IMPL_EXTRA, impl)
            phActivity.launchActivity(intent)

            timeToTest.forEach{ size ->
                philosophersToUse.forEach { tasks ->
                    runPhTest(tasks, size, impl)
                }
            }
            phActivity.finishActivity()
        }
    }

}