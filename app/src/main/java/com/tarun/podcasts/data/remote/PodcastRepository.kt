package com.tarun.podcasts.data.remote

import com.tarun.podcasts.data.model.PodcastsListResult
import io.reactivex.rxjava3.core.Single

/**
 * The repository class used to make API calls to fetch data from web server.
 */
class PodcastRepository {
    companion object {
        private const val TAG = "PodcastRepository"
        fun newInstance() = PodcastRepository()
    }

    private val apiService = ApiService.Creator.createTeamService()

    /**
     * Gets the list of podcasts.
     */
    fun getPodcasts(searchTerm: String): Single<PodcastsListResult> {
        return apiService.searchPodcasts(searchTerm)
    }
}