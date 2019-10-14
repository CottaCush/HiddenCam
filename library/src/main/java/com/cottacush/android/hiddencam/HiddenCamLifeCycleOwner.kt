package com.cottacush.android.hiddencam

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

//Comment we want to use this to control CameraX, manually...
class HiddenCamLifeCycleOwner : LifecycleOwner {

    private val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    init {
        lifecycleRegistry.markState(Lifecycle.State.CREATED)
    }

    fun start() {
        lifecycleRegistry.markState(Lifecycle.State.STARTED)
    }

    fun tearDown() {
        lifecycleRegistry.markState(Lifecycle.State.DESTROYED)
    }


    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }
}


/*
Lifeccyles events to watch {

   // Lifecycle.Event.ON_START
   // Lifecycle.Event.ON_STOP
   // Lifecycle.Event.ON_DESTROY

}*/
