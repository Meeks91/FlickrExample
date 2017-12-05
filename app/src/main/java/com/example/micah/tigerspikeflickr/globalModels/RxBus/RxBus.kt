package com.example.micah.tigerspikeflickr.globalModels.RxBus

import io.reactivex.subjects.PublishSubject

/**
 * Created by Micah on 09/11/2017.
 * Is used to bus [BusEvent]s around the app.
 */
object RxBus {

    val bus: PublishSubject<BusEvent> = PublishSubject.create<BusEvent>()
}

