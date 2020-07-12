package com.tarun.podcasts.ui.podcastsList

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tarun.podcasts.data.model.Podcast
import com.tarun.podcasts.data.model.PodcastsListResult
import com.tarun.podcasts.data.remote.PodcastRepository
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

    val podcasts: MutableLiveData<ArrayList<Podcast>> = MutableLiveData()
    private val disposables = CompositeDisposable()

    private val podcastRepository = PodcastRepository.newInstance()

    /**
     * Fetches and returns the list of podcasts.
     */
    fun getPodcastList(): MutableLiveData<ArrayList<Podcast>> {
        disposables.add(
            podcastRepository
                // FixMe: replace this hardcode search term if you end up adding search bar.
                .getPodcasts("Android")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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