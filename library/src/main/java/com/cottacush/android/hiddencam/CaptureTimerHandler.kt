/**
 * Copyright (c) 2019 Cotta & Cush Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cottacush.android.hiddencam

import android.os.Handler
import android.os.Message

internal class CaptureTimerHandler(
    private val captureInterval: Long,
    private val captureTimeListener: CaptureTimeListener
) : Handler() {

    companion object {
        private const val UPDATE_TIMER_COMMAND = 100
        private const val INITIAL_CAPTURE_DELAY = 2 * 1000L
    }

    override fun handleMessage(msg: Message) {
        captureTimeListener.onCaptureTimeTick()
        queueNextCapture(captureInterval)
    }

    private fun queueNextCapture(delay: Long) {
        removeMessages(UPDATE_TIMER_COMMAND)
        sendMessageDelayed(obtainMessage(UPDATE_TIMER_COMMAND), delay)
    }

    fun stopUpdates() = removeMessages(UPDATE_TIMER_COMMAND)

    fun startUpdates() = queueNextCapture(INITIAL_CAPTURE_DELAY)
}

internal interface CaptureTimeListener {
    fun onCaptureTimeTick()
}
