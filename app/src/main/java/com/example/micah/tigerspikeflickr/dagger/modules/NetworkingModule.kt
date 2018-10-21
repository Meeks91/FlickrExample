package com.example.micah.tigerspikeflickr.dagger.modules

import com.example.micah.tigerspikeflickr.FlickrActivity.model.api.FlickrApiDelegate
import com.example.micah.tigerspikeflickr.FlickrActivity.model.api.FlickrApiHelper
import dagger.Module
import dagger.Provides

/**
 * Created by Micah on 02/12/2017.
 *
 * Provides networking related objects
 */
@Module
class NetworkingModule {

    @Provides
     fun provideApiHelper(): FlickrApiDelegate = FlickrApiHelper()
}