package com.tarun.podcasts.ui

import com.tarun.podcasts.data.model.Podcast
import com.tarun.podcasts.data.model.PodcastsListResult
import com.tarun.podcasts.data.remote.PodcastRepository
import com.tarun.podcasts.schedulers.TestSchedulerProviderManager
import com.tarun.podcasts.ui.podcastsList.PodcastsListViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@RunWith(JUnit4::class)
class PodcastsListViewModelTest {

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    private val test = "test"

    @Mock
    private lateinit var mockPodcastRepository: PodcastRepository

    private lateinit var testSchedulerProviderManager: TestSchedulerProviderManager
    private lateinit var viewModel: PodcastsListViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        testSchedulerProviderManager = TestSchedulerProviderManager(TestScheduler())

        val podcast1 = Mockito.mock(Podcast::class.java)
        val podcast2 = Mockito.mock(Podcast::class.java)
        val podcast3 = Mockito.mock(Podcast::class.java)
        val podcastsList: ArrayList<Podcast> = arrayListOf(podcast1, podcast2, podcast3)

        val podcastListReturn: PodcastsListResult = Mockito.mock(PodcastsListResult::class.java)
        Mockito.`when`(podcastListReturn.podcasts).thenReturn(podcastsList)
        Mockito.`when`(mockPodcastRepository.getPodcasts(test)).thenReturn(Single.just(podcastListReturn))

        viewModel = PodcastsListViewModel(testSchedulerProviderManager, mockPodcastRepository)
    }

    @Test
    fun onQuerySubmitted_distinctQuery_shouldGetPodcastsList() {
        viewModel.onQuerySubmitted(test)
        Mockito.verify(mockPodcastRepository).getPodcasts(test)
    }

    @Test
    fun onQuerySubmitted_sameQuery_shouldGetPodcastsList() {
        viewModel.setQuery(test)
        viewModel.onQuerySubmitted(test)
        Mockito.verify(mockPodcastRepository, Mockito.never()).getPodcasts(test)
    }
}