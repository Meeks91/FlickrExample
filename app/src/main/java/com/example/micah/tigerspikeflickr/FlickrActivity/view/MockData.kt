package com.example.micah.tigerspikeflickr.MockData

import com.example.micah.tigerspikeflickr.FlickrActivity.model.dataModels.FlickrApiResponse
import com.example.micah.tigerspikeflickr.GlobalModels.api.GsonParser
import com.google.gson.reflect.TypeToken

/**
 * Created by Micah on 03/12/2017.
 */
object MockData {


    val testTitleOne = "TEST TITLE ONE"
    val testTitleTwo = "TEST TITLE TWO"
    val testURLOne = "TEST URL ONE"
    val testURLeTwo = "TEST URL TWO"
    val testTagOne = "TEST_TAG_ONE"
    val testTagTwo = "TEST_TAG_TWO"
    val flickrResponseJson = """

{
    "title": "Uploads from everyone",
    "link": "https://www.flickr.com/photos/",
    "description": "",
    "modified": "2017-12-03T12:46:03Z",
    "generator": "https://www.flickr.com",
    "items": [
        {
            "title": "$testTitleOne",
            "link": "https://www.flickr.com/photos/ronrag/23940973487/",
            "media": {
                "m": "$testURLOne"
            },
            "date_taken": "2017-12-03T14:32:16-08:00",
            "description": " <p><a href=\"https://www.flickr.com/people/ronrag/\">ronrag</a> posted a photo:</p> <p><a href=\"https://www.flickr.com/photos/ronrag/23940973487/\" title=\"Park Inn by Radisson\"><img src=\"https://farm5.staticflickr.com/4520/23940973487_a0b65cfa38_m.jpg\" width=\"240\" height=\"180\" alt=\"Park Inn by Radisson\" /></a></p> ",
            "published": "2017-12-03T12:46:03Z",
            "author": "nobody@flickr.com (\"ronrag\")",
            "author_id": "15103629@N00",
            "tags": "$testTagOne"
        },
        {
            "title": "$testTitleTwo",
            "link": "https://www.flickr.com/photos/147978966@N07/23940976587/",
            "media": {
                "m": "$testURLeTwo"
            },
            "date_taken": "2017-08-13T08:40:46-08:00",
            "description": " <p><a href=\"https://www.flickr.com/people/147978966@N07/\">patrickburtin</a> posted a photo:</p> <p><a href=\"https://www.flickr.com/photos/147978966@N07/23940976587/\" title=\"2017_BOT-1308.jpg\"><img src=\"https://farm5.staticflickr.com/4575/23940976587_d47266469a_m.jpg\" width=\"240\" height=\"160\" alt=\"2017_BOT-1308.jpg\" /></a></p> ",
            "published": "2017-12-03T12:46:11Z",
            "author": "nobody@flickr.com (\"patrickburtin\")",
            "author_id": "147978966@N07",
            "tags": "$testTagTwo"
        }
    ]
}
                     """

    fun getParsedResponse(): FlickrApiResponse {

        val type = object : TypeToken<FlickrApiResponse>() {}.type

        return  GsonParser.parse<FlickrApiResponse>(MockData.flickrResponseJson, type)
    }
}