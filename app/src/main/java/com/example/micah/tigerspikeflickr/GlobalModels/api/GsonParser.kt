package com.example.micah.tigerspikeflickr.GlobalModels.api

import com.google.gson.Gson
import java.lang.reflect.Type

object GsonParser {

    private val gson = Gson()

    /**
     * returns a T made using the [type]
     * and parsing the [jsonResponseString]
     */
     fun <T> parse(jsonResponseString: String, type: Type): T =

            gson.fromJson(jsonResponseString, type)
}