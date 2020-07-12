package com.tarun.podcasts.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
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
        .placeholder(ColorDrawable(Color.DKGRAY))
        .override(this.layoutParams.width, this.layoutParams.height)
        .into(this)
}

/**
 * An extension function that sets a view to [View.VISIBLE] or [View.GONE] based on the boolean passed in.
 *
 * @param shouldShow Boolean indicating if the view should be made [View.VISIBLE] or [View.GONE].
 */
fun View.setVisibility(shouldShow: Boolean) {
    if (shouldShow) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}