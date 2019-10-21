package com.cottacush.android.hiddencam

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.util.Rational
import android.util.Size
import androidx.camera.core.CameraX
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureConfig
import androidx.camera.core.UseCase
import com.cottacush.android.hiddencam.CaptureTimerHandler.Companion.DEFAULT_CAPTURE_INTERVAL
import java.io.File

//TODO Idea: Timer can be set to default values zero and zero for single images capture

class HiddenCam(
    private val context: Context, private val baseFileDirectory: File,
    private val imageCapturedListener: OnImageCapturedListener,
    captureInterval: Long = DEFAULT_CAPTURE_INTERVAL,
    private val aspectRatio: Rational = getDefaultAspectRatio(context),
    private val cameraResolution: Size = getDefaultScreenResoultion(context),
    private val cameraFacingDirection: CameraX.LensFacing = CameraX.LensFacing.FRONT
    // TODO add more configurable inputs here. Provide default values if needed
) {
    private val captureTimer: CaptureTimerHandler
    private val lifeCycleOwner = HiddenCamLifeCycleOwner()
    private var imageCapture: ImageCapture
    private var imageCaptureConfig: ImageCaptureConfig = ImageCaptureConfig.Builder()
        .apply {
            setLensFacing(cameraFacingDirection)
            setTargetResolution(cameraResolution)
            setTargetAspectRatio(aspectRatio) // TODO make this configurable?
            setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)// TODO experiment with all possible capture modes to see which one gives clear picture

            //TODO make other necessary configurations, to get best picture

        }.build()

    init {
        if (!context.hasPermissions()) {
            imageCapturedListener.onImageCaptureError(Throwable("You need to have access to both CAMERA and WRITE_EXTERNAL_STORAGE permissions"))
        }
        imageCapture = ImageCapture(imageCaptureConfig)
        CameraX.bindToLifecycle(lifeCycleOwner, imageCapture)
        captureTimer = CaptureTimerHandler(captureInterval) {
            capture()
        }
    }

    //Start: -- Cam Engine life cycle
    fun start() {
        lifeCycleOwner.start()
        captureTimer.startUpdates()
    }

    fun stop() {
        captureTimer.stopUpdates()
        lifeCycleOwner.stop()
    }

    fun destroy() {
        captureTimer.stopUpdates()
        lifeCycleOwner.tearDown()
    }

    //End: -- Cam Engine life cycle

    private fun capture() {
        imageCapture.takePicture(
            createFile(baseFileDirectory),
            object : ImageCapture.OnImageSavedListener {
                override fun onError(
                    imageCaptureError: ImageCapture.ImageCaptureError,
                    message: String,
                    cause: Throwable?
                ) {
                    Log.e(TAG, "Photo capture failed: $message")
                    imageCapturedListener.onImageCaptureError(cause)
                }

                override fun onImageSaved(file: File) {
                    Log.d(TAG, "Photo capture succeeded: ${file.absolutePath}")
                    imageCapturedListener.onImageCaptured(file)
                }
            })
    }

    companion object {
        private const val TAG = "HIDDEN_CAM"
    }
}

