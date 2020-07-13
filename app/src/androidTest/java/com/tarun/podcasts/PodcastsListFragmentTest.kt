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
import com.tarun.podcasts.data.remote.ApiService
import com.tarun.podcasts.ui.main.MainActivity
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PodcastsListFragmentTest {

    companion object {
        lateinit var server: MockWebServer

        @BeforeClass @JvmStatic
        fun setUp() {
            server = MockWebServer()
            server.start()

            ApiService.Creator.updateBaseUrl(server.url("/").toString())
            server.setDispatcher(getDispatcher())
        }

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
                        "/search?media=podcast&country=ca&term=weird_string" -> {
                            return MockResponse()
                                .setResponseCode(200)
                                .setBody(
                                    FileReaderTestHelper.getStringFromFile(
                                        InstrumentationRegistry.getInstrumentation()
                                            .context,
                                        "get_podcasts_no_results_200_ok_response.json"
                                    )
                                )
                        }
                    }

                    throw IllegalStateException("no mock set up for " + request.path)
                }
            }
        }

        @AfterClass @JvmStatic
        fun tearDown() {
            server.shutdown()
        }
    }

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> =
        ActivityTestRule(MainActivity::class.java, false, false)

    @Before
    fun beforeTest() {
        activityRule.launchActivity(Intent())
    }

    @Test
    fun testIfInitialPodcastsListLoading() {
        onView(withId(R.id.podcast_list_recycler_view))
            .perform(
                RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(4)
            )
        onView(withText("Android Police Podcast")).check(matches(isDisplayed()))
    }

    @Test
    fun testSearchPodcasts_shouldShowSearchResults() {
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

    @Test
    fun testSearchPodcasts_noResults_shouldShowEmptyState() {
        onView(withId(R.id.action_search))
            .perform(click())
        onView(withId(R.id.search_src_text))
            .perform(click(), typeText("weird_string"), pressImeActionButton())

        onView(withId(R.id.empty_label)).check(matches(isDisplayed()))
    }
}