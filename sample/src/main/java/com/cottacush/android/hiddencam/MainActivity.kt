package com.cottacush.android.hiddencam

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity(), OnImageCapturedListener {

    private val permissionCode = 500
    private lateinit var hiddenCam: HiddenCam
    private val baseStorageFolder = File(getExternalFilesDir(null), "HiddenCam")

    private val requiredPermissions =
        mutableListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).toTypedArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hiddenCam = HiddenCam(this, baseStorageFolder, this)
        startCaptureButton.setOnClickListener {
            if (hasPermissions()) hiddenCam.start()
            else ActivityCompat.requestPermissions(this, requiredPermissions, permissionCode)
        }

        stopCaptureButton.setOnClickListener {
            hiddenCam.stop()
        }
    }

    override fun onImageCaptured(image: File) {
        val message = "Image captured, saved to:${image.absolutePath}"
        showToast(message)
    }

    override fun onImageCaptureError(e: Throwable?) {
        e?.run {
            val message = "Image captured failed:${e.message}"
            e.printStackTrace()
            showToast(message)
        }
    }


    private fun hasPermissions(): Boolean {
        for (permission in requiredPermissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) return false
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        hiddenCam.destroy()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun log(message: String) {
        Log.d(TAG, message)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
