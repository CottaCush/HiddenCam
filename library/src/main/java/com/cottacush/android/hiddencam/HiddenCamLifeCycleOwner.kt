package com.cottacush.android.hiddencam

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

internal class HiddenCamLifeCycleOwner : LifecycleOwner {

    private val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    init {
        lifecycleRegistry.markState(Lifecycle.State.CREATED)
    }

    fun start() = lifecycleRegistry.markState(Lifecycle.State.STARTED)

    fun stop() = lifecycleRegistry.markState(Lifecycle.State.CREATED)

    fun tearDown() = lifecycleRegistry.markState(Lifecycle.State.DESTROYED)

    override fun getLifecycle(): Lifecycle = lifecycleRegistry
}