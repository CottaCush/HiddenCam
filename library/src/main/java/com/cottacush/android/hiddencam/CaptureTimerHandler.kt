package com.cottacush.android.hiddencam

import android.os.Handler
import android.os.Message

internal class CaptureTimerHandler(
    private val captureInterval: Long,
    val captureTimeListener: () -> Unit
) : Handler() {

    companion object {
        const val DEFAULT_CAPTURE_INTERVAL = 30 * 1000L
        private const val UPDATE_TIMER_COMMAND = 100
        private const val INITIAL_CAPTURE_DELAY = 5 * 1000L
    }

    override fun handleMessage(msg: Message) {
        captureTimeListener()
        queueNextCapture(captureInterval)
    }

    private fun queueNextCapture(delay: Long) {
        removeMessages(UPDATE_TIMER_COMMAND)
        sendMessageDelayed(obtainMessage(UPDATE_TIMER_COMMAND), delay)
    }

    fun stopUpdates() {
        removeMessages(UPDATE_TIMER_COMMAND)
    }

    fun startUpdates() {
        queueNextCapture(INITIAL_CAPTURE_DELAY)
    }
}