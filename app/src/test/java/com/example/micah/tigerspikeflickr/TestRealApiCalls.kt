package com.example.micah.tigerspikeflickr.MockData

import com.example.micah.tigerspikeflickr.FlickrActivity.model.dataModels.api.FlickrApiHelper
import org.junit.Assert
import org.junit.Test

/**
 * Created by Micah on 03/12/2017.
 */


class TestRealApiCalls {

    private var foundDetailedImagesCount = 0
    private var foundTaggedImagesCount = 0
    private var detailedApiCallErrorMessage = ""
    private var taggedApiCallErrorMessage = ""

    /**
     * tests real API call for getting images
     */
    @Test
    fun testAllImagesApiCall() {

        FlickrApiHelper().getAllImages().subscribe(

                //success
                { foundDetailedImagesCount = it.flickrModelsArrayList.size },

                //failure
                { detailedApiCallErrorMessage = it.message!! })

        Thread.sleep(2000)

        Assert.assertTrue("testAllImagesApiCall no images returned and error is: ${detailedApiCallErrorMessage}", foundDetailedImagesCount > 0)
    }

    /**
     * tests real API call for getting tagged images
     */
    @Test
    fun testTaggedImagesApiCall() {

        FlickrApiHelper().getImagesWith("TEST").subscribe(

                //success
                { foundTaggedImagesCount = it.flickrModelsArrayList.size },

                //failure
                { taggedApiCallErrorMessage = it.message!! })

        Thread.sleep(2000)

        Assert.assertTrue("testTaggedImagesApiCall no images returned and error is: ${taggedApiCallErrorMessage}", foundTaggedImagesCount > 0)
    }
}