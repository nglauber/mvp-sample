package br.com.nglauber.exemplolivro.ui


import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItem
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import br.com.nglauber.exemplolivro.App
import br.com.nglauber.exemplolivro.R
import br.com.nglauber.exemplolivro.features.postslist.ListPostsActivity
import br.com.nglauber.exemplolivro.mock.DaggerTestComponent
import br.com.nglauber.exemplolivro.mock.TestModule
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestCrud {

    @get:Rule
    var activityTestRule = object : ActivityTestRule<ListPostsActivity>(ListPostsActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            val application : App = (InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as App)

            val component = DaggerTestComponent.builder()
                    .testModule(TestModule(application))
                    .build()

            application.component = component
        }
    }

    @Test
    fun testCrud() {
        val initialText = "Text original"
        val newText = "Text modified"

        // INSERT
        clickNewPost()
        typePostText(initialText)
        clickSave()

        // EDIT
        clickListItem(initialText)
        typePostText(newText)
        clickSave()

        // DELETE
        clickListItem(newText)
        clickDelete()
    }

    fun clickNewPost(){
        onView(allOf(withId(R.id.post_list_fab),
                withParent(allOf(withId(R.id.post_list_fragment),
                        withParent(withId(R.id.activity_main)))),
                isDisplayed())).perform(click())
    }

    fun typePostText(text : String){
        onView(allOf(withId(R.id.post_edit_text), isDisplayed()))
                .perform(scrollTo(), replaceText(text))
    }

    fun clickSave() {
        onView(allOf(withId(R.id.post_button_save), isDisplayed()))
                .perform(scrollTo(), click())
    }

    fun clickListItem(text : String) {
        onView(allOf(withId(R.id.post_list_recyclerview), isDisplayed()))
                .perform(actionOnItem<RecyclerView.ViewHolder>(hasDescendant(withText(text)), click()))
    }

    fun clickDelete(){
        onView(allOf(withId(R.id.post_button_delete), isDisplayed()))
                .perform(scrollTo(), click())
    }
}
