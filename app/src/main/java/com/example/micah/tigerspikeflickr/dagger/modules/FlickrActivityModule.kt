package com.example.micah.tigerspikeflickr.dagger.modules

import com.example.micah.tigerspikeflickr.FlickrActivity.view.activity.FlickrActivityDelegate
import com.example.micah.tigerspikeflickr.FlickrActivity.view.activity.FlickrApiHelper
import com.example.micah.tigerspikeflickr.FlickrActivity.view.activity.FlickrPresenter
import dagger.Module
import dagger.Provides

/**
 * Created by Micah on 02/12/2017.
 */

@Module (includes = arrayOf(NetworkingModule::class))
class  FlickrActivityModule(private val delegate: FlickrActivityDelegate) {



    @Provides
    fun provideFlickrPresenter(flickrApiHelper: FlickrApiHelper): FlickrPresenter =

            FlickrPresenter(flickrApiHelper, delegate)
}