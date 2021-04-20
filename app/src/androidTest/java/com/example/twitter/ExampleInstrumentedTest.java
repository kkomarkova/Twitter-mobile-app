package com.example.twitter;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
@Test
public void useAppContext() {
//Context of the app under test.
/* Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
 assertEquals("com.example.twitter", appContext.getPackageName());*/
}
@Rule
public ActivityScenarioRule rule =
        new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testItAll() {
        //1.We are using Viewmatcher to find a view onView(withId(R.id.my_view))
        //2.We are using ViewAssertion  to check if the result of the action matches
        // an assertion: .check(matches(isDisplayed()))
        //3.Use a ViewAction to perform an action: .perform(click())
        //See if there is text Twister App in the app
        onView(withText("Twister App")).check(matches(isDisplayed()));
        //TextView contains
        //onView(withId(R.id.activity_main_passwordEditText)).check(matches(withText("Password")));
        //onView(withId(R.id.activity_main_emailEditText)).check(matches(withText("Email")));

        //Test the EditText main
        
        //Button action test if they work on click
        onView(withId(R.id.activity_main_loginButton)).perform(click());
        onView(withId(R.id.activity_main_logoutButton)).perform(click());

    }





}