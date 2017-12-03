package com.example.micah.tigerspikeflickr.dagger.modules

import com.example.micah.tigerspikeflickr.FlickrActivity.FlickrPresenter
import com.example.micah.tigerspikeflickr.FlickrActivity.model.dataModels.api.FlickrApiHelper
import com.example.micah.tigerspikeflickr.FlickrActivity.view.activity.FlickrActivityDelegate
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Micah on 02/12/2017.
 */

/**
 * provides objects for the FlickrActivity
 */
@Module (includes = arrayOf(NetworkingModule::class))
class  FlickrActivityModule(private val delegate: FlickrActivityDelegate, private val compositeDisposable: CompositeDisposable) {

    @Provides
    fun provideFlickrPresenter(flickrApiHelper: FlickrApiHelper): FlickrPresenter =

            FlickrPresenter(flickrApiHelper, delegate, compositeDisposable)
}