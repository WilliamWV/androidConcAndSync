package com.example.concurrencyeval


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.concurrencyeval.activities.InitActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class DwTest {

    @get:Rule
    var initActivityRule: ActivityTestRule<InitActivity> = ActivityTestRule(InitActivity::class.java)



    private fun downloadAndCheckImg(id: Int){
        EspressoUtil.clickOnViewId(R.id.id_spinner_choose_img)
        EspressoUtil.clickOnViewText(Constants.imgDescr[id]!!)
        EspressoUtil.clickOnViewId(R.id.fd_run_button)
        Thread.sleep(TimeUnit.SECONDS.toMillis(2))
        onView(withId(R.id.id_iv_image_report)).check(matches(isDisplayingAtLeast(1)))
    }

    private fun openDownloadActivity(implId: Int){
        onView(withId(R.id.init_spinner_problems)).perform(click())
        onView(withText(Constants.problemNames[Constants.DOWNLOAD_FILE])).perform(click())
        onView(withId(R.id.init_spinner_impl)).perform(click())
        onView(withText(Constants.implNames[implId])).perform(click())
        onView(withId(R.id.init_b_done)).perform(click())
    }

    private fun backToInitActivity(){
        onView(isRoot()).perform(pressBack())

    }

    @Test
    fun dwTest(){

        val implementations = listOf(
            Constants.THREADS, Constants.THREAD_POOL, Constants.ASYNC_TASK,
            Constants.INTENT_SERV, Constants.HAMER, Constants.COROUTINES
        )
        val imgs = listOf(
            Constants.CAT, Constants.DOG, Constants.LION,
            Constants.PIGEON, Constants.PLATYPUS
        )

        implementations.forEach { impl ->
            openDownloadActivity(impl)
            imgs.forEach { img -> downloadAndCheckImg(img) }
            backToInitActivity()
        }

    }



}