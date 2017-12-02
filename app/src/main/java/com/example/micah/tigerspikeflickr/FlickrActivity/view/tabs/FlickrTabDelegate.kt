package com.example.micah.tigerspikeflickr.FlickrActivity.view.tabs

import com.example.micah.tigerspikeflickr.FlickrActivity.view.activity.FlickrModel

/**
 * Created by Micah on 02/12/2017.
 */

interface FlickrTabDelegate {

    fun load(flickrModelsArrayList: ArrayList<FlickrModel>)
}