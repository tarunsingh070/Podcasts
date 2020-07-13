package com.tarun.podcasts.ui.podcastsList

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.tarun.podcasts.R
import com.tarun.podcasts.data.remote.PodcastRepository
import com.tarun.podcasts.databinding.PodcastsListFragmentBinding
import com.tarun.podcasts.schedulers.SchedulerProviderManager
import com.tarun.podcasts.ui.setVisibility

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
        viewModel = ViewModelProvider(
            activity as AppCompatActivity,
            PodcastsListViewModelFactory(SchedulerProviderManager, PodcastRepository.instance)
        )
            .get(PodcastsListViewModel::class.java)
        adapter = PodcastsListAdapter()
        setupRecyclerView()
        observePodcastList()
        viewModel.onViewCreated()
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
                viewModel.onQuerySubmitted(query)
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })

        viewModel.searchTerm.observe(viewLifecycleOwner, Observer {
            // Restore the last saved search query text.
            searchViewMenuItem.expandActionView()
            searchView.setQuery(it, false)
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Sets up the recycler view for showing the Podcasts list.
     */
    private fun setupRecyclerView() {
        val columnCount = resources.getInteger(R.integer.podcasts_list_column_count)

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
     * Observes the list of podcasts and updates the UI when changes are detected.
     */
    private fun observePodcastList() {
        viewModel.podcasts.observe(viewLifecycleOwner, Observer {
            adapter.setPodcasts(it)
            adapter.notifyDataSetChanged()
            binding.emptyLabel.setVisibility(it.isEmpty())
        })
    }
}