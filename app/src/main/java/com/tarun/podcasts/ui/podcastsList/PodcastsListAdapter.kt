package com.tarun.podcasts.ui.podcastsList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tarun.podcasts.databinding.ItemPodcastBinding

/**
 * The adapter class for showing list of Podcasts.
 */
class PodcastsListAdapter : RecyclerView.Adapter<PodcastsListAdapter.ViewHolder>() {

    private var podcasts: ArrayList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemPodcastBinding =
            ItemPodcastBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        return ViewHolder(
            itemPodcastBinding
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.podcast = podcasts[position]
        holder.binding.collectionName.text = holder.podcast
    }

    override fun getItemCount(): Int {
        return podcasts.size
    }

    /**
     * Sets the list of podcasts to be shown with the updated list received.
     *
     * @param updatedPodcasts The updated list of podcasts received.
     */
    fun setPodcasts(updatedPodcasts: ArrayList<String>) {
        podcasts = updatedPodcasts
    }

    /**
     * View holder class for [PodcastsListAdapter]
     */
    inner class ViewHolder(val binding: ItemPodcastBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        lateinit var podcast: String
        override fun onClick(v: View) {
            // Todo: Open Podcast details fragment.
        }
    }
}