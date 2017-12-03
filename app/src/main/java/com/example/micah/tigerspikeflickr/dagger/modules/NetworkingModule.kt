package com.example.micah.tigerspikeflickr.dagger.modules

import com.example.micah.tigerspikeflickr.FlickrActivity.model.dataModels.api.FlickrApiHelper
import dagger.Module
import dagger.Provides

/**
 * Created by Micah on 02/12/2017.
 * Provides networking related objects
 */

@Module
class NetworkingModule {

    @Provides
    fun provideApiHelper(): FlickrApiHelper = FlickrApiHelper()
}