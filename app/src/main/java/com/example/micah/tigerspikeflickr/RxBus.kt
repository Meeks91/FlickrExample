package com.example.micah.tigerspikeflickr

import io.reactivex.subjects.PublishSubject

/**
 * Created by Micah on 09/11/2017.
 */
object RxBus {

    val bus: PublishSubject<BusEvent> = PublishSubject.create<BusEvent>()
}

enum class EventType {

    retrieveMoreDetailed, retrieveNewTag, retrieveMoreCurrentTag
}

data class  BusEvent(val type: EventType, val data: Any)

//EVENT MODELS:
