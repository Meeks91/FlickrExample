package com.example.micah.tigerspikeflickr.FlickrActivity.model.dataModels

import com.google.gson.annotations.SerializedName

/**
 * model of the images returned by Flickr
 * from the public feed endpoint
 */
data class FlickrImageModel(val title: String,
                            val link: String,
                            private val author: String,
                            private @SerializedName("date_taken") val dateTaken: String,
                            private @SerializedName("tags") val rawTags: String,
                            private val media: FlickrMedia){

    data class FlickrMedia (@SerializedName("m") val imageUrl: String)

    val imageUrl get() =  media.imageUrl
    val tags get() = rawTags.takeIf {it.isNotEmpty()} ?: "Default tags"
    val searchTag get() = rawTags.substringBefore(" ", tags)
}