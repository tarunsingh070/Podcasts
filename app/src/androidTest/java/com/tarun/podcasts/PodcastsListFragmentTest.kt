package com.tarun.podcasts

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tarun.podcasts.data.BASE_URL
import com.tarun.podcasts.ui.main.MainActivity
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PodcastsListFragmentTest {
    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> =
        ActivityTestRule(MainActivity::class.java, false, false)

    lateinit var server: MockWebServer

    /**
     * Creates a mock web server dispatcher with prerecorded requests and responses.
     *
     * @return An instance of [Dispatcher]
     */
    private fun getDispatcher(): Dispatcher? {
        return object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse? {
                when (request.path) {
                    "/search?media=podcast&country=ca&term=Android" -> {
                        return MockResponse()
                            .setResponseCode(200)
                            .setBody(
                                FileReaderTestHelper.getStringFromFile(
                                    InstrumentationRegistry.getInstrumentation()
                                        .context,
                                    "get_podcasts_android_200_ok_response.json"
                                )
                            )
                    }
                    "/search?media=podcast&country=ca&term=Mustang" -> {
                        return MockResponse()
                            .setResponseCode(200)
                            .setBody(
                                FileReaderTestHelper.getStringFromFile(
                                    InstrumentationRegistry.getInstrumentation()
                                        .context,
                                    "get_podcasts_mustang_200_ok_response.json"
                                )
                            )
                    }
                }

                throw IllegalStateException("no mock set up for " + request.path)
            }
        }
    }

    @Before
    fun setUp() {
        server = MockWebServer()
//        server.shutdown()
        server.start()

        // FixMe: Find a better way to set Retrofit's base URL as MockWebServer's base url.
        BASE_URL = server.url("/").toString()
        server.setDispatcher(getDispatcher())

        activityRule.launchActivity(Intent())
    }

    @Test
    fun checkIfInitialPodcastsListLoadWorks() {
        onView(withId(R.id.podcast_list_recycler_view))
            .perform(
                RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(4)
            )
        onView(withText("Android Police Podcast")).check(matches(isDisplayed()))
    }

    @Test
    fun testSearchFunctionality() {
        onView(withId(R.id.action_search))
            .perform(click())
        onView(withId(R.id.search_src_text))
            .perform(click(), typeText("Mustang"), pressImeActionButton())
        onView(withId(R.id.podcast_list_recycler_view))
            .perform(
                RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10)
            )

        onView(withText("Mustang Car Show")).check(matches(isDisplayed()))
    }

    @After
    fun tearDown() {
        server.shutdown()
    }
}