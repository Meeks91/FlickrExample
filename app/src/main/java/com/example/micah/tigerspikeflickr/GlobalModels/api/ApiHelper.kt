package com.example.micah.tigerspikeflickr.GlobalModels.api

import android.util.Log.d
import com.google.gson.Gson
import io.reactivex.Single
import okhttp3.*
import java.io.IOException
import java.lang.reflect.Type

/**
 * Created by Micah Simmons on 03/05/2017.
 */

object ApiHelper {

    private val TAG = ApiHelper::class.java.simpleName
    private val baseFlickrApiURL = "https://api.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1"
    private val okHttpClient = OkHttpClient()
    private val gson = Gson()

    /**
     * Makes a request to the Flickr API using the specified parameters.
     * The result of the request and parsed [T] is returned in the returned Single.
     * The  [type] is necessary for edge case parsing issues.
     *
     * [type] - the type token used to parse the result
     *        - this should be the same type as [T]
     *
     * [queryString] - the query String for the Flickr api
     *
     */
    @Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")
    inline fun <reified T> request(type: Type, queryString: String): Single<T> {

        return Single.create {

            //create httpUrl:
            val urlString = baseFlickrApiURL+queryString
            val httpUrl = HttpUrl.parse(urlString)

            //validate httpUrl:
            if (httpUrl == null) {

                it.onError(Throwable("invalid url: $urlString"))
            }

            //make api call to flickr:
            okHttpClient.newCall(generateRequest(httpUrl!!)).enqueue(object : Callback {

                override fun onFailure(call: Call?, e: IOException?) {

                    it.onError(Throwable(e?.message ?: "An Unknown error occurred"))
                }

                override fun onResponse(call: Call?, response: Response?) {

                    val jsonResponseString = response?.body()?.string() ?: "{}"

                    d(TAG, "jsonResponseString is: $jsonResponseString")

                    //parse and return response
                    it.onSuccess(parse<T>(jsonResponseString, type))
                }
            })
        }
    }

    /**
     * returns a T made using the [type]
     * and parsing the [jsonResponseString]
     */
    private inline fun <reified T> parse(jsonResponseString: String, type: Type): T  =

          gson.fromJson(jsonResponseString, type)

    /**
     * generates and returns a GET Request object
     *
     * [httpUrl] - the url of the endpoint
     */
    private fun generateRequest(httpUrl: HttpUrl): Request =

        Request.Builder().url(httpUrl).build()
}