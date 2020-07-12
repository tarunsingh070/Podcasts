package com.tarun.podcasts.data.model

import com.google.gson.annotations.SerializedName

/**
 * Model class for storing the result object containing the list of Podcasts received from API.
 */
class PodcastsListResult {
    var resultCount: Int = 0

    @SerializedName("results")
    lateinit var podcasts: ArrayList<Podcast>
}