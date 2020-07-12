package com.tarun.podcasts.ui.podcastsList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tarun.podcasts.databinding.PodcastsListFragmentBinding

/**
 * Fragment showing the list of podcasts.
 */
class PodcastsListFragment : Fragment() {

    companion object {
        const val TAG = "PodcastsListFragment"
        fun newInstance() =
            PodcastsListFragment()
    }

    private lateinit var adapter: PodcastsListAdapter
    private lateinit var binding: PodcastsListFragmentBinding
    private lateinit var viewModel: PodcastsListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PodcastsListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PodcastsListViewModel::class.java)
        adapter = PodcastsListAdapter()
        setupRecyclerView()
    }

    /**
     * Sets up the recycler view for showing the Podcasts list.
     */
    private fun setupRecyclerView() {
        binding.podcastListRecyclerView.adapter = adapter
        // Todo: Replace with list of actual podcast objects later when logic to fetch Podcasts
        //  from iTunes Web API is implemented.
        adapter.setPodcasts(arrayListOf("Podcast 1", "Podcast 2", "Podcast 3"))
    }

}