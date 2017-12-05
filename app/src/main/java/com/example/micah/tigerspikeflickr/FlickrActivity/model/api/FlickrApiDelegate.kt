package com.example.micah.tigerspikeflickr.FlickrActivity.model.api

import com.example.micah.tigerspikeflickr.FlickrActivity.model.dataModels.FlickrApiResponse
import io.reactivex.Single

/**
 * defines the required methods of the FlickrApiHelper
 */
interface FlickrApiDelegate{

    fun getAllImages(): Single<FlickrApiResponse>
    fun getImagesWith(tag: String): Single<FlickrApiResponse>
}