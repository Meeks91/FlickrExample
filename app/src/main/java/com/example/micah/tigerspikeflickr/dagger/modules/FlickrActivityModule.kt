package com.example.micah.tigerspikeflickr.dagger.modules

import com.example.micah.tigerspikeflickr.FlickrActivity.FlickrPresenter
import com.example.micah.tigerspikeflickr.FlickrActivity.model.api.FlickrApiDelegate
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
     fun provideFlickrPresenter(flickrApiHelper: FlickrApiDelegate): FlickrPresenter =

            FlickrPresenter(flickrApiHelper, delegate, compositeDisposable)

}