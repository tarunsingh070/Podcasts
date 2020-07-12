package com.tarun.podcasts.data.remote

import com.tarun.podcasts.data.model.PodcastsListResult
import io.reactivex.rxjava3.core.Single

/**
 * The repository class used to make API calls to fetch data from web server.
 */
class PodcastRepository private constructor(){
    companion object {
        private const val TAG = "PodcastRepository"
        val instance = PodcastRepository()
        private val apiService = ApiService.Creator.createApiService()
    }

    /**
     * Gets the list of podcasts.
     *
     * @param searchTerm The search term for which to fetch the list of podcasts.
     */
    fun getPodcasts(searchTerm: String): Single<PodcastsListResult> {
        return apiService.searchPodcasts(searchTerm)
    }
}