package com.example.micah.tigerspikeflickr.dagger

import com.example.micah.tigerspikeflickr.FlickrActivity.view.activity.FlickrActivity
import com.example.micah.tigerspikeflickr.dagger.modules.FlickrActivityModule
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Micah on 02/12/2017.
 */
object DaggerInjector {

    private val appComponent = DaggerAppComponent.builder()

    fun configureInjectionFor(flickrActivity: FlickrActivity, compositeDisposable: CompositeDisposable): AppComponent =

        appComponent.flickrActivityModule(FlickrActivityModule(flickrActivity, compositeDisposable)).build()
}