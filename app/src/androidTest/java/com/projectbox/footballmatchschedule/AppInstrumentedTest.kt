package com.projectbox.footballmatchschedule

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.IdlingResource
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.projectbox.footballmatchschedule.view.MainActivity
import org.hamcrest.CoreMatchers.allOf

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class AppInstrumentedTest {
    @Rule
    @JvmField val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.projectbox.footballmatchschedule", appContext.packageName)
    }

    @Test
    fun testRecyclerView() {
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))

        try {
            Thread.sleep(2500)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        onView(withText("Burnley")).check(matches(isDisplayed()))

        onView(allOf(withId(R.id.recycler_view))).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(2, click()))

        onView(withText("Bournemouth")).check(matches(isDisplayed()))

        onView(withText("Sun, 13 May 2018")).check(matches(isDisplayed()))


    }
}
