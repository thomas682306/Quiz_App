package com.google.developers.mojimaster2;

import androidx.test.espresso.action.ViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.builders.JUnit4Builder;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UITestforFabButton {

    @Rule
    public ActivityTestRule mactvityrule=new ActivityTestRule<>(SmileyListActivity.class);


    @Test
    public void fabButtonTest(){
        onView(withId(R.id.fab)).perform(ViewActions.click());

        onView(withId(R.id.emoji_char)).check(matches(isDisplayed()));
    }

    @Test
    public void SharedPrefTest(){


    }
}
