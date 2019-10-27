package com.cottacush.android.hiddencam

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cottacush.android.R
import kotlinx.android.synthetic.main.fragment_landing_page.*

class LandingPageFragment : Fragment() {

    private val mainActivity: MainActivity
        get() {
            return activity as? MainActivity ?: throw IllegalStateException("Not attached!")
        }

    private val requiredPermissions =
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_landing_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (checkPermissions()) onPermissionsGranted()
        recurringButton.setOnClickListener {

        }

        oneShotButton.setOnClickListener {

        }

    }

    fun onPermissionsGranted() {
        recurringButton.isEnabled = true
        oneShotButton.isEnabled = true
    }

    private fun checkPermissions(): Boolean {
        return if (mainActivity.hasPermissions(requiredPermissions)) true
        else {
            requestPermissions(
                requiredPermissions,
                CAMERA_AND_STORAGE_PERMISSION_REQUEST_CODE
            )
            false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == MainActivity.CAMERA_AND_STORAGE_PERMISSION_REQUEST_CODE &&
            confirmPermissionResults(grantResults)
        ) onPermissionsGranted()
    }

    private fun confirmPermissionResults(results: IntArray): Boolean {
        results.forEach {
            if (it != PackageManager.PERMISSION_GRANTED) return false
        }
        return true
    }

    companion object {
        const val CAMERA_AND_STORAGE_PERMISSION_REQUEST_CODE = 100
    }
}
