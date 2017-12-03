package com.example.micah.tigerspikeflickr.GlobalModels.RxBus

import io.reactivex.subjects.PublishSubject

/**
 * Created by Micah on 09/11/2017.
 */
object RxBus {

    val bus: PublishSubject<BusEvent> = PublishSubject.create<BusEvent>()
}

