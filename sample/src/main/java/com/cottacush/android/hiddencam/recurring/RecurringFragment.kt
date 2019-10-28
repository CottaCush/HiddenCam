package com.cottacush.android.hiddencam.recurring

import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cottacush.android.R
import com.cottacush.android.hiddencam.CaptureTimeFrequency
import com.cottacush.android.hiddencam.HiddenCam
import com.cottacush.android.hiddencam.MainActivity
import com.cottacush.android.hiddencam.OnImageCapturedListener
import kotlinx.android.synthetic.main.fragment_recurring.*
import java.io.File

class RecurringFragment : Fragment(), OnImageCapturedListener {

    private val mainActivity: MainActivity
        get() {
            return activity as? MainActivity ?: throw IllegalStateException("Not attached!")
        }

    private lateinit var hiddenCam: HiddenCam
    private lateinit var baseStorageFolder: File

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_recurring, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.setUpToolBar(getString(R.string.recurring))
        baseStorageFolder = File(mainActivity.getExternalFilesDir(null), "HiddenCam").apply {
            if (!exists()) mkdir()
        }
        hiddenCam = HiddenCam(mainActivity, baseStorageFolder, this,
            CaptureTimeFrequency.Recurring(RECURRING_INTERVAL),
            targetResolution = Size(1080, 1920)
        )
        startCaptureButton.setOnClickListener {
            hiddenCam.start()
        }
        stopCaptureButton.setOnClickListener {
            hiddenCam.stop()
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

    override fun onDestroyView() {
        super.onDestroyView()
        hiddenCam.destroy()
    }

    companion object {
        const val TAG = "RecurringFragment"
        const val RECURRING_INTERVAL = 10 * 1000L
    }
}