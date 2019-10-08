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
import com.example.concurrencyeval.activities.ConcSumActivity
import com.example.concurrencyeval.util.TestReport
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeoutException


@RunWith(AndroidJUnit4::class)
class FinalSumTest : GeneralInstrTest{
    private val logTag = "CS_TEST"
    @After
    override fun analizeReports() {
        Log.d(logTag, "=".repeat(100))
        Log.d(logTag, "=".repeat(42) + " CONCURRENT SUM " + "=".repeat(42))
        Log.d(logTag, "=".repeat(100))
        reports.forEach { report ->
            Log.d(logTag,
                "Repetition "+ report.testParams["rep"] + ": " + "Report execution of " + Constants.implNames[report.testParams["impl"]] +
                        " implementation, adding " + report.testParams["numbers"] +
                        " numbers and using " + report.testParams["tasks"] + " tasks"
            )
            Log.d(logTag, "Time: " + report.runReport.time.toString() + " ms")
        }
        Log.d(logTag, "=".repeat(100))
    }

    override var reports: MutableList<TestReport> = mutableListOf()


    @get:Rule
    var csActivity: ActivityTestRule<ConcSumActivity> = ActivityTestRule(ConcSumActivity::class.java, false, false)

    private fun performInteractions(tasks: Int, numbers: Int, impl: Int){
        onView(withId(R.id.cs_et_numbers)).perform(clearText(), typeText(numbers.toString()), click())
        onView(withId(R.id.cs_et_tasks)).perform(clearText(), typeText(tasks.toString()), click())
        onView(isRoot()).perform(closeSoftKeyboard())
        for (i in 0 .. Constants.REPETITIONS) {
            onView(withId(R.id.cs_run_button)).perform(click())
            csActivity.activity.waitTask()
            if (!(i == 0 && Constants.IGNORE_FIRST)) {

                assertTrue(csActivity.activity.report.time > 0)
                reports.add(
                    TestReport(
                        Constants.CONCURR_SUM,
                        csActivity.activity.report,
                        mapOf(
                            "tasks" to tasks,
                            "numbers" to numbers,
                            "impl" to impl,
                            "rep" to i
                        )
                    )
                )
            }
        }
    }

    private fun runSumTest(tasks: Int, numbers: Int, impl: Int, fails: Int = 0){
        try {
            performInteractions(tasks, numbers, impl)
        }catch(te: TimeoutException){
            // try again if number of fails does not have passed maximum
            if (fails >= Constants.MAX_TIMEOUT_FAILS) throw te
            else runSumTest(tasks, numbers, fails + 1)
        }
    }

    @Test
    override fun runTest(){

        val tasksToUse = listOf(1, 16, 256)
        val numbersToTest = listOf(262144, 1048576)
        val implementations = listOf(Constants.THREADS, Constants.THREAD_POOL, Constants.HAMER, Constants.COROUTINES, Constants.THREADS_BARRIER)

        implementations.forEach { impl ->
            val intent = Intent()
            intent.putExtra(Constants.IMPL_EXTRA, impl)
            csActivity.launchActivity(intent)

            numbersToTest.forEach{ numbers ->
                tasksToUse.forEach { tasks ->
                    runSumTest(tasks, numbers, impl)
                }
            }
            csActivity.finishActivity()
        }
    }

}