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

/**
 * A CaptureTimeFrequency allows you to specify if you want to activate continuous captures at a
 * regular interval, or manually triggered captures. see also, [OneShot] and [Recurring]
 */
sealed class CaptureTimeFrequency {

    /**
     * A [CaptureTimeFrequency] option that specifies manually triggered captures.
     */
    object OneShot : CaptureTimeFrequency()

    /**
     * A [CaptureTimeFrequency] option that specifies captures at regular intervals of
     * [captureIntervalMillis] milliseconds.
     */
    class Recurring(val captureIntervalMillis: Long) : CaptureTimeFrequency()
}
