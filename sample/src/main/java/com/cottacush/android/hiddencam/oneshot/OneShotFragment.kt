package com.cottacush.android.hiddencam.oneshot

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cottacush.android.R
import com.cottacush.android.hiddencam.HiddenCam
import com.cottacush.android.hiddencam.MainActivity
import com.cottacush.android.hiddencam.OnImageCapturedListener
import kotlinx.android.synthetic.main.fragment_oneshot.*
import java.io.File

class OneShotFragment : Fragment(), OnImageCapturedListener {

    private val mainActivity: MainActivity
        get() {
            return activity as? MainActivity ?: throw IllegalStateException("Not attached!")
        }

    private lateinit var hiddenCam: HiddenCam
    private lateinit var baseStorageFolder: File

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_oneshot, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baseStorageFolder = File(mainActivity.getExternalFilesDir(null), "HiddenCam").apply {
            //Clean up first.
            deleteRecursively()
        }
        hiddenCam = HiddenCam(mainActivity, baseStorageFolder, this)
        hiddenCam.start()

        captureButton.setOnClickListener {
            hiddenCam.captureImage()
        }
    }

    override fun onImageCaptured(image: File) {
        val message = "Image captured, saved to:${image.absolutePath}"
        log(message)
        showToast(message)
    }

    override fun onImageCaptureError(e: Throwable?) {
        e?.run {
            val message = "Image captured failed:${e.message}"
            showToast(message)
            log(message)
            printStackTrace()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(mainActivity, message, Toast.LENGTH_LONG).show()
    }

    private fun log(message: String) {
        Log.d(TAG, message)
    }

    override fun onStop() {
        super.onStop()
        hiddenCam.stop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hiddenCam.destroy()
    }

    companion object {
        const val TAG = "OneShotFragment"
    }
}
