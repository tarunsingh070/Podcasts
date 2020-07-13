package com.tarun.podcasts.data.remote

import com.tarun.podcasts.data.model.PodcastsListResult
import io.reactivex.rxjava3.core.Single
import okhttp3.OkHttpClient
import org.jetbrains.annotations.TestOnly
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

/**
 * The API service interface containing all required endpoints to the iTunes API.
 */
interface ApiService {
    @GET("search?media=podcast&country=ca")
    fun searchPodcasts(@Query("term") searchTerm: String?): Single<PodcastsListResult>

    object Creator {
        private var BASE_URL = "https://itunes.apple.com/"

        /**
         * Create an instance of [ApiService].
         *
         * @return An instance of [ApiService]
         */
        fun createApiService(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(createHttpClient())
                .build()
            return retrofit.create(ApiService::class.java)
        }

        /**
         * Creates and configures an instance of OkHttpClient.
         *
         * @return An instance of [OkHttpClient]
         */
        private fun createHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
        }

        @TestOnly
        fun updateBaseUrl(newBaseUrl: String) {
            BASE_URL = newBaseUrl
        }
    }
}