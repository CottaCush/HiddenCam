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
package com.cottacush.android.hiddencam.oneshot

import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cottacush.android.R
import com.cottacush.android.hiddencam.*
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_oneshot, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.setUpToolBar(getString(R.string.one_shot))
        baseStorageFolder = File(mainActivity.getExternalFilesDir(null), "HiddenCam").apply {
            if (exists()) deleteRecursively()
            mkdir()
        }
        hiddenCam = HiddenCam(
            mainActivity, baseStorageFolder, this,
            targetResolution = Size(1920, 1080)
        )

        captureButton.setOnClickListener {
            hiddenCam.captureImage()
        }
    }

    override fun onStart() {
        super.onStart()
        hiddenCam.start()
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
