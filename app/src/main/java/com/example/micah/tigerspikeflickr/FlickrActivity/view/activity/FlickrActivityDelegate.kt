package com.example.micah.tigerspikeflickr.FlickrActivity.view.activity

/**
 * Created by Micah on 02/12/2017.
 *
 * defines the required delegate method of the FlickrActivity
 */
interface FlickrActivityDelegate {
    fun selectTabAt(index: Int)
    fun displayAlert(message: String)
}
