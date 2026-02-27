package com.example.androiduitesting;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.anything;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest

public class ShowActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> scenario =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void cleanup() {
        Intents.release();
    }

    /*Helper function, adds a city through UI and clicks the first list item.*/
    private void addCityAndClickIt(String cityName) {
        onView(withId(R.id.button_add)).perform(click());
        onView(withId(R.id.editText_name)).perform(click());
        onView(withId(R.id.editText_name)).perform(replaceText(cityName), closeSoftKeyboard());
        onView(withId(R.id.button_confirm)).perform(click());

        onData(anything())
                .inAdapterView(withId(R.id.city_list))
                .atPosition(0)
                .perform(click());
    }

    // Check whether the activity correctly switched
    @Test
    public void activitySwitchesToShowActivity_whenCityClicked() {
        addCityAndClickIt("Edmonton");

        // Confirms the launched activity is ShowActivity
        intended(hasComponent(ShowActivity.class.getName()));

        // Also confirm a unique view in ShowActivity is visible
        onView(withId(R.id.text_city_name)).check(matches(isDisplayed()));
    }

    // Test whether the city name is consistent
    @Test
    public void cityNameIsPassedAndDisplayedCorrectly() {
        addCityAndClickIt("Tokyo");

        // Confirms the intent extra is correct
        intended(hasExtra(ShowActivity.key_selected_city, "Tokyo"));

        // Confirms ShowActivity displays the same city name
        onView(withId(R.id.text_city_name)).check(matches(withText("Tokyo")));
    }

    // Test the "back" button
    @Test
    public void backButtonReturnsToMainActivity() {
        addCityAndClickIt("Berlin");

        // We should be in ShowActivity now
        onView(withId(R.id.text_city_name)).check(matches(withText("Berlin")));

        // Tap back button in UI
        onView(withId(R.id.button_back)).perform(click());

        // Confirm ShowActivity is gone
        onView(withId(R.id.text_city_name)).check(doesNotExist());

        // Confirm MainActivity is visible
        onView(withId(R.id.city_list)).check(matches(isDisplayed()));
        onView(withId(R.id.button_add)).check(matches(isDisplayed()));
    }

}
