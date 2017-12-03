package com.example.micah.tigerspikeflickr.FlickrActivity.model.dataModels

import com.example.micah.rxRecyclerViewArrayListAdaper.RxRecyclerViewArrayList
import com.google.gson.annotations.SerializedName

/**
 * Model for the FlickrApiResponse returned when getting images from the public images feed
 */
data class FlickrApiResponse(@SerializedName("items") val flickrModelsArrayList: RxRecyclerViewArrayList<FlickrImageModel>)