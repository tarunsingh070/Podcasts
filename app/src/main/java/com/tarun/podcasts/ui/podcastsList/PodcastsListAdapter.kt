package com.tarun.podcasts.ui.podcastsList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tarun.podcasts.R
import com.tarun.podcasts.data.model.Podcast
import com.tarun.podcasts.databinding.ItemPodcastBinding
import com.tarun.podcasts.ui.loadImageFromUrl

/**
 * The adapter class for showing list of Podcasts.
 */
class PodcastsListAdapter : RecyclerView.Adapter<PodcastsListAdapter.ViewHolder>() {

    private var podcasts: ArrayList<Podcast> = ArrayList()

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
        val context = holder.itemView.context
        holder.podcast = podcasts[position]
        holder.binding.artistName.text = holder.podcast.artistName
        holder.binding.collectionName.text = holder.podcast.collectionName
        holder.binding.genre.text = context.getString(R.string.genre_name, holder.podcast.genre)
        holder.binding.artworkImageView.loadImageFromUrl(holder.podcast.artworkUrl)
    }

    override fun getItemCount(): Int {
        return podcasts.size
    }

    /**
     * Sets the list of podcasts to be shown with the updated list received.
     *
     * @param updatedPodcasts The updated list of podcasts received.
     */
    fun setPodcasts(updatedPodcasts: ArrayList<Podcast>) {
        podcasts = updatedPodcasts
    }

    /**
     * View holder class for [PodcastsListAdapter]
     *
     * @param binding: The instance of [ItemPodcastBinding] for this instance of holder.
     */
    inner class ViewHolder(val binding: ItemPodcastBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        lateinit var podcast: Podcast
        override fun onClick(v: View) {
            // Todo: Open Podcast details fragment.
        }
    }
}