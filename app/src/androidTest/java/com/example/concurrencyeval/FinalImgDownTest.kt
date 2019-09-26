/**
 * This file contains tests to Matrix multiplication problem implemented with the HaMeR framework,
 * these tests are here because they needed some APIs functions to run properly, so these calls
 * could not be mocked on unit tests
 *
 * **/

package com.example.concurrencyeval

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.example.concurrencyeval.activities.ImgDownloadActivity
import com.example.concurrencyeval.util.RunReport
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeoutException


@RunWith(AndroidJUnit4::class)
class FinalImgDownTest : GeneralInstrTest{
    override fun analizeReports() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override var reports: MutableList<RunReport> = mutableListOf()

    @get:Rule
    var idActivity: ActivityTestRule<ImgDownloadActivity> = ActivityTestRule(ImgDownloadActivity::class.java, false, false)
    private fun performInteractions(img: Int){
        onView(withId(R.id.id_spinner_choose_img)).perform(click())
        onView(withText(Constants.imgDescr[img]!!)).perform(click())
        onView(withId(R.id.fd_run_button)).perform(click())
        idActivity.activity.waitTask()
        assertTrue(idActivity.activity.report.time > 0)
        reports.add(idActivity.activity.report)
    }

    private fun runIDTest(img: Int, fails: Int = 0){
        try {
            performInteractions(img)
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

            imgs.forEach{ img -> runIDTest(img) }
            idActivity.finishActivity()
        }
    }

}