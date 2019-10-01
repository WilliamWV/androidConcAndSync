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
        Log.d(logTag, "=".repeat(100))
        Log.d(logTag, "=".repeat(38) + " MATRIX MULTIPLICATION " + "=".repeat(39))
        Log.d(logTag, "=".repeat(100))
        reports.forEach { report ->
            Log.d(logTag,
                "Repetition "+ report.testParams["rep"] + ": " + "Report execution of " + Constants.implNames[report.testParams["impl"]] +
                        " implementation, with size: " + report.testParams["size"] + "x" +
                        report.testParams["size"] + "; using " + report.testParams["tasks"] +
                        " tasks"
            )
            Log.d(logTag, "Time: " + report.runReport.time.toString() + " ms")
        }
        Log.d(logTag, "=".repeat(100))
    }

    override var reports: MutableList<TestReport> = mutableListOf()

    @get:Rule
    var mmActivity: ActivityTestRule<MatMultActivity> = ActivityTestRule(MatMultActivity::class.java, false, false)

    private fun performInteractions(tasks: Int, size: Int, impl: Int){
        onView(withId(R.id.mm_et_size)).perform(clearText(), typeText(size.toString()), click())
        onView(withId(R.id.mm_et_tasks)).perform(clearText(), typeText(tasks.toString()), click())
        onView(isRoot()).perform(closeSoftKeyboard())
        for (i in 0 until Constants.REPETITIONS) {
            onView(withId(R.id.mm_run_button)).perform(click())
            mmActivity.activity.waitTask()
            assertTrue(mmActivity.activity.report.time > 0)
            reports.add(
                TestReport(
                    Constants.MATRIX_MULT,
                    mmActivity.activity.report,
                    mapOf("tasks" to tasks, "size" to size, "impl" to impl, "rep" to i + 1)
                )
            )
        }
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