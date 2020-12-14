package com.dyukov.dyuandrolearn.global

import androidx.lifecycle.MutableLiveData
import java.lang.NullPointerException

object GlobalEventController {
    private val events: HashMap<String, MutableLiveData<Event<Any>>> = HashMap()

    fun getGlobalEvent(key: String): MutableLiveData<Event<Any>> {
        if (key.isEmpty()) throw NullPointerException()
        return if (!events.containsKey(key)) {
            val event = MutableLiveData<Event<Any>>()
            events[key] = event
            event
        } else {
            events[key]!!
        }
    }
}