package com.cottacush.android.hiddencam

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.util.Rational
import android.util.Size
import androidx.camera.core.CameraX
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureConfig
import java.io.File

//TODO remove all Logs once we are done?.

//TODO Idea: Timer can be set to default values zero and zero for single images capture

class HiddenCam(
    private val context: Context, private val baseFileDirectory: File,
    private val interval: Long, private val totalTime: Long,
    private val aspectRatio: Rational = getDefaultAspectRatio(context),
    private val cameraResolution: Size = getDefaultScreenResoultion(context),
    private val cameraFacingDirection: CameraX.LensFacing = CameraX.LensFacing.FRONT
    // TODO add more configurable inputs here. Provide default values if needed

) {

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
        imageCapture = ImageCapture(imageCaptureConfig)
        CameraX.bindToLifecycle(lifeCycleOwner, imageCapture)
        startCameraSchedule()
    }

    private fun startCameraSchedule() {
        object : CountDownTimer(totalTime * ONE_SECOND, interval * ONE_SECOND) {
            override fun onFinish() {
                //TODO We are done with the interval. Should we tear the camera down?
            }

            override fun onTick(p0: Long) {
                capture()
            }

        }
    }

    //Start: -- Cam Engine life cycle
    fun start() {
        lifeCycleOwner.start()
    }

    fun stop() {
        lifeCycleOwner.stop()
    }

    fun tearDown() {
        lifeCycleOwner.tearDown()
    }

    //End: -- Cam Engine life cycle

    fun capture() {
        imageCapture.takePicture(
            createFile(baseFileDirectory),
            object : ImageCapture.OnImageSavedListener {
                override fun onError(
                    imageCaptureError: ImageCapture.ImageCaptureError,
                    message: String,
                    cause: Throwable?
                ) {
                    //TODO report back to the listener
                    Log.e(TAG, "Photo capture failed: $message")
                    cause?.printStackTrace()
                }

                override fun onImageSaved(file: File) {
                    val msg = "Photo capture succeeded: ${file.absolutePath}"
                    //TODO report back to the listener
                    //TODO should we stop capturing after one error? ... or we should keep trying based on the schedule timer?
                    Log.d(TAG, msg)
                }
            })
    }

    companion object {
        private const val TAG = "HiddenCam"
        // Number of milliseconds in a second
        const val ONE_SECOND = 1000L
    }

}

