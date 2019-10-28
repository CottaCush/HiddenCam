package com.cottacush.android.hiddencam

import android.content.Context
import android.util.Size
import androidx.camera.core.CameraX
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureConfig
import com.cottacush.android.hiddencam.CaptureTimeFrequency.OneShot
import com.cottacush.android.hiddencam.CaptureTimeFrequency.Recurring
import java.io.File

class HiddenCam(
    context: Context, private val baseFileDirectory: File,
    private val imageCapturedListener: OnImageCapturedListener,
    private val captureFrequency: CaptureTimeFrequency = OneShot,
    private val targetAspectRatio: TargetAspectRatio? = null,
    private val targetResolution: Size? = null,
    private val targetRotation: Int? = null,
    private val cameraType: CameraType = CameraType.FRONT_CAMERA
) {
    private lateinit var captureTimer: CaptureTimerHandler
    private val lifeCycleOwner = HiddenCamLifeCycleOwner()
    private var imageCapture: ImageCapture
    private var imageCaptureConfig: ImageCaptureConfig = ImageCaptureConfig.Builder()
        .apply {
            setLensFacing(cameraType.lensFacing)
            if (targetRotation != null) setTargetRotation(targetRotation)
            if (targetResolution != null) setTargetResolution(targetResolution)
            if (targetAspectRatio != null) setTargetAspectRatio(targetAspectRatio.aspectRatio)
        }.build()

    init {
        if (context.hasPermissions()) {
            imageCapture = ImageCapture(imageCaptureConfig)
            CameraX.bindToLifecycle(lifeCycleOwner, imageCapture)
            when (val interval = captureFrequency) {
                OneShot -> {
                    //Nothing for now, we don't need to schedule anything
                }
                is Recurring -> {
                    captureTimer = CaptureTimerHandler(
                        interval.captureIntervalMillis,
                        object : CaptureTimeListener {
                            override fun onCaptureTimeTick() {
                                capture()
                            }
                        })
                }
            }
        } else throw SecurityException("You need to have access to both CAMERA and WRITE_EXTERNAL_STORAGE permissions")
    }

    fun start() {
        lifeCycleOwner.start()
        if (captureFrequency is Recurring) captureTimer.startUpdates()
    }

    fun stop() {
        lifeCycleOwner.stop()
        if (captureFrequency is Recurring) captureTimer.stopUpdates()
    }

    fun destroy() {
        lifeCycleOwner.tearDown()
        if (captureFrequency is Recurring) captureTimer.stopUpdates()
    }

    fun captureImage() {
        if (captureFrequency is OneShot)
            capture()
    }

    private fun capture() {
        imageCapture.takePicture(createFile(baseFileDirectory), MainThreadExecutor,
            object : ImageCapture.OnImageSavedListener {
                override fun onError(
                    imageCaptureError: ImageCapture.ImageCaptureError,
                    message: String,
                    cause: Throwable?
                ) = imageCapturedListener.onImageCaptureError(cause)

                override fun onImageSaved(file: File) = imageCapturedListener.onImageCaptured(file)
            })
    }
}