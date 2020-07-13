package com.tarun.podcasts.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tarun.podcasts.R
import com.tarun.podcasts.data.remote.PodcastRepository
import com.tarun.podcasts.databinding.ActivityMainBinding
import com.tarun.podcasts.schedulers.SchedulerProviderManager
import com.tarun.podcasts.ui.podcastsList.PodcastsListFragment
import com.tarun.podcasts.ui.podcastsList.PodcastsListViewModel
import com.tarun.podcasts.ui.podcastsList.PodcastsListViewModelFactory
import com.tarun.podcasts.ui.setVisibility

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: PodcastsListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this, PodcastsListViewModelFactory(
                SchedulerProviderManager,
                PodcastRepository.instance
            )
        )
            .get(PodcastsListViewModel::class.java)

        attachPodcastsListFragment()
        observeLoadingState()
        observeErrorMessage()
    }

    /**
     * Attaches the [PodcastsListFragment]
     */
    private fun attachPodcastsListFragment() {
        replaceFragment(PodcastsListFragment.TAG)
    }

    /**
     * Replaces the fragment based on the Fragment tag passed in.
     *
     * @param fragmentTag The tag of the fragment to be replaced.
     * @param args        The arguments to be sent to the fragment being replaced.
     */
    private fun replaceFragment(fragmentTag: String, args: Bundle? = null) {
        val manager = supportFragmentManager
        val prev = manager.findFragmentByTag(fragmentTag)

        // If current fragment already exists, do nothing.
        if (prev != null) {
            return
        }

        val fragment: Fragment = when (fragmentTag) {
            PodcastsListFragment.TAG -> PodcastsListFragment.newInstance()
            else -> throw IllegalArgumentException("Unexpected fragment tag received: $fragmentTag")
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_frame, fragment, fragmentTag).commit()
    }

    /**
     * Observes the loading state and shows/hides a loader based on that.
     */
    private fun observeLoadingState() {
        viewModel.isLoading.observe(this, Observer {
            binding.mainActivityProgress.setVisibility(it)
        })
    }

    /**
     * Observes and shows the error message as a Toast to the user.
     */
    private fun observeErrorMessage() {
        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }
}