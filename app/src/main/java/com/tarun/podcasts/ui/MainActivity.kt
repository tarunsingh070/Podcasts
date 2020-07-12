package com.tarun.podcasts.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tarun.podcasts.R
import com.tarun.podcasts.databinding.ActivityMainBinding
import com.tarun.podcasts.ui.podcastsList.PodcastsListFragment
import java.lang.IllegalArgumentException

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        attachPodcastsListFragment()
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
        val fragment: Fragment = when (fragmentTag) {
            PodcastsListFragment.TAG -> PodcastsListFragment.newInstance()
            else -> throw IllegalArgumentException("Unexpected fragment tag received: $fragmentTag")
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_frame, fragment, fragmentTag).commit()
    }
}