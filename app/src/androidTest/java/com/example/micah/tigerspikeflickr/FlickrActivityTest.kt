package com.example.micah.tigerspikeflickr

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.example.micah.tigerspikeflickr.FlickrActivity.view.activity.FlickrActivity
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class FlickrActivityTest {

    @get: Rule val flickrActivity = ActivityTestRule(FlickrActivity::class.java)

    /**
     * ensures images are being download when the app is run
     */
    @Test
    fun testImagesAreBeingDownloaded() {

        Thread.sleep(2000)

        Assert.assertTrue(flickrActivity.activity.presenter.unTaggedImagesRxArrayList.size > 0)
        Assert.assertTrue(flickrActivity.activity.presenter.taggedImagesRxArrayList.size == 0)
    }
}



