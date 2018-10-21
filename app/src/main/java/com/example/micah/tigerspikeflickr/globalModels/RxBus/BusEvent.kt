package com.example.micah.tigerspikeflickr.globalModels.RxBus

/**
 * holder for different bus events. The [type]
 * specifies the type of event and the [data] variable
 * hold any bussed data
 */
data class BusEvent(val type: EventType, val data: Any)