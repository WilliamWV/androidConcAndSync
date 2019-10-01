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
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.concurrencyeval.activities.ImgDownloadActivity
import com.example.concurrencyeval.util.TestReport
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeoutException


@RunWith(AndroidJUnit4::class)
class FinalImgDownTest : GeneralInstrTest{
    private val logTag = "ID_TEST"
    @After
    override fun analizeReports() {
        Log.d(logTag, "=".repeat(100))
        Log.d(logTag, "=".repeat(42) + " IMAGE DOWNLOAD " + "=".repeat(42))
        Log.d(logTag, "=".repeat(100))
        reports.forEach { report ->
            Log.d(logTag,
                "Repetition "+ report.testParams["rep"] + ": " + "Report execution of " + Constants.implNames[report.testParams["impl"]] +
                        " implementation, downloading image of a: " +
                        Constants.imgDescr[report.testParams["img"]]
            )
            Log.d(logTag, "Time: " + report.runReport.time.toString() + " ms")
        }
        Log.d(logTag, "=".repeat(100))
    }

    override var reports: MutableList<TestReport> = mutableListOf()

    @get:Rule
    var idActivity: ActivityTestRule<ImgDownloadActivity> = ActivityTestRule(ImgDownloadActivity::class.java, false, false)
    private fun performInteractions(img: Int, impl: Int){
        onView(withId(R.id.id_spinner_choose_img)).perform(click())
        onView(withText(Constants.imgDescr[img]!!)).perform(click())
        for (i in 0 until Constants.REPETITIONS) {
            onView(withId(R.id.fd_run_button)).perform(click())
            idActivity.activity.waitTask()
            assertTrue(idActivity.activity.report.time > 0)
            reports.add(
                TestReport(
                    Constants.DOWNLOAD_FILE,
                    idActivity.activity.report,
                    mapOf("img" to img, "impl" to impl, "rep" to i + 1)
                )
            )
        }
    }

    private fun runIDTest(img: Int, impl: Int, fails: Int = 0){
        try {
            performInteractions(img, impl)
        }catch(te: TimeoutException){
            // try again if number of fails does not have passed maximum
            if (fails >= Constants.MAX_TIMEOUT_FAILS) throw te
            else runIDTest(img , fails + 1)
        }
    }

    @Test
    override fun runTest(){

        val implementations = listOf(
            Constants.THREADS, Constants.THREAD_POOL, Constants.ASYNC_TASK,
            Constants.INTENT_SERV, Constants.HAMER, Constants.COROUTINES
        )
        val imgs = listOf(
            Constants.CAT, Constants.DOG, Constants.LION,
            Constants.PIGEON, Constants.PLATYPUS
        )

        implementations.forEach { impl ->
            val intent = Intent()
            intent.putExtra(Constants.IMPL_EXTRA, impl)
            idActivity.launchActivity(intent)

            imgs.forEach{ img -> runIDTest(img, impl) }
            idActivity.finishActivity()
        }
    }

}