package com.tarun.podcasts.ui.podcastsList

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tarun.podcasts.data.model.Podcast
import com.tarun.podcasts.data.model.PodcastsListResult
import com.tarun.podcasts.data.remote.PodcastRepository
import com.tarun.podcasts.schedulers.SchedulerProviderManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * The view model class for the [PodcastsListFragment] view.
 */
class PodcastsListViewModel : ViewModel() {
    companion object {
        private const val TAG = "PodcastsListViewModel"
    }

    private val podcasts: MutableLiveData<ArrayList<Podcast>> = MutableLiveData()
    private val disposables = CompositeDisposable()
    private val schedulerProvider = SchedulerProviderManager
    private val podcastRepository = PodcastRepository.newInstance()

    /**
     * Fetches and returns the list of podcasts.
     */
    fun getPodcastList(searchTerm: String): MutableLiveData<ArrayList<Podcast>> {
        disposables.add(
            podcastRepository
                .getPodcasts(searchTerm)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : DisposableSingleObserver<PodcastsListResult>() {
                    override fun onSuccess(t: PodcastsListResult?) {
                        podcasts.value = t?.podcasts
                    }

                    override fun onError(e: Throwable?) {
                        Log.e(TAG, "Error fetching podcasts list.", e)
                    }
                })
        )

        return podcasts
    }
}