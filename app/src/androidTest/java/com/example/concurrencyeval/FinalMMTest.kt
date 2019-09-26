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
import com.example.concurrencyeval.activities.MatMultActivity
import com.example.concurrencyeval.util.TestReport
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeoutException


@RunWith(AndroidJUnit4::class)
class FinalMMTest : GeneralInstrTest{

    private val logTag = "MM_TEST"
    @After
    override fun analizeReports() {
        Log.d(logTag, "=".repeat(30))
        Log.d(logTag, "=".repeat(3) + " MATRIX MULTIPLICATION " + "=".repeat(4))
        Log.d(logTag, "=".repeat(30))
        reports.forEach { report ->
            Log.d(logTag,
                "Report execution of " + Constants.implNames[report.testParams["impl"]] +
                        " implementation, with size: " + report.testParams["size"] + "X" +
                        report.testParams["size"] + "; using " + report.testParams["tasks"] +
                        " tasks"
            )
            Log.d(logTag, "Time: " + report.runReport.time.toString() + " ms")
        }
        Log.d(logTag, "=".repeat(30))
    }

    override var reports: MutableList<TestReport> = mutableListOf()

    @get:Rule
    var mmActivity: ActivityTestRule<MatMultActivity> = ActivityTestRule(MatMultActivity::class.java, false, false)

    private fun performInteractions(tasks: Int, size: Int, impl: Int){
        onView(withId(R.id.mm_et_size)).perform(clearText(), typeText(size.toString()), click())
        onView(withId(R.id.mm_et_tasks)).perform(clearText(), typeText(tasks.toString()), click())
        onView(isRoot()).perform(closeSoftKeyboard())
        onView(withId(R.id.mm_run_button)).perform(click())
        mmActivity.activity.waitTask()
        assertTrue(mmActivity.activity.report.time > 0)
        reports.add(
            TestReport(
                Constants.MATRIX_MULT,
                mmActivity.activity.report,
                mapOf("tasks" to tasks, "size" to size, "impl" to impl)
            ))
    }
    private fun runMMTest(tasks: Int, size: Int, impl: Int, fails: Int = 0){
        try {
            performInteractions(tasks, size, impl)
        }catch(te: TimeoutException){
            // try again if number of fails does not have passed maximum
            if (fails >= Constants.MAX_TIMEOUT_FAILS) throw te
            else runMMTest(tasks, size, fails + 1)
        }
    }
    @Test
    override fun runTest() {
        val tasksToUse = listOf(1, 2, 8, 64)
        val sizesToTest = listOf(128, 256, 512)
        val implementations = listOf(Constants.THREADS, Constants.THREAD_POOL, Constants.HAMER, Constants.COROUTINES)

        implementations.forEach { impl ->
            val intent = Intent()
            intent.putExtra(Constants.IMPL_EXTRA, impl)
            mmActivity.launchActivity(intent)

            sizesToTest.forEach{ size ->
                tasksToUse.forEach { tasks ->
                    runMMTest(tasks, size, impl)
                }
            }
            mmActivity.finishActivity()
        }
    }

}