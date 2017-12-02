package com.example.micah.tigerspikeflickr.dagger.modules

import com.example.micah.tigerspikeflickr.FlickrActivity.view.activity.FlickrApiHelper
import dagger.Module
import dagger.Provides

/**
 * Created by Micah on 02/12/2017.
 */

@Module
class NetworkingModule {

    @Provides
    fun provideApiHelper(): FlickrApiHelper = FlickrApiHelper()

//    @Provides
//    fun provideOkhttpClient(): OkHttpClient{
//
//        val okHttpClient = OkHttpClient()
//
//        okHttpClient. = true
//
//    }
}