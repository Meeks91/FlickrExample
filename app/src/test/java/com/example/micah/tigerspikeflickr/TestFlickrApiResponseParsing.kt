package com.example.micah.tigerspikeflickr

import com.example.micah.tigerspikeflickr.FlickrActivity.model.dataModels.FlickrApiResponse
import com.example.micah.tigerspikeflickr.GlobalModels.api.GsonParser
import com.example.micah.tigerspikeflickr.MockData.MockData
import com.example.micah.tigerspikeflickr.MockData.MockData.testTagOne
import com.example.micah.tigerspikeflickr.MockData.MockData.testTagTwo
import com.example.micah.tigerspikeflickr.MockData.MockData.testTitleOne
import com.example.micah.tigerspikeflickr.MockData.MockData.testTitleTwo
import com.example.micah.tigerspikeflickr.MockData.MockData.testURLOne
import com.example.micah.tigerspikeflickr.MockData.MockData.testURLeTwo
import com.google.gson.reflect.TypeToken
import org.junit.Assert
import org.junit.Test

/**
 * Example local unit testNoTaggedImagesAlert, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestFlickrApiResponseParsing {

    /**
     * tests the parsed flickrResponseJson and makes
     * sure the parsed values are correct
     */
    @Test
    fun testResponseParsing() {

        val type = object : TypeToken<FlickrApiResponse>() {}.type

        val flickrApiResponse = GsonParser.parse<FlickrApiResponse>(MockData.flickrResponseJson, type)

        //ensure correct number of images parsed
        Assert.assertTrue("Wrong number of images parsed", flickrApiResponse.flickrModelsArrayList.size == 2)

        //testNoTaggedImagesAlert parsed titles:
        Assert.assertTrue("Wrong title one", flickrApiResponse.flickrModelsArrayList.first().title == testTitleOne)
        Assert.assertTrue("Wrong title two", flickrApiResponse.flickrModelsArrayList[1].title == testTitleTwo)

        //testNoTaggedImagesAlert parsed image URLS:
        Assert.assertTrue("Wrong image URL one", flickrApiResponse.flickrModelsArrayList.first().imageUrl == testURLOne)
        Assert.assertTrue("Wrong image URL two", flickrApiResponse.flickrModelsArrayList[1].imageUrl == testURLeTwo)

        //testNoTaggedImagesAlert parsed tags:
        Assert.assertTrue("Wrong tag one", flickrApiResponse.flickrModelsArrayList.first().searchTag == testTagOne)
        Assert.assertTrue("Wrong tag two", flickrApiResponse.flickrModelsArrayList[1].searchTag == testTagTwo)
    }
}