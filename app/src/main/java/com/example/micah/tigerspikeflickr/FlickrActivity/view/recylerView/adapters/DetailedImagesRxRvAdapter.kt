package com.example.micah.tigerspikeflickr.FlickrActivity.view.recylerView.adapters

import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.micah.rxRecyclerViewArrayListAdaper.LayoutConfig
import com.example.micah.rxRecyclerViewArrayListAdaper.Orientation
import com.example.micah.rxRecyclerViewArrayListAdaper.RowType
import com.example.micah.rxRecyclerViewArrayListAdaper.RxRecyclerViewArrayList
import com.example.micah.tigerspikeflickr.FlickrActivity.model.dataModels.FlickrImageModel
import com.example.micah.tigerspikeflickr.FlickrActivity.view.recylerView.viewHolders.DetailedImageRVViewHolder
import com.example.micah.tigerspikeflickr.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

/**
 * implements the RxRecyclerViewArrayList and imagesRV for the detailed images tab
 */
class DetailedImagesRxRvAdapter(private val imagesRxArrayList: RxRecyclerViewArrayList<FlickrImageModel>, private val compositeDisposable: CompositeDisposable): FlickrImagesAdapter(compositeDisposable){

    /**
     * binds the global [imagesRxArrayList] to the
     * specified [imagesRV] and handles configuring
     * the layout of the [imagesRV]'s rows
     */
    override fun bindTo(imagesRV: RecyclerView) {

        imagesRxArrayList.bind<DetailedImageRVViewHolder>(imagesRV, R.layout.detailed_image_item, LayoutConfig(Orientation.vertical, RowType.staggered, 2)) { vh, imageModel ->

            //load image:
            Glide.with(vh.itemView.context).load(imageModel.imageUrl).apply(glideOptions).listener(object: RequestListener<Drawable> {

                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {

                    //set to GONE to save resources
                    vh.progressBar.visibility = View.GONE

                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                    //set to GONE to save resources
                    vh.progressBar.visibility = View.GONE

                    return false
                }

            }).into(vh.flickrImageIV)

            //assign row text information:
            vh.tagsTV.text = "Tags: ${imageModel.tags}"
            vh.titleTV.text ="Title: ${imageModel.title}"

        }.addTo(compositeDisposable)
    }
}