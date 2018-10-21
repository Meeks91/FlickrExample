package com.example.micah.tigerspikeflickr.FlickrActivity.view.recylerView.viewHolders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.micah.tigerspikeflickr.globalModels.RxBus.BusEvent
import com.example.micah.tigerspikeflickr.globalModels.RxBus.EventType
import com.example.micah.tigerspikeflickr.globalModels.RxBus.RxBus
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
     * more images with the model at this index's tag
     */
    @OnClick((R.id.seeMoreButton))
     fun onClick() {

        RxBus.bus.onNext(BusEvent((EventType.retrieveNewTag), adapterPosition))
    }
}