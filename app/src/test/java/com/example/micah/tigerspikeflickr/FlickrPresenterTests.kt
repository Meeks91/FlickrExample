package com.example.micah.tigerspikeflickr

import com.example.micah.tigerspikeflickr.FlickrActivity.FlickrPresenter
import com.example.micah.tigerspikeflickr.FlickrActivity.model.api.FlickrApiHelper
import com.example.micah.tigerspikeflickr.FlickrActivity.view.activity.FlickrActivityDelegate
import io.reactivex.disposables.CompositeDisposable
import org.junit.Assert
import org.junit.Test

/**
 * Created by Micah on 03/12/2017.
 */
class FlickrPresenterTests {

    /**
     * tests that the alert notifying the user
     * that no tagged imags are available will show
     */
    @Test
    fun testNoTaggedImagesAlert(){

        val presenter = FlickrPresenter(FlickrApiHelper(), activityDelegate, CompositeDisposable())

        presenter.onTaggedImageTabSelected()
    }

    private val activityDelegate = object: FlickrActivityDelegate{
        override fun selectTabAt(index: Int) {

        }

        override fun displayAlert(message: String) {

            //check that the correct alert is being displayed
            Assert.assertTrue("No tagged images alert is failing to show" , message == "No tagged images to show :(")
        }
    }
}