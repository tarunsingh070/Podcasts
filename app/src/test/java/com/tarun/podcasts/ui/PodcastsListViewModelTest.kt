package com.tarun.podcasts.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tarun.podcasts.R
import com.tarun.podcasts.data.model.Podcast
import com.tarun.podcasts.data.model.PodcastsListResult
import com.tarun.podcasts.data.remote.PodcastRepository
import com.tarun.podcasts.schedulers.TestSchedulerProviderManager
import com.tarun.podcasts.ui.podcastsList.PodcastsListViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.lang.Exception

@RunWith(JUnit4::class)
class PodcastsListViewModelTest {
    @Rule
    @JvmField
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val test = "Android"

    @Mock
    private lateinit var mockPodcastRepository: PodcastRepository

    private lateinit var testSchedulerProviderManager: TestSchedulerProviderManager
    private lateinit var viewModel: PodcastsListViewModel
    private lateinit var podcastsList: ArrayList<Podcast>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        testSchedulerProviderManager = TestSchedulerProviderManager(TestScheduler())

        val podcast1 = Mockito.mock(Podcast::class.java)
        val podcast2 = Mockito.mock(Podcast::class.java)
        val podcast3 = Mockito.mock(Podcast::class.java)
        podcastsList = arrayListOf(podcast1, podcast2, podcast3)

        val podcastListReturn: PodcastsListResult = Mockito.mock(PodcastsListResult::class.java)
        Mockito.`when`(podcastListReturn.podcasts).thenReturn(podcastsList)
        Mockito.`when`(mockPodcastRepository.getPodcasts(test))
            .thenReturn(Single.just(podcastListReturn))

        viewModel = PodcastsListViewModel(testSchedulerProviderManager, mockPodcastRepository)
    }

    @Test
    fun testOnViewCreated_existingPodcastListIsEmpty_shouldGetPodcasts() {
        viewModel.onViewCreated()
        Mockito.verify(mockPodcastRepository).getPodcasts("Android")
    }

    @Test
    fun testOnViewCreated_existingPodcastListIsNotEmpty_shouldNotGetPodcasts() {
        viewModel.setPodcasts(podcastsList)
        viewModel.onViewCreated()
        Mockito.verify(mockPodcastRepository, Mockito.never()).getPodcasts("Android")
    }

    @Test
    fun testOnQuerySubmitted_distinctQuery_shouldGetPodcastsList() {
        viewModel.onQuerySubmitted(test)

        testSchedulerProviderManager.testScheduler.triggerActions()
        Mockito.verify(mockPodcastRepository).getPodcasts(test)
        Assert.assertEquals(3, viewModel.podcasts.value?.size)
    }

    @Test
    fun testOnQuerySubmitted_sameQuery_shouldGetPodcastsList() {
        viewModel.setQuery(test)
        viewModel.onQuerySubmitted(test)
        Mockito.verify(mockPodcastRepository, Mockito.never()).getPodcasts(test)
    }

    @Test
    fun testOnQuerySubmitted_getPodcastsListCallFails_shouldHandleError() {
        Mockito.`when`(mockPodcastRepository.getPodcasts(test))
            .thenReturn(Single.error(Exception()))

        viewModel.onQuerySubmitted(test)

        testSchedulerProviderManager.testScheduler.triggerActions()
        Mockito.verify(mockPodcastRepository).getPodcasts(test)
        Assert.assertEquals(R.string.error_message, viewModel.errorMessage.value)
    }
}