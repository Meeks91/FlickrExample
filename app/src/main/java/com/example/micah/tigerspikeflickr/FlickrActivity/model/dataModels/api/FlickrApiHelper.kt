package com.example.micah.tigerspikeflickr.FlickrActivity.model.dataModels.api

import com.example.micah.tigerspikeflickr.FlickrActivity.model.dataModels.FlickrApiResponse
import com.example.micah.tigerspikeflickr.GlobalModels.api.ApiHelper
import com.google.gson.reflect.TypeToken
import io.reactivex.Single

/**
 * provides a wrapper over the ApiHelper
 * to make requests to the Flickr public images feed.
 */
class FlickrApiHelper {

    private val flickrModelType = object: TypeToken<FlickrApiResponse>(){}.type

    /**
     * retrieves all available images from Flickr without applying a
     * filter and returns the FlickrApiResponse in a Single
     */
    fun getAllImages(): Single<FlickrApiResponse> =

            ApiHelper.request<FlickrApiResponse>(flickrModelType, "")

    /**
     * retrieves images from Flickr that match the
     * specified tag and returns the result in a Single
     */
    fun getImagesWith(tag: String): Single<FlickrApiResponse> =

            ApiHelper.request<FlickrApiResponse>(flickrModelType, "&tags=$tag")
}