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

import java.io.File

/**
 * [OnImageCapturedListener] serves as a listener for image captures and image capture errors.
 * An implementation of [OnImageCapturedListener] should be provided for [HiddenCam] to get results.
 * @see [onImageCaptured] and [onImageCaptureError].
 */
interface OnImageCapturedListener {

    /**
     * Listener for image captures. This method is called whenever there is a new image capture.
     * @param image the [File] that represents the captured image.
     */
    fun onImageCaptured(image: File)

    /**
     * Listener for capture errors. This method is called whenever there is an error during the capture process.
     * @param e the [Throwable] that describes the error.
     */
    fun onImageCaptureError(e: Throwable?)
}
