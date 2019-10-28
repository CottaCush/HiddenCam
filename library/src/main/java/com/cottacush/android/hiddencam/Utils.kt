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

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraX
import androidx.core.content.ContextCompat
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor

private const val DEFAULT_FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
private const val DEFAULT_PHOTO_EXTENSION = ".jpg"
private val requiredPermissions =
    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

enum class CameraType(val lensFacing: CameraX.LensFacing) {
    FRONT_CAMERA(CameraX.LensFacing.FRONT),
    BACK_CAMERA(CameraX.LensFacing.BACK)
}

enum class TargetAspectRatio(val aspectRatio: AspectRatio) {
    /** 4:3 standard aspect ratio.  */
    RATIO_4_3(AspectRatio.RATIO_4_3),
    /** 16:9 standard aspect ratio.  */
    RATIO_16_9(AspectRatio.RATIO_16_9)
}

internal fun createFile(
    baseDirectory: File
): File = File(
    baseDirectory, SimpleDateFormat(DEFAULT_FILENAME, Locale.US)
        .format(System.currentTimeMillis()) + DEFAULT_PHOTO_EXTENSION
)

internal fun Context.hasPermissions(): Boolean {
    for (permission in requiredPermissions) {
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) return false
    }
    return true
}

internal object MainThreadExecutor : Executor {
    private val handler: Handler = Handler(Looper.getMainLooper())
    override fun execute(r: Runnable?) {
        r?.let { handler.post(it) }
    }
}
