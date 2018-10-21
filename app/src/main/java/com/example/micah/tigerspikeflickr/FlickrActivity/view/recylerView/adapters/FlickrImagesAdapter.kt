package com.example.micah.tigerspikeflickr.FlickrActivity.view.recylerView.adapters

import android.support.v7.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import io.reactivex.disposables.CompositeDisposable

/**
 * a base class for RxRecyclerViewArrayList based adapters
 */
abstract class FlickrImagesAdapter(private val compositeDisposable: CompositeDisposable) {

   lateinit var glideOptions: RequestOptions

     init {

         initGlideOpitons()
     }

    /**
     * inits the glide options to maximise
     * efficiency for infinite scrolling
     */
     private fun initGlideOpitons(){

         glideOptions = RequestOptions()
         glideOptions.diskCacheStrategy(DiskCacheStrategy.ALL).options
     }

    /**
     * abstract method subclasses have to implement
     * to bind the imagesRv to a RxRecyclerViewArrayList
     */
    abstract fun bindTo(imagesRV: RecyclerView)
}