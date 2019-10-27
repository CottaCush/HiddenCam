package com.cottacush.android.hiddencam

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.hasPermissions(permissionList: Array<String>): Boolean {
    for (permission in permissionList) {
        if (!hasPermission(permission)) return false
    }
    return true
}

fun Context.hasPermission(permission: String): Boolean =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED