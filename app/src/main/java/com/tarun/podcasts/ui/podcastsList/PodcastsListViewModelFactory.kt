package com.tarun.podcasts.ui.podcastsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tarun.podcasts.data.remote.PodcastRepository
import com.tarun.podcasts.schedulers.SchedulerProvider

/**
 * Factory class for [PodcastsListViewModel].
 */
class PodcastsListViewModelFactory(private val schedulerProvider: SchedulerProvider,
                                   private val podcastRepository: PodcastRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PodcastsListViewModel::class.java)) {
            return PodcastsListViewModel(schedulerProvider, podcastRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}