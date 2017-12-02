package com.example.micah.tigerspikeflickr.dagger

import com.example.micah.tigerspikeflickr.FlickrActivity.view.activity.FlickrActivity
import com.example.micah.tigerspikeflickr.dagger.modules.FlickrActivityModule

/**
 * Created by Micah on 02/12/2017.
 */
object DaggerInjector {

    private val appComponent = DaggerAppComponent.builder()

    fun configureInjectionFor(flickrActivity: FlickrActivity): AppComponent =

        appComponent.flickrActivityModule(FlickrActivityModule(flickrActivity)).build()

//    fun inject(flickrActivity: FlickrActivity){
//
//        appComponent.inject(flickrActivity)
//    }
}