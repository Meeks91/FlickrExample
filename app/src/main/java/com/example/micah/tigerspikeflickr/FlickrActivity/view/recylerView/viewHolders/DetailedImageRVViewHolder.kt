package com.example.micah.tigerspikeflickr.FlickrActivity.view.recylerView.viewHolders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.micah.tigerspikeflickr.GlobalModels.RxBus.BusEvent
import com.example.micah.tigerspikeflickr.GlobalModels.RxBus.EventType
import com.example.micah.tigerspikeflickr.GlobalModels.RxBus.RxBus
import com.example.micah.tigerspikeflickr.R

class DetailedImageRVViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    @BindView(R.id.titleTV) lateinit var titleTV: TextView
    @BindView(R.id.tagsTV) lateinit var tagsTV: TextView
    @BindView(R.id.flickrImageIV) lateinit var flickrImageIV: ImageView
    @BindView(R.id.progressBar) lateinit var progressBar: ProgressBar

    init {

        ButterKnife.bind(this, itemView)
    }

    /**
     * handles: broadcasting request to retrieve
     * images with a new tag from the Flickr API
     */
    @OnClick((R.id.seeMoreButton))
     fun onClick() {

        //include adapterPosition to retrieve the model at this index's tag
        RxBus.bus.onNext(BusEvent((EventType.retrieveNewTag), adapterPosition))
    }
}