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
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.example.concurrencyeval.activities.ImgDownloadActivity
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class FinalImgDownTest {

    @get:Rule
    var idActivity: ActivityTestRule<ImgDownloadActivity> = ActivityTestRule(ImgDownloadActivity::class.java, false, false)

    private fun runIDTest(img: Int){
        onView(withId(R.id.id_spinner_choose_img)).perform(click())
        onView(withText(Constants.imgDescr[img]!!)).perform(click())
        onView(withId(R.id.fd_run_button)).perform(click())
        idActivity.activity.waitTask()
        assertTrue(idActivity.activity.report.time > 0)
    }

    @Test
    fun idTest(){

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