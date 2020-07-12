package com.tarun.podcasts.ui.podcastsList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.tarun.podcasts.R
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
        observePodcastList()
    }

    /**
     * Sets up the recycler view for showing the Podcasts list.
     */
    private fun setupRecyclerView() {
        val columnCount = resources.getInteger(R.integer.podcast_list_column_count)

        binding.podcastListRecyclerView.adapter = adapter
        binding.podcastListRecyclerView.layoutManager = GridLayoutManager(context, columnCount)

        val itemSpacing: Int =
            resources.getDimension(R.dimen.podcast_item_grid_spacing).toInt()
        binding.podcastListRecyclerView.addItemDecoration(
            PodcastItemDecoration(
                columnCount,
                itemSpacing
            )
        )
    }

    /**
     * Observes the list of podcasts and take actions when changes are detected.
     */
    private fun observePodcastList() {
        viewModel.getPodcastList().observe(viewLifecycleOwner, Observer {
            adapter.setPodcasts(it)
            adapter.notifyDataSetChanged()
        })
    }
}