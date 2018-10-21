package com.example.micah.tigerspikeflickr.GlobalModels.api

import com.google.gson.reflect.TypeToken
import junit.framework.Assert
import org.junit.Test

/**
 * Created by Micah on 03/12/2017.
 */
class ApiHelperTest {

    @Test
    fun testInvalidUrl() {

//        Assert.assertTrue(true == true)

        val type = object : TypeToken<Any>() {}.type

        ApiHelper.request<Any>(type, "--   ---   ---").subscribe({}, {

            Assert.assertTrue("Invalid http url is valud", (it.message == null || it.message!!.contains("invalid url:") == false))
        })
    }
}