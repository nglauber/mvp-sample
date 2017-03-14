package br.com.nglauber.exemplolivro.ui


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.view.View
import br.com.nglauber.exemplolivro.R
import br.com.nglauber.exemplolivro.features.postslist.ListPostsActivity
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestCrud {

    @get:Rule
    var mActivityTestRule = ActivityTestRule(ListPostsActivity::class.java)

    @Test
    fun testCrud() {
        onView(allOf<View>(withId(R.id.post_list_fab),
                withParent(allOf<View>(withId(R.id.post_list_fragment),
                        withParent(withId(R.id.activity_main)))), isDisplayed()))
                .perform(click())

        onView(withId(R.id.post_edit_text))
                .perform(scrollTo(), replaceText("abc"), closeSoftKeyboard())

        onView(allOf<View>(withId(R.id.post_button_save), withText("Save")))
                .perform(scrollTo(), click())

        onView(allOf<View>(withId(R.id.post_list_recyclerview), isDisplayed()))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        waitFor(1000)

        onView(allOf<View>(withId(R.id.post_edit_text), withText("abc")))
                .perform(scrollTo(), click())

        onView(allOf<View>(withId(R.id.post_edit_text), withText("abc")))
                .perform(scrollTo(), replaceText("abcdef"), closeSoftKeyboard())

        onView(allOf<View>(withId(R.id.post_button_save), withText("Save")))
                .perform(scrollTo(), click())

        onView(allOf<View>(withId(R.id.post_list_recyclerview), isDisplayed()))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        onView(allOf<View>(withId(R.id.post_button_delete), withText("Delete")))
                .perform(scrollTo(), click())
    }

    fun waitFor(time: Long) {
        Thread.sleep(time)
    }

}
