package com.example.micah.tigerspikeflickr.dagger

import com.example.micah.tigerspikeflickr.FlickrActivity.view.activity.FlickrActivity
import com.example.micah.tigerspikeflickr.dagger.modules.FlickrActivityModule
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Micah on 02/12/2017.
 *
 * used to abstract app component building away
 * from the FlickrActivity
 */
object DaggerInjector {

    private val appComponentBuilder = DaggerAppComponent.builder()

    /**
     * configures the app components to inject
     * dependencies into the specified [flickrActivity]
     * and returns the built AppComponent
     */
    fun configureInjectionFor(flickrActivity: FlickrActivity, compositeDisposable: CompositeDisposable): AppComponent =

            appComponentBuilder.flickrActivityModule(FlickrActivityModule(flickrActivity, compositeDisposable)).build()
}