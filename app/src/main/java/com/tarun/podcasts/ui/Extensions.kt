package com.tarun.podcasts.ui

import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * An extension function for loading an image into an [ImageView] from a URL.
 *
 * @param imageUrl: The URL to fetch the image from.
 */
fun ImageView.loadImageFromUrl(imageUrl: String) {
    Glide.with(context)
        .load(imageUrl)
        .override(this.layoutParams.width, this.layoutParams.height)
        .into(this)
}