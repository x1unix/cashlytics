package com.x1unix.cashlytics

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

class PermissionHelper (val context: Context) {
    private val requiredPermissions = arrayOf<String>(
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS
    )

    val permissionsGranted : Boolean
        get() {
            for (permission in requiredPermissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
            return true
        }

    fun requirePermissions(activity: Activity) {
        ActivityCompat.requestPermissions(activity, requiredPermissions, GRANTED)
    }

    companion object {
        const val GRANTED = 1
    }

}