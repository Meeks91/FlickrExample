package com.example.micah.tigerspikeflickr.FlickrActivity.view.recylerView.viewHolders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import butterknife.BindView
import butterknife.ButterKnife
import com.example.micah.tigerspikeflickr.R

class TaggedImageRVViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    @BindView(R.id.flickrImageIV) lateinit var flickrImageIV: ImageView
    @BindView(R.id.progressBar) lateinit var progressBar: ProgressBar

    init {

        ButterKnife.bind(this, itemView)
    }
}