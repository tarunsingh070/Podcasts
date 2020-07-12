package com.tarun.podcasts.ui.podcastsList

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tarun.podcasts.R
import com.tarun.podcasts.data.model.Podcast
import com.tarun.podcasts.data.model.PodcastsListResult
import com.tarun.podcasts.data.remote.PodcastRepository
import com.tarun.podcasts.schedulers.SchedulerProvider
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import org.jetbrains.annotations.TestOnly

/**
 * The view model class for the [PodcastsListFragment] view.
 */
class PodcastsListViewModel(
    private val schedulerProvider: SchedulerProvider,
    private val podcastRepository: PodcastRepository
) : ViewModel() {
    companion object {
        private const val TAG = "PodcastsListViewModel"
    }

    internal val podcasts: MutableLiveData<ArrayList<Podcast>> = MutableLiveData()
    private val disposables = CompositeDisposable()
    internal var searchTerm: MutableLiveData<String> = MutableLiveData()
    internal var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    internal var errorMessage: MutableLiveData<Int> = MutableLiveData()

    /**
     * Handles the event when onViewCreated method of [PodcastsListFragment] is called.
     */
    fun onViewCreated() {
        if (podcasts.value.isNullOrEmpty()) {
            getPodcastList()
        }
    }

    /**
     * Handles the event when user submits the search term.
     *
     * @param searchTerm The search term submitted by user.
     */
    fun onQuerySubmitted(searchTerm: String) {
        if (searchTerm.isEmpty() || searchTerm == this.searchTerm.value) {
            return
        }

        this.searchTerm.value = searchTerm
        getPodcastList(searchTerm)
    }

    @TestOnly
    fun setQuery(searchTerm: String) {
        this.searchTerm.value = searchTerm
    }

    @TestOnly
    fun setPodcasts(podcasts: ArrayList<Podcast>) {
        this.podcasts.value = podcasts
    }

    /**
     * Fetches and returns the list of podcasts.
     *
     * @param searchTerm The search term submitted by user.
     */
    fun getPodcastList(searchTerm: String = "Android"): MutableLiveData<ArrayList<Podcast>> {
        setLoadingState(true)
        disposables.add(
            podcastRepository
                .getPodcasts(searchTerm)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : DisposableSingleObserver<PodcastsListResult>() {
                    override fun onSuccess(t: PodcastsListResult?) {
                        setLoadingState(false)
                        podcasts.value = t?.podcasts
                    }

                    override fun onError(e: Throwable?) {
                        setLoadingState(false)
                        setErrorMessage(R.string.error_message)
                        Log.e(TAG, "Error fetching podcasts list.", e)
                    }
                })
        )

        return podcasts
    }

    /**
     * Sets the loading state value.
     *
     * @param shouldShow A boolean indicating if the loading state should be shown or hidden.
     */
    private fun setLoadingState(shouldShow: Boolean) {
        isLoading.value = shouldShow
    }

    /**
     * Sets the error message to be shown to the user.
     *
     * @param errorMessageResId The resource ID of the error message to be shown
     */
    private fun setErrorMessage(errorMessageResId: Int) {
        errorMessage.value = errorMessageResId
    }
}