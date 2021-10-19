package com.iu.open311;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ApplicationTest {
    @Test
    public void useAppContext() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.iu.open311", appContext.getPackageName());
    }

    /**
     * @see <a href="https://developer.android.com/guide/components/activities/testing" />
     */
    @Test
    public void mainActivity() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        assertEquals(Lifecycle.State.RESUMED, scenario.getState());

        onView(withId(R.id.headerImage1)).check(matches(isDisplayed()));
        onView(withId(R.id.headerImage2)).check(matches(isDisplayed()));

        onView(withId(R.id.btn_new_issue)).check(matches(isDisplayed()));
        onView(withId(R.id.btnIssues)).check(matches(isDisplayed()));
        onView(withId(R.id.btnSearch)).check(matches(isDisplayed()));

        onView(withId(R.id.btnSearch)).perform(click());
        onView(withId(R.id.recyclerList)).check(matches(isDisplayed()));

        scenario.close();
    }

    @Test
    public void newIssue() {
        ActivityScenario<NewIssueActivity> scenario =
                ActivityScenario.launch(NewIssueActivity.class);
        assertEquals(Lifecycle.State.RESUMED, scenario.getState());

        onView(withId(R.id.gridView)).check(matches(isDisplayed()));

        scenario.close();
    }

    @Test
    public void settingsActivity() {
        ActivityScenario<SettingsActivity> scenario =
                ActivityScenario.launch(SettingsActivity.class);
        assertEquals(Lifecycle.State.RESUMED, scenario.getState());
        onView(withId(R.id.settings)).check(matches(isDisplayed()));
        scenario.close();
    }

    @Test
    public void creditsActivity() {
        ActivityScenario<CreditsActivity> scenario = ActivityScenario.launch(CreditsActivity.class);
        assertEquals(Lifecycle.State.RESUMED, scenario.getState());
        onView(withId(R.id.textDescription)).check(matches(isDisplayed()));
        onView(withId(R.id.textImages)).check(matches(isDisplayed()));
        scenario.close();
    }
}