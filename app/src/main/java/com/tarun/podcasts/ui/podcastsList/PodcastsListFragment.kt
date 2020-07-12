package com.tarun.podcasts.ui.podcastsList

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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
        displayPodcastList()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.podcasts_list_fragment_menu, menu)

        val searchViewMenuItem = menu.findItem(R.id.action_search)
        val searchView =
            searchViewMenuItem.actionView as SearchView
        searchView.queryHint = getString(R.string.search_podcasts)
        searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    displayPodcastList(query)
                }
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })


        super.onCreateOptionsMenu(menu, inflater)
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
     * Fetches the list of podcasts and updates the UI when changes are detected.
     */
    private fun displayPodcastList(searchTerm: String = "Android") {
        viewModel.getPodcastList(searchTerm).observe(viewLifecycleOwner, Observer {
            adapter.setPodcasts(it)
            adapter.notifyDataSetChanged()
            binding.emptyLabel.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        })
    }
}