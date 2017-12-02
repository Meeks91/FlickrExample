package com.example.micah.tigerspikeflickr.dagger

import com.example.micah.tigerspikeflickr.FlickrActivity.view.activity.FlickrActivity
import com.example.micah.tigerspikeflickr.dagger.modules.FlickrActivityModule
import com.example.micah.tigerspikeflickr.dagger.modules.NetworkingModule
import dagger.Component

/**
 * Created by Micah on 02/12/2017.
 */

@Component (modules = arrayOf(NetworkingModule::class, FlickrActivityModule::class))
interface AppComponent {

    fun inject(flickrActivity: FlickrActivity)
}