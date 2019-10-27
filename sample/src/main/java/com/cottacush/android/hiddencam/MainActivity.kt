package com.cottacush.android.hiddencam

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.cottacush.android.R
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity(), OnImageCapturedListener {

    private lateinit var hiddenCam: HiddenCam

    private val baseStorageFolder = File(getExternalFilesDir(null), "HiddenCam")

    private val requiredPermissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)


    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration


    private fun setUpNavigation() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)
    }

    fun setUpToolBar(toolbarTitle: String, isRootPage: Boolean = false) {
        supportActionBar!!.run {
            setDisplayHomeAsUpEnabled(!isRootPage)
            setHomeAsUpIndicator(if (!isRootPage) R.drawable.ic_arrow_back_white_24dp else 0)
            toolbar.title = toolbarTitle
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpNavigation()

        hiddenCam = HiddenCam(this, baseStorageFolder, this)
        checkPermissions()
        startCaptureButton.setOnClickListener {
            hiddenCam.start()
        }
        stopCaptureButton.setOnClickListener {
            hiddenCam.stop()
        }
    }

    fun onPermissionsGranted() {
        startCaptureButton.isEnabled = true
        stopCaptureButton.isEnabled = true
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

    private fun checkPermissions(): Boolean {
        return if (hasPermissions(requiredPermissions)) true
        else {
            ActivityCompat.requestPermissions(this, requiredPermissions,
                CAMERA_AND_STORAGE_PERMISSION_REQUEST_CODE)
            false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_AND_STORAGE_PERMISSION_REQUEST_CODE &&
            confirmPermissionResults(grantResults)
        ) onPermissionsGranted()
    }

    override fun onDestroy() {
        super.onDestroy()
        hiddenCam.destroy()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun log(message: String) {
        Log.d(TAG, message)
    }

    private fun confirmPermissionResults(results: IntArray): Boolean {
        results.forEach {
            if (it != PackageManager.PERMISSION_GRANTED) return false
        }
        return true
    }

    companion object {
        private const val TAG = "MainActivity"
        const val CAMERA_AND_STORAGE_PERMISSION_REQUEST_CODE = 100
    }
}