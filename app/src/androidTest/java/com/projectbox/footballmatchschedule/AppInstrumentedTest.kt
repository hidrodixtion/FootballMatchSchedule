package com.projectbox.footballmatchschedule

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.IdlingResource
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.swipeLeft
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
        val recyclerView = onView(allOf(isDisplayed(), withId(R.id.recycler_view)))
        recyclerView.check(matches(isDisplayed()))

        try {
            Thread.sleep(700)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        recyclerView.perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(2, click()))

        onView(withText("Leicester")).check(matches(isDisplayed()))

        onView(withText("Sat, 11 Aug 2018")).check(matches(isDisplayed()))
    }

    @Test
    fun testTeamDetail() {
        onView(withId(R.id.menu_team)).perform(click())

        val recyclerView = onView(allOf(isDisplayed(), withId(R.id.recycler_view)))
        recyclerView.check(matches(isDisplayed()))

        try {
            Thread.sleep(700)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        onView(withText("Arsenal")).check(matches(isDisplayed()))
    }

    @Test
    fun testFavorite() {
        onView(withId(R.id.menu_team)).perform(click())

        try {
            Thread.sleep(700)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        val recyclerView = onView(allOf(isDisplayed(), withId(R.id.recycler_view)))
        recyclerView.check(matches(isDisplayed()))

        try {
            Thread.sleep(700)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        recyclerView.perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        onView(withId(R.id.add_to_favorite)).perform(click())

        Espresso.pressBack()

        onView(withId(R.id.menu_favorite)).perform(click())

        val viewPager = onView(allOf(isDisplayed(), withId(R.id.view_pager)))
        viewPager.perform(swipeLeft())

        val favRV = onView(allOf(isDisplayed(), withId(R.id.recycler_view)))

        onView(withText("Arsenal")).check(matches(isDisplayed()))
    }
}
