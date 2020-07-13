package com.tarun.podcasts.data.model

import com.google.gson.annotations.SerializedName

/**
 * Model class for storing a Podcast object.
 */
class Podcast {
    lateinit var artistName: String
    lateinit var collectionName: String

    @SerializedName("primaryGenreName")
    lateinit var genre: String

    @SerializedName("artworkUrl600")
    lateinit var artworkUrl: String
}